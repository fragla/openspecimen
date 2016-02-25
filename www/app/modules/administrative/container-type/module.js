
angular.module('os.administrative.container-type',
  [
    'ui.router',
    'os.administrative.container-type.list',
    'os.administrative.container-type.detail',
    'os.administrative.container-type.overview'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('container-type-root', {
        abstract: true,
        template: '<div ui-view></div>',
        parent: 'signed-in'
      })
      .state('container-type-list', {
        url:'/container-type',
        templateUrl:'modules/administrative/container-type/list.html',
        controller: 'ContainerTypeListCtrl',
        parent: 'container-type-root'
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
        parent: 'container-type-root'
      })
      .state('container-type-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/container-type/overview.html',
        controller: 'ContainerTypeOverviewCtrl',
        parent: 'container-type-detail'
      })
    });

