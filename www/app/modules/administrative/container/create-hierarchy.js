angular.module('os.administrative.container.createhierarchy', ['os.administrative.models'])
  .controller('ContainerCreateHierarchyCtrl', function(
    $scope, $state, $stateParams,
    ContainerType, Container, Site, CollectionProtocol, PvManager, ContainerUtil, Util) {
    var siteContainersMap = {};

    function init() {
      $scope.hierarchySpec =  {
        allowedCollectionProtocols: [],
        allowedSpecimenClasses: [],
        allowedSpecimenTypes: []
      };

      $scope.specimenTypes = [];

      loadPvs();

      $scope.specimenTypeSelectorOpts = {
        items: $scope.specimenTypes,
        categoryAttr: 'parent',
        valueAttr: 'value',
        allowAll: undefined
      };

      //
      // If user creates hierarchy from container type, container type should be pre selected
      //
      $scope.hierarchySpec.containerTypeName = $stateParams.containerType || undefined;

      watchParentContainer();
    }

    function loadPvs() {
      $scope.containerTypes = [];
      ContainerType.list().then(function(containerTypes) {
        $scope.containerTypes = containerTypes;
      });

      $scope.sites = [];
      Site.listForContainers('create').then(function(sites) {
        $scope.sites = sites;
      });

      ContainerUtil.loadAllCpsAndSpecimenTypes($scope, $scope.hierarchySpec);
    }

    function watchParentContainer() {
      $scope.$watch('hierarchySpec.parentContainer', function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if (!newVal) {
          ContainerUtil.loadAllCpsAndSpecimenTypes($scope, $scope.hierarchySpec);
        } else {
          ContainerUtil.restrictCpsAndSpecimenTypes($scope, $scope.hierarchySpec);
        }
      });
    };

    function setSiteContainers(hierarchySpec) {
      if (!hierarchySpec.siteName) {
        return;
      }

      if (!siteContainersMap[hierarchySpec.siteName]) {
        siteContainersMap[hierarchySpec.siteName] = Container.listForSite(hierarchySpec.siteName, true, true);
      }

      siteContainersMap[hierarchySpec.siteName].then(
        function(containers) {
          $scope.containers = containers;
        }
      );
    }

    $scope.onSiteSelect = function() {
      $scope.hierarchySpec.parentContainer = undefined;
      setSiteContainers($scope.hierarchySpec);
      ContainerUtil.loadAllCps($scope, $scope.hierarchySpec);
    }

    $scope.searchContainers = function(name) {
      if (!name) {
        setSiteContainers($scope.hierarchySpec);
      } else {
        Container.listForSite($scope.hierarchySpec.siteName, true, true, name).then(
          function(containers) {
            $scope.containers = containers;
          }
        );
      }
    }

    $scope.create = function() {
      Container.createHierarchy($scope.hierarchySpec).then(
        function(resp) {
          if ($scope.hierarchySpec.parentContainer) {
            $state.go('container-detail.overview', {containerId: resp.data[0].storageLocation.id});
          }
          else {
            $state.go('container-list');
          }
        }
      )
    }

    init();
  });