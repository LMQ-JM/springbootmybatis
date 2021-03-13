package com.example.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.common.utils.ReturnVo;
import com.example.common.utils.SHA1Util;
import com.example.home.dao.HomeMapper;
import com.example.personalCenter.dao.PersonalCenterMapper;
import com.example.user.dao.UserMapper;
import com.example.user.entity.AdminUser;
import com.example.user.entity.LoginTag;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import com.example.user.service.IUserService;
import com.example.user.vo.UserHtVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private PersonalCenterMapper personalCenterMapper;

    @Override
    public User wxLogin(String code,String userName,String avatar,String address,String sex) {
        //微信登录的code值
        String wxCode = code;

        //服务器端调用接口的url
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        //封装需要的参数信息
        Map<String,String> requestUrlParam = new HashMap<String,String>(16);
        //开发者设置中的appId
        requestUrlParam.put("appid", ConstantUtil.appid);
        //开发者设置中的appSecret
        requestUrlParam.put("secret",ConstantUtil.secret);
        //小程序调用wx.login返回的code
        requestUrlParam.put("js_code", wxCode);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //效验
        JSONObject jsonObject = JSON.parseObject(sendPost(requestUrl,requestUrlParam));

        //得到用户的唯一id
        String openid = jsonObject.getString("openid");

        //根据openid查询数据库是否存在
        User user = userMapper.selectUserByOpenId(openid);
        if(user!=null){
            return user;
        }else{
            //增加新用户信息
            User user1=new User();
            user1.setAvatar(avatar);
            user1.setOpenId(openid);
            user1.setUserName(userName);
            user1.setUserSex(sex);
            user1.setCreateAt(System.currentTimeMillis()/1000+"");

            int i1 = userMapper.selectMaxId()+1;
            user1.setMCode("gft"+i1);
            int i = userMapper.addUser(user1);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return user1;
        }
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while(it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


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
    public User selectUserNamePassword(String userName, String password) {
         //password = SHA1Util.encode(password);
        User user = userMapper.selectUserNamePassword(userName, password);
        log.info("{}",user);
        return user;
    }

    @Override
    public int addAdminUser(AdminUser adminUser) {

        AdminUser adminUser1 = userMapper.selectUserByUserName(adminUser.getAccount());
        if(adminUser1!=null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"用户名已存在");
        }

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

    @Override
    public int updateUser(User user) {

        List<User> users = userMapper.selectRandom();
        //和数据库的比较是否有相同名字
        users.stream().filter(u->u.getUserName()==user.getUserName()).forEach(u ->{
            throw new ApplicationException(CodeType.RESOURCES_EXISTING);
        });


        String sql="";

        if(user.getAvatar()!=null && !user.getAvatar().equals("") && user.getUserName()!=null && !user.getUserName().equals("")){
            sql="avatar='"+user.getAvatar()+"',user_name='"+user.getUserName()+"'";
        }else{
            if(user.getAvatar()!=null && !user.getAvatar().equals("")){
                sql=" avatar='"+user.getAvatar()+"'";
            }

            if(user.getUserName()!=null && !user.getUserName().equals("")){
                sql=" user_name='"+user.getUserName()+"'";
            }
        }



        int i = personalCenterMapper.updateUserMessage(sql, user.getId());
         if(i<=0){
             throw new ApplicationException(CodeType.SERVICE_ERROR);
         }
        return i;
    }

    @Override
    public List<LoginTag> selectAllUserLabel() {
        return userMapper.selectAllUserLabel();
    }

    @Override
    public int addUserAndLabel(UserTag userTag) {

        //查询出自己选中的标签
        UserTag userTag1=homeMapper.selectOneselfLabel(userTag.getUId());
        if(userTag1==null){
            userTag.setCreateAt(System.currentTimeMillis()/1000+"");
            int i = userMapper.addUserAndLabel(userTag);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return i;
        }


        //删除用户之前选中的标签
        int i = userMapper.deleteUserAndLabel(userTag.getUId());
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除失败");
        }

        //增加新的用户与标签关系
        userTag.setCreateAt(System.currentTimeMillis()/1000+"");
        int i1 = userMapper.addUserAndLabel(userTag);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return i1;
    }





    @Override
    public int selectWhetherHaveLabel(int userId) {
        int i = userMapper.selectWhetherHaveLabel(userId);
        return i;
    }

    @Override
    public User selectUserById(int userId) {
        return userMapper.selectUserById(userId);
    }
}
