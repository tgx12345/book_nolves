package com.cduestc.book_novels.bean;


import lombok.Data;


import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 追风
 * @since 2020-01-05
 */

@Data
public class Shelf implements Serializable {

    private static final long serialVersionUID=1L;


    private Integer id;

    private Integer fictionId;

    private Integer sort;

    private Integer userId;
    private Fiction fiction;
    private  Integer lastReadSort; //上传阅读

    public Shelf() {
    }

    public Shelf(Integer id, Integer fictionId, Integer sort, Integer userId, Integer lastReadSort) {
        this.id = id;
        this.fictionId = fictionId;
        this.sort = sort;
        this.userId = userId;
        this.lastReadSort = lastReadSort;
    }
}
