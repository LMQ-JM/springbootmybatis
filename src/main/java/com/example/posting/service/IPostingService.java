package com.example.posting.service;

import com.example.common.utils.ReturnVo;
import com.example.posting.vo.PostingVo;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/1/15 10:18
 */
public interface IPostingService {

    /**
     * 后台查寻所有帖子
     * @param postingVo
     * @return
     * @throws ParseException
     */
    ReturnVo selectAllPosting(PostingVo postingVo) throws ParseException;

    /**
     * 批量删除帖子
     * @param id
     * @return
     */
    Integer deletes(Integer[] id);
}
