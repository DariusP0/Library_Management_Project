package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.book.BookRepositoryMySQL;
import service.book.BookService;
import service.book.BookService;
import service.user.AuthenticationService;
import view.AdminView;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final BookService bookService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, BookService bookService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.bookService = bookService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);
            User user = loginNotification.getResult();
            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("LogIn Successfull!");
                System.out.println(loginNotification.getResult().getRoles().get(0).getRole());
                if (loginNotification.getResult().getRoles().get(0).getRole().equals("customer")) {
                    loginView.closeLoginView();
                    CustomerView customerView = new CustomerView(user);
                    CustomerController customerController = new CustomerController(customerView, bookService);
                }
                if(loginNotification.getResult().getRoles().get(0).getRole().equals("administrator")) {
                    loginView.closeLoginView();
                    List<User> selected = new ArrayList<>();
                    AdminView adminView = new AdminView(user);
                    AdminController adminController = new AdminController(adminView, authenticationService,selected);
                }
                if(loginNotification.getResult().getRoles().get(0).getRole().equals("employee")){
                    loginView.closeLoginView();
                    EmployeeView employeeView = new EmployeeView(user);
                    EmployeeController employeeController = new EmployeeController(employeeView,bookService);
                }


            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
