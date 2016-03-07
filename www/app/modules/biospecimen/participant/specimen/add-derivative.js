
angular.module('os.biospecimen.specimen.addderivative', [])
  .controller('AddDerivativeCtrl', function(
    $scope, $state, $stateParams, specimen, cpr, visit, extensionCtxt, 
    SpecimenUtil, ExtensionsUtil) {

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
      $scope.extnOpts = ExtensionsUtil.getExtnOpts($scope.derivative, extensionCtxt);
    }

    function setFreezeThaw() {
      var increment = $scope.derivative.incrementParentFreezeThaw ? 1 : 0;
      $scope.derivative.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles + increment;
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
