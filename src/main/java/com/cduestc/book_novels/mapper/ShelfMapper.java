package com.cduestc.book_novels.mapper;

import com.cduestc.book_novels.bean.Shelf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShelfMapper {
//      user_id   fiction_id 相等  才存在我的书架shelf
Shelf  queryShelf(@Param("user_id")int user_id,@Param("fiction_id")int fiction_id);

//    通过user_id查询书架里的书 返回list
List<Shelf> queryShelfsById(@Param("id") int id);

//添加书籍到书架
    void  insertShelf(Shelf shelf);

    //删除书架中的书籍
    void  deleteShelfOneFiction(@Param("user_id")int user_id,@Param("fiction_id")int fiction_id);

//    管理员删除书籍  应该吧书架中的这本书也删掉(通过书的id)
    void  deleteShelfByIds(List<Integer> id);

//    读书前置  修改last_read_sort
    void  updateShelf(Shelf shelf);

    void  deleteShelfByuserId(@Param("user_id")int user_id);
}
