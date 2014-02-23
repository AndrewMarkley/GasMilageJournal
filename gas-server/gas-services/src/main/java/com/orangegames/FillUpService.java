package com.orangegames;

import com.orangegames.fillups.FillUpManager;
import com.orangegames.fillups.FillUpTO;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *  FillUpService
 */
@WebService(serviceName = "FillUpService")
public class FillUpService {
    @EJB
    FillUpManager fillUpManager;
    
    @WebMethod(action = "getFillUpTO")
    public FillUpTO getFillUpTO(
            @WebParam(name = "fillUpId") int fillUpId)
    {
        return fillUpManager.getFillUpTO(fillUpId);
    }
    
    @WebMethod(action = "saveFillUp")
    public FillUpTO saveFillUp(
            @WebParam(name = "data") FillUpTO data)
    {
        return fillUpManager.saveFillUp(data);
    }
}
