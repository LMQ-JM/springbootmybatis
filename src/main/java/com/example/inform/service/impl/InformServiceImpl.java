package com.example.inform.service.impl;

import com.example.common.utils.Paging;
import com.example.inform.dao.InformMapper;
import com.example.inform.service.IInformService;
import com.example.inform.vo.InformUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/22 10:46
 */
@Slf4j
@Service
@Transactional(rollbackFor =Exception.class)
public class InformServiceImpl implements IInformService {

    @Autowired
    private InformMapper informMapper;

    @Override
    public List<InformUserVo> queryCommentsNotice(int userId,int type, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        List<InformUserVo> informUserVos = informMapper.queryCommentsNotice(userId, type, pagings);
        return informUserVos;
    }
}
