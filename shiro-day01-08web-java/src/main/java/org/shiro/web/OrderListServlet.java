package org.shiro.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/order-list")
public class OrderListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //  获取主体
        Subject subject = SecurityUtils.getSubject();

        //  判断是否拥有角色
        boolean hasRole = subject.hasRole("admin");

        //  没有则跳转到401页面
        if (hasRole) {
            req.getRequestDispatcher("order-list.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/login").forward(req, resp);
        }
    }
}
