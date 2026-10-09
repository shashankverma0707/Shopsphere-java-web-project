package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            User user = email == null ? null : dao.findByEmail(email.trim().toLowerCase());
            if (user != null && password != null && PasswordUtil.matches(password, user.getPasswordHash())) {
                req.changeSessionId();
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + ("ADMIN".equals(user.getRole()) ? "/admin/products" : "/products"));
            } else { req.setAttribute("error", "Email or password is incorrect."); doGet(req, resp); }
        } catch (Exception e) { throw new ServletException("Login failed.", e); }
    }
}
