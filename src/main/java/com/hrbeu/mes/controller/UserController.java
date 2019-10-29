package com.hrbeu.mes.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.hrbeu.mes.service.UserService;
import com.hrbeu.mes.tools.Constants;
import com.hrbeu.mes.tools.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.mysql.jdbc.StringUtils;
import com.hrbeu.mes.pojo.User;


@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;

    // 登录成功 -----首页
    @RequestMapping(value = "/main.html")
    public String main(HttpSession session, HttpServletRequest request) throws Exception {
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            return "redirect:/login.html";// 跳转到登录页面
        }

        session.setAttribute("environmentInfo", "");
        String p25c = "";
        String p10c = "";


        session.setAttribute("p25c", p25c);
        session.setAttribute("p10c", p10c);

        return "index"; // 登录成功：跳转到首页
    }

    // 退出登录
    @RequestMapping(value = "/logout.html")
    public String logout(HttpSession session) {
        // 清除session
        session.removeAttribute(Constants.USER_SESSION);
        return "login";
    }

    // 用户条件查询 --------分页
    @RequestMapping(value = "/userlist.html")
    public String getUserlist(Model model,
                              @RequestParam(value = "queryUserName", required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole", required = false) String queryUserRole,
                              @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        int _queryUserRole = 0;
        List<User> userList = null;
        // 设置页面容量
        int pageSize = Constants.pageSize;
        // 当前页码
        int currentPageNo = 1;
        if (queryUserName == null) {
            queryUserName = "";
        }
        if (queryUserRole != null && !queryUserRole.equals("")) {
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "redirect:/user/syserror.html";
            }
        }
        // 总数量（表）
        int totalCount = userService.getUserCount(queryUserName, _queryUserRole);
        // 总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        // 控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        userList = userService.getUserList(queryUserName, _queryUserRole, currentPageNo, pageSize);
        model.addAttribute("userList", userList);

        return "jsp/userlist";
    }

    @RequestMapping(value = "/syserror.html")
    public String sysError() {
        return "syserror";
    }

    // 跳到修改密码页面
    @RequestMapping(value = "/pwdmodify.html")
    public String pwdmodify() {
        return "jsp/pwdmodify";
    }

    // 查询原始密码是否正确
    @ResponseBody
    @RequestMapping(value = "/pwd.html", method = RequestMethod.GET)
    public String pwd(@RequestParam String oldpassword, HttpSession session) {
        Map<String, String> map = new HashMap<String, String>();
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        if (user.getUserPassword().equals(oldpassword)) {
            map.put("result", "true");
        } else if (!oldpassword.equals(user.getUserPassword())) {
            map.put("result", "false");
        } else if (user.getUserPassword().equals("") && user.getUserPassword() == null) {
            map.put("result", "sessionerror");
        } else {
            map.put("result", "error");
        }
        return JSONArray.toJSONString(map);
    }

    // 修改密码
    @RequestMapping(value = "/updatepwd.html", method = RequestMethod.POST)
    public String updatepwd(@RequestParam String method, @RequestParam String newpassword) {
        userService.updatePwd(Integer.valueOf(method), newpassword);
        return "redirect:/user/login.html";
    }

    // 根据用户id删除用户信息
    @ResponseBody
    @RequestMapping(value = "/deluser.html", method = RequestMethod.GET)
    public String deluser(@RequestParam String uid) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(uid)) {
            resultMap.put("delResult", "notexist");
        } else {
            if (userService.deleteByPrimaryKey(Integer.parseInt(uid)) > 0) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    // 根据用户id查询指定用户信息
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(@RequestParam String uid, Model model) {
        User user = userService.selectByPrimaryKey(Integer.valueOf(uid));
        model.addAttribute("user", user);
        return "jsp/userview";
    }

    // 根据用户id查询指定用户信息 (显示usermodify.jsp)
    @RequestMapping(value = "/usermodify", method = RequestMethod.GET)
    public String usermodify(@RequestParam String uid, Model model) {
        User user = userService.selectByPrimaryKey(Integer.valueOf(uid));
        model.addAttribute("user", user);
        return "jsp/usermodify";
    }


    // 修改用户信息
    @RequestMapping(value = "/usermodifysave.html", method = RequestMethod.POST)
    public String modifyUserSave(User user, HttpSession session) {
        logger.debug("modifyUserSave userid===================== " + user.getId());
        user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if (userService.updateByPrimaryKeySelective(user) > 0) {
            return "redirect:/user/userlist.html";
        }
        return "jsp/usermodify";
    }

    // 跳转到添加用户页面
    @RequestMapping("/useradd.html")
    public String useradd() {
        return "jsp/useradd";
    }

    // 查询userCode是否存在
    @ResponseBody
    @RequestMapping(value = "/ucexist.html")
    public String ucexist(@RequestParam String userCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userService.selectUserCodeExist(userCode);
        if (user != null) {
            map.put("userCode", "exist");
        }
        return JSONArray.toJSONString(map);
    }

    // 添加用户操作
    @RequestMapping(value = "/addUser.html", method = RequestMethod.POST)
    public String addUser(User user, HttpSession session, HttpServletRequest request,
                          @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = "F:/Work/SSM/SSM-MySmbms/WebContent/statics/images/";
        //String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
        logger.info("uploadFile path ============== > " + path);
        for (int i = 0; i < attachs.length; i++) {
            MultipartFile attach = attachs[i];
            if (!attach.isEmpty()) {
                if (i == 0) {
                    errorInfo = "uploadFileError";
                } else if (i == 1) {
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();// 原文件名
                logger.info("uploadFile oldFileName ============== > " + oldFileName);
                String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
                logger.debug("uploadFile prefix============> " + prefix);
                int filesize = 500000;
                logger.debug("uploadFile size============> " + attach.getSize());
                if (attach.getSize() > filesize) {// 上传大小不得超过 500k
                    request.setAttribute(errorInfo, " * 上传大小不得超过 500k");
                    flag = false;
                } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    // 保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if (i == 0) {
                        idPicPath = fileName;
                    } else if (i == 1) {
                        workPicPath = fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);

                } else {
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if (flag) {
            user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            if (userService.insert(user) == 1) {
                return "redirect:/user/userlist.html";
            }
        }
        return "jsp/useradd";
    }

}
