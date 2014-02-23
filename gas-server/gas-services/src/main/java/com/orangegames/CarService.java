package com.orangegames;

import com.orangegames.car.CarManager;
import com.orangegames.car.CarTO;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *  CarService
 */
@WebService(serviceName = "CarService")
public class CarService {
    @EJB
    CarManager carManager;
    
    @WebMethod(action = "getCarTO")
    public CarTO getCarTO(
            @WebParam(name = "carId") int carId)
    {
        return carManager.getCarTO(carId);
    }
    
    @WebMethod(action = "saveCar")
    public CarTO saveCar(
            @WebParam(name = "data") CarTO data)
    {
        return carManager.saveCar(data);
    }
}
