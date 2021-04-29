package com.example.home.service.impl;

import com.example.circle.dao.AttentionMapper;
import com.example.circle.dao.CircleGiveMapper;
import com.example.circle.dao.CircleMapper;
import com.example.circle.dao.CommentMapper;
import com.example.circle.entity.Attention;
import com.example.circle.entity.Img;
import com.example.circle.vo.CircleClassificationVo;
import com.example.circle.vo.CommentUserVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.*;
import com.example.home.dao.*;
import com.example.home.entity.Collection;
import com.example.home.entity.*;
import com.example.home.service.IHomeService;
import com.example.home.vo.*;
import com.example.personalCenter.dao.PersonalCenterMapper;
import com.example.tags.dao.TagMapper;
import com.example.tags.entity.Tag;
import com.example.user.dao.UserMapper;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import com.example.user.vo.UserHtVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PersonalCenterMapper personalCenterMapper;


    @Override
    public Object selectAllSearch(int strata,String postingName,int userId, Paging paging) {
        if(postingName.equals("undefined")){
            return null;
        }

        if(userId!=0){
            //增加搜索记录
            int i = searchRecordMapper.addSearchRecord(postingName, System.currentTimeMillis() / 1000 + "",userId);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加历史记录错误");
            }
        }

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        //查用户
        if(strata==0){
            List<UserHtVo> users = userMapper.queryFuzzyUser(postingName,sql);
            for (int i=0;i<users.size();i++){
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, users.get(i).getId());
                if(i1>0){
                    users.get(i).setWhetherAttention(1);
                }
            }

            return users;
        }

        //查圈子
        if(strata==2){
            List<CircleClassificationVo> circles = circleMapper.queryFuzzyCircle(postingName, sql);
            for (int i=0;i<circles.size();i++){
                //得到图片组
                String[] strings = homeMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //得到点过赞人的头像
                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
                circles.get(i).setGiveAvatar(strings1);

                //得到点赞数量
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);


                //等于0在用户没有到登录的情况下 直接设置没有点赞
                if(userId==0){
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                }else{
                    //查看我是否关注了此人
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if(i1>0){
                        circles.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if(integer>0){
                        circles.get(i).setWhetherGive(1);
                    }
                }

                //得到帖子评论数量
                Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
                circles.get(i).setNumberPosts(integer2);

                //得到评论数据
                List<CommentUserVo> comments = commentMapper.selectComment(circles.get(i).getId());
                circles.get(i).setComments(comments);

                //将时间戳转换为多少天或者多少个小时和多少年
                String time = DateUtils.getTime(circles.get(i).getCreateAt());
                circles.get(i).setCreateAt(time);

            }
            return circles;
        }

        //查资源
        if(strata==1){
            List<HomeClassificationVo> homeClassificationVos = homeMapper.selectAllSearch(postingName, sql);
            return homeClassificationVos;
        }

        //业务异常
        throw new ApplicationException(CodeType.SERVICE_ERROR);
    }


    static <T> Predicate<T> distinctByKey1(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    @Override
    public Map<String,Object> querySearchRecords(int userId) {

        Map<String,Object> map=new HashMap<>(15);

        List<TagVo> tagVoList=new ArrayList<>();

        //查询用户表所有数据
        List<User> users = userMapper.selectRandom();

        //从list集合随机冲去5条数据
        List<User> randomList = getRandomList(users, 5);

        //查询出我关注的人
        List<Attention> followList=personalCenterMapper.selectFollowByGid(userId);

        //拿用户集合和我关注的人集合匹配id是有相等 如果相等就不要
        List<User> collect2 = randomList.stream().filter(mapa -> followList.stream().anyMatch(map1 -> mapa.getId() != map1.getId())).collect(Collectors.toList());

        //根据用户id查询历史记录
        List<SearchHistory> searchHistories = searchRecordMapper.selectSearchRecordByUserId(userId);

        //使用stream流根据字段去重
        List<SearchHistory> collect = searchHistories.stream().filter(distinctByKey1(s -> s.getHistoricalContent())).collect(Collectors.toList());


        /**
         * 查询前三名的圈子
         */
        //根据一级标签id查询二级标签信息
        List<Tag> tags = tagMapper.queryCorrespondingSecondaryLabel(1);
        for (int i=0;i<tags.size();i++){
            TagVo tagVo=new TagVo();
            //根据二级标签id去帖子表查询统计每个标签有多个帖子
            int q = circleMapper.countPostsBasedTagIdCircle(tags.get(i).getId());
            tagVo.setId(tags.get(i).getId());
            tagVo.setTagName(tags.get(i).getTagName());
            tagVo.setNum(q);
            tagVoList.add(tagVo);
        }

        //前三个
        List<TagVo> tagVoList1=new ArrayList<>();

        //根据集合里面的每个标签帖子数量排序，只取前三个
        List<TagVo> collect1 = tagVoList.stream().sorted(Comparator.comparing(TagVo::getNum).reversed()).collect(Collectors.toList());
        for (int i=0;i<collect1.size();i++){
            int a=i+1;
            collect1.get(i).setRanking("Top."+a);
            //取前三个
            tagVoList1.add(collect1.get(i));
            if(i==2){
                break;
            }
        }
//-------------------------------------------------------------

        if(userId==0){
            map.put("users",collect2);
            map.put("searchHistories",collect);
            map.put("collect1",tagVoList1);
            return map;
        }


        UserTag userTag=homeMapper.selectOneselfLabel(userId);
        if(userTag==null){
            map.put("users",collect2);
            map.put("searchHistories",collect);
            map.put("collect1",tagVoList1);
            return map;
        }

        List<Integer> idArr=new ArrayList<>();

        if(userTag!=null){
            JSONArray j= JSONArray.fromObject(userTag.getTab());
            List<LabelVo> list = JSONArray.toList(j, LabelVo.class);
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getChecked()=="true"){
                    idArr.add(list.get(i).getTagId());
                }
            }
        }

        //根据标签id多表联查出用户数据
        List<User> users1 = homeMapper.selectUserByTagOne(idArr);
        if(users1==null || users1.size()==0){
            //查询用户表所有数据
             users1 = userMapper.selectRandom();
        }
        //从list集合随机冲去5条数据
        List<User> randomList1 = getRandomList(users1, 5);

        //拿用户集合和我关注的人集合匹配id是有相等 如果相等就不要
        List<User> collect3 = randomList1.stream().filter(mapa -> followList.stream().anyMatch(map1 -> mapa.getId() != map1.getId())).collect(Collectors.toList());

        map.put("searchHistories",collect);
        map.put("users",collect3);
        map.put("collect1",tagVoList1);
        return map;
    }


    /**
     * @Description list 随机取数据
     * @params     list    list集合
     *           num     随机取多少条
     **/
    public static List<User> getRandomList(List<User> list, int num) {
        List<User> olist = new ArrayList<>();
        if (list.size() <= num) {
            return list;
        } else {
            Random random = new Random();
            for (int i = 0 ;i<num;i++){
                int intRandom = random.nextInt(list.size() - 1);
                olist.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return olist;
        }
    }

    @Override
    public List<Tag> selectFirstLevelLabelResource() {
        return tagMapper.selectResourcesAllTag(0);
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
    public List<HomeClassificationVo> selectPostsByCommunityCategoryId(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        String str="order by a.create_at desc";

        List<HomeClassificationVo> resources = homeMapper.selectPostsByCommunityCategoryId(id, pagings,str);
        return resources;
    }

    @Override
    public CommunityVo selectCommunityCategoryId(int id) {
        //查询圈子信息
        CommunityVo communityVo = communityMapper.selectCommunityCategoryId(id);
        if(communityVo==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        //根据圈子id查询这个圈子有哪些人参加了讨论
        String[] strings = communityMapper.selectCirclesAvatar(communityVo.getId());
        communityVo.setAvatar(strings);

        //得到用户圈子里面的讨论人数
        communityVo.setTotalNumberCircles(strings.length);

        //得到单元体导航栏
        List<Haplont> haplonts = haplontMapper.selectHaplontByTagId(id);
        communityVo.setHaplontList(haplonts);

        return communityVo;
    }


    @Override
    public ResourcesVo selectSingleResourcePost(int id,int userId) throws ParseException {

        ResourcesVo resourcesVo = homeMapper.selectSingleResourcePost(id);

        //在用户登录的情况下 增加帖子浏览记录
        if(userId!=0){
            //查看是否收藏
            int selectWhetherCollection = collectionMapper.selectWhetherCollection(userId, id);
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
    public List<ResourcesVo> queryAllVideosPrimaryTagId(int id, Paging paging,int userId) throws ParseException {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        //是否收藏
        int selectWhetherCollection=0;

        //List存储数据顺序与插入数据顺序一致，存在先进先出的概念。
        List<ResourcesVo> resourcesVoa=new ArrayList<>();

        //根据id查询单个帖子
        ResourcesVo resourcesVo = homeMapper.selectSingleResourcePost(id);
        //得到这个帖子的观看数量
        int countPostNum = browseMapper.countPostNum(resourcesVo.getId());
        resourcesVo.setBrowse(countPostNum);

        //得到收藏数量
        int selectCollectNumber = collectionMapper.selectCollectNumber(resourcesVo.getId());
        resourcesVo.setCollect(selectCollectNumber);

        //查看是否收藏
        selectWhetherCollection= collectionMapper.selectWhetherCollection(userId, resourcesVo.getId());
        if(selectWhetherCollection>0){
            resourcesVo.setWhetherCollection(1);
        }
        resourcesVoa.add(resourcesVo);

        //根据一级标签id查询所有视频
        List<ResourcesVo> resourcesVos1 = homeMapper.queryAllVideosPrimaryTagId(resourcesVo.getTagsOne(),pagings);

        //去除一样的
        List<ResourcesVo> resourcesVos = resourcesVos1.stream().filter(u -> u.getId() != resourcesVo.getId()).collect(Collectors.toList());
        for (int i =0;i<resourcesVos.size();i++){

            //查看是否收藏
             selectWhetherCollection = collectionMapper.selectWhetherCollection(userId, resourcesVos.get(i).getId());
            if(selectWhetherCollection>0){
                resourcesVos.get(i).setWhetherCollection(1);
            }

            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(resourcesVos.get(i).getCreateAt());
            resourcesVos.get(i).setCreateAt(time);

            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(resourcesVos.get(i).getId(), userId);
            if(s==null){
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis()/1000+"");
                browse.setUId(userId);
                browse.setZqId(resourcesVos.get(i).getId());
                browse.setType(0);

                //增加浏览记录
                int iq = browseMapper.addBrowse(browse);
                if(iq<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = homeMapper.updateBrowse(resourcesVos.get(i).getId());
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
                    browse.setZqId(resourcesVos.get(i).getId());
                    browse.setType(0);

                    //增加浏览记录
                    int ie = browseMapper.addBrowse(browse);
                    if(ie<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = homeMapper.updateBrowse(resourcesVos.get(i).getId());
                    if(i1<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }

            //得到这个帖子的观看数量
            int i2 = browseMapper.countPostNum(resourcesVos.get(i).getId());
            resourcesVos.get(i).setBrowse(i2);

            //得到收藏数量
            int i1 = collectionMapper.selectCollectNumber(resourcesVos.get(i).getId());
            resourcesVos.get(i).setCollect(i1);

            //将查询出来的帖子视屏存放打list中
            resourcesVoa.add(resourcesVos.get(i));
        }

        return resourcesVoa;
    }

    @Override
    public List<HomeClassificationVo> selectRecommendedSecondaryTagId(int id,int userId,int tid) {
        List<HomeClassificationVo> homeClassificationVos = homeMapper.selectRecommendedSecondaryTagId(id);

        //筛选掉等于当前用户id的数据
        //筛选掉当前点进来的帖子是一样的就干掉
        List<HomeClassificationVo> collect = homeClassificationVos.stream().filter(u -> u.getUId() != userId).filter(a-> a.getId()!=tid).collect(Collectors.toList());

        return collect;
    }

    @Override
    public ReturnVo selectResourcesAllPosting(Resources resources, Integer page, Integer limit, String startTime, String endTime,String userName) throws Exception {
        String sql="";

        Integer pages=(page-1)*limit;

        if(!resources.getTitle().equals("undefined") && !resources.getTitle().equals("")){
            sql+=" and a.title like '%"+resources.getTitle()+"%'";
        }

        //如果发帖人不为空 ，根据发帖人查询帖子
        if(!userName.equals("undefined") && !userName.equals("")){
            sql+="and c.user_name like '%"+userName+"%'";
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

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(resources.getTitle(), token);
        if(identifyTextContent.equals("87014")){
           throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //获取token
        String token1 = ConstantUtil.getToken();
        String identifyTextContent1 = ConstantUtil.identifyText(resources.getContent(), token1);
        if(identifyTextContent1.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }


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
            if(resources.getType()==0){
                split=imgUrl.split(",");
            }
        }

        //系统默认封面
        if(whetherCover==0){
            //视频
            if(resources.getType()==1){
                String videoCover = FfmpegUtil.getVideoCover(resources.getVideo());
                resources.setCover(videoCover);
                //图片
            }else if(resources.getType()==0){
                split=imgUrl.split(",");
                resources.setCover(split[0]);
            }
        }

        int i = homeMapper.addResourcesPost(resources);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }



        if(resources.getType()==0){
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


        List<HomeClassificationVo> homeClassificationVos=null;
        //没有登录的情况下 随机给出数据
        if(userId==0){
            //查询出浏览量最多的帖子
            homeClassificationVos = homeMapper.selectRandom(sql);

            return homeClassificationVos;
        }

        List<Integer> idArr=new ArrayList<Integer>();

        //查询出自己选中的标签
        UserTag userTag=homeMapper.selectOneselfLabel(userId);

        //如果用户没有选中标签就查询出浏览量最多的帖子
        if(userTag==null){
            homeClassificationVos= homeMapper.selectRandom(sql);
            return homeClassificationVos;
        }

        JSONArray  j= JSONArray.fromObject(userTag.getTab());
        List<LabelVo> list = JSONArray.toList(j, LabelVo.class);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getChecked()=="true"){
                idArr.add(list.get(i).getTagId());
            }
        }

        List<HomeClassificationVo> homeClassificationVos1 = homeMapper.selectPostByTagOne(idArr, sql);
        if(homeClassificationVos1==null || homeClassificationVos1.size()==0){

            //查询合作 资源 学习的帖子
            homeClassificationVos= homeMapper.selectRandom(sql);

            //混乱的意思
            Collections.shuffle(homeClassificationVos);

            return homeClassificationVos;
        }

         //使用stream流筛选不等于当前用户id的数据
         List<HomeClassificationVo> collect = homeClassificationVos1.stream().filter(u -> u.getUId() != userId).collect(Collectors.toList());

        //混乱的意思
         Collections.shuffle(collect);

         return collect;
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

    @Override
    public Object queryClickUnitNavigationBar(int type,int postType,int userId,int tagId,Paging paging) {
        int page=(paging.getPage()-1)*paging.getLimit();

        String sql="limit "+page+","+paging.getLimit()+"";

        String str="";
        //资源
        if(postType==0){
            //查询最新的数据
            if(type==1){
                str="order by a.create_at desc";
                List<HomeClassificationVo> resources = homeMapper.selectPostsByCommunityCategoryId(tagId, sql,str);
                return resources;
            }

            //查询最热的数据
            if(type==2){
                str="order by a.browse desc";
                List<HomeClassificationVo> resources = homeMapper.selectPostsByCommunityCategoryId(tagId, sql,str);
                return resources;
            }

            //查询找货数据
            if(type==3){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询找人办事数据
            if(type==4){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询发任务数据
            if(type==5){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询介绍数据
            if(type==6){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询开店问题数据
            if(type==7){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询运营问题数据
            if(type==8){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询技巧问题数据
            if(type==9){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }

            //查询提问数据
            if(type==10){
                List<HomeClassificationVo> homeClassificationVos = homeMapper.queryPostByHaplontType(type,sql,tagId);
                return homeClassificationVos;
            }


        }

        if(postType==1){

            List<CircleClassificationVo> circles=null;

            //查询最新的数据
            if(type==1){
                str="order by a.create_at desc";
                 circles = circleMapper.selectPostsBasedTagIdCircleTwo(tagId, sql);
            }

            //查询最热的数据
            if(type==2){
                str="order by a.favour desc";
                 circles = circleMapper.selectPostsBasedTagIdCircleTwo(tagId, sql);
            }
            //查询发任务数据
            if(type==5){
                 circles = circleMapper.queryPostByHaplontType(type,sql,tagId);
            }

            //查询介绍数据
            if(type==6){
                 circles = circleMapper.queryPostByHaplontType(type,sql,tagId);

            }


            for (int i=0;i<circles.size();i++){
                //得到图片组
                String[] strings = homeMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //得到点过赞人的头像
                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
                circles.get(i).setGiveAvatar(strings1);

                //得到点赞数量
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);


                //等于0在用户没有到登录的情况下 直接设置没有点赞
                if(userId==0){
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                }else{
                    //查看我是否关注了此人
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if(i1>0){
                        circles.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if(integer>0){
                        circles.get(i).setWhetherGive(1);
                    }
                }


                //得到帖子评论数量
                Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
                circles.get(i).setNumberPosts(integer2);

                //得到评论数据
                List<CommentUserVo> comments = commentMapper.selectComment(circles.get(i).getId());
                circles.get(i).setComments(comments);
            }

            return circles;
        }

        return null;
    }

    @Override
    public List<Tag> queryMiddleSecondaryTagHomePage(int tagId,int userId) {
        if(tagId==0){
            List<Tag> tags = homeMapper.queryMiddleSecondaryTagHomePage();
            return tags;
        }

        //根据一级标签查询二级标签
        List<Tag> tags = tagMapper.selectResourcesAllTags(tagId);

        return tags;
    }



    @Override
    public void deleteFile(int type, String imgUrl) {

        String substring = imgUrl.substring(imgUrl.lastIndexOf("/"));

        String documentType="";
        //0代表是图片
        if(type==0){
            documentType="img";
        }

        //1代表是视屏
        if(type==1){
            documentType="video";
        }
        File file = new File("e://file/"+documentType+""+substring+"");
        //判断文件是否存在
        if (file.exists()){
            boolean delete = file.delete();
            if(!delete){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"删除服务器文件错误!");
            }
        }else{
            throw new ApplicationException(CodeType.SERVICE_ERROR,"图片不存在!");
        }
    }

    @Override
    public void deletePosts(int type, int id) {
        String str="";

        if(type==0){
            str="tb_resources";
        }

        if(type==1){
            str="tb_circles";
        }

        int i = homeMapper.deletePosts(str, id);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除失败");
        }

    }


}