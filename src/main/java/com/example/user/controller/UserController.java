package com.example.user.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ReturnVo;
import com.example.user.entity.AdminUser;
import com.example.user.entity.LoginTag;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
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
        if("undefined".equals(code)){
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
    public User selectUserNamePassword(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        User useList=iUserService.selectUserNamePassword(userName, password);
        if(useList!=null){
            return useList;
        }
        return null;
    }


    @ApiOperation(value = "得到图片地址", notes = "得到图片地址")
    @ResponseBody
    @PostMapping("/getImgAddress")
    public ReturnVo getImgAddress(@RequestParam("files") MultipartFile file) throws Exception {
        List<String> upload = this.upload.upload(file);
        ReturnVo returnVo=new ReturnVo();
        returnVo.setList(upload.get(0));
        return returnVo;
    }

    @ApiOperation(value = "增加系统用户", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/addAdminUser")
    public int addAdminUser(AdminUser adminUser) {
        int i = iUserService.addAdminUser(adminUser);
        return i;
    }

    @ApiOperation(value = "修改用户信息", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/updateUser")
    public int updateUser(User user) {
        int i = iUserService.updateUser(user);
        return i;
    }


    @ApiOperation(value = "查询所有标签", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/selectAllUserLabel")
    public List<LoginTag> selectAllUserLabel() {
        return iUserService.selectAllUserLabel();
    }

    @ApiOperation(value = "增加或修改用户选中的标签关系", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/addUserAndLabel")
    public int addUserAndLabel(UserTag userTag) {
        if(userTag.getUId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iUserService.addUserAndLabel(userTag);
    }


    @ApiOperation(value = "根据用户id查询是否有登录进来的时候选中过标签", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/selectWhetherHaveLabel")
    public int selectWhetherHaveLabel(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iUserService.selectWhetherHaveLabel(userId);
    }


    @ApiOperation(value = "根据id查询用户", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/selectUserById")
    public User selectUserById(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iUserService.selectUserById(userId);
    }

    @ApiOperation(value = "点击头像进入的接口", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/ClickInterfaceHeadImageEnter")
    public User ClickInterfaceHeadImageEnter(int bUserId,int gUserId) {
        if(bUserId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iUserService.ClickInterfaceHeadImageEnter(bUserId,gUserId);
    }

    @ApiOperation(value = "封号", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/sealUserNumber")
    public void sealUserNumber(int userId,int status) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
         iUserService.sealUserNumber(userId,status);
    }
}
