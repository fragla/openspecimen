
angular.module('os.administrative.container-type',
  [
    'ui.router',
    'os.administrative.container-type.list',
    'os.administrative.container-type.detail',
    'os.administrative.container-type.overview',
    'os.administrative.container-type.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('container-type-list', {
        url:'/container-types',
        templateUrl:'modules/administrative/container-type/list.html',
        controller: 'ContainerTypeListCtrl',
        parent: 'signed-in'
      })
      .state('container-type-addedit', {
        url: '/container-type-addedit/:containerTypeId',
        templateUrl: 'modules/administrative/container-type/addedit.html',
        resolve: {
          containerType: function($stateParams, ContainerType) {
            if ($stateParams.containerTypeId) {
              return ContainerType.getById($stateParams.containerTypeId);
            }

            return new ContainerType();
          }
        },
        controller: 'ContainerTypeAddEditCtrl',
        parent: 'signed-in'
      })
      .state('container-type-detail', {
        url: '/container-types/:containerTypeId',
        templateUrl: 'modules/administrative/container-type/detail.html',
        resolve: {
          containerType: function($stateParams, ContainerType) {
            return ContainerType.getById($stateParams.containerTypeId);
          }
        },
        controller: 'ContainerTypeDetailCtrl',
        parent: 'signed-in'
      })
      .state('container-type-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/container-type/overview.html',
        controller: 'ContainerTypeOverviewCtrl',
        parent: 'container-type-detail'
      })
  });

