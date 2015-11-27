
angular.module('os.administrative.models.shipment', ['os.common.models'])
  .factory('Shipment', function(osModel) {
    var Shipment = osModel('shipments');

    Shipment.prototype.getType = function() {
      return 'shipment';
    }

    Shipment.prototype.getDisplayName = function() {
      return this.name;
    }

    Shipment.prototype.$saveProps = function() {
      angular.forEach(this.shipmentItems, function(shipmentItem) {
        shipmentItem.specimen = {
          id: shipmentItem.specimen.id,
          storageLocation: shipmentItem.specimen.storageLocation
        };
      });

      return this;
    }

    return Shipment;
  }
);
