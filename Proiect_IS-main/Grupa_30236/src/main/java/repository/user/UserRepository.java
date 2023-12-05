package repository.user;

import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean save(User user);
    User findById(Long id);
    boolean updateEmployee(Long id, String username, String password);
    void removeAll();
    boolean removeById(Long id);
    boolean updateUserRole(Long id, Long role);
    boolean existsByUsername(String username);
}
