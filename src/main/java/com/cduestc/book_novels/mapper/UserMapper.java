package com.cduestc.book_novels.mapper;

import com.cduestc.book_novels.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectOne(@Param(value = "username") String username,@Param(value = "password") String password);
    int insertOne(@Param(value = "username") String username,@Param(value = "password") String password,
                  @Param("shelf_id")String shelf_id,@Param("username1")String username1);
    User selectByName(@Param(value = "username") String username );

    void  updateUser(User user);
    List<User> getAllUser();

    void deleteUser(@Param("userid") int userid);
}
