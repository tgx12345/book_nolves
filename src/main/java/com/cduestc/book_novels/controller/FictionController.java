package com.cduestc.book_novels.controller;

import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Responseresult;
import com.cduestc.book_novels.commom.DataGridView;
import com.cduestc.book_novels.service.IFictionService;
import com.cduestc.book_novels.vo.FictionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@Slf4j
@RequestMapping(value = "/fiction")
public class FictionController {


    IFictionService fictionService;
    @Autowired
    public FictionController(IFictionService fictionService) {
        this.fictionService = fictionService;
    }


    @RequestMapping(value = "/deleteFile")
    public  String  deleteFile(@RequestParam("id")int id
                             ){

        fictionService.deleteFiction(id);

       return  "redirect:/admin/mainPage";
    }

    @RequestMapping(value = "/repeatedlyDelete",method = RequestMethod.POST)
    @ResponseBody
    public Responseresult repeatedlyDelete(String values){

        String[] strids=values.split(",");
        List<Integer> ids=new ArrayList<Integer>();
        for(int i=0;i<strids.length;i++){
            ids.add(Integer.parseInt(strids[i]));
        }
        System.out.println(ids);
        fictionService.deleteByIds(ids);
        return new Responseresult(200,"删除成功");
    }
    /**
     * 首页
     */
    @RequestMapping(value = "/index")
    public String index(FictionVo fictionVo, Model model) {
        DataGridView dataGridView = fictionService.queryAllFiction(fictionVo);
        model.addAttribute("data",dataGridView);
        return "index/index";
    }


    @RequestMapping(value = "/search")
    public String  search( Model model,String fictionName){
        model.addAttribute("fictionName",fictionName);
        List<Fiction> fictions = fictionService.queryLikeFiction(fictionName);
        model.addAttribute("list",fictions);  //将查询的fictions 放入list

        List<Fiction> ViewsList = fictionService.queryViewsFiction();
        model.addAttribute("ViewsList",ViewsList);
//        第二功能  大家都在搜  使用点击数查询前3个
        return "index/search";
    }
    @RequestMapping(value = "/update_info/{id}")
    public String update_info(@PathVariable("id") int id,Model model){
//                根据id查出书籍 回显
        Fiction fiction = fictionService.queryFictionById(id);
        model.addAttribute("fiction",fiction);
        return "admin/update";
    }

//    根据所传信息修改
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public String update(@PathVariable("id")int id, Fiction fiction){
        fiction.setId(id);
        fictionService.updateFiction(fiction);
        return "redirect:/admin/mainPage";
    }


}
