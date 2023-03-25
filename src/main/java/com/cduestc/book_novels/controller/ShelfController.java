package com.cduestc.book_novels.controller;

import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.bean.Shelf;
import com.cduestc.book_novels.service.IShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/shelf")
public class ShelfController {

    IShelfService shelfService;
    @Autowired
    public ShelfController(IShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @RequestMapping(value = "/shelf",method = RequestMethod.GET)
    public String  queryShelflist(HttpServletRequest request,Model model){
//
        HttpSession session = request.getSession();
        int userid = (int) session.getAttribute("id");
        List<Shelf> list = shelfService.queryShelfs(userid);

        model.addAttribute("list",list);

        return "shelf/shelf";
    }

    @RequestMapping(value = "/addShelf")
    @ResponseBody
    public Responseresult addShelf(@RequestParam("fiction_id") int fiction_id, HttpServletRequest request){
        return shelfService.addShelf(request, fiction_id);
    }


    /**
     *  从书架删除小说
     *
     */
    @ResponseBody
    @RequestMapping(value = "/deleteShelf")
    public Responseresult deleteShelf(HttpServletRequest request, @RequestParam("id") int fiction_id){
        HttpSession session = request.getSession();
        int userId= (int) session.getAttribute("id");
            boolean b = shelfService.deleteShelf(fiction_id,userId);
        if (b==false){
            return new Responseresult(-1,"移除书架失败");
        }
        return new Responseresult(200,"移除书架成功");
    }
}