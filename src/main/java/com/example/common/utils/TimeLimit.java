package com.example.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MQ
 * @PROJECT_NAME: game_project
 * @DESCRIPTION:
 * @DATE: 2020/6/23 14:51
 *
 * 限制一天只能调用多少次方法
 */
public class TimeLimit {

    public static Map<String,Map<Long,Integer>> accountPwdCount = new HashMap<String,Map<Long,Integer>>();

    /**
     *
     * @param userId 唯一
     * @param number 次数
     * @return
     */
    public static synchronized boolean TackBackLimit(Long userId,Integer number){
        boolean isPass = true;
        //拿到一个时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        String oneKey = df.format(new Date());

        Map<Long,Integer> todayMap = accountPwdCount.get(oneKey);

        if(todayMap==null){

            todayMap = new HashMap<Long,Integer>();
            todayMap.put(userId, 1);
            accountPwdCount.put(oneKey, todayMap);

        }else{

            Integer acountCount = todayMap.get(userId);
            if(acountCount==null){
                todayMap.put(userId, 1);
            }else{
                if(acountCount>=number){
                    isPass=false;
                }else{
                    todayMap.put(userId, acountCount+1);
                }
            }
        }
        /**
         * 清理历史数据start
         */
        accountPwdCount = new HashMap<String,Map<Long,Integer>>();
        accountPwdCount.put(oneKey, todayMap);
        /**
         * 清理历史数据end
         */
        return isPass;
    }
}
