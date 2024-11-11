package nuc.edu.cn.testspringmvc.controller;

import nuc.edu.cn.testspringmvc.entity.User;
import nuc.edu.cn.testspringmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model, @ModelAttribute("message") String message) {
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User addedUser = userService.addUser(user);
        if (addedUser != null) {
            redirectAttributes.addFlashAttribute("message", "用户添加成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "用户添加失败");
        }
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "editUser";
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            redirectAttributes.addFlashAttribute("message", "用户信息更新成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "用户信息更新失败");
        }
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "用户删除成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "用户删除失败：用户不存在");
        }
        return "redirect:/users";
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable String id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "viewUser";
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/updateRemark")
    public String updateRemark(@RequestParam String id, @RequestParam String remark, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.updateUserRemark(id, remark);
            redirectAttributes.addFlashAttribute("message", "用户备注更新成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "用户备注更新失败：用户不存在");
        }
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