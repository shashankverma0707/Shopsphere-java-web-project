package com.ecommerce.servlet;

import com.ecommerce.dao.DbConnection;
import com.ecommerce.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }
        List<Map<String,Object>> orders = new ArrayList<>();
        String sql = "SELECT id,total_amount,status,created_at FROM orders WHERE user_id=? ORDER BY created_at DESC";
        try (Connection c=DbConnection.get(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setInt(1,user.getId()); try(ResultSet r=p.executeQuery()) { while(r.next()) {
                Map<String,Object> row=new HashMap<>(); row.put("id",r.getInt("id")); row.put("total",r.getBigDecimal("total_amount"));
                row.put("status",r.getString("status")); row.put("createdAt",r.getTimestamp("created_at")); orders.add(row);
            }}
        } catch(SQLException e) { throw new ServletException("Unable to load order history.",e); }
        req.setAttribute("orders",orders); req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req,resp);
    }
}
