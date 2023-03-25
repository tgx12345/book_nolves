package com.cduestc.book_novels.service;


import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.User;

import java.util.List;

public interface IUserService {
//      登录检查
      User veriUser(String username, String password);
//      注册检查
      boolean selectByName(String username);
//      注册用户
      Responseresult registerUser(String username, String password,String shelf_id,String username1);

      void update(String password,int b,int user_id);
     List<User> getAll();

    void deleteUser(int userid);
}
