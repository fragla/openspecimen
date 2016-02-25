
angular.module('os.administrative.models.container-type', ['os.common.models'])
  .factory('ContainerType', function(osModel, $q, $http) {
    var ContainerType = new osModel('container-types');

    ContainerType.list = function(opts) {
      var defOpts = {topLevelContainerTypes: true};
      return ContainerType.query(angular.extend(defOpts, opts || {}));
    }; 

    return ContainerType;
  });
