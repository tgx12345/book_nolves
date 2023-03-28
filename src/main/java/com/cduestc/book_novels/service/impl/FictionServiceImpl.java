package com.cduestc.book_novels.service.impl;

import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.commom.DataGridView;
import com.cduestc.book_novels.mapper.ChapterMapper;
import com.cduestc.book_novels.mapper.FictionMapper;
import com.cduestc.book_novels.mapper.ShelfMapper;
import com.cduestc.book_novels.service.IFictionService;

import com.cduestc.book_novels.vo.FictionVo;
import com.cduestc.book_novels.utils.FileOperation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class FictionServiceImpl implements IFictionService {
    @Resource
    RedisTemplate<String,String> redisTemplate;

    private  final ObjectMapper objectMapper=new ObjectMapper();
    private  final DataSourceTransactionManager transactionManager;

    @Autowired
    public FictionServiceImpl(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 首页需要
     * 默认参数
     */
    private final static Integer CURR = 1;
//    当前页
    private final static Integer LIMIT = 20;
//    分页 页内数目
    private final static Integer VIEW_TYPE = 2;
//    默认图文
    private final static Integer TYPE = 0;
//    书的类型



    @Resource
    FictionMapper fictionMapper;
    @Resource
    ShelfMapper shelfMapper;
    @Resource
    ChapterMapper chapterMapper;

    @Override
    public List<Fiction> getALL() {
        List<Fiction> list = fictionMapper.getAllfiction();
        return list;
    }

    @Override
    @Async("async")
    public void deleteFiction(int id) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("planetwo-deleteFiction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        设置状态点
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        List<Integer> ids =new ArrayList<Integer>();
        ids.add(id);
        List<Fiction> fictions = fictionMapper.getFictionByIds(ids);
        try {
//  先把 shelf中存在这本书的删掉
            shelfMapper.deleteShelfByIds(ids);
            fictionMapper.deletefictionByIds(ids);
            fictionMapper.deleteByIdChapter(id);

            transactionManager.commit(transactionStatus);
        }
        catch (Exception e){
            //事务回滚
            transactionManager.rollback(transactionStatus);
            log.info("事务发生回滚"+def.getName());
           return;
        }
//        先通过ids 查询Fictions imgurl  txturl  再把这本书源文件和fiction删除 最后删除相关chapter
        for (int i = 0; i <fictions.size(); i++) {
            Fiction fiction = fictions.get(i);

        FileOperation.delFile(fiction.getFictionUrl());
        FileOperation.delFile(fiction.getImgUrl());
        }

    }

    @Override
    @Async("async")
    public void deleteByIds(List<Integer> ids) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("planetwo-deleteFiction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        设置状态点
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        shelfMapper.deleteShelfByIds(ids);
        List<Fiction> fictions = fictionMapper.getFictionByIds(ids);
        int n = fictionMapper.deletefictionByIds(ids);
        for (int i = 0; i <fictions.size(); i++) {
            Fiction fiction = fictions.get(i);
            fictionMapper.deleteByIdChapter(fiction.getId());
        }
        transactionManager.commit(transactionStatus);
        for (int i = 0; i <fictions.size(); i++) {
            Fiction fiction = fictions.get(i);

            FileOperation.delFile(fiction.getFictionUrl());
            FileOperation.delFile(fiction.getImgUrl());

        }


    }

    @Override
    public DataGridView queryAllFiction(FictionVo fictionVo) {
//        首页根据条件查询
        String sqltype=null;
        String sqlstate=null;
        String sqlnum=null;
        String sqltime=null;
        //当前页和页数
        if (null == fictionVo.getCurr()) {
            fictionVo.setCurr(CURR);
            fictionVo.setLimit(LIMIT);
        }
        //图片模式或者列表模式
        Integer viewType = null;
        if (null == fictionVo.getViewType()) {
            viewType = VIEW_TYPE;
        } else {
            viewType = fictionVo.getViewType();
        }
        if (null == fictionVo.getType()) {

            fictionVo.setType(TYPE);
        }
//        fictionVo.setNewState(FictionVo.getTypeName(fictionVo.getType());
        //分类查询
        if (null != fictionVo.getType() && !fictionVo.getType().equals(TYPE)) {
            String typeName = FictionVo.getTypeName(fictionVo.getType());
            sqltype=typeName;
        }
        //写作进度查询
        if (null != fictionVo.getState() && !fictionVo.getState().equals(TYPE)) {
            String state = FictionVo.getState(fictionVo.getState());
            sqlstate=state;
        }//字数查询
        if (null != fictionVo.getNumber() && !fictionVo.getNumber().equals(TYPE)) {
            int a=1,b=2,c=3,d=4,e=5;
//            &lt; &gt;
            if (fictionVo.getNumber().equals(a)) {
                sqlnum ="`number` <=  300000";
            }
            if (fictionVo.getNumber().equals(b)) {
                sqlnum ="`number`  >= 500000";
            }
            if (fictionVo.getNumber().equals(c)) {
                sqlnum ="`number`  >= 500000 and `number`  <= 1000000";
            }
            if (fictionVo.getNumber().equals(d)) {
                sqlnum ="`number`  >= 1000000 and `number`  <= 2000000";
            }
            if (fictionVo.getNumber().equals(e)) {
                sqlnum ="`number`  >= 2000000";
            }
        }
        if (null != fictionVo.getTime() && !fictionVo.getTime().equals(TYPE)) {
            DateTime today = new DateTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月和小时的格式为两个大写字母


            int a=1,b=2,c=3,d=4;
            if (fictionVo.getTime().equals(a)){
                sqltime=df.format(today.minusDays(3).toDate());
            }
            if (fictionVo.getTime().equals(b)){
                sqltime=df.format(today.minusDays(7).toDate());
            }
            if (fictionVo.getTime().equals(c)){
                sqltime=df.format(today.minusDays(15).toDate());
            }
            if (fictionVo.getTime().equals(d)){
                sqltime=df.format(today.minusDays(30).toDate());
            }
        }
        PageHelper.startPage(fictionVo.getCurr(),fictionVo.getLimit());
//        根据条件查询出fictions  再把分页信息 输出
//        ！注意点 ： fiction没有章节名称列（而且是最新）  sql里加个max(sort)
        List<Fiction> fictions = fictionMapper.getListByCondition(sqltype, sqlstate, sqlnum, sqltime);
//         浏览器不支持访问本地路径的图片  需要将路由转换
        List<Fiction> fictions1 = FileOperation.updateImgurl(fictions);


        PageInfo<Fiction> page= new PageInfo<>(fictions,4);
        return new DataGridView(page.getPageNum(), page.getSize(), page.getTotal(), fictions1, viewType, fictionVo.getType());

    }

    @Override
    public List<Fiction> queryLikeFiction(String fictionName) {
        List<Fiction> fictions = fictionMapper.queryFictionLike(fictionName);
        List<Fiction> fictions1 = FileOperation.updateImgurl(fictions);
        return fictions1;
    }

    @Override
    public Fiction queryFictionById(int id) throws JsonProcessingException {

        Fiction fiction = fictionMapper.getFictionById(id);

//        需要使用图片路径 需要更改
        String imgUrl = fiction.getImgUrl();
        String newImgUrl = imgUrl.substring(imgUrl.lastIndexOf("\\"));
        fiction.setImgUrl("/imgurl"+newImgUrl);
        return fiction;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addView(Fiction fiction) {
//        传入sql 新fiction
        Integer views = fiction.getViews();
        fiction.setViews(views+1);
        fictionMapper.updateView(fiction);

    }

    @Override
    public List<Fiction> queryViewsFiction() {
        List<Fiction> fictions = fictionMapper.queryViews();
        if(fictions.size()!=3) {
            log.info("获取点击量的书籍集合个数为" + fictions.size());
        }
        List<Fiction> fictions1 = FileOperation.updateImgurl(fictions);
        return fictions1;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @Async("async")
    public void updateFiction(Fiction fiction) {
//     修改   fiction_name,author,state,type,brief
        fictionMapper.updateFiciton(fiction);
    }

    @Override
    @Async("async")
    public void queryChaptersById(int id) throws JsonProcessingException {
//        传入redis   key为fiction_id  value:Map<integer,Chapter>
        Map<Integer,Chapter> map = chapterMapper.queryMapChapterByficiton_id(id);
        Map<String,String>  map2=new HashMap<>();
        for (Map.Entry<Integer,Chapter> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String s = objectMapper.writeValueAsString(entry.getValue());
            map2.put(key, s);
        }

//        加入redis
        String s = String.valueOf(id);
        redisTemplate.opsForHash().putAll(s,map2);
//        redisTemplate.expire(s, 1000, TimeUnit.SECONDS);
    }


}
