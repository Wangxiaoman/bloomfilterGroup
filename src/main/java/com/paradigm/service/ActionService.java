package com.paradigm.service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.BloomFilter;
import com.paradigm.bloomfilter.BloomFilterGroup;
import com.paradigm.log.CommonLogger;
import com.paradigm.service.async.AsyncService;

@Service
public class ActionService {

    // private BlockingQueue<String> actionQueue = new ArrayBlockingQueue<>(10000000);
    //
    // public BlockingQueue<String> getActionQueue() {
    // return this.actionQueue;
    // }

    // private static final String USER_ACTION_KEY = "uf:%s:%s:%s:%s";

    private String getUserAction(String userId, String itemSetId, String itemId, int action) {
        // return String.format(USER_ACTION_KEY, itemSetId, userId, itemId, action);
        return "uf:" + itemSetId + ":" + userId + ":" + itemId + ":" + action;
    }

    @Resource
    private ActionBloomFilterGroup actionBloomFilterGroup;
    @Resource
    private AsyncService asyncService;

    // @PostConstruct
    // private void init() {
    // Thread saveThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // String action = StringUtils.EMPTY;
    // while(true){
    // try {
    // action = actionQueue.take();
    // } catch (Exception ex) {
    // CommonLogger.error("get action from queue error, ex:", ex);
    // }
    // if (StringUtils.isNotBlank(action)) {
    // CommonLogger.infoOneInTenThousand("save action to bloomFilter,actionQueue
    // size:{}, action:{}",actionQueue.size(),action);
    // actionBloomFilterGroup.put(action);
    // }
    // }
    // }
    // });
    // saveThread.setName("saveActionToBloomFilter");
    // saveThread.setDaemon(true);
    // saveThread.start();
    // }

    public void putUserAction(String userId, String itemSetId, String itemId, int action) {
        String userAction = getUserAction(userId, itemSetId, itemId, action);
        // actionQueue.add(userAction);
        CommonLogger.bloomInfo(userAction);
        actionBloomFilterGroup.put(userAction);
    }

    public void putUserAction(String action) {
        CommonLogger.bloomInfo(action);
        actionBloomFilterGroup.put(action);
    }

    public List<String> getUserActionFilter(String userId, String itemSetId, List<String> itemIds,
            int action) {
        Iterator<String> iter = itemIds.iterator();
        while (iter.hasNext()) {
            String itemId = iter.next();
            String userAction = getUserAction(userId, itemSetId, itemId, action);
            if (actionBloomFilterGroup.contains(userAction)) {
                iter.remove();
            }
        }
        return itemIds;
    }

    public JSONObject getGroupDesc() {
        return actionBloomFilterGroup.getDesc();
    }

    public void saveCurrentGroupToFile() {
        BloomFilterGroup current = actionBloomFilterGroup.getCurrentFilterGroup().get();
        String filePath = current.getFilePath() + "/" + current.getGroupName();
        File fp = new File(filePath);
        if(!fp.exists()){
            fp.mkdirs();
        }
        for (Entry<String, BloomFilter<String>> entry : current.getFilterGroup().entrySet()) {
            String fileName = filePath + "/" + entry.getKey();
            // async mutiThread save
            asyncService.saveBloomFilter(fileName, entry.getValue());
        }
    }
    
    public void moveCurrentToLastFile(){
        String filePath = actionBloomFilterGroup.getFilePath() + "/" + actionBloomFilterGroup.getCurrentGroupName();
        File fp = new File(filePath);
        if(!fp.exists()){
            fp.mkdirs();
        }
        
        String lastPath = actionBloomFilterGroup.getFilePath() + "/" + actionBloomFilterGroup.getLastGroupName();
        File lp = new File(lastPath);
        
        CommonLogger.info("move current file, filePath:{},lastPath:{}", filePath, lastPath);
        try {
            FileUtils.deleteDirectory(lp);
            FileUtils.moveDirectory(fp, lp);
        } catch (IOException e) {
            CommonLogger.error("move file directory error, ex:",e);
        }
    }
    
    public void alterMonthBloomFilter(){
        actionBloomFilterGroup.alterMonthBloomFilter();
        moveCurrentToLastFile();
    }
    
    public boolean contains(String action){
        return actionBloomFilterGroup.contains(action);
    }
}
