package com.service.imp;

import com.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.UserMapper;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements IUserService{

	@Autowired
	private UserMapper usermapper;
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public User login(String user_name, String user_pwd) {
		List<User> userList = usermapper.findUserByName(user_name);
		int size = userList.size();
		System.out.println(size);
		//不存在的话
		if (size==0){
			User user = new User();
			user.setUser_role(999);
			return user;
		}
		for(User user : userList) {
			//密码正确的话
			if(user.getUser_pwd().equals(user_pwd)) {
				return user;
			}
		}
		return null;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Integer updateUserInfo(User user) {
		return this.usermapper.updateUser(user);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public User findUserById(long user_id) {
		return this.usermapper.findUserById(user_id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public List<User> findUserByName(String name) {	
		return this.usermapper.findUserByName(name);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Integer addUser(User user) {
		return this.usermapper.addUser(user);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Integer deleteUser(long user_id) {
		return this.usermapper.deleteUser(user_id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public PageInfo<User> findAllUserBySplitPage(Integer page, Integer limit, String keyword) {
		PageHelper.startPage(page, limit);
		List<User> list = new ArrayList<User>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.usermapper.findUserLikeName(keyword);
		}else {
			list = this.usermapper.findAllUser();
		}
		PageInfo<User> info = new PageInfo<User>(list);
		return info;
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public List<User> findAllUserInfos() {
		return this.usermapper.findAllUser();
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public List<User> findUserLikeName(String name) {
		return this.usermapper.findUserLikeName(name);
	}



}
