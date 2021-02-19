package com.example.user.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ReturnVo;
import com.example.common.utils.SHA1Util;
import com.example.user.dao.UserMapper;
import com.example.user.entity.AdminUser;
import com.example.user.entity.User;
import com.example.user.service.IUserService;
import com.example.user.vo.UserHtVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/16 16:19
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ReturnVo queryAllUserForSql(UserHtVo userHtVo, Integer pageNum, Integer pageSize) throws Exception {
        String sql="";

        pageNum=(pageNum-1)*pageSize;


        if(!"undefined".equals(userHtVo.getUserName())){
                sql+=" and user_name like '%"+userHtVo.getUserName()+"%'";
        }

        Integer integer = userMapper.userCount(sql);

        String paging=" limit "+pageNum+","+pageSize+"";

        List<User> userHtVos = userMapper.queryAllUserForSql(sql, paging);

        ReturnVo returnVo=new ReturnVo();
        returnVo.setCount(integer);
        returnVo.setList(userHtVos);

        return returnVo;
    }

    @Override
    public AdminUser selectUserNamePassword(String userName, String password) {
         password = SHA1Util.encode(password);
        AdminUser useList=userMapper.selectUserNamePassword(userName, password);
        log.info("{}",useList);
        return useList;
    }

    @Override
    public int addAdminUser(AdminUser adminUser) {
        String password = SHA1Util.encode(adminUser.getPassword());
        adminUser.setPassword(password);
        adminUser.setCreateAt(System.currentTimeMillis()/1000+"");
        adminUser.setUpdateAt(System.currentTimeMillis()/1000+"");
        int i = userMapper.addAdminUser(adminUser);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加系统账号错误");
        }
        return i;
    }
}
