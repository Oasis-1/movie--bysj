package com.service;
import com.entity.Cinema;

import java.util.List;

public interface ICinemaService {
	//根据id获取电影院
	Cinema findCinemaById(long cinema_id);
	//增
	Integer addCinema(Cinema cinema);
	//改
	Integer updateCinema(Cinema cinema);
	//删
	Integer deleteCinema(long cinema_id);
	//获取所有
	List<Cinema> findAllCinemas();
	List<Cinema> findCinemasLikeName(String cinema_name);
	List<Cinema> findCinemasByMovieId(long movie_id);
}
