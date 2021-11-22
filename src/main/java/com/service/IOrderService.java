package com.service;

import com.entity.Order;
import com.entity.User;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IOrderService {
	Order findOrderById(String order_id);
	//增加订单
	Integer addOrder(long schedule_id, String[] position, int price, User user);
	//申请退票
	Integer updateOrderStateToRefund(String order_id);
	//同意退票
	Integer updateOrderStateToRefunded(String order_id);
	PageInfo<Order> findOrdersByUserName(Integer page,Integer limit,String user_name);
	List<Order> findAllOrders();
	List<Order> findRefundOrderByUserName(String user_name);
	PageInfo<Order> findOrdersByState(Integer page,Integer limit,int order_state);
	PageInfo<Order> findAllOrdersBySplitPage(Integer page,Integer limit,String keyword);
}
