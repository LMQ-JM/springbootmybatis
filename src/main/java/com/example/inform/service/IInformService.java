package com.example.inform.service;

import com.example.common.utils.Paging;
import com.example.inform.vo.InformUserVo;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/22 10:36
 */
public interface IInformService {

    /**
     * 查询评论通知
     * @param userId 当前用户id
     * @param type 1评论，2获赞
     * @param paging 分页
     * @return
     */
    List<InformUserVo> queryCommentsNotice(int userId,int type, Paging paging);
}
