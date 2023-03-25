package com.cduestc.book_novels.test;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test1 {
    public static void main(String[] args) {

    }
    @Test
    public void t1(){
        DateTime today = new DateTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月和小时的格式为两个大写字母
        Date date1 = today.minusDays(30).toDate();
        String nowdate = df.format(date1);//将当前时间转换成特定格式的时间字符串，这样便可以插入到数据库中

    }
}
