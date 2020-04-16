package com.alps.job.admin.core.route.strategy;

import com.alps.job.admin.core.route.ExecutorRouter;
import com.alps.job.common.biz.model.ReturnT;
import com.alps.job.common.biz.model.TriggerParam;

import java.util.List;

/**
 * @author  Yujie.lee on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
