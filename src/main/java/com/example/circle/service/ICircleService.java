package com.example.circle.service;

import com.example.circle.entity.Circle;
import com.example.circle.vo.CircleClassificationVo;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/1/19 16:10
 */
public interface ICircleService {
    /**
     * 查询所有圈子的数据
     * @return
     */
    ReturnVo queryAllCircles();

    /**
     * 添加圈子
     * @param circle
     * @return
     */
    int addCirclePost(Circle circle);

    /**
     * 查询所有圈子的和资源数据
     * @param circle
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    ReturnVo selectAllPosting(Circle circle,Integer page,Integer limit,String startTime,String endTime) throws ParseException;

    /**
     * 批量删除帖子
     * @param id
     * @return
     */
    Integer deletes(Integer[] id);

    /**
     * 根据圈子中的标签id查询帖子
     * @param id 一级标签id
     * @param paging 分页
     * @return List<Circle>
     * @param userId 用户id
     * @return
     */
    List<CircleClassificationVo> selectPostsBasedTagIdCircle(int id, Paging paging,int userId);

    /**
     * 点赞
     * @param id 帖子id
     * @param userId 用户id
     * @return
     */
    int givePost(int id,int userId);

    /**
     * 查询单个圈子的帖子
     * @param id 圈子id
     * @param userId 用户id
     * @return
     */
    CircleClassificationVo querySingleCircle(int id,int userId);

    /**
     * 发布
     * @param circle 对象
     * @param imgUrl 图片地址
     * @param postType 帖子类型 （0资源贴，1圈子贴）
     * @param whetherCover（1自己选的，0系统默认）
     * @throws Exception
     */
    void issueResourceOrCircle(Circle circle, String imgUrl, int postType, int whetherCover) throws Exception ;
}
