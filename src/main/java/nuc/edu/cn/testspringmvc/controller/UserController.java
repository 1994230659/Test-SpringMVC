package nuc.edu.cn.testspringmvc.controller;

import nuc.edu.cn.testspringmvc.entity.User;
import nuc.edu.cn.testspringmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable String id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "viewUser";
    }

    @PostMapping("/updateRemark")
    public String updateRemark(@RequestParam String id, @RequestParam String remark) {
        userService.updateUserRemark(id, remark);
        return "redirect:/users/view/" + id;
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(required = false) String keyword, Model model) {
        List<User> searchResults;
        if (keyword != null && !keyword.isEmpty()) {
            searchResults = userService.searchUsers(keyword);
        } else {
            searchResults = userService.getAllUsers();
            keyword = ""; // 确保 keyword 不为 null
        }
        model.addAttribute("users", searchResults);
        model.addAttribute("keyword", keyword);
        return "userList";
    }
}