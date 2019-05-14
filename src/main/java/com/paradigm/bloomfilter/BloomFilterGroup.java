package com.paradigm.bloomfilter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.paradigm.log.CommonLogger;

public class BloomFilterGroup {

    private long filterSize = 1 * 10000 * 1000; // 单个bloomfilter大小为1亿
    private double fpp = 0.001F;// 过滤的误差率
    private int groupSize = 50;
    private String fileNamePrefix = StringUtils.EMPTY; // 当前月还是前一个月的存储

    private String filePath;
    private String groupName;
    private AtomicLong elementCount = new AtomicLong(0);

    private Map<String, BloomFilter<String>> filterGroup = new HashMap<>();
    
    public Map<String, BloomFilter<String>> getFilterGroup(){
        return filterGroup;
    }
    
    public String getFilePath(){
        return this.filePath;
    }
    
    public String getGroupName(){
        return this.groupName;
    }

    public BloomFilterGroup(String filePath, String groupName) {
        this.filePath = filePath;
        this.groupName = groupName;

        init(true);
    }
    
    public BloomFilterGroup(long filterSize, double fpp, String filePath, String groupName,
            String fileNamePrefix, int groupSize) {
        this.filterSize = filterSize;
        this.fpp = fpp;
        this.fileNamePrefix = fileNamePrefix;
        this.filePath = filePath;
        this.groupName = groupName;
        this.groupSize = groupSize;

        init(true);
    }

    public BloomFilterGroup(long filterSize, double fpp, String filePath, String groupName,
            String fileNamePrefix, int groupSize, boolean loadFile) {
        this.filterSize = filterSize;
        this.fpp = fpp;
        this.fileNamePrefix = fileNamePrefix;
        this.filePath = filePath;
        this.groupName = groupName;
        this.groupSize = groupSize;

        init(loadFile);
    }

    private void init(boolean loadFile) {
        long bTime = System.currentTimeMillis();
        for (int i = 0; i < this.groupSize; i++) {
            String fileName = getFileName(i);
            long bfTime = System.currentTimeMillis();
            BloomFilter<String> filter = null;
            if(loadFile){
                filter = loadFile(this.filePath, this.groupName, fileName);
            }
            if (filter == null) {
                filter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
                        this.filterSize, this.fpp);
            }
            filterGroup.put(fileName, filter);
            long efTime = System.currentTimeMillis();
            CommonLogger.info("[filterGroup init] init fileName:{},cost:{}", fileName,(efTime-bfTime));
            System.out.println("[filterGroup init] init fileName:"+fileName+",cost:"+(efTime-bfTime));
        }
        long eTime = System.currentTimeMillis();
        CommonLogger.info("[filterGroup init] cost ms:{}", (eTime - bTime));
        System.out.println("[filterGroup init] cost ms:" + (eTime - bTime));
    }

    private static BloomFilter<String> loadFile(String filePath,String groupName, String fileName) {
        File file = new File(filePath + "/"+ groupName +"/" + fileName);
        if (file.exists()) {
            BufferedInputStream inputSteam = null;
            try {
                inputSteam = new BufferedInputStream(new FileInputStream(file));
                return BloomFilter.readFrom(inputSteam,
                        Funnels.stringFunnel(Charset.defaultCharset()));
            } catch (Exception ex) {
                CommonLogger.error("load bloomfilter file error, ex:", ex);
            } finally{
                if(inputSteam != null){
                    try {
                        inputSteam.close();
                    } catch (IOException e) {
                        CommonLogger.error("close input stream error, ex:", e);
                    }
                }
            }
        }
        return null;
    }

    public void putBloomFilter(String action) {
        int index = BloomFilterTools.getActionGroupIndex(action, groupSize);
        filterGroup.get(getFileName(index)).put(action);

        // 累积计数
        long currentCount = elementCount.incrementAndGet();
        CommonLogger.infoOneInThousand(
                "save action to bloomfilter,action:{},index:{},currentCount:{}", action, index,
                currentCount);
    }

    public boolean contains(String action) {
        int index = BloomFilterTools.getActionGroupIndex(action, groupSize);
        BloomFilter<String> bloomFilter = filterGroup.get(getFileName(index));
        if (bloomFilter != null) {
            return bloomFilter.mightContain(action);
        }
        return false;
    }

    public long approximateElementCount() {
        long counter = 0;
        for (int i = 0; i < groupSize; i++) {
            if (filterGroup.get(i) != null) {
                counter += filterGroup.get(i).approximateElementCount();
            }
        }
        return counter;
    }

    public String getFileName(int index) {
        return this.fileNamePrefix + "_" + index;
    }

    public JSONObject getBloomFilterDesc() {
        JSONObject result = new JSONObject();
        long approximateElementCount = 0;
        for (Entry<String, BloomFilter<String>> entry : filterGroup.entrySet()) {
            approximateElementCount += entry.getValue().approximateElementCount();
            result.put(entry.getKey(), entry.getValue().approximateElementCount());
        }
        result.put("approximateElementCount", approximateElementCount);
        result.put("elementCount", this.elementCount);
        return result;
    }

    @Override
    public String toString() {
        return "ActionBloomFilterGroup [filterSize=" + filterSize + ", fpp=" + fpp + ", groupSize="
                + groupSize + ", fileNamePrefix=" + fileNamePrefix + ", filePath=" + filePath
                + ", groupName=" + groupName + ", elementCount=" + elementCount + ", filterGroup="
                + filterGroup + ",approximateElementCount =" + approximateElementCount() + "]";
    }

}
