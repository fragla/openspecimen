angular.module('os.administrative.container-type.addedit', ['os.administrative.models'])
  .controller('ContainerTypeAddEditCtrl', function(
    $scope, $state, containerType, ContainerType, 
    PvManager, Util) {
    
    function init() {
      $scope.containerType = containerType;
      loadPvs();
    }

    function loadPvs() {
      $scope.positionLabelingSchemes = PvManager.getPvs('container-position-labeling-schemes');

      $scope.canHolds = [];
      ContainerType.query().then(
        function(containerTypeList) {
          angular.forEach(containerTypeList, function(canHold) {
            $scope.canHolds.push(canHold);
          });
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
