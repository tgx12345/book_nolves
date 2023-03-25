package com.cduestc.book_novels.config;

import com.cduestc.book_novels.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件大小
        factory.setMaxFileSize(DataSize.parse("1024MB"));
        // 上传的总文件大小
        factory.setMaxRequestSize(DataSize.parse("2048MB"));
        return factory.createMultipartConfig();
    }
       @Override
       public void addResourceHandlers(ResourceHandlerRegistry registry) {
                   /**
                    * * 资源映射路径
                    * addResourceHandler：访问映射路径
                    * * addResourceLocations：资源绝对路径
                    */
                   registry.addResourceHandler("/imgurl/**").addResourceLocations("file:D:/study/JavaIDEA/book_novels_resource/img/");
       }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new LoginInterceptor())
               .addPathPatterns("/**")  //所有请求都被拦截 包括静态资源
               .excludePathPatterns("/","/signin","/login","/admin/loginCheck", "/signinV","/error",
                       "/assets/**","/css/**","/images/**","/js/**","/layui/**","/picture/**");
       //不拦截的路径


    }
}

