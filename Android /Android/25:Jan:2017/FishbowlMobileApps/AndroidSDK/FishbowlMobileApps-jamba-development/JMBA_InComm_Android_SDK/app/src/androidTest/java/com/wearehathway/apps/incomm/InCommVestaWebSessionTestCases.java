package com.wearehathway.apps.incomm;

import com.wearehathway.apps.incomm.Interfaces.InCommVestaWebSessionDataCallback;
import com.wearehathway.apps.incomm.Models.InCommVestaWebSessionData;
import com.wearehathway.apps.incomm.Services.InCommVestaWebSessionService;

/**
 * Created by Nauman Afzaal on 17/09/15.
 */
public class InCommVestaWebSessionTestCases extends BaseTests
{

    public void testVestWebSessionData()
    {
        InCommVestaWebSessionService.getVestaWebSessionData(new InCommVestaWebSessionDataCallback()
        {
            @Override
            public void onVestaWebSessionDataCallback(InCommVestaWebSessionData inCommVestaWebSessionData, Exception error)
            {
                assertNull("Vesta web session call failed", error);
                assertNotNull("Invalid Vesta web session data response", inCommVestaWebSessionData);
                assertNotSame("Invalid Org Id", inCommVestaWebSessionData.getVestaOrgId(), "");
                assertNotSame("Invalid Session Id", inCommVestaWebSessionData.getVestaWebSessionId(), "");
                latch.countDown();
            }
        });
        waitForCompletion();
    }
}
