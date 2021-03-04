package com.example.home.dao;

import com.example.home.entity.Haplont;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 9:57
 */
@Component
public interface HaplontMapper {

    /**
     * 根据标签id查询单元体导航栏
     * @param id 标签id
     * @return
     */
    @Select("SELECT a.id,a.h_name FROM `tb_haplont` a INNER JOIN tb_tag_haplont b on  a.id=b.haplont_id where b.tag_id=${id}")
    List<Haplont> selectHaplontByTagId(@Param("id") int id);
}
