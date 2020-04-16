package com.alps.job.admin.core.alarm;

import com.alps.job.admin.core.model.AlpsJobInfo;
import com.alps.job.admin.core.model.AlpsJobLog;

/**
 * @author  Yujie.lee 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(AlpsJobInfo info, AlpsJobLog jobLog);

}
