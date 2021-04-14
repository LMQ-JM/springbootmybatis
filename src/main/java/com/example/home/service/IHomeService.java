package com.example.home.service;

import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.home.entity.Collection;
import com.example.home.entity.Resources;
import com.example.home.vo.CommunityVo;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.ResourcesVo;
import com.example.tags.entity.Tag;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface IHomeService {


    /**
     * 搜索数据接口
     * @param strata 状态
     * @param postingName 内容
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    Object selectAllSearch(int strata,String postingName,int userId, Paging paging);

    /**
     * 查询搜索记录和其他相关信息
     * @param userId 用户id
     * @return
     */
    Map<String,Object> querySearchRecords(int userId);

    /**
     *  查询资源的第一级标签
     * @return
     */
    List<Tag> selectFirstLevelLabelResource();

    /**
     * 资源市场和学习交流的接口
     * @param id 一级标签id
     * @param paging 分页
     * @return
     */
    Object selectResourceLearningExchange(int id, Paging paging);

    /**
     * 根据社区分类id查询帖子
     * @param id 二级标签id
     * @param paging 分页
     * @return
     */
    List<HomeClassificationVo> selectPostsByCommunityCategoryId(int id, Paging paging);

    /**
     * 根据社区分类id查询圈子信息
     * @param id 标签id
     * @return
     */
    CommunityVo selectCommunityCategoryId(int id);


    /**
     * 查询单个资源帖子
     * @param id 帖子id
     * @param userId 用户id
     * @return
     */
    ResourcesVo selectSingleResourcePost(int id, int userId) throws ParseException;

    /**
     * 根据一级标签id查询所有视频
     * @param id 一级标签id
     * @param paging 分页
     * @return
     */
    List<ResourcesVo> queryAllVideosPrimaryTagId(int id, Paging paging,int userId) throws ParseException;

    /**
     * 根据二级标签id查询推荐数据
     * @param id 二级标签id
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    List<HomeClassificationVo> selectRecommendedSecondaryTagId(int id,int userId,int tid);

    /**
     * 插叙所有资源数据
     * @param resources
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    ReturnVo selectResourcesAllPosting(Resources resources , Integer page, Integer limit, String startTime, String endTime,String userName) throws Exception;

    /**
     * 后台
     * 批量删除
     * @param id 帖子id
     * @return
     */
    Integer resourcesDeletes(Integer[] id);


    /**
     * 增加资源贴
     * @param resources
     * @return
     */
    int addResourcesPost(Resources resources);

    /**
     * 发布
     * @param resources 对象
     * @param imgUrl 图片地址
     * @param postType 帖子类型 （0资源贴，1圈子贴）
     * @param whetherCover（1自己选的，0系统默认）
     * @throws Exception
     */
    void issueResourceOrCircle(Resources resources,String imgUrl,int postType,int whetherCover) throws Exception;


    /**
     * 查询推荐的帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    List<HomeClassificationVo> selectRecommendPost(int userId,Paging paging);


    /**
     * 收藏帖子
     * @param collection 收藏对象
     * @return
     */
    int collectionPost(Collection collection);


    /**
     * 单元体导航栏点击查询
     * @param type 单元体导航栏id
     * @param postType 0 资源 1圈子
     * @param userId 用户id
     * @param tagId 二级标签id
     * @param paging 分页
     * @return
     */
    Object queryClickUnitNavigationBar(int type,int postType,int userId,int tagId,Paging paging);

    /**
     * 查询首页中间二级标签
     * @param tagId 一级标签id
     * @param userId 用户id
     * @return
     */
    List<Tag> queryMiddleSecondaryTagHomePage(int tagId,int userId);

    /**
     * 删除服务器图片
     * @param type 0 代表是要删除图片  1删除视频
     * @param imgUrl 图片路劲
     */
    void deleteFile(int type,String imgUrl);

    /**
     * 删除圈子和资源帖
     * @param type 类型 0资源 1圈子
     * @param id 帖子id
     */
    void deletePosts(int type,int id);

}
