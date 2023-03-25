package com.cduestc.book_novels.service;

import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.Shelf;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IShelfService {
    List<Shelf>   queryShelfs(int userid);

    Shelf  queryShelf(int user_id , int fiction_id); //查询书籍是否存在书架

//    添加书籍到书架

    Responseresult addShelf(HttpServletRequest request, int fiction_id);
   boolean deleteShelf(int fiction_id,int userId);
  boolean  updateShelf(int fiction_id,int  user_id,int sort);
}
