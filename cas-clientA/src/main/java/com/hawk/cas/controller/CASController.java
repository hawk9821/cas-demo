package com.hawk.cas.controller;


import com.hawk.cas.conf.CASConfigEntity;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author hawk9821
 * @Date 2020-01-21
 */
@Controller
public class CASController {

    @Autowired
    private CASConfigEntity casConfig;

    @RequestMapping
    public String index(ModelMap map, HttpServletRequest request) {
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        final Map attributes = principal.getAttributes();
        map.addAttribute("attributes",attributes);
        map.addAttribute("user",request.getRemoteUser());
        return "index";
    }

    @RequestMapping("hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        //使用cas退出成功后,跳转到http://cas.client1.com:9003/logout/success
        return "redirect:"+casConfig.getClientLogoutUrl();
    }
    @RequestMapping("logout/success")
    public String logoutsuccess(HttpSession session) {
        return "logoutsuccess";
    }
}
