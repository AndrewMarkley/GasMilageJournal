package com.orangegames.car;

import com.orangegames.crud.CrudManagerBean;
import com.orangegames.fillups.FillUp;
import com.orangegames.fillups.FillUpManager;
import com.orangegames.fillups.FillUpTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *  CarManager
 */
@Stateless
@LocalBean
public class CarManager {

    @EJB
    CrudManagerBean crudService;
    @EJB
    FillUpManager fillUpManager;
    
    public CarTO getCarTO(Car data)
    {
        if(data == null) {
            throw new IllegalStateException("Car cannot be null");
        }
        if(data.getId() < 1) {
            throw new IllegalArgumentException("Car id cannot be less than 1");
        }
        
        CarTO result = new CarTO();
        result.setId(data.getId());
        result.setEngineSize(data.getEngineSize());
        result.setMilage(data.getMilage());
        result.setName(data.getName());
        result.setDeleted(data.isDeleted());
        
        List<FillUpTO> fillUps = new ArrayList<>();
        for(FillUp fillUp : data.getFillUps()) {
            fillUps.add(fillUpManager.getFillUpTO(fillUp));
        }
        result.setFillUps(fillUps);
        
        return result;
    }

    public CarTO getCarTO(int carId)
    {
        Car result = crudService.find(Car.class, carId);
        
        return getCarTO(result);
    }
    
    public CarTO saveCar(CarTO data)
    {
        Car result = new Car();
        if (data.getId() > 0) {
            result = crudService.find(Car.class, data.getId());
        }
        
        result.setId(data.getId());
        result.setEngineSize(data.getEngineSize());
        result.setMilage(data.getMilage());
        result.setName(data.getName());
        result.setDeleted(data.isDeleted());
        
        List<FillUp> fillUps = new ArrayList<>();
        for(FillUpTO fu : data.getFillUps()) {
            int id = fillUpManager.saveFillUp(fu).getId();
            fillUps.add(crudService.find(FillUp.class, id));
        }
        result.setFillUps(fillUps);
        result = crudService.save(result);
        
        return getCarTO(result);
    }
    
    public void deleteCar(Car data)
    {
        data.setDeleted(true);
        crudService.save(data);
    }

    public void deleteCar(int CarId)
    {
        deleteCar(crudService.find(Car.class, CarId));
    }
}
