package com.service.imp;

import com.entity.Hall;
import com.entity.Order;
import com.entity.Schedule;
import com.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.*;
import com.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImp implements IOrderService{
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ScheduleMapper scheduleMapper;
	@Autowired
	private HallMapper hallMapper;
	@Autowired
	private MovieMapper movieMapper;
	@Autowired
	private CinemaMapper cinemaMapper;
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public Order findOrderById(String order_id) {
		Order order = this.orderMapper.findOrderById(order_id);
		if(order != null) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}else {
			order = null;
		}
		return order;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public List<Order> findRefundOrderByUserName(String user_name) {
		List<Order> list = this.orderMapper.findRefundOrderByUserName(user_name);
		if(list.size() > 0) {
			for(Order order : list) {
				order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
				Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
				Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
				hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
				schedule.setSchedule_hall(hall);
				schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
				order.setOrder_schedule(schedule);
			}
		}else {
			list = null;
		}
		return list;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Integer addOrder(long schedule_id, String[] position, int price, User user) {
		int done = 0;
		//总价/票的数量=单价
		int order_price = price / position.length;
		String user_id = "";
		//补零，保证订单的id长度相同
		switch(String.valueOf(user.getUser_id()).length()) {
			case 1: user_id = "000" + String.valueOf(user.getUser_id()); break;
			case 2: user_id = "00" + String.valueOf(user.getUser_id()); break;
			case 3: user_id = "0" + String.valueOf(user.getUser_id()); break;
			case 4: user_id = String.valueOf(user.getUser_id()); break;
		}
		//有几张票，添加几个订单
		for(int i = 0;i < position.length;i++) {
			Order order = new Order();
			String order_id = "";
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
			//id等于当前日期+用户补零后的id
			order_id += dateFormat.format(date);
			order_id += user_id;
			String index = "";
			//再把座位号去除汉字，拼到id上
			switch(position[i].length()) {
				case 4:
					index = "0" + position[i].replaceAll("排", "0");
					index = index.replaceAll("座", "");
					break;
				case 5:
					if(position[i].charAt(2) >= 48 && position[i].charAt(2) <= 57) {
						index = "0" + position[i].replaceAll("排", "");
						index = index.replaceAll("座", "");
					}else {
						index = position[i].replaceAll("排", "0");
						index = index.replaceAll("座", "");
					}
					break;
				case 6:
					index = position[i].replaceAll("排", "");
					index = index.replaceAll("座", "");
					break;
			}
			order_id += index;
			order.setOrder_id(order_id);
			order.setOrder_position(position[i]);
			order.setSchedule_id(schedule_id);
			order.setUser_id(user.getUser_id());
			order.setOrder_price(order_price);
			order.setOrder_time(new Date());
			Integer rs = this.orderMapper.addOrder(order);
			//座位-1
			Integer rs1 = this.scheduleMapper.delScheduleRemain(schedule_id);
			done++;

		}
//		System.out.println("----------------------------");
//		int i=10/0;
//		System.out.println("----------------------");
//		System.out.println(10/0);
		//增加票房
		float sum = (float)price/10000;
		Integer rs2 = this.movieMapper.changeMovieBoxOffice(sum, this.scheduleMapper.findScheduleById(schedule_id).getMovie_id());
		return done;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Integer updateOrderStateToRefund(String order_id) {
		return this.orderMapper.updateOrderStateToRefund(order_id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Integer updateOrderStateToRefunded(String order_id) {
		return this.orderMapper.updateOrderStateToRefunded(order_id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public PageInfo<Order> findOrdersByUserName(Integer page,Integer limit,String user_name) {

		PageHelper.startPage(page, limit);
		List<Order> list = this.orderMapper.findOrdersByUserName(user_name);
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		PageInfo<Order> info = new PageInfo<Order>(list);
		return info;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public List<Order> findAllOrders() {
		List<Order> list = this.orderMapper.findAllOrders();
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		return list;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public PageInfo<Order> findOrdersByState(Integer page,Integer limit,int order_state) {
		PageHelper.startPage(page, limit);
		List<Order> list = this.orderMapper.findOrdersByState(order_state);
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		PageInfo<Order> info = new PageInfo<Order>(list);
		return info;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	@Override
	public PageInfo<Order> findAllOrdersBySplitPage(Integer page, Integer limit, String keyword) {
		PageHelper.startPage(page, limit);
		List<Order> list = new ArrayList<Order>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.orderMapper.findOrdersByUserName(keyword);
		}else {
			list = this.orderMapper.findAllOrders();
		}
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		PageInfo<Order> info = new PageInfo<Order>(list);
		return info;
	}
	
}
