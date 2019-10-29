package com.hrbeu.mes.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hrbeu.mes.pojo.User;

/**
 * 用户业务接口
 *
 * @author 微笑
 */
public interface UserService {

    /**
     * 登录
     *
     * @param userCode
     * @return
     * @throws Exception
     */
    public User getLoginUser(String userCode, String password);

    /**
     * 根据角色id查询用户是否存在数据
     *
     * @param roleId
     * @return
     */
    public List<User> getRoleIdByUserInfo(Integer roleId);

    /**
     * 通过条件查询-userList
     *
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    /**
     * 通过条件查询-用户表记录数
     *
     * @param queryUserName
     * @param queryUserRole
     * @return
     */
    public int getUserCount(String queryUserName, Integer queryUserRole);

    /**
     * 根据用户id删除指定用户信息
     *
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id);

    /**
     * 查询userCode是否存在
     *
     * @param userCode
     * @return
     */
    public User selectUserCodeExist(String userCode);

    /**
     * 根据userId修改密码
     *
     * @param id
     * @param pwd
     * @return
     */
    public boolean updatePwd(int id, String pwd);


    /**
     * 添加一条完整数据
     *
     * @param record
     * @return
     */
    public int insert(User record);

    /**
     * 为用户添加一条数据 可选择性
     *
     * @param record
     * @return
     */
    public int insertSelective(User record);

    /**
     * 根据用户id查询指定用户信息
     *
     * @param id
     * @return
     */
    public User selectByPrimaryKey(Integer id);

    /**
     * 根据用户id修改用户不为空的字段信息
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(User record);

    /**
     * 根据用户id修改用户信息 如为空的字段在数据库中置为NULL。
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKey(User record);
}
