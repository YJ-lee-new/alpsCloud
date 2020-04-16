package com.alps.job.admin.core.route.strategy;

import com.alps.job.admin.core.route.ExecutorRouter;
import com.alps.job.common.biz.model.ReturnT;
import com.alps.job.common.biz.model.TriggerParam;

import java.util.List;
import java.util.Random;

/**
 * @author  Yujie.lee on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
