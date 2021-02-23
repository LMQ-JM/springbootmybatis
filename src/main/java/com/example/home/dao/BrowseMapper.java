package com.example.home.dao;

import com.example.home.entity.Browse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @create 2021/2/22
 **/
@Component
public interface BrowseMapper {
    /**
     * 增加浏览量
     * @param browse
     * @return
     */
    @Insert("insert into tb_browse(u_id,zq_id,type,create_at)values(${browse.uId},${browse.zqId},${browse.type},${browse.createAt})")
    int addBrowse(@Param("browse") Browse browse);
}
