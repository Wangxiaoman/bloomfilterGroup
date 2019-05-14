package com.paradigm.quartz;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.paradigm.log.CommonLogger;
import com.paradigm.service.ActionService;

@Component
public class ActionBloomFilterQuartz {
    
    @Resource
    private ActionService actionService;
    
    @Scheduled(cron = "0 30 0/5 * * ?")
    public void saveBloomFilter(){
        CommonLogger.info("[bloomfilter groups] save to file begin ~");
        long bTime = System.currentTimeMillis();
        actionService.saveCurrentGroupToFile();
        long eTime = System.currentTimeMillis();
        
        CommonLogger.info("[bloomfilter groups] save to file finish, cost ms :{}",(eTime - bTime));
    }
    
    @Scheduled(cron = "0 0 0 1,8,15,23 * ?")
    public void alterBloomFilter(){
        CommonLogger.info("[bloomfilter groups] alter bloomfilter begin ~");
        long bTime = System.currentTimeMillis();
        actionService.alterMonthBloomFilter();
        long eTime = System.currentTimeMillis();
        
        CommonLogger.info("[bloomfilter groups] alter bloomfilter finish, cost ms :{}",(eTime - bTime));
    }
    
    

}
