angular.module('os.administrative.container.createhierarchy', ['os.administrative.models'])
  .controller('ContainerCreateHierarchyCtrl', function(
    $scope, $state, $stateParams,
    ContainerType, Container, Site, CollectionProtocol, PvManager, Util) {
    var allSpecimenTypes = undefined;
    var siteContainersMap = {};

    function init() {
      $scope.hierarchy =  {
        allowedSpecimenClasses: [],
        allowedSpecimenTypes: [],
        allowedCollectionProtocols: []
      };

      $scope.specimenTypes = [];

      loadPvs();

      $scope.specimenTypeSelectorOpts = {
        items: $scope.specimenTypes,
        categoryAttr: 'parent',
        valueAttr: 'value',
        allowAll: undefined
      };

      $scope.hierarchy.containerTypeName = $stateParams.containerType || undefined;
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

    function loadAllCpsAndSpecimenTypes() {
      loadAllCps();
      loadAllSpecimenTypes();
    };

    function loadAllCps() {
      var siteName = $scope.hierarchy.parentSite;
      if (!siteName) {
        return;
      }

      CollectionProtocol.query({repositoryName: siteName}).then(
        function(cps) {
          $scope.cps = cps.map(function(cp) { return cp.shortTitle; });

          // fix - pre-selected cps were getting cleared
          //scope.hierarchy.allowedCollectionProtocols = [];
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

    function setSiteContainers(dest) {
      if (!dest.parentSite) {
        return;
      }

      if (!siteContainersMap[dest.parentSite]) {
        siteContainersMap[dest.parentSite] = Container.listForSite(dest.parentSite, true, true);
      }

      siteContainersMap[dest.parentSite].then(
        function(containers) {
          $scope.containers = containers;
        }
      );
    }

    $scope.onSiteSelect = function() {
      $scope.hierarchy.parentContainer = undefined;
      setSiteContainers($scope.hierarchy);
      loadAllCps();
    }

    $scope.searchContainers = function(name) {
      if (!name) {
        setSiteContainers($scope.hierarchy);
      } else {
        Container.listForSite($scope.hierarchy.parentSite, true, true, name).then(
          function(containers) {
            $scope.containers = containers;
          }
        );
      }
    }

    $scope.create = function() {
      Container.createHierarchy($scope.hierarchy).then(
        function(resp) {
          console.log(resp);
          if ($scope.hierarchy.parentContainer) {
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