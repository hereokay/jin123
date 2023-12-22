package com.muselive.bemuselive.service.quartz;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BatchService {
    private final QuartzService quartzService;

    @PostConstruct
    public void init(){
        try{
            quartzService.addCronJob(QuartzJob.class,"QuartzServiceScheduler","QuartzServiceScheduler",null,"0/2 * * * * ?");
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

}
