package com.paradigm.config;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.paradigm.log.CommonLogger;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	public void jsonErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
		Map<String, String[]> params = req.getParameterMap();
		StringBuffer paramsBuffer = new StringBuffer();
		if (!CollectionUtils.isEmpty(params)) {
			for (Entry<String, String[]> param : params.entrySet()) {
				paramsBuffer.append(param.getKey() + ":");
				for (String s : param.getValue()) {
					paramsBuffer.append(s).append(",");
				}
				paramsBuffer.append(";");
			}
		}
		
		// 针对一些异常的特殊处理
		if(e instanceof HttpRequestMethodNotSupportedException || e instanceof HttpMessageNotReadableException){
            CommonLogger.warn("global warn, request url:'" + req.getRequestURI() + "',method:'"
                    + req.getMethod() + "',params:'" + paramsBuffer.toString() + "' ,ex:"
                    + e.getMessage());
		}  else{
            CommonLogger.error(
                    "global excpetion, request url:'" + req.getRequestURI() + "',method:'"
                            + req.getMethod() + "',params:'" + paramsBuffer.toString() + "' ,ex:",
                    e);
		}

		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.getWriter().write("INTERNAL_SERVER_ERROR："+ e);
	}
}