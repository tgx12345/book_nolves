package com.cduestc.book_novels.mapper;

import com.cduestc.book_novels.bean.Chapter;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ChapterMapper {
    List<Chapter>  queryChapterByficiton_id(@Param("fiction_id")int fiction_id);

//    根据 {fiction_id}/{sort}  查询章节
    Chapter queryChapter(@Param("fiction_id")int fiction_id,@Param("sort")int sort);
    Integer queryMaxSort(@Param("fiction_id")int fiction_id);
    void  insertChapter(Chapter chapter);

    @MapKey("sort")
    Map<Integer, Chapter>  queryMapChapterByficiton_id(@Param("fiction_id")int fiction_id);

}
