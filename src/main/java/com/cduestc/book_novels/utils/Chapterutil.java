package com.cduestc.book_novels.utils;

import com.cduestc.book_novels.bean.Chapter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class Chapterutil {

    public static ArrayList getChapters(File file,Integer fiction_id,String date) throws IOException {
        // 初始化 rowsList

        ArrayList<Chapter> chapters = new ArrayList<>();

        /**
         * 思路：使用 BufferedReader 的 readLine() 方法读取，匹配章节正则。
         */
        // 创建计数器：记录行数
        long row = 0L;
        // 创建BufferedReader 流
        BufferedReader br=null;
        int  o=1;
        try {
            // 创建正则表达式
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            Pattern p = Pattern.compile("(([正序])([章卷文]))?(\\s*第)(.{1,9})[章节卷集部篇回](\\s{1})(.*)($\\s*)");

//            Pattern p = Pattern.compile("(^[\\s　]*|[正序][章卷文]\\s*)第?\\s*\\w+\\s*[章节卷集部篇回]\\s*(.*)");
            // 匹配对象
            Matcher m = null;
            // 读取数据
            String line = null;
             //获得章节内容
            StringBuffer chapterContent = new StringBuffer();

            // 读取并匹配
            while((line=br.readLine())!=null) {
                // 行数 + 1
                row += 1;
                // 如能匹配上，则为章节行
                m = p.matcher(line);
                if (m.find()) {
                    // 如果上一章节内容不为空，则将其设置给上一章节   并且第一章之前的内容舍弃
                    if (chapterContent.length() > 0) {
                        if(chapters.size()!=0) {
                            chapters.get(chapters.size() - 1).setChapterContent(chapterContent.toString());
                            chapterContent.setLength(0);
                        }else {
                            chapterContent.setLength(0);
                        }
                    }
                    // 添加chapter对象
                    String s = line.trim();
                    chapters.add(new Chapter(null,fiction_id,s,date,o, (int) row));
                    o++;
                } else {
                    // 非章节行，将该行内容添加到上一个章节的内容中
                    chapterContent.append(line).append(System.lineSeparator());
//                    插入数据库无格式
                }
            }
            // 处理最后一章节的内容
            if (chapterContent.length() > 0) {
                chapters.get(chapters.size() - 1).setChapterContent(chapterContent.toString());
            }

        } catch (IOException e) {
           log.error(e.toString());
        }
        finally {
            br.close();
        }
        return  chapters;
    }



}
