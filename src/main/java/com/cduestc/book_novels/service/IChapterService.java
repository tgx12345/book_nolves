package com.cduestc.book_novels.service;

import com.cduestc.book_novels.bean.Chapter;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Map;

public interface IChapterService {
    Map<Integer, Chapter> queryChaptersByfiction_id(int fiction_id) throws JsonProcessingException;

    Chapter  queryChapter(int fiction_id,int sort) throws JsonProcessingException; //查询章节信息

    void updateChapter( int fiction_id,String chapterName,String chapterContent) throws IOException;

}
