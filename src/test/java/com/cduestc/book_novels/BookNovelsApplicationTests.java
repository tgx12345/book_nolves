package com.cduestc.book_novels;

import com.cduestc.book_novels.bean.Chapter;
import com.cduestc.book_novels.mapper.ChapterMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class BookNovelsApplicationTests {

    @Resource
    ChapterMapper chapterMapper;
    @Test
    void contextLoads() {
        Map<Integer, Chapter> map = chapterMapper.queryMapChapterByficiton_id(84);
        System.out.println("------");
        System.out.println(map.keySet());
    }

}
