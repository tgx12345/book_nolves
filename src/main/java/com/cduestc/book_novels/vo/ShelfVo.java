package com.cduestc.book_novels.vo;


import lombok.Data;

import java.sql.Timestamp;

/**
 * @author coffee
 */
@Data
public class ShelfVo {

    private Integer id;

    private Integer fictionId;

    private String fictionName;

    private Timestamp createDate;

    private String author;

    private String type;

    private String newest;

    private String state;

    private String imgUrl;

    private String brief;

    private  Integer lastReadId;   //继续上次阅读

    public ShelfVo(Integer id,Integer fictionId, String fictionName, Timestamp createDate, String author, String type, String newest, String state, String imgUrl, String brief,Integer lastReadId) {
        this.id = id;
        this.fictionId = fictionId;
        this.fictionName = fictionName;
        this.createDate = createDate;
        this.author = author;
        this.type = type;
        this.newest = newest;
        this.state = state;
        this.imgUrl = imgUrl;
        this.brief = brief;
        this.lastReadId = lastReadId;
    }

}

