package com.hawk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author hawk9821
 * @date 2020-01-22
 */
@Controller
public class CASController {

    /**
     * 主页
     * @param map
     * @return
     */
    @RequestMapping()
    public String index(ModelMap map) {
        map.addAttribute("name", "client B");
        return "index";
    }

    /**
     * hello
     * @return
     */
    @RequestMapping("hello")
    public String hello() {
        return "hello";
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();//销毁session
        //使用cas退出成功后,跳转到http://cas.clientB.com:9002/logout/success
        //"redirect:https://cas.server.com:8443/cas/logout?service=http://b.cas.com:9002/";
        return "redirect:https://cas.server.com:8443/cas/logout?service=http://app2.cas.com:9002/";
    }

    /**
     * 退出成功页
     * @param session
     * @return
     */
    @RequestMapping("logout/success")
    public String logoutsuccess(HttpSession session) {
        return "logoutsuccess";
    }

}
