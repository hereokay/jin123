package com.muselive.bemuselive.service.quartz;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QuartzJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime localDateTime = LocalDateTime.now();
    }


}
