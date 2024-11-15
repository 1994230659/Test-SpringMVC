package nuc.edu.cn.testspringmvc.service;

import nuc.edu.cn.testspringmvc.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();
    private static int currentNumber = 1;


    private String generateId() {
        return String.format("ljy%03d", currentNumber++);
    }

    public UserService() {
        // 添加初始用户，使用新的ID生成方式
        addUser(new User(generateId(), "张三", "销售部经理"));
        addUser(new User(generateId(), "李四", "技术部工程师"));
        addUser(new User(generateId(), "王五", "人事部主管"));
    }

    @GetMapping("/search")
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllUsers();
        }
        return users.stream()
                .filter(user -> user.getName().toLowerCase().contains(keyword.toLowerCase())
                        || user.getRemark().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public User addUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(generateId());
        }
        users.add(user);
        return user;
    }

    public User updateUser(User user) {
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        return user;
    }

    public void deleteUser(String id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    public void updateUserRemark(String id, String remark) {
        User user = getUserById(id);
        if (user != null) {
            user.setRemark(remark);
        }
    }
}