angular.module('os.administrative.containertype.addedit', ['os.administrative.models'])
  .controller('ContainerTypeAddEditCtrl', function(
    $scope, $state, containerType, ContainerType, PvManager, Util) {
    
    function init() {
      $scope.containerType = containerType;
      loadPvs();
    }

    function loadPvs() {
      $scope.positionLabelingSchemes = PvManager.getPvs('container-position-labeling-schemes');

      $scope.canHolds = [];
      ContainerType.query().then(
        function(containerTypes) {
          $scope.canHolds = containerTypes;
        }
      );
    }

    $scope.save = function() {
      var containerType = angular.copy($scope.containerType);
      containerType.$saveOrUpdate().then(
        function(result) {
          $state.go('container-type-detail.overview', {containerTypeId: result.id});
        }
      );
    };

    init();
  });