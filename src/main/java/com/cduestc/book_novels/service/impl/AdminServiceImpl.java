package com.cduestc.book_novels.service.impl;

import com.cduestc.book_novels.bean.Admin;
import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.mapper.AdminMapper;
import com.cduestc.book_novels.service.IAdminService;
import com.cduestc.book_novels.utils.Chapterutil;
import com.cduestc.book_novels.utils.FileOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;


@Slf4j
@Service
public class AdminServiceImpl implements IAdminService{

    private  final   DataSourceTransactionManager transactionManager;

    @Autowired
    public AdminServiceImpl(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Resource
    AdminMapper adminMapper;

    @Override
    public boolean loginCheck(Admin admin) {
        Admin admin2 = adminMapper.selectOneAdmin(admin.getUsername(), admin.getPassword());

        if (null==admin2){
            return false;
        }else {
            return true;
        }

    }

    @Override
    @Async("async")
    public void insertOneFiction(File txtfile,
                                 File imgfile,
                                 String author,
                                 String fictionName, String brief,
                                 String state, String type) throws IOException {

        DefaultTransactionDefinition  def = new DefaultTransactionDefinition();
        def.setName("planeOne-insertOneFiction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        设置状态点
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);

//        使用MultipartFile获取了 name
        String filename1 = txtfile.getName();
        //通过 上传的文件名 获取后缀 得到新文件名 ,再通过新文件名,通过组装路径实现上传
        String finalpath = FileOperation.insertDocument(filename1,
                author, "D:\\study\\JavaIDEA\\book_novels_resource\\txt");



        String filename2 = imgfile.getName();
        //通过 上传的文件名 获取后缀 得到新文件名 ,再通过新文件名,通过组装路径实现上传
        String finalimgpath = FileOperation.insertDocument(filename2,
                author, "D:\\study\\JavaIDEA\\book_novels_resource\\img");
        Fiction   fiction=null;
        BufferedReader reader =null;
        //一起上传
        try {
           FileOperation.copyFile(txtfile,finalpath);
            FileOperation.copyFile(imgfile,finalimgpath);
//            上传之后就删除

             reader = new BufferedReader(new InputStreamReader(new FileInputStream(txtfile), "UTF-8"));


            //获取时间
            String date = FileOperation.seletNowDate();
            //---------统计字数number

            String num = String.valueOf(FileOperation.wordsCount(reader));
             fiction  = new Fiction(fictionName, author, date, type, state,
                        num, brief, finalimgpath, finalpath, 0);
            adminMapper.insertOneFiction(fiction);  //插入后返回 fiction  获取id
//            分章节
            ArrayList chapters = Chapterutil.getChapters(txtfile,fiction.getId(),date);
            if(chapters.isEmpty()){
                log.error("章节未匹配成功");
            }
            adminMapper.insertChapter(chapters);
            Object o = chapters.get(chapters.size() - 1);
            Chapter chapter = (Chapter) o;
            String newest = chapter.getChapterName();
//            根据id写入 Newest
            adminMapper.updateNewestByid(fiction.getId(),newest);
            log.info("上传成功");
            transactionManager.commit(transactionStatus);

        } catch (IOException e) {

            //事务回滚
            transactionManager.rollback(transactionStatus);
            log.error(e.toString());

            return  ;

        }
        finally {
            reader.close();
            Files.delete(txtfile.toPath());
            Files.delete(imgfile.toPath());
        }




    }




}
