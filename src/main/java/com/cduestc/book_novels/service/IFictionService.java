package com.cduestc.book_novels.service;

import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Shelf;
import com.cduestc.book_novels.commom.DataGridView;
import com.cduestc.book_novels.vo.FictionVo;

import java.util.List;

public interface IFictionService {
    List<Fiction> getALL();
    void deleteFiction(int id);

    void deleteByIds(List<Integer> ids);

    DataGridView queryAllFiction(FictionVo fictionVo);
    List<Fiction>  queryLikeFiction(String fictionName);
    Fiction  queryFictionById(int id);
    void addView(Fiction fiction);

    List<Fiction> queryViewsFiction();

    void  updateFiction(Fiction fiction);
//    fiction_name,author,state,type,brief
}
