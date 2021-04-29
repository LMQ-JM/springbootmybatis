package com.example.learn.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.learn.dao.QuestionMapper;
import com.example.learn.vo.DryGoodsTagVo;
import com.example.learn.vo.DryGoodsVo;
import com.example.learn.dao.DryGoodsCollectMapper;
import com.example.learn.dao.DryGoodsGiveMapper;
import com.example.learn.dao.DryGoodsMapper;
import com.example.learn.entity.Collect;
import com.example.learn.entity.DryGoods;
import com.example.learn.entity.Give;
import com.example.learn.service.IDryGoodsService;
import com.example.learn.vo.QuestionVo;
import com.example.user.dao.UserMapper;
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

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private DryGoodsGiveMapper dryGoodsGiveMapper;

    @Autowired
    private DryGoodsCollectMapper dryGoodsCollectMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Object queryLearnList(int type, Paging paging, int orderRule) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql = "";
        if (orderRule == 0){
            sql = "order by collect DESC ";
        }
        if (orderRule == 1){
            sql = "order by create_at DESC ";
        }
        if (orderRule == 2){
            sql = "order by favour DESC ";
        }
        sql = sql + "limit "+page+","+paging.getLimit()+"";

        //提问
        if(type == 0){
            String sql2 = "";
            if (orderRule == 0){
                sql2 = "order by a.collect DESC ";
            }
            if (orderRule == 1){
                sql2 = "order by a.create_at DESC ";
            }
            if (orderRule == 2){
                sql2 = "order by a.favour DESC ";
            }
            sql2 = sql2 + "limit "+page+","+paging.getLimit()+"";
            List<QuestionVo> questionVos = questionMapper.queryQuestionList(sql2);
            return questionVos;
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
    public DryGoodsTagVo queryDryGoodsById(int id,int userId) {
        DryGoodsTagVo goodsTagVo = dryGoodsMapper.queryDryGoodsById(id);
        //统计点赞数量(暂未写入tb_dry_goods表favour字段)
        //Integer giveCount = dryGoodsGiveMapper.selectGiveNumber(1,id);
        //goodsTagVo.setFavour(giveCount);
        //统计收藏数量(暂未写入tb_dry_goods表collect字段)
        //Integer collectCount = dryGoodsCollectMapper.selectCollectNumber(1,id);
        //goodsTagVo.setCollect(collectCount);
        //获取发帖人名称
        String uName = userMapper.selectUserById(goodsTagVo.getUId()).getUserName();
        if(uName==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        goodsTagVo.setUName(uName);
        //如果userId为0，用户处于未登录状态，状态设为未点赞
        if (userId == 0){
            goodsTagVo.setWhetherGive(0);
            goodsTagVo.setWhetherCollect(0);
            return goodsTagVo;
        }
        //我是否对该帖子点过赞
        Integer giveStatus = dryGoodsGiveMapper.whetherGive(1,userId, goodsTagVo.getId());
        if (giveStatus == 0) {
            goodsTagVo.setWhetherGive(0);
        } else {
            goodsTagVo.setWhetherGive(1);
        }
        //我是否对该帖子收过藏
        Integer collectStatus = dryGoodsCollectMapper.whetherCollect(1,userId, goodsTagVo.getId());
        if (collectStatus == 0) {
            goodsTagVo.setWhetherCollect(0);
        } else {
            goodsTagVo.setWhetherCollect(1);
        }
        return goodsTagVo;
    }

    @Override
    public int addDryGoods(DryGoods dryGoods) {
        dryGoods.setCreateAt(System.currentTimeMillis() / 1000 + "");
        return dryGoodsMapper.addDryGoods(dryGoods);
    }

    @Override
    public int giveLike(int id, int userId) {
        //查询数据库是否存在该条数据
        Give give = dryGoodsGiveMapper.selectCountWhether(1,userId,id);
        if(give == null){
            int i = dryGoodsGiveMapper.giveLike(1,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = dryGoodsMapper.updateDryGoodsGive(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消点赞
        if(give.getGiveCancel()==1){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 0);
            j = dryGoodsMapper.updateDryGoodsGive(id,"-");
        }

        //如果当前状态是0 那就改为1 为点赞状态
        if(give.getGiveCancel()==0){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 1);
            j = dryGoodsMapper.updateDryGoodsGive(id,"+");
        }

        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return j;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(1,userId,id);
        if(collect == null){
            int i = dryGoodsCollectMapper.giveCollect(1,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = dryGoodsMapper.updateDryGoodsCollect(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if(collect.getGiveCancel()==1){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = dryGoodsMapper.updateDryGoodsCollect(id,"-");
        }

        //如果当前状态是0 那就改为1 为收藏状态
        if(collect.getGiveCancel()==0){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = dryGoodsMapper.updateDryGoodsCollect(id,"+");
        }

        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return j;
    }
}
