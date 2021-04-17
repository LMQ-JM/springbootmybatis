package com.example.learn.service.impl;

import com.example.common.utils.Paging;
import com.example.learn.Vo.DryGoodsVo;
import com.example.learn.dao.DryGoodsMapper;
import com.example.learn.entity.DryGoods;
import com.example.learn.service.IDryGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/16 15:59
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DryGoodsServiceImpl implements IDryGoodsService {

    @Autowired
    private DryGoodsMapper dryGoodsMapper;

    @Override
    public Object queryLearnList(int type, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        //提问
        if(type == 0){

        }
        //干货
        if(type == 1){
            List<DryGoodsVo> dryGoods = dryGoodsMapper.queryDryGoodsList(sql);
            return dryGoods;
        }
        //公开课
        if(type == 2){

        }
        return null;
    }

    @Override
    public int addDryGoods(DryGoods dryGoods) {
        dryGoods.setCreateAt(System.currentTimeMillis() / 1000 + "");
        return dryGoodsMapper.addDryGoods(dryGoods);
    }
}
