package eshop.service;

import eshop.model.Cart;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

public interface CartService {

    Cart getCart(HttpSession session);

    void addGoodToCart(String option, String login, Cart cart);

    void deleteGoodFromCart(String option, Cart cart);

    void updateData(HttpSession session, Cart cart);

    String printChosenGoods(Cart cart);

    String printOrder(Cart cart);

    BigDecimal getTotalPrice(Cart cart);
}
