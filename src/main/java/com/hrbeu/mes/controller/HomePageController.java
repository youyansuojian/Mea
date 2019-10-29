package com.hrbeu.mes.controller;

import com.alibaba.fastjson.JSONArray;
import com.hrbeu.mes.pojo.Electrolyze;
import com.hrbeu.mes.pojo.User;
import com.hrbeu.mes.service.EleService;
import com.hrbeu.mes.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomePageController {
    @RequestMapping(value = "/")
    public String homePage() {
        return "hello";
    }
    @Resource
    private UserService userService;

    @Resource
    private EleService eleService;

    //以下作为测试，数据库是否访问正常
    @ResponseBody
    @RequestMapping(value = "/test1.html")
    public String test() {
        Map<String, Object> map = new HashMap<String, Object>();
        String userCode = "liming";
        User user = userService.selectUserCodeExist(userCode);
        if (user != null) {
            map.put("userCode", "exist");
            map.put("data",user);
        }
        return JSONArray.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/test2")
    public List<Electrolyze> queryData() {
        List<Electrolyze> list = eleService.queryData();
        return list;
    }
}
