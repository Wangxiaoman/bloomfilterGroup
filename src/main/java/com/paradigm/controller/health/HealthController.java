package com.paradigm.controller.health;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {

	@GetMapping("/health")
	public void health(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
	}
}
