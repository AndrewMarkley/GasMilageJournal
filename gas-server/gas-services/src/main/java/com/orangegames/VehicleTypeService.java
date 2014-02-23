package com.orangegames;

import com.orangegames.vehicle.VehicleTypeManager;
import com.orangegames.vehicle.VehicleTypeTO;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *  VehicleTypeService
 */
@WebService(serviceName = "VehicleTypeService")
public class VehicleTypeService {
    @EJB
    VehicleTypeManager vehicleManager;
    
    @WebMethod(action = "getVehicleTypeTO")
    public VehicleTypeTO getVehicleTypeTO(
            @WebParam(name = "vId") int vId)
    {
        return vehicleManager.getVehicleTypeTO(vId);
    }
    
    @WebMethod(action = "saveVehicleType")
    public VehicleTypeTO saveVehicleType(
            @WebParam(name = "data") VehicleTypeTO data)
    {
        return vehicleManager.saveVehicleType(data);
    }
}
