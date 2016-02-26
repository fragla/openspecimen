angular.module('os.administrative.container-type.detail', ['os.administrative.models'])
  .controller('ContainerTypeDetailCtrl', function(
      $scope, containerType) {

    $scope.containerType = containerType;

  });
