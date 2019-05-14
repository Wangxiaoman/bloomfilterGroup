package com.paradigm.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paradigm.service.ActionBloomFilterGroup;
import com.paradigm.service.ActionService;

@RestController
@RequestMapping("/recom/filter")
public class TestController {

    @Resource
    private ActionBloomFilterGroup actionBloomFilterGroup;

    @GetMapping("/test/action")
    public String testAction(@RequestParam("action") String action) {
        actionBloomFilterGroup.put(action);
        return "OK";
    }

    @GetMapping("/test/action/search")
    public String testActionSearch(@RequestParam("action") String action) {
        return String.valueOf(actionBloomFilterGroup.contains(action));
    }

    @Resource
    private ActionService actionService;

    @GetMapping("/test/save")
    public String save() {
        actionService.saveCurrentGroupToFile();
        return "OK";
    }

    @GetMapping("/test/alter")
    public String testAlter() {
        actionService.alterMonthBloomFilter();
        return "OK";
    }
}
