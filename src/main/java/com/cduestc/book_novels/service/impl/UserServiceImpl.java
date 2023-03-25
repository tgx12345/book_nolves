package com.cduestc.book_novels.service.impl;

import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.User;
import com.cduestc.book_novels.mapper.ShelfMapper;
import com.cduestc.book_novels.mapper.UserMapper;
import com.cduestc.book_novels.service.IUserService;
import com.cduestc.book_novels.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserServiceImpl  implements IUserService {

    @Resource
    UserMapper userMapper;
    @Resource
    ShelfMapper shelfMapper;

    @Override
    public User veriUser(String username, String password) {
        User user = userMapper.selectOne(username, password);

        return  user;
    }

    @Override
    public boolean selectByName(String username) {
        User user = userMapper.selectByName(username);
        if (null==user){
            return false;
        }else {
            return true;
        }
    }



    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Responseresult registerUser(String username, String password,String shelf_id,String username1) {
        int i = userMapper.insertOne(username, password,shelf_id,username1);
        if (i==0){
            return new Responseresult(-1,"注册失败");
        }else {
            return new Responseresult(200,"注册成功");
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void update(String password, int b,int userid) {
//        b为0则重置111111  b为1 则获取password
        if(b==0){
            User user = new User();
            user.setId(userid);
            user.setPassword(MD5Utils.MD5Upper("111111"));
            userMapper.updateUser(user);
        }
        else if(b==1) {
            User user = new User();
            user.setId(userid);
            user.setPassword(MD5Utils.MD5Upper(password));
            userMapper.updateUser(user);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> user = userMapper.getAllUser();
        return user;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteUser(int userid) {
        userMapper.deleteUser(userid);
        shelfMapper.deleteShelfByuserId(userid);
    }
}
