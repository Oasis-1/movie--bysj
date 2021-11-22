package com.mapper;

import com.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
	User findUserById(long user_id);
	Integer addUser(User user);
	Integer deleteUser(long user_id);
	Integer updateUser(User user);
	List<User> findAllUser();
	List<User> findUserByName(String name);
	List<User> findUserLikeName(String name);
}
