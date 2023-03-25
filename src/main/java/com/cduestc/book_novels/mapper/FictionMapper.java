package com.cduestc.book_novels.mapper;

import com.cduestc.book_novels.bean.Fiction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface FictionMapper  {
//    获取所有的小说的信息 id,fiction_name,create_date,author,views,number
    List<Fiction>  getAllfiction();
//    根据条件查询 所有书籍
    List<Fiction>  getListByCondition(@Param("sqltype") String sqltype,@Param("sqlstate") String sqlstate,
                                      @Param("sqlnum") String sqlnum,@Param("sqltime") String sqltime);
   Fiction getFictionById(@Param("id") int id);

    List<Fiction> getFictionByIds(List<Integer> ids);
//    删除书
    void   deleteByIdFiction(@Param("id") int id);
//    删除章节
    void   deleteByIdChapter(@Param("fiction_id")int id);
//   方法描述：根据ids集合进行删除
    int deletefictionByIds(List<Integer> ids);

//    模糊查询  通过名字
    List<Fiction>  queryFictionLike(@Param("fictionName")String fictionName);
//添加访问量
    void updateView(@Param("fiction")Fiction fiction);

//    查询点击量前3的书籍
    List<Fiction>  queryViews();

//    根据id修改fiction_name,author,state,type,brief
    void updateFiciton(Fiction fiction);

    void updateFictionBynewChapter(Fiction fiction);
}
