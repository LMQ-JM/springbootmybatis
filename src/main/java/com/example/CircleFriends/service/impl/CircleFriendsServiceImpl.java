package com.example.CircleFriends.service.impl;

import com.example.CircleFriends.service.ICircleFriendsService;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.common.utils.WxPoster;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/4/6 13:40
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CircleFriendsServiceImpl implements ICircleFriendsService {


    @Override
    public List<String> selectCircleFriendsFigure(String headUrl, String postImg, String postContent, String userName,String pageUrl,String title) {
        if(postImg.equals("") || postContent.equals("")){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }


        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        String time = "";

        List<String> posterList=new ArrayList<>();

        //获取token
        String token = ConstantUtil.getToken();

        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token;

            Map<String,Object> param = new HashMap<>();
            //秘钥
            param.put("scene", ConstantUtil.secret);
            //二维码指向的地址
            param.put("page", pageUrl);
            param.put("width", 430);
            param.put("auto_color", false);
            param.put("is_hyaline", true);//去掉二维码底色
            Map<String,Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            param.put("line_color", line_color);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String,String>();
            // 头部信息
            List<String> list = new ArrayList<String>();
            list.add("Content-Type");
            list.add("application/json");
            headers.put("header", list);

            @SuppressWarnings("unchecked")
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);

            byte[] result = entity.getBody();

            inputStream = new ByteArrayInputStream(result);

            File file = new File("e:/file/img/"+System.currentTimeMillis()+".png");

            if (!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();

            time=System.currentTimeMillis()/1000+13+"";


            WxPoster wxPoster=new WxPoster();
            //生成海报5
            String posterUrlGreatMaster = wxPoster.getPosterUrlGreatMaster("e:/file/img/2021515.jpg", file.getPath(), "e:/file/img/" + time + ".png", headUrl, postImg,postContent,userName,title);
            String newGreat = posterUrlGreatMaster.replace("e:/file/img/", "https://www.gofatoo.com/img/");
            posterList.add(newGreat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posterList;
    }

}
