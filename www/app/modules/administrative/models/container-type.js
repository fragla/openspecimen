
angular.module('os.administrative.models.containertype', ['os.common.models'])
  .factory('ContainerType', function(osModel) {
    var ContainerType = new osModel('container-types');

    ContainerType.list = function(opts) {
      return ContainerType.query(opts);
    }; 
    return ContainerType;
  });
