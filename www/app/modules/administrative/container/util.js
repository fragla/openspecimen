
angular.module('os.administrative.container.util', ['os.common.box'])
  .factory('ContainerUtil', function($q, CollectionProtocol, Container,
    BoxLayoutUtil, NumberConverterUtil, PvManager, Util) {

    var allSpecimenTypes = undefined;

    function createSpmnPos(container, label, x, y, oldOccupant) {
      return {
        occuypingEntity: 'specimen', 
        occupyingEntityName: label,
        posOne: NumberConverterUtil.fromNumber(container.columnLabelingScheme, x),
        posTwo: NumberConverterUtil.fromNumber(container.rowLabelingScheme, y),
        posOneOrdinal: x,
        posTwoOrdinal: y,
        oldOccupant: oldOccupant
      };
    }

    function getOpts(container, allowClicks) {
      return {
        box: {
          instance             : container,
          row                  : function(occupant) { return occupant.posTwoOrdinal; },
          column               : function(occupant) { return occupant.posOneOrdinal; },
          numberOfRows         : function() { return container.noOfRows; },
          numberOfColumns      : function() { return container.noOfColumns; },
          rowLabelingScheme    : function() { return container.rowLabelingScheme; },
          columnLabelingScheme : function() { return container.columnLabelingScheme; }
        },

        occupants: [],
        occupantName: function(occupant) {
          return occupant.occupyingEntityName
        },
        occupantSref: function(occupant) {
          if (occupant.occuypingEntity == 'specimen') {
            return 'specimen({specimenId: ' + occupant.occupyingEntityId + '})';
          } else {
            return 'container-detail.locations({containerId: ' + occupant.occupyingEntityId + '})';
          }
        },
        allowClicks: allowClicks,
        isVacatable: function(occupant) {
          return occupant.occuypingEntity == 'specimen';
        },
        createCell: function(label, x, y, existing) {
          return createSpmnPos(container, label, x, y, existing);
        }
      };
    }

    function loadAllCpsAndSpecimenTypes(scope, entity) {
      loadAllCps(scope, entity);
      loadAllSpecimenTypes(scope);
    };

    function loadAllCps(scope, entity) {
      CollectionProtocol.query({repositoryName: entity.siteName}).then(
        function(cps) {
          scope.cps = cps.map(function(cp) { return cp.shortTitle; });

          // fix - pre-selected cps were getting cleared
          entity.allowedCollectionProtocols = scope.allowedCps;
        }
      );
    };

    function loadAllSpecimenTypes(scope) {
      if (allSpecimenTypes) {
    	Util.assign(scope.specimenTypes, allSpecimenTypes);  
        var d = $q.defer();
        d.resolve(allSpecimenTypes);
        return d.promise;
      }

      return PvManager.loadPvsByParent('specimen-class', undefined, true).then(
        function(specimenTypes) {
          allSpecimenTypes = specimenTypes;
          Util.assign(scope.specimenTypes, specimenTypes);
          return allSpecimenTypes;
        }
      );
    };

    function restrictCpsAndSpecimenTypes(scope, entity) {
      var parentName = entity.storageLocation ? entity.storageLocation.name : entity.parentContainer;
      Container.getByName(parentName).then(
        function(parentContainer) {
          restrictCps(scope, entity, parentContainer);
          restrictSpecimenTypes(scope, parentContainer);
        }
      );
    };

    function restrictCps(scope, entity, parentContainer) {
      var parentCps = parentContainer.calcAllowedCollectionProtocols;
      if (parentCps.length > 0) {
        scope.cps = parentCps;
      } else {
        loadAllCps(scope, entity);
      }

      entity.allowedCollectionProtocols = scope.allowedCps;
    };

    function restrictSpecimenTypes(scope, parentContainer) {
      if (allSpecimenTypes) {
        filterSpecimenTypes(scope, parentContainer);
      } else {
        loadAllSpecimenTypes(scope).then(
          function() {
            filterSpecimenTypes(scope, parentContainer);
          }
        );
      }
    };

    function filterSpecimenTypes(scope, parentContainer) {
      var allowedClasses = parentContainer.calcAllowedSpecimenClasses;
      var allowedTypes = parentContainer.calcAllowedSpecimenTypes;
      scope.specimenTypeSelectorOpts.allowAll = allowedClasses;

      var filtered = allSpecimenTypes.filter(
        function(specimenType) {
          return allowedClasses.indexOf(specimenType.parent) >= 0 ||
                   allowedTypes.indexOf(specimenType.value) >= 0;
        }
      );
      Util.assign(scope.specimenTypes, filtered);
    };

    return {
      fromOrdinal: NumberConverterUtil.fromNumber,

      getOpts: getOpts,

      assignPositions: function(container, occupancyMap, inputLabels, vacateOccupants) {
        var opts = getOpts(container, false);
        opts.occupants = occupancyMap;

        var result = BoxLayoutUtil.assignCells(opts, inputLabels, vacateOccupants);
        return {map: result.occupants, noFreeLocs: result.noFreeLocs};
      },

      loadAllCpsAndSpecimenTypes: loadAllCpsAndSpecimenTypes,

      loadAllCps: loadAllCps,

      restrictCpsAndSpecimenTypes: restrictCpsAndSpecimenTypes
    };
  });
