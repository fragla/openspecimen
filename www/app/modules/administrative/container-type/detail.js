angular.module('os.administrative.containertype.detail', ['os.administrative.models'])
  .controller('ContainerTypeDetailCtrl', function(
      $scope, $q, containerType, DeleteUtil) {

    function init() {
      $scope.containerType = containerType;
    }

    $scope.editContainerType = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteContainerType = function() {
      DeleteUtil.delete($scope.containerType, {
        onDeleteState: 'container-type-list',
        confirmDelete: 'container-type.confirm_delete'
      });
    }

    init();
  });
