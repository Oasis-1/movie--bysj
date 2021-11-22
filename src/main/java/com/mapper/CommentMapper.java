package com.mapper;

import com.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
	//用户： 修改评论、增加评论
	//管理员： 删除评论、 修改评论
	//查询用户的评论
	Comment findCommentById(long comment_id);
	Integer addComemnt(Comment comment);
	Integer updateComment(Comment comment);
	Integer deleteComment(long comment_id);
	List<Comment> findAllComments();
	List<Comment> findCommentsByMoiveId(long movie_id);
	List<Comment> findCommentsByUserName(String user_name);
}
