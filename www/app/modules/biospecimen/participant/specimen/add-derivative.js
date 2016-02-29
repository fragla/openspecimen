
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

      if ($scope.parentSpecimen.freezeThawCycles != null) {
        $scope.derivative.incrementParentFreezeThaw = true;
        setFreezeThaw();
      }


      $scope.deFormCtrl = {};
      $scope.extnOpts = Util.getExtnOpts($scope.derivative, extensionCtxt);
    }

    function setFreezeThaw() {
      if ($scope.derivative.incrementParentFreezeThaw) {
        $scope.derivative.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles + 1;
      } else {
        $scope.derivative.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles;
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
