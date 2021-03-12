package com.example.complaintsSuggestions.service;

import com.example.complaintsSuggestions.entity.ComplaintsSuggestions;
import org.apache.ibatis.annotations.Param;

/**
 * @author MQ
 * @date 2021/3/12 17:06
 */
public interface IComplaintsSuggestionsService {

    /**
     * 添加投诉与建议新
     * @param complaintsSuggestions 投诉与建议新对象
     * @return
     */
    int addComplaintsSuggestions(@Param("complaintsSuggestions") ComplaintsSuggestions complaintsSuggestions);
}
