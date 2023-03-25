package com.cduestc.book_novels.service;


import com.cduestc.book_novels.bean.Admin;
import com.cduestc.book_novels.bean.Fiction;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public interface IAdminService {
    boolean loginCheck(Admin admin);
    void insertOneFiction(File txtfile,
                          File imgfile,
                          String author,
                          String fictionName,
                          String brief,
                          String state,
                          String type) throws IOException;
//    存放


}
