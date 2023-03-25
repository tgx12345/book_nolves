package com.cduestc.book_novels.service.impl;

import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.mapper.ChapterMapper;
import com.cduestc.book_novels.mapper.FictionMapper;
import com.cduestc.book_novels.service.IChapterService;
import com.cduestc.book_novels.utils.FileOperation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

@Service
public class ChapterServiceImpl implements IChapterService {

    @Resource
    ChapterMapper chapterMapper;
    @Resource
    FictionMapper fictionMapper;

    @Override
    public List<Chapter> queryChaptersByfiction_id(int fiction_id) {
        List<Chapter> chapters = chapterMapper.queryChapterByficiton_id(fiction_id);

        return chapters;
    }

    @Override
    public Chapter queryChapter(int fiction_id, int sort) {
        Chapter chapter = chapterMapper.queryChapter(fiction_id, sort);
        return chapter;
    }

    @Override
    @Async("async")
    @Transactional(rollbackFor = {Exception.class})
    public void updateChapter( int fiction_id,String chapterName,String chapterContent) throws IOException {
        //        查询最新章节sort  更新章节内容   - 改变书籍的时间   字数  最新章节
        BufferedReader reader=null;
        Integer words=null;
        try {
         reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(chapterContent.getBytes())));
             words = FileOperation.wordsCount(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            reader.close();
        }

        Integer sort = chapterMapper.queryMaxSort(fiction_id);
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterName);
        chapter.setChapterContent(chapterContent);
        chapter.setFictionId(fiction_id);
        chapter.setSort(sort+1);
        String nowDate = FileOperation.seletNowDate();
        chapter.setCreateDate(nowDate);
       chapterMapper.insertChapter(chapter);
        Fiction fictionold = fictionMapper.getFictionById(fiction_id);
      int  newwords= Integer.parseInt(fictionold.getNumber())+words;
        Fiction fiction = new Fiction();
        fiction.setId(fiction_id);
        fiction.setCreateDate(nowDate);
        fiction.setNewest(chapterName);
        fiction.setNumber(String.valueOf(newwords));
        fictionMapper.updateFictionBynewChapter(fiction);

    }


}