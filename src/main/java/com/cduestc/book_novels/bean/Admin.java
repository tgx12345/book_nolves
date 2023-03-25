package com.cduestc.book_novels.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Admin  implements Serializable {
    private static final long serialVersionUID=1L;
    private Integer id;

    private String username;


    private String password;
    public Admin() {
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
