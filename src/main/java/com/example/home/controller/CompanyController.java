package com.example.home.controller;

import com.example.home.entity.RecruitingUsers;
import com.example.home.service.ICompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/3/29 15:19
 */
@Api(tags = "公司API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CompanyController")
public class CompanyController {

    @Autowired
    private ICompanyService iCompanyService;

    @ApiOperation(value = "添加公司信息", notes = "成功返回集合")
    @ResponseBody
    @PostMapping("/addRecruitingUsers")
    public int addRecruitingUsers(RecruitingUsers recruitingUsers) throws ParseException {
        return iCompanyService.addRecruitingUsers(recruitingUsers);
    }

    @ApiOperation(value = "查询是否填写过公司信息", notes = "成功返回集合")
    @ResponseBody
    @PostMapping("/queryCount")
    public int queryCount(int userId) throws ParseException {
        return iCompanyService.queryCount(userId);
    }


    @ApiOperation(value = "查询单个公司信息", notes = "成功返回集合")
    @ResponseBody
    @PostMapping("querySingleCompanyInformation")
    public RecruitingUsers querySingleCompanyInformation(int userId) throws ParseException {
        return iCompanyService.querySingleCompanyInformation(userId);
    }

}
