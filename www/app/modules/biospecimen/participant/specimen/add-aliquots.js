
angular.module('os.biospecimen.specimen.addaliquots', [])
  .controller('AddAliquotsCtrl', function(
    $scope, $rootScope, $state, $stateParams, specimen, cpr, visit, extensionCtxt,
    CollectSpecimensSvc, SpecimenUtil, Util, Alerts) {

    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.aliquotSpec = {createdOn : new Date()};

      if ($scope.parentSpecimen.freezeThawCycle) {
        $scope.aliquotSpec.increaseParentFreezeThaw = true;
        setFreezeThaw();
      }

      //
      // On successful collection of aliquots, direct user to specimen detail view
      // TODO: where to go should be state input param
      //
      if ($rootScope.stateChangeInfo.fromState.url.indexOf("collect-specimens") == 1) {
         var params = {specimenId:  $scope.parentSpecimen.id, srId:  $scope.parentSpecimen.reqId};
         $state.go('specimen-detail.overview', params);
      }

      $scope.deFormCtrl = {};
      $scope.extnOpts = Util.getExtnOpts($scope.aliquotSpec, extensionCtxt);
    }

    function getState() {
      return {state: $state.current, params: $stateParams};
    }

    function setFreezeThaw() {
      if ($scope.aliquotSpec.increaseParentFreezeThaw) {
        $scope.aliquotSpec.freezeThawCycle = $scope.parentSpecimen.freezeThawCycle + 1;
      } else {
        $scope.aliquotSpec.freezeThawCycle = $scope.parentSpecimen.freezeThawCycle;
      }
    }

    $scope.setFreezeThaw = setFreezeThaw;

    $scope.collectAliquots = function() {
      var specimens = SpecimenUtil.collectAliquots($scope);
      if (specimens) {
        CollectSpecimensSvc.collect(getState(), $scope.visit, specimens, parent);
      }
    }

    init();
  });
