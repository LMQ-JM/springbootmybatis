package com.example.user.dao;

import com.example.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/15 17:33
 */
@Component
public interface ViewingRecordMapper {

    /**
     * 添加观看记录
     * @param bUserId 被观看人id
     * @param gUserId 观看人id
     * @param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_viewing_record(viewers_id,beholder_id,create_at)values(${gUserId},${bUserId},#{createAt})")
    int addViewingRecord(@Param("bUserId") int bUserId,@Param("gUserId") int gUserId,@Param("createAt") String createAt);

    /**
     * 查询观看过我的人
     * @param bUserId
     * @return
     */
    @Select("select b.id,b.avatar,b.user_name,b.introduce from tb_viewing_record a INNER JOIN tb_user b on a.viewers_id=b.id " +
            "where a.beholder_id=${bUserId} GROUP BY a.viewers_id ORDER BY a.create_at desc")
    List<User> selectViewingRecord(@Param("bUserId") int bUserId);

}
