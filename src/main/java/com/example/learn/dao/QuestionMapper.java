package com.example.learn.dao;

import com.example.learn.entity.Question;
import com.example.learn.vo.QuestionVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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

    /**
     * 增加提问帖
     * @param question
     * @return
     */
    @Insert("insert into tb_question(u_id,title,tags_one,tags_two,description,cover_img,content,create_at) value(${question.uId},#{question.title},${question.tagsOne},${question.tagsTwo},#{question.description},#{question.coverImg},#{question.content},#{question.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="question.id",keyColumn="id")
    int addQuestion(@Param("question")Question question);
}
