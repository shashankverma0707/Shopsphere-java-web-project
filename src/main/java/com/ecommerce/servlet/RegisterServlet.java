package com.ecommerce.servlet;

import com.ecommerce.dao.DbConnection;
import com.ecommerce.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = value(req.getParameter("name"));
        String email = value(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password");
        if (name.length() < 2 || name.length() > 100 || !EMAIL.matcher(email).matches() || password == null || password.length() < 8) {
            req.setAttribute("error", "Enter a valid name/email and a password of at least 8 characters."); doGet(req, resp); return;
        }
        String sql = "INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,'CUSTOMER')";
        try (Connection c = DbConnection.get(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, name); p.setString(2, email); p.setString(3, PasswordUtil.hash(password)); p.executeUpdate();
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                req.setAttribute("error", "That email is already registered."); doGet(req, resp);
            } else throw new ServletException("Could not register account.", e);
        }
    }
    private String value(String s) { return s == null ? "" : s.trim(); }
}
