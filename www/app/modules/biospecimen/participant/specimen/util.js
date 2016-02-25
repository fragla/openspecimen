angular.module('os.biospecimen.specimen')
  .factory('SpecimenUtil', function(Specimen, PvManager, Alerts) {

    function collectAliquots(scope) {
      var spec = scope.aliquotSpec;
      var parent = scope.parentSpecimen;
      var extensionDetail = getExtensionDetail(scope);

      if (!!spec.qtyPerAliquot && !!spec.noOfAliquots) {
        var requiredQty = spec.qtyPerAliquot * spec.noOfAliquots;
        if (requiredQty > parent.availableQty) {
          Alerts.error("specimens.errors.insufficient_qty");
          return;
        }
      } else if (!!spec.qtyPerAliquot) {
        spec.noOfAliquots = Math.floor(parent.availableQty / spec.qtyPerAliquot);
      } else if (!!spec.noOfAliquots) {
        spec.qtyPerAliquot = Math.round(parent.availableQty / spec.noOfAliquots * 10000) / 10000;
      }

      if (scope.aliquotSpec.createdOn.getTime() < scope.parentSpecimen.createdOn) {
        Alerts.error("specimens.errors.created_on_lt_parent");
        return;
      } else if (scope.aliquotSpec.createdOn > new Date()) {
        Alerts.error("specimens.errors.created_on_gt_curr_time");
        return;
      }

      parent.isOpened = parent.hasChildren = true;
      parent.depth = 0;
      parent.closeAfterChildrenCreation = spec.closeParent;

      if (parent.freezeThawCycle) {
        parent.increaseFreezeThaw = spec.increaseParentFreezeThaw;
        if (spec.increaseParentFreezeThaw && parent.freezeThawCycle >= spec.freezeThawCycle ||
          !spec.increaseParentFreezeThaw && parent.freezeThawCycle > spec.freezeThawCycle) {
          Alerts.error('specimens.child_freeze_thaw_cycle_lt_parent');
          return;
        }
      }

      var aliquot = new Specimen({
        lineage: 'Aliquot',
        specimenClass: parent.specimenClass,
        type: parent.type,
        parentId: parent.id,
        initialQty: spec.qtyPerAliquot,
        storageLocation: {name: '', positionX:'', positionY: ''},
        status: 'Pending',
        children: [],
        cprId: scope.cpr.id,
        visitId: parent.visitId,
        createdOn: spec.createdOn,
        freezeThawCycle: spec.freezeThawCycle,

        selected: true,
        parent: parent,
        depth: 1,
        isOpened: true,
        hasChildren: false,
        labelFmt: scope.cpr.aliquotLabelFmt
      });

      var aliquots = [];
      for (var i = 0; i < spec.noOfAliquots; ++i) {
        var clonedAlqt = angular.copy(aliquot);
        clonedAlqt.extensionDetail = extensionDetail;
        aliquots.push(clonedAlqt);
      }

      parent.children = [].concat(aliquots);
      var specimens = aliquots;
      specimens.unshift(parent);
      return specimens;
    }

    function createDerivatives(scope) {
      var closeParent = scope.derivative.closeParent;
      scope.derivative.extensionDetail = getExtensionDetail(scope);
      delete scope.derivative.closeParent;

      if (scope.derivative.createdOn.getTime() < scope.parentSpecimen.createdOn) {
        Alerts.error("specimens.errors.created_on_lt_parent");
        return;
      } else if (scope.derivative.createdOn > new Date()) {
        Alerts.error("specimens.errors.created_on_gt_curr_time");
        return;
      }

      var parent = scope.parentSpecimen;
      var increaseParentFreezeThaw = scope.derivative.increaseParentFreezeThaw;
      if (parent.freezeThawCycle) {
        parent.increaseFreezeThaw = increaseParentFreezeThaw;
        if (increaseParentFreezeThaw && parent.freezeThawCycle >= scope.derivative.freezeThawCycle ||
          !increaseParentFreezeThaw && parent.freezeThawCycle > scope.derivative.freezeThawCycle) {
          Alerts.error('specimens.child_freeze_thaw_cycle_lt_parent');
          return;
        }

        delete scope.derivative.increaseParentFreezeThaw;
      }

      var specimensToSave = undefined;
      if (closeParent || increaseParentFreezeThaw) {
        specimensToSave = [new Specimen({
          id: scope.parentSpecimen.id,
          lineage: scope.parentSpecimen.lineage,
          visitId: scope.visit.id,
          closeAfterChildrenCreation: closeParent,
          increaseFreezeThaw: scope.parentSpecimen.increaseFreezeThaw,
          children: [scope.derivative]
        })];
      } else {
        specimensToSave = [scope.derivative];
      }

      return Specimen.save(specimensToSave).then(
        function(result) {
          scope.revertEdit();
        }
      );
    }

    function getNewDerivative(scope) {
      return new Specimen({
        parentId: scope.parentSpecimen.id,
        lineage: 'Derived',
        storageLocation: {},
        status: 'Collected',
        visitId: scope.visit.id,
        pathology: scope.parentSpecimen.pathology,
        closeParent: false,
        createdOn : new Date()
      });
    }

    function loadSpecimenClasses(scope) {
      if (scope.classesLoaded) {
        return;
      }

      scope.specimenClasses = PvManager.getPvs('specimen-class');
      scope.classesLoaded = true;
    }

    function loadSpecimenTypes(scope, specimenClass, notClear) {
      if (!notClear) {
        scope.derivative.type = '';
      }

      if (!specimenClass) {
        scope.specimenTypes = [];
        return;
      }

      if (!scope.specimenClasses[specimenClass]) {
        scope.specimenClasses[specimenClass] = PvManager.getPvsByParent('specimen-class', specimenClass);
      }

      scope.specimenTypes = scope.specimenClasses[specimenClass];
    }

    function loadPathologyStatuses(scope) {
      if (scope.pathologyLoaded) {
        return;
      }

      scope.pathologyStatuses = PvManager.getPvs('pathology-status');
      scope.pathologyLoaded = true;
    }

    function getExtensionDetail(scope) {
      var formCtrl = scope.deFormCtrl.ctrl;
      if (!formCtrl || !formCtrl.validate()) {
        return;
      }

      return formCtrl.getFormData();
    }

    return {
      collectAliquots: collectAliquots,

      createDerivatives: createDerivatives,

      getNewDerivative: getNewDerivative,

      loadSpecimenClasses: loadSpecimenClasses,

      loadSpecimenTypes: loadSpecimenTypes,

      loadPathologyStatuses: loadPathologyStatuses
    }
  });
