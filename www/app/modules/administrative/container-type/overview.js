
angular.module('os.administrative.container-type.overview', ['os.administrative.models'])
  .controller('ContainerTypeOverviewCtrl', function($scope, containerType) {

      $scope.containerType = containerType;
  });

