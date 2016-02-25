angular.module('os.administrative.container-type.list', ['os.administrative.models'])
  .controller('ContainerTypeListCtrl', function($scope, $state, ContainerType, Util) {

    function init() {
      $scope.containerTypeFilterOpts = {};
      loadContainerTypes();
      Util.filter($scope, 'containerTypeFilterOpts', loadContainerTypes);
    }

    function loadContainerTypes(filterOpts) {
      ContainerType.list(filterOpts).then(
        function(containerTypes) {
          $scope.containerTypeList = containerTypes;
        }
      );
    }

    $scope.showContainerTypeOverview = function(containerType) {
      console.log(containerType);
      $state.go('container-type-detail.overview', {containerTypeId: containerType.id});
    };

    init();
  });

