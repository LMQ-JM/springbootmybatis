package com.example.posting.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class PostingVo {

	/**
	 * 帖子名称
	 */
	private String Postingname;

	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;
	
	private Integer page;
	
	private Integer limit;
	
	private String userName;
	

	

}
