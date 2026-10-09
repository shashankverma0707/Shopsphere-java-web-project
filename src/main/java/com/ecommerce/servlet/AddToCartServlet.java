package com.ecommerce.servlet;

import com.ecommerce.model.*;
import com.ecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/cart/add")
public class AddToCartServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            int qty = Integer.parseInt(req.getParameter("quantity"));
            if (qty < 1 || qty > 99) throw new IllegalArgumentException("Quantity must be between 1 and 99.");
            Product product = service.get(id);
            if (product == null) throw new IllegalArgumentException("Product not found.");
            Map<Integer,CartItem> cart = getCart(req);
            CartItem old = cart.get(id); int newQty = (old == null ? 0 : old.getQuantity()) + qty;
            if (newQty > product.getStock()) throw new IllegalArgumentException("Requested quantity exceeds available stock.");
            cart.put(id, new CartItem(product, newQty));
            resp.sendRedirect(req.getContextPath()+"/cart");
        } catch (IllegalArgumentException e) { req.getSession().setAttribute("cartError", e.getMessage()); resp.sendRedirect(req.getContextPath()+"/cart"); }
          catch (Exception e) { throw new ServletException("Could not add item to cart.",e); }
    }
    @SuppressWarnings("unchecked") private Map<Integer,CartItem> getCart(HttpServletRequest req) {
        HttpSession session=req.getSession(); Map<Integer,CartItem> cart=(Map<Integer,CartItem>)session.getAttribute("cart");
        if(cart==null){cart=new LinkedHashMap<>();session.setAttribute("cart",cart);} return cart;
    }
}
