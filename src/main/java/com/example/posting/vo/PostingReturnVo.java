package com.example.posting.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class PostingReturnVo {
	/**
	 * 发帖表id
	 */
	private int id;
	/**
	 * 帖子名称
	 */
	private String name;
	/**
	 * 用户表名称
	 */
	private String  username ;
	/**
	 * 用户表头像
	 */
	private String  avatar ;
	/**
	 * 封面
	 */
	private String cover;
	/**
	 * 视频
	 */
	private String videos;
	/**
	 * 详细介绍
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private String createAt;
	/**
	 * 最后操作时间
	 */
	private String endAt;

	/**
	 * 发帖地址
	 */
	private String address;

	/**
	 * 点赞数量
	 */
	private int thumbNumber;

	/**
	 * 用户id
	 */
	private int userId;
	
	private int status;
	
	

	
	
	

}
