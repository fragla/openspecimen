
angular.module('os.administrative.container-type.overview', ['os.administrative.models'])
  .controller('ContainerTypeOverviewCtrl', function($scope, $q, $translate, containerType) {
    var placeholder = {};

    console.log(containerType);
    function init() {
      placeholder = {name: $translate.instant('common.loading')};
      containerType.isOpened = true;
      $scope.containerType = containerType;
    }

    init();
  });

