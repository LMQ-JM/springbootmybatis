package com.example.home.service.impl;

import com.example.circle.dao.CircleMapper;
import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.*;
import com.example.home.dao.*;
import com.example.home.entity.*;
import com.example.home.service.IHomeService;
import com.example.home.vo.*;
import com.example.tags.dao.TagMapper;
import com.example.tags.entity.Tag;
import com.example.user.dao.UserMapper;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private SearchRecordMapper searchRecordMapper;

    @Autowired
    private HaplontMapper haplontMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Resources> selectAllSearch(String postingName,int userId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        if(userId!=0){
            //增加搜索记录
            int i = searchRecordMapper.addSearchRecord(postingName, System.currentTimeMillis() / 1000 + "",userId);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加历史记录错误");
            }
        }


        return homeMapper.selectAllSearch(postingName, sql);
    }

    @Override
    public Map<String,Object> querySearchRecords(int userId) {
        Map<String,Object> map=new HashMap<>(15);

        //根据用户id查询历史记录
        List<SearchHistory> searchHistories = searchRecordMapper.selectSearchRecordByUserId(userId);

        if(userId==0){
            List<User> users = userMapper.selectRandom();
            map.put("users",users);
            map.put("searchHistories",searchHistories);
            return map;
        }




        //查询出自己选中的标签
        UserTag userTag=homeMapper.selectOneselfLabel(userId);

        /*List<Integer> idArr=new ArrayList<>();

        JSONArray  j= JSONArray.fromObject(userTag.getTab());
        List<LabelVo> list = JSONArray.toList(j, LabelVo.class);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getChecked()=="true"){
                idArr.add(list.get(i).getTagId());
            }
        }*/
        List<User> users = userMapper.selectRandom();
        map.put("searchHistories",searchHistories);
        map.put("users",users);
        return map;
    }

    @Override
    public List<Tag> selectFirstLevelLabelResource() {
        return tagMapper.selectFirstLevelLabelResource(0);
    }

    @Override
    public Object selectResourceLearningExchange(int id, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        //点击人才 进入判断 返回人才页面格式的数据
        if(id==14){
            List<RecruitVo> recruits = recruitMapper.selectAllRecruit(pagings,"ORDER BY a.view_count desc");
            for (int i=0;i<recruits.size();i++){
                //得到岗位要求标签组
                List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruits.get(i).getId());
                recruits.get(i).setRecruitLabels(recruitLabels);
            }
            return recruits;
        }

        List<HomeClassificationVo> homeClassificationVos = homeMapper.selectResourceLearningExchange(id, pagings);
        return homeClassificationVos;
    }

    @Override
    public List<Resources> selectPostsByCommunityCategoryId(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";
        List<Resources> resources = homeMapper.selectPostsByCommunityCategoryId(id, pagings);
        for (int i=0;i<resources.size();i++){
            //根据帖子id查询出当前帖子图片
            String[] strings = homeMapper.selectImgByPostId(resources.get(i).getId());
            resources.get(i).setImg(strings);
        }


        return resources;
    }

    @Override
    public CommunityVo selectCommunityCategoryId(int id) {
        //查询圈子信息
        CommunityVo communityVo = communityMapper.selectCommunityCategoryId(id);
        //得到圈子总人数
        int i = communityMapper.selectTotalNumberCirclesById(communityVo.getId());
        communityVo.setTotalNumberCircles(i);

        //查询圈子的用户头像
        String[] strings = communityMapper.selectCirclesAvatar(communityVo.getId());
        communityVo.setAvatar(strings);

        //得到单元体导航栏
        List<Haplont> haplonts = haplontMapper.selectHaplontByTagId(id);
        communityVo.setHaplontList(haplonts);

        return communityVo;
    }


    @Override
    public ResourcesVo selectSingleResourcePost(int id,int userId) throws ParseException {

        ResourcesVo resourcesVo = homeMapper.selectSingleResourcePost(id);

        //在用户登录的情况下 增加帖子浏览记录
        System.out.println(userId);
        if(userId!=0){
            //查看是否收藏
            int selectWhetherCollection = collectionMapper.selectWhetherCollection(userId, id);
            System.out.println(selectWhetherCollection);
            if(selectWhetherCollection>0){
                resourcesVo.setWhetherCollection(1);
            }

            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(id, userId);
            if(s==null){
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis()/1000+"");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(0);
                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = homeMapper.updateBrowse(id);
                if(i1<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }else{
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if(minutesApart>=1440){
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis()/1000+"");
                    browse.setUId(userId);
                    browse.setZqId(id);
                    browse.setType(0);
                    //增加浏览记录
                    int i = browseMapper.addBrowse(browse);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = homeMapper.updateBrowse(id);
                    if(i1<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }

        }


        //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
        String time = DateUtils.getTime(resourcesVo.getCreateAt());
        //根据帖子id查询出当前帖子图片
        String[] strings = homeMapper.selectImgByPostId(resourcesVo.getId());
        resourcesVo.setImg(strings);
        resourcesVo.setCreateAt(String.valueOf(time));

        //得到收藏数量
        int i = collectionMapper.selectCollectNumber(id);
        resourcesVo.setCollect(i);


        //得到浏览过人的头像
        String[] strings1 = browseMapper.selectBrowseAvatar(id);
        resourcesVo.setBrowseAvatar(strings1);
        //得到这个帖子的观看数量
        int browse = browseMapper.countPostNum(id);
        resourcesVo.setBrowse(browse);



        return resourcesVo;
    }

    @Override
    public List<HomeClassificationVo> selectRecommendedSecondaryTagId(int id) {
        List<HomeClassificationVo> homeClassificationVos = homeMapper.selectRecommendedSecondaryTagId(id);
        return homeClassificationVos;
    }

    @Override
    public ReturnVo selectResourcesAllPosting(Circle circle, Integer page, Integer limit, String startTime, String endTime) throws Exception {
        String sql="";
        Integer pages=(page-1)*limit;
        if(!circle.getTitle().equals("undefined") && !circle.getTitle().equals("")){
            sql+=" and a.title like '%"+circle.getTitle()+"%'";
        }
        //如果发帖人不为空 ，根据发帖人查询帖子
        if(!circle.getUserName().equals("undefined") && !circle.getUserName().equals("")){
            sql+="and a.user_name like '%"+circle.getUserName()+"%'";
        }

        //将时间格式转换为时间戳
        //开始时间
        if(!startTime.equals("undefined") && !endTime.equals("undefined") && !startTime.equals("null") && !endTime.equals("null")){
            if(!startTime.equals("") && !endTime.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String startTimes=String.valueOf(sdf.parse(startTime).getTime() / 1000);

                //结束时间
                SimpleDateFormat sdftwo = new SimpleDateFormat("yyyy-MM-dd");
                String endTimes=String.valueOf(sdftwo.parse(endTime).getTime() / 1000);

                if(!"undefined".equals(startTime) && !endTime.equals("undefined") ){
                    sql+="and a.create_at>= "+startTimes+" and a.create_at<="+endTimes+"";
                }
            }
        }
        String paging=" limit "+pages+","+limit+"";
        List<ResourcesLabelVo> resourcesLabelVos = homeMapper.selectResourcesAllPosting(sql, paging);
        //根据不同条件得到不同帖子数量
        Integer integer = homeMapper.selectResourcesAllPostingCount(sql);

        ReturnVo returnVo=new ReturnVo();
        returnVo.setCount(integer);
        returnVo.setList(resourcesLabelVos);
        return returnVo;
    }

    @Override
    public Integer resourcesDeletes(Integer[] id) {
        Integer deletes=0;
        for (int i=0; i<id.length;i++){
            deletes= homeMapper.deletes(id[i]);
        }

        if(deletes<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"批量删除失败");
        }
        return deletes;
    }

    @Override
    public int addResourcesPost(Resources resources) {
        resources.setCreateAt(System.currentTimeMillis()/1000+"");

        int i1 = homeMapper.addResourcesPost(resources);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加圈子帖子失败");
        }


        if(resources.getType()==0) {
            //添加图片组
            Img img = new Img();
            img.setType(0);
            img.setZId(resources.getId());
            img.setCreateAt(System.currentTimeMillis() / 1000 + "");
            for (int i = 0; i < resources.getImg().length; i++) {
                img.setImgUrl(resources.getImg()[i]);
                int addImg = circleMapper.addImg(img);
                if (addImg <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "添加图片组失败");
                }
            }
        }


        return 1;
    }

    @Override
    public void issueResourceOrCircle(Resources resources, String imgUrl, int postType, int whetherCover) throws Exception {

        //资源帖子
        if(postType==0){
            issue(resources,imgUrl,postType,whetherCover);
        }

        //圈子帖子
        if(postType==1){
            issue(resources,imgUrl,postType,whetherCover);
        }
    }

    public void issue(Resources resources, String imgUrl, int postType, int whetherCover)throws Exception{
        resources.setCreateAt(System.currentTimeMillis()/1000+"");

        String[] split = null;

        //自己选封面
        if(whetherCover==1){
            if(imgUrl!=null || !"undefined".equals(imgUrl)){
                split=imgUrl.split(",");
            }

        }

        //系统默认封面
        if(whetherCover==0){
            if(imgUrl!=null || !"undefined".equals(imgUrl)){
                split=imgUrl.split(",");
                resources.setCover(split[0]);
            }else{
                String videoCover = FfmpegUtil.getVideoCover(resources.getVideo());
                resources.setCover(videoCover);
            }
        }

        int i = homeMapper.addResourcesPost(resources);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        if(imgUrl!=null || !"undefined".equals(imgUrl)){
            int addImg = homeMapper.addImg(resources.getId(), split, System.currentTimeMillis() / 1000 + "", postType);
            if(addImg<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }


    }

    @Override
    public List<HomeClassificationVo> selectRecommendPost(int userId, Paging paging) {
        int page=(paging.getPage()-1)*paging.getLimit();

        String sql="limit "+page+","+paging.getLimit()+"";

        //没有登录的情况下 随机给出数据

        if(userId==0){
            //查询出浏览量最多的帖子
            List<HomeClassificationVo> homeClassificationVos = homeMapper.selectRandom(sql);
            return homeClassificationVos;
        }

        List<Integer> idArr=new ArrayList<Integer>();

        //查询出自己选中的标签
        UserTag userTag=homeMapper.selectOneselfLabel(userId);
        //如果用户没有选中标签就查询出浏览量最多的帖子
        if(userTag==null){
            List<HomeClassificationVo> homeClassificationVos = homeMapper.selectRandom(sql);
            return homeClassificationVos;
        }

        JSONArray  j= JSONArray.fromObject(userTag.getTab());
        List<LabelVo> list = JSONArray.toList(j, LabelVo.class);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getChecked()=="true"){
                idArr.add(list.get(i).getTagId());
            }
        }

        List<HomeClassificationVo> homeClassificationVos = homeMapper.selectPostByTagOne(idArr,sql);
        return homeClassificationVos;
    }

    @Override
    public int collectionPost(Collection collection) {

        collection.setCreateAt(System.currentTimeMillis()/1000+"");
        //查看是否有数据存在
        Collection collection1 = collectionMapper.selectCountWhether(collection.getUId(),collection.getTId());


        //如果不存在
        if(collection1==null){
            //添加收藏信息
            int addCollection = collectionMapper.addCollectionPost(collection.getUId(),collection.getTId(),collection.getCreateAt(),collection.getRemarks());
            if(addCollection<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"添加收藏信息错误");
            }
            return addCollection;
        }

        int i =0;
        //如果当前状态是1 那就改为0 取消收藏
        if(collection1.getIsDelete()==1){
             i=collectionMapper.updateCollectionStatus(collection1.getId(), 0);
        }

        //如果当前状态是0 那就改为1 为收藏状态
        if(collection1.getIsDelete()==0){
             i = collectionMapper.updateCollectionStatus(collection1.getId(), 1);
        }

        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }


        return i;
    }






}