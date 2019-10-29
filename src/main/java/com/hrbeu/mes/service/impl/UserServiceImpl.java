package com.hrbeu.mes.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrbeu.mes.dao.UserMapper;
import com.hrbeu.mes.pojo.User;
import com.hrbeu.mes.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource(name = "userMapper")
    private UserMapper userMapper; // 声明UserMapper接口引用

    @Override  //登录验证
    public User getLoginUser(String userCode, String password) {
        User user = null;
        try {
            user = userMapper.getLoginUser(userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override // 查询用户列表 getUserList 分页
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        List<User> userList = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", queryUserName);
            map.put("userRole", queryUserRole);
            map.put("pageSize", pageSize);
            map.put("pageIndex", (currentPageNo - 1) * pageSize);
            // 调用dao方法实现查询
            userList = userMapper.getUserList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override //查询用户数量
    public int getUserCount(String queryUserName, Integer queryUserRole) {
        int count = 0;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        try {
            count = userMapper.getUserCount(queryUserName, queryUserRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    @Override //查询userCode是否存在
    public User selectUserCodeExist(String userCode) {
        User user = null;
        try {
            user = userMapper.selectUserCodeExist(userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        return false;
    }


    @Override
    public int deleteByPrimaryKey(Integer id) {
        int count = 0;
        try {
            if (userMapper.deleteByPrimaryKey(id) == 1) { // 调用dao方法实现查询
                count = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int insert(User record) {
        int count = 0;
        if (userMapper.insert(record) == 1) {
            count = 1;
        }
        return count;
    }

    @Override
    public int insertSelective(User record) {
        int count = 0;
        try {
            if (userMapper.insertSelective(record) == 1) { // 调用dao方法实现查询
                count = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        try {
            return userMapper.selectByPrimaryKey(id); // 调用dao方法实现查询
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        int count = 0;
        try {
            if (userMapper.updateByPrimaryKeySelective(record) == 1) { // 调用dao方法实现查询
                count = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int updateByPrimaryKey(User record) {
        int count = 0;
        try {
            if (userMapper.updateByPrimaryKey(record) == 1) { // 调用dao方法实现查询
                count = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<User> getRoleIdByUserInfo(Integer roleId) {
        List<User> user = null;
        try {
            user = userMapper.getRoleIdByUserInfo(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


}
