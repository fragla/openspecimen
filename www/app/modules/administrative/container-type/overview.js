
angular.module('os.administrative.containertype.overview', ['os.administrative.models'])
  .controller('ContainerTypeOverviewCtrl', function($scope, $q, $translate, containerType) {
    var placeholder = {};

    function init() {
      placeholder = {name: $translate.instant('common.loading')};
      containerType.isOpened = true;
      $scope.containerType = containerType;
    }

    init();
  });

