package org.shiro.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //  获得当前主体 Subject
        Subject subject = SecurityUtils.getSubject();

        //  当前主体是否登录
        boolean authenticated = subject.isAuthenticated();

        //  如果没有登录，则返回Login页面
        if(authenticated) {
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/login").forward(req, resp);
        }

    }
}
