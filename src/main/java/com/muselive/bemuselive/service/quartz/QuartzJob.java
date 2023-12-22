package com.muselive.bemuselive.service.quartz;

import com.muselive.bemuselive.service.LibraryBatchService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QuartzJob extends QuartzJobBean {
    private final LibraryBatchService libraryBatchService;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime localDateTime = LocalDateTime.now();
        libraryBatchService.LibraryLateFee();
    }


}
