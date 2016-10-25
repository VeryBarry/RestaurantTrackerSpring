package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zach on 6/21/16.
 */
@Controller
public class RestaurantTrackerController {
    @Autowired
    UserRepository users;
    @Autowired
    RestaurantRepository restaurants;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model, String search) {
        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);
        List<Restaurant> rests;
        if (search != null) {
            rests = restaurants.searchLocation(search);
        }
        else {
            rests = restaurants.findByUser(user);
        }
        model.addAttribute("restaurants", rests);
        model.addAttribute("user", user);
        return "home";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(password, user.password)) {
            throw new Exception("Wrong password!");
        }
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/create-restaurant", method = RequestMethod.POST)
    public String create(String name, String location, int rating, String comment, HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);
        if (user == null) {
            throw new Exception("Not logged in.");
        }
        Restaurant r = new Restaurant(name, location, rating, comment, user);
        restaurants.save(r);
        return "redirect:/";
    }

    @RequestMapping(path = "/delete-restaurant", method = RequestMethod.POST)
    public String delete(int id) {
        restaurants.delete(id);
        return "redirect:/";
    }
}
