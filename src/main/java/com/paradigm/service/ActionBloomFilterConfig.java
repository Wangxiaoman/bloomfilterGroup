package com.paradigm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActionBloomFilterConfig {

    @Bean(name = "actionBloomFilterGroup")
    public ActionBloomFilterGroup getCurrentFilterGroup(
            @Value("${action.filter.bloomfilter.size}") long filterSize,
            @Value("${action.filter.fpp}") double fpp, 
            @Value("${action.filter.file.path}") String filePath,
            @Value("${action.filter.group.size}") int groupSize) {
        return new ActionBloomFilterGroup(filterSize, fpp, filePath, groupSize);
    }
}
