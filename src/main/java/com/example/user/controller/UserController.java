package com.example.user.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ReturnVo;
import com.example.user.entity.AdminUser;
import com.example.user.entity.User;
import com.example.user.service.IUserService;
import com.example.user.util.Upload;
import com.example.user.vo.UserHtVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/16 16:18
 */
@Api(tags = "用户API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/UserController")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private Upload upload;


    /**
     *
     *  小程序登陆
     * @return
     */
    @ApiOperation(value = "小程序登陆",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/wxLogin")
    public User wxLogin(String code, String userName, String avatar, String address,String sex) {
        if(code==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iUserService.wxLogin(code,userName,avatar,address,sex);
    }




    /**
     *
     *  根据条件查询帖子
     * @return
     */
    @ApiOperation(value = "根据条件查询帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryAllUserForSql")
    public ReturnVo queryAllUserForSql(UserHtVo userHtVo,Integer page, Integer limit) throws Exception {
        if(page==null || limit==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iUserService.queryAllUserForSql(userHtVo, page, limit);
    }

    @ApiOperation(value = "查询用户名密码", notes = "查询用户名密码")
    @ResponseBody
    @GetMapping("/selectUserNamePassword")
    public AdminUser selectUserNamePassword(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        // TODO Auto-generated method stub
        AdminUser useList=iUserService.selectUserNamePassword(userName, password);
        if(useList!=null){
            return useList;
        }
        return null;
    }


    @ApiOperation(value = "得到图片地址", notes = "得到图片地址")
    @ResponseBody
    @PostMapping("/getImgAddress")
    public ReturnVo getImgAddress(@RequestParam("files") MultipartFile file) {
        // TODO Auto-generated method stub
        List<String> upload = this.upload.upload(file);
        System.out.println("=="+ this.upload);
        ReturnVo returnVo=new ReturnVo();
        returnVo.setList(upload.get(0));
        return returnVo;
    }

    @ApiOperation(value = "增加系统用户", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/addAdminUser")
    public int addAdminUser(AdminUser adminUser) {
        // TODO Auto-generated method stub
        int i = iUserService.addAdminUser(adminUser);
        return i;

    }


}
