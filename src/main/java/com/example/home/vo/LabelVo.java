package com.example.home.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class LabelVo {

	/**
	 * //标签表ID
	 */
	int id ;
	/**
	 * 标签表名称
	 */
	String tagName;

	/**
	 * 是否默认选中
	 */
	String checked ;

	/**
	 * 是否加上HOT
	 */
	int	hot ;

	/**
	 * 是否删除
	 */
	int	isDelete ;

	/**
	 * 创建时间
	 */
	String  createAt;

	int value;

	/**
	 * 标签id
	 */
	int tagId;


}
