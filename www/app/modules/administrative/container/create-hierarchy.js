angular.module('os.administrative.container.createhierarchy', ['os.administrative.models'])
  .controller('ContainerCreateHierarchyCtrl', function(
    $scope, $state, $stateParams,
    ContainerType, Container, Site, CollectionProtocol, PvManager, Util) {
    var allSpecimenTypes = undefined;
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

      loadAllCpsAndSpecimenTypes();
    }

    function watchParentContainer() {
      $scope.$watch('hierarchySpec.parentContainer', function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if (!newVal) {
          loadAllCpsAndSpecimenTypes();
        } else {
          restrictCpsAndSpecimenTypes();
        }
      });
    };

    function loadAllCpsAndSpecimenTypes() {
      loadAllCps();
      loadAllSpecimenTypes();
    };

    function restrictCpsAndSpecimenTypes() {
      var parentName = $scope.hierarchySpec.parentContainer;
      Container.getByName(parentName).then(
        function(parentContainer) {
          restrictCps(parentContainer);
          restrictSpecimenTypes(parentContainer);
        }
      );
    };

    function restrictCps(parentContainer) {
      var parentCps = parentContainer.calcAllowedCollectionProtocols;
      if (parentCps.length > 0) {
        $scope.cps = parentCps;
      } else {
        loadAllCps(parentContainer.siteName);
      }

      $scope.hierarchySpec.allowedCollectionProtocols = [];
    };

    function loadAllCps() {
      var siteName = $scope.hierarchySpec.parentSite;
      if (!siteName) {
        return;
      }

      CollectionProtocol.query({repositoryName: siteName}).then(
        function(cps) {
          $scope.cps = cps.map(function(cp) { return cp.shortTitle; });
        }
      );
    };

    function loadAllSpecimenTypes() {
      if (allSpecimenTypes) {
        var d = $q.defer();
        d.resolve(allSpecimenTypes);
        return d.promise;
      }

      return PvManager.loadPvsByParent('specimen-class', undefined, true).then(
        function(specimenTypes) {
          allSpecimenTypes = specimenTypes;
          Util.assign($scope.specimenTypes, specimenTypes);
          return allSpecimenTypes;
        }
      );
    };

    function restrictSpecimenTypes(parentContainer) {
      if (allSpecimenTypes) {
        filterSpecimenTypes(parentContainer);
      } else {
        loadAllSpecimenTypes().then(
          function() {
            filterSpecimenTypes(parentContainer);
          }
        );
      }
    };

    function filterSpecimenTypes(parentContainer) {
      var allowedClasses = parentContainer.calcAllowedSpecimenClasses;
      var allowedTypes = parentContainer.calcAllowedSpecimenTypes;
      $scope.specimenTypeSelectorOpts.allowAll = allowedClasses;


      var filtered = allSpecimenTypes.filter(
        function(specimenType) {
          return allowedClasses.indexOf(specimenType.parent) >= 0 ||
                   allowedTypes.indexOf(specimenType.value) >= 0;
        }
      );
      Util.assign($scope.specimenTypes, filtered);
    };


    function setSiteContainers(hierarchySpec) {
      if (!hierarchySpec.parentSite) {
        return;
      }

      if (!siteContainersMap[hierarchySpec.parentSite]) {
        siteContainersMap[hierarchySpec.parentSite] = Container.listForSite(hierarchySpec.parentSite, true, true);
      }

      siteContainersMap[hierarchySpec.parentSite].then(
        function(containers) {
          $scope.containers = containers;
        }
      );
    }

    $scope.onSiteSelect = function() {
      $scope.hierarchySpec.parentContainer = undefined;
      setSiteContainers($scope.hierarchySpec);
      loadAllCps();
    }

    $scope.searchContainers = function(name) {
      if (!name) {
        setSiteContainers($scope.hierarchySpec);
      } else {
        Container.listForSite($scope.hierarchySpec.parentSite, true, true, name).then(
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