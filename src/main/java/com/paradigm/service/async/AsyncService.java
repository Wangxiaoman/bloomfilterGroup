package com.paradigm.service.async;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.google.common.hash.BloomFilter;
import com.paradigm.bloomfilter.BloomFilterTools;
import com.paradigm.log.CommonLogger;

@Service
public class AsyncService {

    //@Async
    public void saveBloomFilter(String fileName, BloomFilter<String> bloomFilter) {
        long btime = System.currentTimeMillis();
        try {
        		TimeUnit.MINUTES.sleep(RandomUtils.nextLong(0, 25));
		} catch (InterruptedException e) {
			CommonLogger.error("save bloomfilter sleep error, ex:",e);
		}
        BloomFilterTools.saveEachBloolFiterToFile(fileName, bloomFilter);
        long etime = System.currentTimeMillis();
        CommonLogger.info("[bloomFilter save] save to file, fileName:{}, cost ms:{}", fileName,
                (etime - btime));
    }
}
