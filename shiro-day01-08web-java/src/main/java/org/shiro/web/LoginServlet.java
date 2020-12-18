package org.shiro.web;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.shiro.service.LoginService;
import org.shiro.service.impl.LoginServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    LoginService loginService = new LoginServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //  获得用户名密码
        String loginName = req.getParameter("loginName");
        String password = req.getParameter("password");

        //  构建登录使用的Token
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);

        //  登录操作
        boolean login = loginService.login(token);

        //  成功跳转首页，失败跳转登录页
        if (login) {
            req.getRequestDispatcher("/home").forward(req, resp);
        } else {
            resp.sendRedirect("login.jsp");
        }

    }
}
