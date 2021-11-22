package com.service;

import com.entity.Hall;

import java.util.List;

public interface IHallService {
	Hall findHallById(long hall_id);
	Hall findHallByCinemaAndHallName(String cinema_name,String hall_name);
	Integer addHall(Hall hall);
	Integer updateHall(Hall hall);
	Integer deleteHall(long hall_id);
	List<Hall> findHallByCinemaId(long cinema_id);
	List<Hall> findAllHalls();
}
