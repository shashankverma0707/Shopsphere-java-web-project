package com.ecommerce.servlet;

import com.ecommerce.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @SuppressWarnings("unchecked") private Map<Integer,CartItem> cart(HttpSession session) {
        Map<Integer,CartItem> cart=(Map<Integer,CartItem>)session.getAttribute("cart");
        if(cart==null){cart=new LinkedHashMap<>();session.setAttribute("cart",cart);} return cart;
    }
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.setAttribute("cart",cart(req.getSession()));
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req,resp);
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        Map<Integer,CartItem> cart=cart(req.getSession());
        try {
            int id=Integer.parseInt(req.getParameter("id")); String action=req.getParameter("action");
            if("remove".equals(action)) cart.remove(id);
            else if("update".equals(action)) {
                int quantity=Integer.parseInt(req.getParameter("quantity")); CartItem item=cart.get(id);
                if(item==null) throw new IllegalArgumentException("Cart item not found.");
                if(quantity<1||quantity>item.getProduct().getStock()) throw new IllegalArgumentException("Quantity must be within available stock.");
                item.setQuantity(quantity);
            }
        } catch(Exception e) { req.getSession().setAttribute("cartError",e.getMessage()==null?"Unable to update cart.":e.getMessage()); }
        resp.sendRedirect(req.getContextPath()+"/cart");
    }
}
