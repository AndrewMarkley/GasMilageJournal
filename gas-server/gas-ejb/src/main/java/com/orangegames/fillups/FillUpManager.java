package com.orangegames.fillups;

import com.orangegames.crud.CrudManagerBean;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *  FillUpManager
 */
@Stateless
@LocalBean
public class FillUpManager {

    @EJB
    CrudManagerBean crudService;
    
    public FillUpTO getFillUpTO(FillUp data)
    {
        if(data == null) {
            throw new IllegalStateException("FillUp cannot be null");
        }
        if(data.getId() < 1) {
            throw new IllegalArgumentException("FillUp id cannot be less than 1");
        }
        
        FillUpTO result = new FillUpTO();
        result.setId(data.getId());
        result.setComments(data.getComments());
        result.setDeleted(data.isDeleted());
        result.setDistance(data.getDistance());
        result.setFillUpDate(data.getFillUpDate());
        result.setGas(data.getGas());
        result.setMpg(data.getMpg());
        result.setPrice(data.getPrice());
        result.setTotalCost(data.getTotalCost());
        

        return result;
    }

    public FillUpTO getFillUpTO(int carId)
    {
        FillUp result = crudService.find(FillUp.class, carId);
        
        return getFillUpTO(result);
    }
    
    public FillUpTO saveFillUp(FillUpTO data)
    {
        FillUp result = new FillUp();
        if (data.getId() > 0) {
            result = crudService.find(FillUp.class, data.getId());
        }
        
        result.setId(data.getId());
        result.setComments(data.getComments());
        result.setDeleted(data.isDeleted());
        result.setDistance(data.getDistance());
        result.setFillUpDate(data.getFillUpDate());
        result.setGas(data.getGas());
        result.setMpg(data.getMpg());
        result.setPrice(data.getPrice());
        result.setTotalCost(data.getTotalCost());        
        
        result = crudService.save(result);
        
        return getFillUpTO(result);
    }
    
    public void deleteFillUp(FillUp data)
    {
        data.setDeleted(true);
        crudService.save(data);
    }

    public void deleteFillUp(int FillUpId)
    {
        deleteFillUp(crudService.find(FillUp.class, FillUpId));
    }
}