package com.hawk.cas.conf;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hawk9821
 * @Date 2020-01-21
 */
@Configuration
public class CASConfigure {

    @Autowired
    private CASConfigEntity configEntity;

    /**
     * 用于实现单点登出功能
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
        listener.setEnabled(true);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    @Bean
    public FilterRegistrationBean<SingleSignOutFilter> logOutFilter() {
        FilterRegistrationBean<SingleSignOutFilter> authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new SingleSignOutFilter());
        authenticationFilter.setEnabled(true);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", configEntity.getServerUrlPrefix());
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.addUrlPatterns("/*");
        authenticationFilter.setOrder(1);
        return authenticationFilter;
    }

    /**
     * 单点登录
     */
    @Bean
    public FilterRegistrationBean authenticationFilterRegistrationBean() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AuthenticationFilter());
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerLoginUrl", configEntity.getServerLoginUrl());
        initParameters.put("serverName", configEntity.getServerName());//域名换成ip，不然单点退出失效
        initParameters.put("ignorePattern", "/cas/*");
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.setOrder(1);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

    /**
     * 校验
     */
    @Bean
    public FilterRegistrationBean ValidationFilterRegistrationBean() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        //此处cas30，用cas20获取不到cas返回的登录信息
        authenticationFilter.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", configEntity.getServerUrlPrefix());
        initParameters.put("serverName", configEntity.getServerName());
        initParameters.put("ignorePattern", "/cas/*");//不拦截的路径
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.setOrder(1);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

    // 取用户信息
    @Bean
    public FilterRegistrationBean casHttpServletRequestWrapperFilter() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new HttpServletRequestWrapperFilter());
        authenticationFilter.setOrder(1);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

    // 取用户信息
    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AssertionThreadLocalFilter());
        authenticationFilter.setOrder(1);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }
}
