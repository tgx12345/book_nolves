package com.cduestc.book_novels.bean;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
public class Fiction implements Serializable {
    private static final long serialVersionUID=1L;

    private Map<Integer,Chapter>  chapters;
    private Integer id;
    private String fictionName;
    private String author;
    private String type;

    private String state;

    private String brief;

    private String imgUrl;

    private String fictionUrl;
    private String createDate;


    private String number;
    private Integer views; //访问量
    private String newest; //最新章节名称  且数据库没有此列

    public Fiction( String fictionName,
                   String author,String createDate,String type,String state,String number,String brief,
                   String imgUrl,String fictionUrl,Integer views) {

        this.id = id;
        this.fictionName = fictionName;
        this.author = author;
        this.type = type;
        this.state = state;
        this.brief = brief;
        this.imgUrl = imgUrl;
        this.fictionUrl = fictionUrl;
        this.createDate = createDate;
        this.number = number;
        this.views = views;
    }



    public Fiction() {
    }
}
