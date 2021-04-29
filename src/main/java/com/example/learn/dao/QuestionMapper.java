package com.example.learn.dao;

import com.example.learn.vo.QuestionVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/28 15:16
 */
@Component
public interface QuestionMapper {

    /**
     * 查询提问列表
     * @param sql
     * @return
     */
    @Select("select a.id,a.title,a.description,a.content_type,a.cover_img,a.favour,a.collect,a.comment,a.tags_two,b.tag_name from " +
            "tb_question a LEFT JOIN tb_tags b on a.tags_two = b.id where a.is_delete = 1 ${sql}")
    List<QuestionVo> queryQuestionList(@Param("sql") String sql);
}
