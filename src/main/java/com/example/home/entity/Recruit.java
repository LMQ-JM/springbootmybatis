package com.example.home.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/19
 **/
@Data
@TableName("tb_recruit")
public class Recruit extends Model<Recruit> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 工资范围
     */
    private String wageRange;

    /**
     * 公司id对应公司表里面的用户id
     */
    private int companyId;

    /**
     * 浏览次数
     */
    private int viewCount;

    /**
     * 职位要求
     */
    private String jobRequirements;

    /**
     * (1招全职，2招兼职，3找全职，4找兼职)
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效
     */
    private int isDelete;
}
