package com.example.home.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/30 15:59
 * 找工作实体类
 */
@Data
@TableName("tb_recruit_job_search")
public class RecruitJobSearch extends Model<RecruitJobSearch> {

    /**
     * 主键
     */
    @TableId
    private int id;

    /**
     * 发布人id
     */
    private int userId;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 经历内容
     */
    private String jobContent;

    /**
     * 求职人姓名
     */
    private String userName;

    /**
     * 求职人性别
     */
    private int userSex;

    /**
     * 求职人性别
     */
    private String nativePlace;

    /**
     * 类型
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 浏览记录
     */
    private int viewCount;

    /**
     * 是否有效
     */
    private int isDelete;
}
