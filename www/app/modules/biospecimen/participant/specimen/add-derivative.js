
angular.module('os.biospecimen.specimen.addderivative', [])
  .controller('AddDerivativeCtrl', function(
    $scope, $state, $stateParams, specimen, cpr, visit, extensionCtxt, SpecimenUtil, Util) {

    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.derivative = SpecimenUtil.getNewDerivative($scope);
      SpecimenUtil.loadSpecimenClasses($scope);
      SpecimenUtil.loadPathologyStatuses($scope);

      if ($scope.parentSpecimen.freezeThawCycle) {
        $scope.derivative.increaseParentFreezeThaw = true;
        setFreezeThaw();
      }


      $scope.deFormCtrl = {};
      $scope.extnOpts = Util.getExtnOpts($scope.derivative, extensionCtxt);
    }

    function setFreezeThaw() {
      if ($scope.derivative.increaseParentFreezeThaw) {
        $scope.derivative.freezeThawCycle = $scope.parentSpecimen.freezeThawCycle + 1;
      } else {
        $scope.derivative.freezeThawCycle = $scope.parentSpecimen.freezeThawCycle;
      }
    }

    $scope.setFreezeThaw = setFreezeThaw;

    $scope.loadSpecimenTypes = function(specimenClass, notClear) {
      SpecimenUtil.loadSpecimenTypes($scope, specimenClass, notClear);
    };

    $scope.createDerivative = function() {
      SpecimenUtil.createDerivatives($scope);
    };

    $scope.revertEdit = function () {
      $scope.back();
    }

    init();
  });
