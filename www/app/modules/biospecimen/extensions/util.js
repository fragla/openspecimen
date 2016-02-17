
angular.module('os.biospecimen.extensions.util', [])
  .factory('ExtensionsUtil', function($modal, Form, Alerts, ApiUrls) {
    var filesUrl = ApiUrls.getBaseUrl() + 'form-files';

    function getFileDownloadUrl(formId, recordId, ctrlName) {
      var params = '?formId=' + formId +
                   '&recordId=' + recordId +
                   '&ctrlName=' + ctrlName +
                   '&_reqTime=' + new Date().getTime();

      return filesUrl + params;
    }

    function deleteRecord(record, onDeletion) {
      var modalInstance = $modal.open({
        templateUrl: 'modules/biospecimen/extensions/delete-record.html',
        controller: function($scope, $modalInstance) {
          $scope.record= record;

          $scope.yes = function() {
            Form.deleteRecord(record.formId, record.recordId).then(
              function(result) {
                $modalInstance.close();
                Alerts.success('extensions.record_deleted');
              },

              function() {
                $modalInstance.dismiss('cancel');
              }
            );
          }

          $scope.no = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });

      modalInstance.result.then(
        function() {
          if (typeof onDeletion == 'function') {
            onDeletion(record);
          }
        }
      );
    };
    
    function createExtensionFieldMap(entity) {
      var extensionDetail = entity.extensionDetail;
      if (extensionDetail) {
        extensionDetail.attrsMap = {
          id: extensionDetail.id,
          containerId: extensionDetail.formId
        };
        angular.forEach(extensionDetail.attrs, function(attr) {
          extensionDetail.attrsMap[attr.name] = attr.value;
        });
      }
    }

    function getExtnOpts(entity, extnCtxt) {
      if (!extnCtxt) {
        return undefined;
      }

      createExtensionFieldMap(entity);

      return {
        formId: extnCtxt.formId,
        recordId: !!entity.id && !!entity.extensionDetail ? entity.extensionDetail.id : undefined,
        formCtxtId: parseInt(extnCtxt.formCtxtId),
        objectId: entity.id,
        formData: !!entity.id && !!entity.extensionDetail ? entity.extensionDetail.attrsMap : {},
        showActionBtns: false,
        showPanel: false, 
        labelAlignment: 'horizontal'
      };
    }
    
    return {
      getFileDownloadUrl: getFileDownloadUrl,

      deleteRecord: deleteRecord,

      createExtensionFieldMap: createExtensionFieldMap,

      getExtnOpts: getExtnOpts
    }
 
  });
