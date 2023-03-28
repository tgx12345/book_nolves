package com.cduestc.book_novels.bean;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;


@Data
public class Chapter implements Serializable {
    private static final long serialVersionUID=1L;

    private Integer id;

    private Integer fictionId;
    private String chapterName;



    private String createDate;
    private Integer sort;
    private  Integer row;

    private String chapterContent;  //用于存放内容传输
    public Chapter(Integer id, Integer fictionId, String chapterName,
                   String createDate, Integer sort, Integer row) {
        this.id = id;
        this.fictionId = fictionId;
        this.chapterName = chapterName;
        this.createDate = createDate;
        this.sort = sort;
        this.row = row;
    }

    public Chapter() {
    }



}
