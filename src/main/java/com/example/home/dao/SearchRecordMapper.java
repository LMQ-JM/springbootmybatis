package com.example.home.dao;

import com.example.home.entity.SearchHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/3 16:03
 */
@Component
public interface SearchRecordMapper {


    /**
     * 增加搜索历史记录
     * @param historicalContent
     * @param createAt
     * @return
     */
    @Insert("insert into tb_search_history(historical_content,create_at,user_id)values(#{historicalContent},#{createAt},${userId})")
    int addSearchRecord(@Param("historicalContent") String historicalContent,@Param("createAt") String createAt,@Param("userId") int userId);

    /**
     * 根据用户id查询历史记录
     * @param userId
     * @return
     */
    @Select("select *,count(distinct historical_content) from tb_search_history where user_id=${userId} order by create_at desc limit 4")
    List<SearchHistory> selectSearchRecordByUserId(@Param("userId") int userId);
}
