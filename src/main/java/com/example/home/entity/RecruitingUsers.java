package com.example.home.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author MQ
 * @create 2021/2/19
 * 公司实体类
 **/
@Data
@TableName("tb_recruiting_users")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecruitingUsers extends Model<RecruitingUsers> {

    @TableId
    private int id;

    /**
     * 发布人名称
     */
    private String userName;

    /**
     * 公司名称
     */
    private String corporateName;


    /**
     * 公司描述
     */
    private String companyDescription;

    /**
     * 公司人数
     */
    private int numberCompanies;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 是否需要融资
     */
    private String needFinancing;

    /**
     * 一个用户id对应一个公司
     */
    private int userId;

    /**
     * 创建时间
     */
    private String createAt;
}
