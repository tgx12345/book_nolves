package com.cduestc.book_novels.controller;

import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.bean.Fiction;
import com.cduestc.book_novels.bean.Shelf;
import com.cduestc.book_novels.service.IChapterService;
import com.cduestc.book_novels.service.IFictionService;
import com.cduestc.book_novels.service.IShelfService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/chapter")
public class ChapterController {

    IFictionService fictionService;
    IShelfService shelfService;
    IChapterService chapterService;


    @Autowired
    public ChapterController(IFictionService fictionService, IShelfService shelfService,
                             IChapterService chapterService) {
        this.fictionService = fictionService;
        this.shelfService = shelfService;
        this.chapterService = chapterService;
    }

    @RequestMapping(value = "/info/{fiction_id}")
    public String chapterInfo(@PathVariable("fiction_id") int fiction_id, Model model, HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();

// 添加访问量  书籍的信息  是否纯在书架  ,sort 排序

        Fiction fiction = fictionService.queryFictionById(fiction_id);
//   写入redis
          fictionService.queryChaptersById(fiction_id);

        fictionService.addView(fiction);
        model.addAttribute("fiction", fiction);
        model.addAttribute("title", fiction.getFictionName());
        int userid = (int) session.getAttribute("id");
        Shelf shelf = shelfService.queryShelf(userid, fiction_id);
        if (null != shelf) {
            model.addAttribute("presence", 1);
            model.addAttribute("sort", shelf.getLastReadSort());
        } else {
            model.addAttribute("presence", 0);
            model.addAttribute("sort", 1);
        }
//        presence 前段样式需要参数   sort请求章节参数

        return "chapter/info";

    }


    /**
     * 返回一本小说所有章节
     *
     * @param fiction_id
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/{fiction_id}", method = RequestMethod.GET)
    public String chapterList(@PathVariable("fiction_id") int fiction_id, Model model) throws JsonProcessingException {
// 查询相关书籍信息  查询对应章节列表
        Fiction fiction = fictionService.queryFictionById(fiction_id);
        Map<Integer,Chapter> chaptersmap = chapterService.queryChaptersByfiction_id(fiction_id);
        model.addAttribute("chapters", chaptersmap);
        model.addAttribute("fiction", fiction);
        model.addAttribute("size", chaptersmap.size());
        return "chapter/list";
    }

    @RequestMapping(value = "/read/{fiction_id}/{sort}")
    public String readChapter(HttpServletRequest request, Model model, @PathVariable("fiction_id") int fiction_id, @PathVariable("sort") int sort) throws JsonProcessingException {
//        根据所选章节信息 查询相关的章节 fiction_id sort
//        先修改书架信息
        HttpSession session = request.getSession();
        if (null != session.getAttribute("id")) {
            boolean b = shelfService.updateShelf(fiction_id, (Integer) session.getAttribute("id"), sort);
            if (b != true) {
                log.error("更新用户章节失败");
            }
        }

        Chapter chapter = chapterService.queryChapter(fiction_id, sort);
        if (null != chapter) {
            model.addAttribute("chapter", chapter);
            model.addAttribute("title", chapter.getChapterName());
            model.addAttribute("content", chapter.getChapterContent());
        }

        return "chapter/read";
    }


    @RequestMapping(value = "netRead/{fiction_id}/{sort}/{status}", method = RequestMethod.GET)
    public String netChapter(HttpServletRequest request,@PathVariable("fiction_id") int fiction_id,
                             @PathVariable("sort") int sort, @PathVariable("status") int status, Model model) throws JsonProcessingException {
//       如果上传的status 为1则为上一章
        if (status == 1) {
            if (sort != 1) {
                sort = sort - 1;
            }
        } else {
            sort = sort + 1;
        }

        HttpSession session = request.getSession();
        if (null != session.getAttribute("id")) {
            boolean b = shelfService.updateShelf(fiction_id, (Integer) session.getAttribute("id"), sort);
            if (b != true) {
                log.error("更新用户章节失败");
            }
        }


        Chapter chapter = chapterService.queryChapter(fiction_id, sort);
        if (chapter == null) {
            sort = sort - 1;
            chapter = chapterService.queryChapter(fiction_id, sort);
        }

        if (null != chapter) {
            model.addAttribute("chapter", chapter);
            model.addAttribute("title", chapter.getChapterName());
            model.addAttribute("content", chapter.getChapterContent());
        }
        return "chapter/read";


    }

    @RequestMapping(value = "/update_chapterH5/{name}/{id}")
    public String updateChapterH5(@PathVariable("name")String name,@PathVariable("id")int id,Model model){
            model.addAttribute("fictionName",name);
            model.addAttribute("fictionId",id);
        return "admin/update_chapter";
    }
    @RequestMapping(value = "/update_chapter/{id}")
    public String updateChapter(@PathVariable("id")int id,
                                @RequestParam("chapterName")String chapterNameold,
                               @RequestParam("chapterContent")String chapterContent ) throws IOException {
        String chapterName = chapterNameold.trim();
        //        查询最新章节sort  更新章节内容   - 改变书籍的时间   字数  最新章节
        chapterService.updateChapter(id,chapterName,chapterContent);
        return "redirect:/admin/mainPage";
    }
}