
angular.module('os.administrative.models.containertype', ['os.common.models'])
  .factory('ContainerType', function(osModel) {
    var ContainerType = new osModel('container-types');
 
    return ContainerType;
  });
