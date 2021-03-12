package com.example.complaintsSuggestions.dao;

import com.example.complaintsSuggestions.entity.ComplaintsSuggestions;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/3/12 17:08
 */
@Component
public interface ComplaintsSuggestionsMapper {

    /**
     * 添加投诉与建议新
     * @param complaintsSuggestions 投诉与建议新对象
     * @return
     */
    @Insert("insert into tb_complaints_suggestions(content,user_id,create_at)values(#{complaintsSuggestions.content},${complaintsSuggestions.userId},#{complaintsSuggestions.createAt})")
    int addComplaintsSuggestions(@Param("complaintsSuggestions") ComplaintsSuggestions complaintsSuggestions );
}
