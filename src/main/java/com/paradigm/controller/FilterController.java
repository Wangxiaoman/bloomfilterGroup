package com.paradigm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Splitter;
import com.paradigm.constants.CommonStatus;
import com.paradigm.constants.ResultJson;
import com.paradigm.log.CommonLogger;
import com.paradigm.service.ActionService;

@RestController
@RequestMapping("/recom/filter")
public class FilterController {

	@Resource
	private ActionService actionService;

	@GetMapping("/user/items")
	public ResultJson filterUser(@RequestParam("userId") String userId, @RequestParam("itemSetId") String itemSetId,
			@RequestParam("itemIds") List<String> itemIds, @RequestParam("action") int action) {
		if (!CollectionUtils.isEmpty(itemIds)) {
			int itemIdSize = itemIds.size();
			long btime = System.currentTimeMillis();
			List<String> result = actionService.getUserActionFilter(userId, itemSetId, itemIds, action);
			long etime = System.currentTimeMillis();
			CommonLogger.infoOneInThousand("filter user, userId :{}, cost ms:{}, itemCount:{}, resultCount:{}", userId,
					(etime - btime), itemIdSize, result.size());
			return new ResultJson(CommonStatus.SUCCESS, result);
		}
		return new ResultJson(CommonStatus.SUCCESS);
	}

	@PostMapping("/user/items")
	public ResultJson posfFilterUser(@RequestParam("userId") String userId, @RequestParam("itemSetId") String itemSetId,
			@RequestParam("itemIds") List<String> itemIds, @RequestParam("action") int action) {
		if (!CollectionUtils.isEmpty(itemIds)) {
			long btime = System.currentTimeMillis();
			List<String> result = actionService.getUserActionFilter(userId, itemSetId, itemIds, action);
			long etime = System.currentTimeMillis();
			CommonLogger.infoOneInThousand("filter user, userId :{}, cost ms:{}, itemCount:{}, resultCount:{}", userId,
					(etime - btime), itemIds.size(), result.size());
			return new ResultJson(CommonStatus.SUCCESS, result);
		}
		return new ResultJson(CommonStatus.SUCCESS);
	}

	@PostMapping("/user/action")
	public String userAction(@RequestParam("userId") String userId, @RequestParam("itemSetId") String itemSetId,
			@RequestParam("itemId") String itemId, @RequestParam("action") int action) {

		actionService.putUserAction(userId, itemSetId, itemId, action);
		return "OK";
	}

	@PostMapping("/user/actions")
	public String userActions(@RequestParam("userId") String userId, @RequestParam("itemSetId") String itemSetId,
			@RequestParam("itemIds") List<String> itemIds, @RequestParam("action") int action) {

		if (!CollectionUtils.isEmpty(itemIds)) {
			for (String itemId : itemIds) {
				actionService.putUserAction(userId, itemSetId, itemId, action);
			}
		}
		return "OK";
	}

	@GetMapping("/user/actions")
	public String saveUserActions(@RequestParam("userId") String userId, @RequestParam("itemSetId") String itemSetId,
			@RequestParam("itemIds") List<String> itemIds, @RequestParam("action") int action) {

		if (!CollectionUtils.isEmpty(itemIds)) {
			for (String itemId : itemIds) {
				actionService.putUserAction(userId, itemSetId, itemId, action);
			}
		}
		return "OK";
	}

	@GetMapping("/actions")
	public String saveActions(@RequestParam("actions") String actions) {
		List<String> actionList = Splitter.on(",").splitToList(actions);
		if (!CollectionUtils.isEmpty(actionList)) {
			for (String action : actionList) {
				actionService.putUserAction(action);
			}
		}
		return "OK";
	}

	@GetMapping("/group/description")
	public ResultJson getDescription() {
		return new ResultJson(CommonStatus.SUCCESS, actionService.getGroupDesc());
	}
}
