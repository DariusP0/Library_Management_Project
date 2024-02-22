package service.user;

import model.User;
import model.validator.Notification;

import java.util.List;

public interface AuthenticationService {
    Notification<Boolean> register(String username, String password);

    Notification<User> login(String username, String password);

    boolean logout(User user);
    List<User> findAll();
    User findById(Long id);
    boolean removeById(Long id);
    boolean updateEmployee(Long id, String username, String password);
    boolean updateUserRole(Long id, Long role);
}
