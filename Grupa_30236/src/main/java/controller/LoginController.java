package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.User;
import model.validator.UserValidator;
import service.book.BookService;
import service.user.AuthenticationService;
import view.CustomerView;
import view.LoginView;

import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;
    private final BookService bookService;
    public LoginController(LoginView loginView, AuthenticationService authenticationService, UserValidator userValidator, BookService bookService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;
        this.bookService = bookService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }
    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            User user = authenticationService.login(username, password);

            if (user == null){
                loginView.setActionTargetText("Invalid Username or password!");
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                if (user.getRoles().get(0).getRole().equals("customer")) {
                    loginView.closeLoginView();
                    CustomerView customerView = new CustomerView(user);
                    CustomerController customerController = new CustomerController(customerView, bookService);
                }
            }

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            userValidator.validate(username, password);
            final List<String> errors = userValidator.getErrors();
            if (errors.isEmpty()) {
                if (authenticationService.register(username, password)){
                    loginView.setActionTargetText("Register successfull!");
                }else{
                    loginView.setActionTargetText("Register NOT successfull!");
                }
            } else {
                loginView.setActionTargetText(userValidator.getFormattedErrors());
            }
        }
    }
}
