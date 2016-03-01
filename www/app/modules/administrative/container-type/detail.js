angular.module('os.administrative.containertype.detail', ['os.administrative.models'])
  .controller('ContainerTypeDetailCtrl', function($scope, containerType) {
    $scope.containerType = containerType;
  });
