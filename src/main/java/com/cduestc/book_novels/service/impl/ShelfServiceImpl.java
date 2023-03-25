package com.cduestc.book_novels.service.impl;

import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.Shelf;
import com.cduestc.book_novels.mapper.ShelfMapper;
import com.cduestc.book_novels.service.IShelfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Slf4j
@Service
public class ShelfServiceImpl implements IShelfService {
    private  final DataSourceTransactionManager transactionManager;

    @Autowired
    public ShelfServiceImpl(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Resource
    ShelfMapper shelfMapper;

    @Override
    public List<Shelf> queryShelfs(int userid) {

        List<Shelf> shelves = shelfMapper.queryShelfsById(userid);
//    1.    查询一个shelf的结构为 {shelf,fiction:{id,name,...}}   2.修改图片路径
            if(shelves==null){
                log.error("查询书架里的书籍出现问题");
            }
        for(Shelf shelf:shelves){

            String imgUrl = shelf.getFiction().getImgUrl();
            String newImgUrl = imgUrl.substring(imgUrl.lastIndexOf("\\"));
            shelf.getFiction().setImgUrl("/imgurl"+newImgUrl);
        }

        return shelves;
    }

    //查询书籍是否存在书架
    @Override
    public Shelf queryShelf(int user_id, int fiction_id) {
        Shelf shelf = shelfMapper.queryShelf(user_id, fiction_id);

        return shelf;
    }

    @Override
    public Responseresult addShelf(HttpServletRequest request, int fiction_id) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("plane-insertShelf");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);

        HttpSession session = request.getSession();
        if (null == session.getAttribute("id")) {
            return new Responseresult(-1, "未登录");
        }
        int userId = (int) session.getAttribute("id");

        //        第1步 查询书架中最大sort   暂时不写书籍排序

        // 第2步 写入书架
        Shelf shelf = new Shelf(null,fiction_id,null,userId,1);


        try {
            shelfMapper.insertShelf(shelf);
            transactionManager.commit(transactionStatus);
        }catch (Exception e){
            transactionManager.rollback(transactionStatus);
            log.error("userId:"+userId+"书架加入失败");
            log.info(e.toString());
            return new Responseresult(-1, "加入书架失败");
        }


        return new Responseresult(200, "加入书架成功");
    }

    @Override
    public boolean deleteShelf(int fiction_id, int userId) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("plane-deleteShelf");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            shelfMapper.deleteShelfOneFiction(userId,fiction_id);
            transactionManager.commit(transactionStatus);
            return true;
        }catch (Exception e){
            log.error("移除书架失败"+userId+"书籍id："+fiction_id);
            transactionManager.rollback(transactionStatus);
            return false;
        }

    }

    @Override
    public boolean updateShelf(int fiction_id, int user_id, int sort) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("plane-updateShelf");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);

//        如果书籍没在书架  则加入书架
//        存在 修改lastReadSort
        try {
            Shelf shelf = shelfMapper.queryShelf(user_id, fiction_id);
            if (null == shelf) {
                Shelf shelf1 = new Shelf();
                shelf1.setFictionId(fiction_id);
                shelf1.setLastReadSort(sort);
                shelf1.setUserId(user_id);
                shelfMapper.insertShelf(shelf1);
            } else {
//
                shelf.setLastReadSort(sort);
                shelfMapper.updateShelf(shelf);
            }
            transactionManager.commit(transactionStatus);
        }catch (Exception e){
            transactionManager.rollback(transactionStatus);
            log.info(e.toString());
            return false;
        }

        return true;
    }


}