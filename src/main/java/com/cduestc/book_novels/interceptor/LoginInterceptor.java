package com.cduestc.book_novels.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /*
        1.实现接口HandlerInterceptor 创建pre
        2.配置中 去注册拦截器  添加拦截规则
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Object admin = session.getAttribute("admin");
        if(user !=null){
            return true;
        }
        else  if(admin !=null){
            return true;
        }
//        session.setAttribute("messge","请先登录");
        request.setAttribute("msg","请先登录");

        request.getRequestDispatcher("/").forward(request,response);


        return false;
    }
}
