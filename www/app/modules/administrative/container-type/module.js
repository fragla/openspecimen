
angular.module('os.administrative.containertype',
  [
    'ui.router',
    'os.administrative.containertype.list',
    'os.administrative.containertype.detail',
    'os.administrative.containertype.overview'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('container-type-list', {
        url:'/container-type',
        templateUrl:'modules/administrative/container-type/list.html',
        controller: 'ContainerTypeListCtrl',
        parent: 'signed-in'
      })
      .state('container-type-detail', {
        url: '/container-type/:containerTypeId',
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

