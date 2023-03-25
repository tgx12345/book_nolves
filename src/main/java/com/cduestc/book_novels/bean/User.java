package com.cduestc.book_novels.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class User implements Serializable {
    private static final long serialVersionUID=1L;
    private Integer id;

    private String username;

    private String password;

    private String shelfIds; //书架
    private String userAlias;

}
//如果需要加入有参构造  需要将无参构造显式声明