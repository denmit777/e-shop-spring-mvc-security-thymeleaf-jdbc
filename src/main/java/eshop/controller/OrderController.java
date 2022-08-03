package eshop.controller;

import eshop.model.Cart;
import eshop.service.CartService;
import eshop.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/order")
    public String getOrder(Model model, HttpSession session, Authentication authentication, Cart cart) {
        String login = authentication.getName();

        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPrice");

        model.addAttribute("login", login);
        model.addAttribute("orderList", session.getAttribute("order"));
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderResult", orderService.orderResult(totalPrice));

        orderService.save(login, totalPrice);

        cartService.updateData(session, cart);

        return "order";
    }
}
