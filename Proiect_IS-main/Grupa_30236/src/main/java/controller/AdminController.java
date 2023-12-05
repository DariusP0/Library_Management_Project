package controller;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import model.Role;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.AdminView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public class AdminController {

    private final AdminView adminView;
    private final AuthenticationService authenticationService;
    private final List<User> selectedUsers;
    private int finishCounter =0;
    List<Book> addedToCartBooks = new ArrayList<>();


    public AdminController(AdminView adminView, AuthenticationService authenticationService, List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
        this.adminView = adminView;
        this.authenticationService = authenticationService;
        refreshView();

        this.adminView.addCreateButtonListener(new AdminController.CreateButtonHandler());
        this.adminView.addUpdateButtonListener(new AdminController.UpdateButtonHandler());
        this.adminView.addDeleteButtonListener(new AdminController.DeleteButtonHandler());
        this.adminView.addRetrieveButtonListener(new AdminController.RetrieveHandler());
        this.adminView.addShowAllListener(new AdminController.ShowAllHandler());
        this.adminView.addRoleChangeListener(new AdminController.RoleChangeHandler());
    }
    public void refreshView(){
        List<User> users = authenticationService.findAll();
        adminView.setUsersData(users);
    }

    private class RetrieveHandler implements EventHandler<ActionEvent> {

        //Optional<User> optionalUser;
        User optionalUser;
        List<User> userData = new ArrayList<User>() ;
        @Override
        public void handle(ActionEvent event) {

            Long id = adminView.getId();
            optionalUser = authenticationService.findById(id);
            userData.clear();
            userData.add(optionalUser);
            String checkForNull = optionalUser.toString();
            if (checkForNull == null){
                adminView.setActionTargetText("Id not found!");
            }
            adminView.setUsersDataOptional(userData);

        }
    }

    private class ShowAllHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
           refreshView();
        }
    }
    private class RoleChangeHandler implements EventHandler<ActionEvent> {


        @Override
        public void handle(ActionEvent event) {
            Long selectedUserId = adminView.getSelectedUser().getId();

            String role = adminView.getRoleChoosen();
            if(role.equals("Admin")){
            authenticationService.updateUserRole(selectedUserId,(long)1);
            }else if(role.equals("Employee")){
                authenticationService.updateUserRole(selectedUserId,(long)2);
            }
            else{
                authenticationService.updateUserRole(selectedUserId,(long)3);
            }
            refreshView();
        }

    }

    public class DeleteButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            User selectedUser = adminView.getSelectedUser();

            System.out.println(selectedUser.getId());
            authenticationService.removeById(selectedUser.getId());

            refreshView();

        }
    }

    public class CreateButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            List<User> users;
            users = authenticationService.findAll();

            String username = adminView.getUsername();
            String password = adminView.getPassword();
            System.out.println(username);
            Notification<Boolean> registerNotification = authenticationService.register(username, password);
            int verification = 0;
            for (User element : users){
                if (element.getUsername().equals(username)) {
                    System.out.println(element.getUsername());
                    System.out.println(username);
                    adminView.setActionTargetText("Username taken!");
                    verification = 1;
                }
            }
            if (registerNotification.hasErrors()) {
                adminView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                if(verification == 0) {
                    adminView.setActionTargetText("Register successful!");
                }
            }

            refreshView();
        }
    }

    public class UpdateButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            boolean registerNotification;
            User selectedUser = adminView.getSelectedUser();

            String password = adminView.getPassword();

            String username = adminView.getUsername();
            Long id = selectedUser.getId();

            if(username.equals("")) {
                registerNotification = authenticationService.updateEmployee(id, selectedUser.getUsername(), password);
            }else{
                if(password.equals("")){
                    registerNotification = authenticationService.updateEmployee(id, username, selectedUser.getPassword());

                }
                else{
                    registerNotification = authenticationService.updateEmployee(id, username, password);
                }
                refreshView();
            }
            if (registerNotification){
                adminView.setActionTargetText("Something went wrong!");
            }
            else {
                adminView.setActionTargetText("Updated!");
            }

        }
    }
}
