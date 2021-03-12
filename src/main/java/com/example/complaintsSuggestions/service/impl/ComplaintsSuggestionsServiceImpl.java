package com.example.complaintsSuggestions.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.complaintsSuggestions.dao.ComplaintsSuggestionsMapper;
import com.example.complaintsSuggestions.entity.ComplaintsSuggestions;
import com.example.complaintsSuggestions.service.IComplaintsSuggestionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MQ
 * @date 2021/3/12 17:06
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ComplaintsSuggestionsServiceImpl implements IComplaintsSuggestionsService {

    @Autowired
    private ComplaintsSuggestionsMapper complaintsSuggestionsMapper;

    @Override
    public int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions) {
        complaintsSuggestions.setCreateAt(System.currentTimeMillis()/1000+"");
        int i = complaintsSuggestionsMapper.addComplaintsSuggestions(complaintsSuggestions);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"投诉错误");
        }
        return i;
    }
}
