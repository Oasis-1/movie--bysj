package com.service;


import com.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IUserService {
	//查询用户
	User login(String user_name,String user_pwd);
	//改
	Integer updateUserInfo(User user);
	//根据id获取用户
	User findUserById(long user_id);
	List<User> findUserByName(String name);
	List<User> findUserLikeName(String name);
	Integer addUser(User user);
	Integer deleteUser(long user_id);
	List<User> findAllUserInfos();
	PageInfo<User> findAllUserBySplitPage(Integer page,Integer limit,String keyword);
}
