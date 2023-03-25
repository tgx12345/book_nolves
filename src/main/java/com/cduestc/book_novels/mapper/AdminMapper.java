package com.cduestc.book_novels.mapper;

import com.cduestc.book_novels.bean.Admin;
import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.bean.Fiction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
  Admin selectOneAdmin(@Param("username") String username, @Param("password") String password);


  void  insertOneFiction(Fiction fiction);
  void  insertChapter(@Param("chapters") List<Chapter> chapters);
  void  updateNewestByid(@Param("id") int id,@Param("Newest")String Newest);
}
