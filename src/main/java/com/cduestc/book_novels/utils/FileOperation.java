package com.cduestc.book_novels.utils;

import com.cduestc.book_novels.bean.Fiction;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperation {
//    新文件名+路径
    public static String insertDocument(String filename,String author, String path1) {
        String suffixname = filename.substring(filename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + author + suffixname;
        //File.separator路径的斜线
        String path2 = path1;
        String finalpath = path2 + File.separator + newFilename;
        return finalpath;
    }

    public static  String seletNowDate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月和小时的格式为两个大写字母
        java.util.Date date = new Date();//获得当前时间
        String nowdate = df.format(date);//将当前时间转换成特定格式的时间字符串，这样便可以插入到数据库中

        return nowdate;
    }
    //转换BufferedReader   使用工具类 不能关闭流  不能使用  其次使用流只能一次一次使用
    public static BufferedReader InputStreamToBufferedReader(InputStream inputStream) {

        // 判断是否为空
//        if (picFile.isEmpty()) {
//            return null;
//        }
        try {
//            InputStream inputStream = picFile.getInputStream();
            InputStreamReader is = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(is);

//            String s = "";
//

            return br;

        } catch (Exception e) {
            System.out.println((e.getMessage()));
        }
        return null;
    }
    public static int wordsCount(BufferedReader br) throws FileNotFoundException {

        int num = 0;      //字数

        try{

//            File file2=file;
//            BufferedReader br= new BufferedReader(new FileReader(file));
            String str = null;

            while((str=br.readLine())!=null){
                num += countNumber(str);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("字数："+num);
        return num;

    }

    /**
     * 统计数字数
     * @param str
     * @return
     */
// 被上面调用
    private static int countNumber(String str) {
        int count = 0;
//        Pattern p = Pattern.compile("\\d");
//     字母   [a-zA-Z]  汉字 [\u4e00-\u9fa5]  空格 \\s
        Pattern p = Pattern.compile("\\d|[a-zA-Z]|[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        while(m.find()){
            count++;
        }
        return count;
    }
//    删除路径下的某一文件
    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            java.io.File myDelFile = new java.io.File(filePath);
            Files.delete(myDelFile.toPath());
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

//   浏览器不支持本地图片访问  需要使用虚拟路径   以下仅仅为
    public static List<Fiction>  updateImgurl(List<Fiction> fictions){
        for(Fiction fiction:fictions){
            String imgUrl = fiction.getImgUrl();
            String newImgUrl = imgUrl.substring(imgUrl.lastIndexOf("\\"));
            fiction.setImgUrl("/imgurl"+newImgUrl);
        }
        return fictions;
    }

//    解决上传springboot以MultipartFile格式上传文件报错临时文件不存在的问题
    public  static File  toFile(MultipartFile  testfile,File path) throws IOException {
        String code=(int)((Math.random())*100)+"";
        String fileName = testfile.getOriginalFilename().substring(0,testfile.getOriginalFilename().lastIndexOf("."));
        String fileSuffix = testfile.getOriginalFilename().substring(testfile.getOriginalFilename().lastIndexOf("."));
        File  file=File.createTempFile(fileName+code, fileSuffix,path);
        testfile.transferTo(file);
//        //立即删除文件
//        file.delete();
//        //在JVM退出时删除文件
//        file.deleteOnExit();
        return file;
    }


    public static void copyFile(File sourceFile, String destDirectoryPath) throws IOException {

        Path sourceFilePath = sourceFile.toPath();
        Path destFilePath = new File(destDirectoryPath).toPath();
        Files.copy(sourceFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING); // 将文件复制到指定目录中并以新文件名保存
    }
}
