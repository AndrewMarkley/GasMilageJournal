package com.orangegames.vehicle;

import com.orangegames.crud.CrudManagerBean;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *  VehicleTypeManager
 */
@Stateless
@LocalBean
public class VehicleTypeManager {

    @EJB
    CrudManagerBean crudService;
    
    public VehicleTypeTO getVehicleTypeTO(VehicleType data)
    {
        if(data == null) {
            throw new IllegalStateException("VehicleType cannot be null");
        }
        if(data.getId() < 1) {
            throw new IllegalArgumentException("VehicleType id cannot be less than 1");
        }
        
        VehicleTypeTO result = new VehicleTypeTO();
        result.setId(data.getId());
        result.setMake(data.getMake());
        result.setModel(data.getModel());
        result.setModelYear(data.getModelYear());
        result.setDeleted(data.isDeleted());

        return result;
    }

    public VehicleTypeTO getVehicleTypeTO(int carId)
    {
        VehicleType result = crudService.find(VehicleType.class, carId);
        
        return getVehicleTypeTO(result);
    }
    
    public VehicleTypeTO saveVehicleType(VehicleTypeTO data)
    {
        VehicleType result = new VehicleType();
        if (data.getId() > 0) {
            result = crudService.find(VehicleType.class, data.getId());
        }
        
        result.setMake(data.getMake());
        result.setModel(data.getModel());
        result.setModelYear(data.getModelYear());
        result.setDeleted(data.isDeleted());
                
        result = crudService.save(result);
        
        return getVehicleTypeTO(result);
    }
    
    public void deleteVehicleType(VehicleType data)
    {
        data.setDeleted(true);
        crudService.save(data);
    }

    public void deleteVehicleType(int VehicleTypeId)
    {
        deleteVehicleType(crudService.find(VehicleType.class, VehicleTypeId));
    }
}