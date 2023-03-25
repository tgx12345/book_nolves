package com.cduestc.book_novels.controller;

import com.cduestc.book_novels.bean.Admin;
import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.User;
import com.cduestc.book_novels.service.IAdminService;
import com.cduestc.book_novels.service.IFictionService;
import com.cduestc.book_novels.service.IUserService;
import com.cduestc.book_novels.utils.FileOperation;
import com.cduestc.book_novels.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    IAdminService adminService;
    IFictionService fictionService;
    IUserService userService;

    @Autowired
    public AdminController(IAdminService adminService, IFictionService fictionService,IUserService userService) {
        this.adminService = adminService;
        this.fictionService = fictionService;
        this.userService= userService;
    }

    @RequestMapping(value = "/loginCheck")
    public String loginCheck(Admin admin,
                             @RequestHeader String Referer,
                             HttpSession session,
                             Model model){

        if(null!=admin.getUsername() && "".equals(admin.getUsername()) ||
                null !=admin.getPassword() && "".equals(admin.getPassword())){
            model.addAttribute("msg","账户密码不能为空");
            return "login";
        }
        Admin admin1=new Admin(admin.getUsername(),admin.getPassword());
        admin.setUsername(MD5Utils.MD5Upper(admin.getUsername()));
        admin.setPassword(MD5Utils.MD5Upper(admin.getPassword()));

        boolean b = adminService.loginCheck(admin);
        if(b){
            session.setAttribute("admin",admin1);
            return "redirect:/admin/mainPage";
        }
        model.addAttribute("msg","账户或者密码错误");
        return "login";
    }

    //管理员注销
    @RequestMapping(value = "/logout")
    public String Logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("admin");

        return "redirect:/";
    }





    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public Responseresult uploadfiction(@RequestParam(value = "file") MultipartFile[] files,
                                       @RequestParam(value = "author",required = true) String author,
                                        @RequestParam(value = "fictionName",required = true)  String fictionName,
                                        @RequestParam(value = "brief",required = true)String brief,
                                        @RequestParam(value = "state",required = true)  String state,
                                        @RequestParam(value = "type",required = true)   String type
                                       ) throws IOException {
        if(files[0].isEmpty()&&files[1].isEmpty()){
            return new Responseresult(-1,"上传失败");
        }

        File path = new File("D:\\study\\JavaIDEA\\book_novels_resource\\temp");
//        txt    path:D:\study\JavaIDEA\book_novels_resource\temp
        File txtfile = FileOperation.toFile(files[0], path);
        File imgfile = FileOperation.toFile(files[1], path);


        adminService.insertOneFiction(txtfile, imgfile, author, fictionName, brief, state, type);

        return new Responseresult(200,"上传成功");
    }
//      String newFilename= UUID.randomUUID().toString()+suffixname;新的文件名 需要数据库存入
//    没有进行上传文件的编码转换 ，只能对UTF-8进行操作

    @RequestMapping(value = "/mainPage")
    public String mainPage(Model model){
//获取小说列表 和 用户列表
        List<Fiction> fictions = fictionService.getALL();
        List<User> users = userService.getAll();
        model.addAttribute("fictions",fictions);
        model.addAttribute("users",users);
        return "admin/tables";
    }
    @RequestMapping(value = "/toupload")
    public String  toupload(){
        return "admin/upload";
    }




//    修改密码
    @RequestMapping(value = "/updateUser/{int}/{userid}")
    public String updateUser(@PathVariable("int")int isRe,
                             @PathVariable("userid")int userid){
                userService.update(null,0,userid);
        return "redirect:/admin/mainPage";
    }
    //      删除用户需要将其书架删除

    @RequestMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("userid")int userid){
        userService.deleteUser(userid);
        return "redirect:/admin/mainPage";
    }
}
