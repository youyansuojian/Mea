package com.hrbeu.mes.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hrbeu.mes.pojo.User;

/**
 * 用户接口
 *
 * @author 微笑
 */
@Mapper
public interface UserMapper {

    /**
     * 登录
     *
     * @param userCode
     * @return
     * @throws Exception
     */
    public User getLoginUser(String userCode) throws Exception;

    /**
     * 查询用户列表（对象入参）getUserList
     *
     * @param user
     * @return
     */
    public List<User> getUserList(Map<String, Object> map) throws Exception;

    /**
     * 通过条件查询-用户表记录数
     *
     * @param connection
     * @param userName
     * @param userRole
     * @return
     * @throws Exception
     */
    public int getUserCount(@Param("userName") String queryUserName, @Param("userRole") Integer queryUserRole)
            throws Exception;

    /**
     * 根据用户id删除指定用户信息
     *
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) throws Exception;

    /**
     * 查询userCode是否存在
     *
     * @param userCode
     * @return
     * @throws Exception
     */
    public User selectUserCodeExist(String userCode) throws Exception;

    /**
     * 根据用户id修改密码
     *
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    public boolean updatePwd(@Param("id") int id, @Param("pwd") String pwd) throws Exception;

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
    public int insertSelective(User record) throws Exception;

    /**
     * 根据用户id查询指定用户信息
     *
     * @param id
     * @return
     */
    public User selectByPrimaryKey(Integer id) throws Exception;

    /**
     * 根据用户id修改用户不为空的字段信息
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(User record) throws Exception;

    /**
     * 根据用户id修改用户信息 如为空的字段在数据库中置为NULL。
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKey(User record);

    /**
     * 根据角色id查询用户是否存在数据
     *
     * @param roleId
     * @return
     */
    public List<User> getRoleIdByUserInfo(Integer roleId) throws Exception;

}