package eshop.controller;

import eshop.model.Cart;
import eshop.model.Good;
import eshop.service.CartService;
import eshop.service.GoodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class GoodController {
    private static final Logger LOGGER = LogManager.getLogger(GoodController.class.getName());

    private final GoodService goodService;
    private final CartService cartService;

    public GoodController(GoodService goodService, CartService cartService) {
        this.goodService = goodService;
        this.cartService = cartService;
    }

    @GetMapping("/goods")
    public String getAllGoods(Model model, HttpSession session, Authentication authentication) {
        List<Good> goods = goodService.getAll();
        String login = authentication.getName();

        String options = goodService.getStringOfOptionsForDroppingMenuFromGoodList(goods);
        String chosenGoods = (String) session.getAttribute("chosenGoods");
        String choice = goodService.getChoice(chosenGoods);

        model.addAttribute("login", login);
        model.addAttribute("options", options);
        model.addAttribute("choice", choice);

        return "goods";
    }

    @PostMapping("/createCart")
    public String createCart(HttpServletRequest request, HttpSession session,
                             Authentication authentication) {

        Cart cart = cartService.getCart(session);

        String command = request.getParameter("submit");

        return clickingActions(command, request, session, authentication, cart);
    }

    private String clickingActions(String command, HttpServletRequest request, HttpSession session,
                                   Authentication authentication, Cart cart) {

        session.setAttribute("cart", cart);
        BigDecimal totalPrice = cartService.getTotalPrice(cart);
        session.setAttribute("totalPrice", totalPrice);

        String option = goodService.getStringOfNameAndPriceFromOptionMenu(request.getParameter("goodName"));

        switch (command) {
            case "Add Goods":
                cartService.addGoodToCart(option, authentication.getName(), cart);

                getChosenGoods(session, cart);

                return "redirect:/goods";
            case "Remove Goods":
                cartService.deleteGoodFromCart(option, cart);

                getChosenGoods(session, cart);

                return "redirect:/goods";
            case "Submit":
                String order = cartService.printOrder(cart);

                session.setAttribute("order", order);

                return "redirect:/order";
        }

        cartService.updateData(session, cart);

        return "redirect:/login";
    }

    private void getChosenGoods(HttpSession session, Cart cart) {
        String chosenGoods = cartService.printChosenGoods(cart);

        LOGGER.info("Chosen goods: {}", chosenGoods);

        session.setAttribute("chosenGoods", chosenGoods);
    }
}
