package com.cduestc.book_novels.service;

import com.cduestc.book_novels.bean.Chapter;

import java.io.IOException;
import java.util.List;

public interface IChapterService {
    List<Chapter>  queryChaptersByfiction_id(int fiction_id);

    Chapter  queryChapter(int fiction_id,int sort); //查询章节信息

    void updateChapter( int fiction_id,String chapterName,String chapterContent) throws IOException;

}
