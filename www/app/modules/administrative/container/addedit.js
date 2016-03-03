angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function(
    $scope, $state, $stateParams, $q, container, 
    Site, PvManager, ContainerUtil, ContainerType) {

    function init() {
      container.storageLocation = container.storageLocation || {};
      $scope.container = container;

      /**
       * Some how the ui-select's multiple option is removing pre-selected items
       * when cp list is being loaded or not yet loaded...
       * Therefore we copy pre-selected cps and then use it when all CPs are loaded
       */
      $scope.allowedCps = angular.copy(container.allowedCollectionProtocols);

      $scope.cps = [];
      $scope.specimenTypes = [];
      loadPvs();

      $scope.specimenTypeSelectorOpts = {
        items: $scope.specimenTypes,
        selectedCategories: container.allowedSpecimenClasses,
        selectedCategoryItems: container.allowedSpecimenTypes,
        categoryAttr: 'parent',
        valueAttr: 'value',
        allowAll: undefined
      };

      if ($stateParams.parentContainerName && $stateParams.parentContainerId &&
          $stateParams.posOne && $stateParams.posTwo) {
        //
        // This happens when user adds container from container map
        //

        $scope.locationSelected = true;
        container.storageLocation = {
          name: $stateParams.parentContainerName,
          positionX: $stateParams.posOne,
          positionY: $stateParams.posTwo
        };
        restrictCpsAndSpecimenTypes();
      }
      loadContainerTypes();

      watchParentContainer();
    };

    function watchParentContainer() {
      $scope.$watch('container.storageLocation.name', function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if (!newVal) {
          ContainerUtil.loadAllCpsAndSpecimenTypes($scope, $scope.container);
        } else {
          ContainerUtil.restrictCpsAndSpecimenTypes($scope, $scope.container);
        } 
      });
    };

    function loadPvs() {
      $scope.positionLabelingSchemes = PvManager.getPvs('container-position-labeling-schemes');
      
      var op = !!$scope.container.id ? 'Update' : 'Create';
      $scope.sites = [];
      Site.listForContainers(op).then(function(sites) {
        $scope.sites = sites;
      });

      if ($scope.container.storageLocation.name) {
        ContainerUtil.restrictCpsAndSpecimenTypes($scope, $scope.container);
      } else {
        ContainerUtil.loadAllCpsAndSpecimenTypes($scope, $scope.container);
      }

    };

    $scope.loadAllCps = function() {
      ContainerUtil.loadAllCps($scope, $scope.container);
    }

    function loadContainerTypes() {
      $scope.containerTypes = [];
      ContainerType.query().then(function(containerTypes) {
        $scope.containerTypes = containerTypes;
      });
    }

    $scope.onSelectContainerType = function() {
      $scope.container.noOfColumns = $scope.containerTypes.type ? $scope.containerTypes.type.noOfColumns : "";
      $scope.container.noOfRows = $scope.containerTypes.type ? $scope.containerTypes.type.noOfRows : "";
      $scope.container.columnLabelingScheme = $scope.containerTypes.type ? 
        $scope.containerTypes.type.columnLabelingScheme : "";
      $scope.container.rowLabelingScheme = $scope.containerTypes.type ? $scope.containerTypes.type.rowLabelingScheme : "";
      $scope.container.temperature = $scope.containerTypes.type ? $scope.containerTypes.type.temperature : "";
      $scope.container.storeSpecimensEnabled = $scope.containerTypes.type ?
        $scope.containerTypes.type.storeSpecimenEnabled : "";
    }
    
    $scope.save = function() {
      var container = angular.copy($scope.container);
      container.$saveOrUpdate().then(
        function(result) {
          if (!$scope.locationSelected) {
            $state.go('container-detail.overview', {containerId: result.id});
          } else {
            $state.go('container-detail.locations', {containerId: $stateParams.parentContainerId});
          }
        }
      );
    };

    init();
  });
