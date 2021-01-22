package com.example.user.entity;


import lombok.Data;

/*
 * 创建人：LY
 * 
 * 创建时间：2019 12 16
 * 
 * 名称：用户表
 * */
@Data
public class User {

	/**
	 *  用户表ID
	 */
	int id ;

	/**
	 * 用户表名称
	 */
	String  name ;

	String password;

	int is_admin;

	/**
	 * 用户表头像
	 */
	String  avatar ;

	/**
	 *  用户性别（0男  1女）默认男
	 */
	int sex;

	/**
	 *  用户openid
	 */
	String  openid ;

	/**
	 *  用户层级
	 */
	String  grade ;

	/**
	 * 关联分类id
	 */
	int  bid;

	/**
	 * 个人介绍
	 */
	String  introduction ;

	/**
	 * 创建时间
	 */
	String create_at ;

	/**
	 *  是否关停账号 0否 1 是
	 */
	int  is_delete ;

	/**
	 * 上次登陆时间
	 */
	String login_time;

	/**
	 * 地址
	 */
	String address;

	/**
	 * 户籍地省
	 */
	String domicile_province;

	/**
	 * 户籍地市
	 */
	String domicile_city;

	/**
	 * 户籍地县
	 */
	String domicile_county;

	 /**
	   * 生日
	  */
	  String birthday;
	  String phone;

	/**
	 * 后台登录用户名
	 */
	String user_name;

	String background_picture;

	int wealth_value;

	/**
	 * 唯一标识
	 */
	String user_key;


	public String getIntroduction() {
		if(introduction==null){
			return "暂无介绍";
		}
		return introduction;
	}

	  
	  
	  
 

}
