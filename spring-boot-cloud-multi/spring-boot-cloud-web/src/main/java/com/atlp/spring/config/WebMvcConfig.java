package com.atlp.spring.config;

import com.atlp.spring.component.*;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.Arrays;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    // 扩展SpringMVC功能
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            // 添加自定义拦截器规则
            public void addInterceptors(InterceptorRegistry registry) {
                // 设置拦截所有请求
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").
                        // 设置不拦截的请求
                        excludePathPatterns("/", "/index.html", "/user/login", "/error").
                        excludePathPatterns("/**/*.ico", "/**/*.svg", "/**/*.js", "/**/*.css");
            }

            @Override
            // 添加视图映射规则
            public void addViewControllers(ViewControllerRegistry registry) {
                // 设置首页的视图映射规则
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/index.html").setViewName("login");
                // 设置重定向的视图映射规则
                registry.addViewController("/main.html").setViewName("dashboard");
            }
        };
        return adapter;
    }

    @Bean
    // 注册国际化信息组件
    public LocaleResolver localeResolver() {
        return new WebMvcLocaleResolver();
    }

    @Bean
    // 自定义Servlet容器定制器
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                factory.setPort(8090);
            }
        };
    }

    @Bean
    // 注册自定义Servlet
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new WebMvcServlet());
        registrationBean.setUrlMappings(Arrays.asList("/webServlet"));
        return registrationBean;
    }

//    @Bean
//    // 注册自定义过滤器
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new WebMvcFilter());
//        return registrationBean;
//    }
}
