package com.mapper;

import com.entity.Cinema;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaMapper {
	//根据ID获取电影院
	Cinema findCinemaById(long cinema_id);
	//增
	Integer addCinema(Cinema cinema);
	//改
	Integer updateCinema(Cinema cinema);
	//删
	Integer deleteCinema(long cinema_id);
	//查询所有
	List<Cinema> findAllCinemas();
	//根据name
	List<Cinema> findCinemasLikeName(String cinema_name);
	//根据id
	List<Cinema> findCinemasByMovieId(long movie_id);
}
