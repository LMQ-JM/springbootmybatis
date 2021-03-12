package com.example.complaintsSuggestions.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.complaintsSuggestions.entity.ComplaintsSuggestions;
import com.example.complaintsSuggestions.service.IComplaintsSuggestionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author MQ
 * @date 2021/3/12 16:58
 */
@Api(tags = "投诉与建议API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/ComplaintsSuggestionsController")
public class ComplaintsSuggestionsController {

    @Autowired
    private IComplaintsSuggestionsService iComplaintsSuggestionsService;

    /**
     * 进入单元体的接口
     * 根据社区分类id查询圈子信息
     * @return
     */
    @ApiOperation(value = "根据社区分类id查询圈子信息 ",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addComplaintsSuggestions")
    public int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions)  {
        if(complaintsSuggestions.getUserId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iComplaintsSuggestionsService.addComplaintsSuggestions(complaintsSuggestions);
    }

}
