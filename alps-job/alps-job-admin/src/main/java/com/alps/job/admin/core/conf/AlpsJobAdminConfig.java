package com.alps.job.admin.core.conf;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.alps.job.admin.core.alarm.JobAlarmer;
import com.alps.job.admin.core.scheduler.AlpsJobScheduler;
import com.alps.job.admin.dao.*;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * ALPS-JOB config
 *
 * @author  Yujie.lee 2017-04-28
 */

@Component
public class AlpsJobAdminConfig implements InitializingBean, DisposableBean {

    private static AlpsJobAdminConfig adminConfig = null;
    public static AlpsJobAdminConfig getAdminConfig() {
        return adminConfig;
    }


    // ---------------------- AlpsJobScheduler ----------------------

    private AlpsJobScheduler alpsJobScheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;

        alpsJobScheduler = new AlpsJobScheduler();
        alpsJobScheduler.init();
    }

    @Override
    public void destroy() throws Exception {
        alpsJobScheduler.destroy();
    }


    // ---------------------- AlpsJobScheduler ----------------------

    // conf
    @Value("${alps.job.i18n}")
    private String i18n;

    @Value("${alps.job.accessToken}")
    private String accessToken;

    @Value("${spring.mail.username}")
    private String emailUserName;

    @Value("${alps.job.triggerpool.fast.max}")
    private int triggerPoolFastMax;

    @Value("${alps.job.triggerpool.slow.max}")
    private int triggerPoolSlowMax;

    @Value("${alps.job.logretentiondays}")
    private int logretentiondays;

    // dao, service

    @Resource
    private AlpsJobLogDao alpsJobLogDao;
    @Resource
    private AlpsJobInfoDao alpsJobInfoDao;
    @Resource
    private AlpsJobRegistryDao alpsJobRegistryDao;
    @Resource
    private AlpsJobGroupDao alpsJobGroupDao;
    @Resource
    private AlpsJobLogReportDao alpsJobLogReportDao;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private DataSource dataSource;
    @Resource
    private JobAlarmer jobAlarmer;


    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public int getTriggerPoolFastMax() {
        if (triggerPoolFastMax < 200) {
            return 200;
        }
        return triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        if (triggerPoolSlowMax < 100) {
            return 100;
        }
        return triggerPoolSlowMax;
    }

    public int getLogretentiondays() {
        if (logretentiondays < 7) {
            return -1;  // Limit greater than or equal to 7, otherwise close
        }
        return logretentiondays;
    }

    public AlpsJobLogDao getXxlJobLogDao() {
        return alpsJobLogDao;
    }

    public AlpsJobInfoDao getXxlJobInfoDao() {
        return alpsJobInfoDao;
    }

    public AlpsJobRegistryDao getXxlJobRegistryDao() {
        return alpsJobRegistryDao;
    }

    public AlpsJobGroupDao getXxlJobGroupDao() {
        return alpsJobGroupDao;
    }

    public AlpsJobLogReportDao getXxlJobLogReportDao() {
        return alpsJobLogReportDao;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JobAlarmer getJobAlarmer() {
        return jobAlarmer;
    }

}
