package com.atlp.spring.component;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
// 自定义错误信息
public class WebMvcErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        Object ext = webRequest.getAttribute("ext", 0);
        Throwable error = super.getError(webRequest);
        map.put("company", "atlp");
        map.put("ext", ext);
        map.put("exception", error.getClass().getName());
        return map;
    }
}
