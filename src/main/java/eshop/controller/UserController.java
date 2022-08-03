package eshop.controller;

import eshop.dto.UserDto;
import eshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String start() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "redirect:/goods";
    }

    @GetMapping("/loginError")
    public String loginError() {
        return "loginError";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(Model model, HttpServletRequest request, @ModelAttribute UserDto userDto) {
        if (userService.isInvalidUser(userDto)) {
            String errors = userService.invalidUser(userDto);

            model.addAttribute("validationErrors", errors);

            return "register";
        }

        return eventsWithCheckbox(request, userDto);
    }

    private String eventsWithCheckbox(HttpServletRequest request, @ModelAttribute UserDto userDto) {
        if (request.getParameter("isUserCheck") != null) {

            userService.save(userDto);

            return "redirect:/login";
        } else {
            return "checkboxError";
        }
    }
}
