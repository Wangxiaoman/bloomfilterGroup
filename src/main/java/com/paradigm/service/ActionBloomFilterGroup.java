package com.paradigm.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import com.alibaba.fastjson.JSONObject;
import com.paradigm.bloomfilter.BloomFilterGroup;
import com.paradigm.log.CommonLogger;
import com.paradigm.tools.DateTools;

public class ActionBloomFilterGroup {
    private AtomicReference<BloomFilterGroup> currentFilterGroup = new AtomicReference<>();
    private AtomicReference<BloomFilterGroup> lastFilterGroup = new AtomicReference<>();
    
    public AtomicReference<BloomFilterGroup> getCurrentFilterGroup(){
        return this.currentFilterGroup;
    }
    
    public AtomicReference<BloomFilterGroup> getLastFilterGroup(){
        return this.lastFilterGroup;
    }
    
    private long filterSize; // 单个bloomfilter大小为1亿
    private double fpp ;// 过滤的误差率
    private int groupSize;
    private String filePath;
    private String currentGroupName="currentGroup";
    private String lastGroupName="lastGroup";
    
    public String getFilePath(){
        return filePath;
    }
    
    public String getCurrentGroupName(){
        return this.currentGroupName;
    }
    
    public String getLastGroupName(){
        return this.lastGroupName;
    }

    public ActionBloomFilterGroup(long filterSize, double fpp, String filePath, int groupSize) {
        this.filterSize = filterSize;
        this.fpp = fpp;
        this.groupSize = groupSize;
        this.filePath = filePath;

        currentFilterGroup.set(createCurrentMonthBloomFilterGroup());
        lastFilterGroup.set(createLastMonthBloomFilterGroup());
    }
    
    private BloomFilterGroup createCurrentMonthBloomFilterGroup() {
        String fileNamePrefix = DateTools.formatDate(new Date(), "yyyy-MM-dd");
        CommonLogger.info("[bloomFilter init] create bloomFilter begin, groupName:{}",currentGroupName);
        BloomFilterGroup bloomFilterGroup = new BloomFilterGroup(filterSize, fpp, filePath,
                currentGroupName, fileNamePrefix, groupSize);
        CommonLogger.info("[bloomFilter init] create bloomFilter:{}", bloomFilterGroup.toString());
        return bloomFilterGroup;
    }
    
    private BloomFilterGroup createCurrentMonthBloomFilterGroupWithoutLoadFile() {
        String fileNamePrefix = DateTools.formatDate(new Date(), "yyyy-MM-dd");
        CommonLogger.info("[bloomFilter init] create bloomFilter begin, groupName:{}",currentGroupName);
        BloomFilterGroup bloomFilterGroup = new BloomFilterGroup(filterSize, fpp, filePath,
                currentGroupName, fileNamePrefix, groupSize, false);
        CommonLogger.info("[bloomFilter init] create bloomFilter:{}", bloomFilterGroup.toString());
        return bloomFilterGroup;
    }

    private BloomFilterGroup createLastMonthBloomFilterGroup() {
        String lastFileNamePrefix = DateTools.formatDate(new Date(), "yyyy-MM-dd");
        CommonLogger.info("[bloomFilter init] create bloomFilter begin, groupName:{}",lastGroupName);
        BloomFilterGroup bloomFilterGroup = new BloomFilterGroup(filterSize, fpp, filePath,
                lastGroupName, lastFileNamePrefix, groupSize);
        CommonLogger.info("[bloomFilter init] create bloomFilter:{}", bloomFilterGroup.toString());
        return bloomFilterGroup;
    }
    
    public void put(String action) {
        currentFilterGroup.get().putBloomFilter(action);
    }

    public boolean contains(String action) {
        if(lastFilterGroup.get() == null){
            return currentFilterGroup.get().contains(action);
        }
        return currentFilterGroup.get().contains(action) || lastFilterGroup.get().contains(action);
    }
    
    public void alterMonthBloomFilter() {
        // 先将 last bloomfilter清空
        lastFilterGroup.set(null);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            CommonLogger.error("Thread sleep error, ex:",e);
        }
        BloomFilterGroup bloomFilterGroup = createCurrentMonthBloomFilterGroupWithoutLoadFile();
        BloomFilterGroup lastMonthFilter = currentFilterGroup.getAndSet(bloomFilterGroup);
        lastFilterGroup.set(lastMonthFilter);
    }

    public JSONObject getDesc() {
        JSONObject jo = new JSONObject();
        jo.put("filterSize", this.filterSize);
        jo.put("fpp", this.fpp);
        jo.put("groupSize", this.groupSize);
        jo.put("filePath", this.filePath);
        jo.put("currentFilterGroup",currentFilterGroup.get().getBloomFilterDesc());
        if(lastFilterGroup.get() != null){
            jo.put("lastFilterGroup",lastFilterGroup.get().getBloomFilterDesc());
        }
        return jo;
    }
}
