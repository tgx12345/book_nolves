package com.cduestc.book_novels.service.impl;

import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.mapper.ChapterMapper;
import com.cduestc.book_novels.mapper.FictionMapper;
import com.cduestc.book_novels.service.IChapterService;
import com.cduestc.book_novels.utils.FileOperation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ChapterServiceImpl implements IChapterService {

    @Resource
    ChapterMapper chapterMapper;
    @Resource
    FictionMapper fictionMapper;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    private  final ObjectMapper objectMapper=new ObjectMapper();


    @Override
    public Map<Integer, Chapter> queryChaptersByfiction_id(int fiction_id) throws JsonProcessingException {
//查询redis
        if (redisTemplate.hasKey(fiction_id+"")){
            Map<Object, Object> map1 = redisTemplate.opsForHash().entries(fiction_id + "");
            Map<Integer, Chapter> chaptermap = new HashMap<>();

            for (Map.Entry<Object, Object> entry : map1.entrySet()) {
                Integer key = Integer.valueOf(entry.getKey().toString());
                Chapter chapter = objectMapper.readValue(entry.getValue().toString(), Chapter.class);
                chaptermap.put(key, chapter);
            }
            return chaptermap;
        }
//        List<Chapter> chapters = chapterMapper.queryChapterByficiton_id(fiction_id);
        Map<Integer,Chapter> map = chapterMapper.queryMapChapterByficiton_id(fiction_id);
        return map;
    }

    @Override
    public Chapter queryChapter(int fiction_id, int sort) throws JsonProcessingException {
//查询redis
        String s = String.valueOf(fiction_id);

        if ( redisTemplate.opsForHash().hasKey(s, sort + "")){
            Object cha = redisTemplate.opsForHash().get(s, sort + "");
            Chapter chapter1 = objectMapper.readValue(cha.toString(), Chapter.class);
            return chapter1;
        }
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