package com.cduestc.book_novels.controller;

import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.User;
import com.cduestc.book_novels.service.IUserService;
import com.cduestc.book_novels.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class UserController {

    IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @RequestMapping(value = "/")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/signin")
    public String signin(){
        return "signin";
    }

    @ResponseBody
    @RequestMapping(value = "/signinV",method = RequestMethod.POST)
    public Responseresult signinV(@RequestParam("username") String pusername, @RequestParam("password") String ppassword)
    {
        if(null!=pusername && "".equals(pusername) || null !=ppassword && "".equals(ppassword)){

            return  new Responseresult(-1,"账户不能为空") ;
        }
        String username=pusername.trim();
        String password=ppassword.trim();
        boolean b = iUserService.selectByName(MD5Utils.MD5Upper(username));
        if(!b){
            //产生唯一书架id
          UUID idOne = UUID.randomUUID();

            String username1 = MD5Utils.MD5Upper(username);
            String password1 = MD5Utils.MD5Upper(password);
            Responseresult responseresult = iUserService.registerUser(username1, password1, String.valueOf(idOne),username);
            return responseresult;
        }
        return new Responseresult(-2,"账户已存在");
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public  String loginCheck(User user, HttpSession session, Model model){
        String username = user.getUsername().trim();
        String password = user.getPassword().trim();
        if(null!=username && "".equals(username) || null !=password && "".equals(password)){
            model.addAttribute("msg","账户密码不能为空");
            return "login";
        }

        String username1 = MD5Utils.MD5Upper(username);
        String password1 = MD5Utils.MD5Upper(password);
        User result = iUserService.veriUser(username1, password1);
        if(null != result) {
            session.setAttribute("id",result.getId());
            session.setAttribute("user",user);
            return "redirect:/fiction/index";
            //             根据路由来重定向
        }
        else {
            model.addAttribute("msg","账户密码错误");
            return "login";
        }

    }




    @RequestMapping(value = "/logout")
    public String Logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");

        return "redirect:/";
    }
}
