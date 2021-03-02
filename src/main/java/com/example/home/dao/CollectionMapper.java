package com.example.home.dao;

import com.example.home.entity.Collection;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/3/2 17:34
 */
@Component
public interface CollectionMapper {

    /**
     * 根据用户id和帖子id查询数据是否存在数据库
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select * from tb_user_collection where u_id=${userId} and t_id=${tid}")
    Collection selectCountWhether(@Param("userId") int userId,@Param("tid") int tid);

    /**
     * 添加收藏信息
     * @param collection 收藏对象
     * @return
     */
    @Insert("insert into tb_user_collection(u_id,t_id,create_at,remarks)values(${collection.uId},${collection.tId},#{collection.createAt},#{collection.remarks})")
    int addCollection(@Param("collection") Collection collection);

    /**
     * 修改帖子收藏的状态
     * @param id 收藏id
     * @param status 状态id
     * @return
     */
    @Update("update tb_user_collection set is_delete=${status} where id=${id}")
    int updateCollectionStatus(@Param("id") int id,@Param("status") int status);
}
