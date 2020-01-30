package com.hawk.cas.utils;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import javax.servlet.annotation.WebFilter;

/**
 *  url匹配策略
 * @Author hawk9821
 * @Date 2020-01-21
 */
@WebFilter(urlPatterns = "/*")
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {

    /**
     * 过滤url
     * @param url
     * @return
     */
    @Override
    public boolean matches(String url) {
        //过滤的url
        return url.contains("/logout");
    }

    /**
     * 正则表达式过滤方法
     * @param s
     */
    @Override
    public void setPattern(String s) {

    }
}
