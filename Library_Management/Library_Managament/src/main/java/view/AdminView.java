package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Role;
import model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminView {

    private Button logOutButton;
    private TableView<User> userTableView;
    private Text actiontarget;
    private Button retrieveButton;
    private Button deleteButton;
    private Button createButton;
    private Button updateButton;
    private Button showAllButton;
    private ObservableList<User> userData;
    private Label usernameText;
    private TextField usernameArea;
    private TableColumn<User, List<Role>> rolesColumn;
    private TableColumn<User, String> usernameColumn;
    private Label passwordLabel;
    private PasswordField passwordArea;
    private TextField retrieveArea;
    private Label retrieveLabel;
    private Label changeIdLabel;
    private Button changeIdButton;
    private User user;
    private ComboBox<String> comboBoxRole;

    public AdminView(User user) {
        this.user = user;

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Admin MODE: ON");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        usernameText = new Label("Username :");
        usernameText.setFont(Font.font("Tahome", FontWeight.NORMAL, 15));

        passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 15));

        retrieveLabel = new Label("Retrieve ID:");
        retrieveLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 15));

        changeIdLabel = new Label("Change Role- First select an account from the list");
        changeIdLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 15));

        usernameArea = new TextField();
        retrieveArea = new TextField();
        passwordArea = new PasswordField();
        //changeIdField = new TextField();

        usernameArea.setEditable(true);
        retrieveArea.setEditable(true);
        passwordArea.setEditable(true);
        //changeIdField.setEditable(true);

        usernameArea.setPrefSize(100, 20);
        retrieveArea.setPrefSize(100, 20);  // Set the preferred width and height
        passwordArea.setPrefSize(200, 20);  // Set the preferred width and height
        //changeIdField.setPrefSize(100,20);


        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);

        Scene scene = new Scene(gridPane, 750, 500);
        primaryStage.setScene(scene);

        initializeAdminView(gridPane);


        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 0, 0, 0));
    }

    private void initializeAdminView(GridPane gridPane){

        userTableView = new TableView<>();
        usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        rolesColumn = new TableColumn<>("Roles");
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
        rolesColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(List<Role> roles, boolean empty) {
                super.updateItem(roles, empty);
                if (empty || roles == null || roles.isEmpty()) {
                    setText("");
                } else {
                    setText(roles.stream().map(Role::getRole).collect(Collectors.joining(", ")));
                }
            }
        });

        // Add columns to the TableView
        userTableView.getColumns().addAll(usernameColumn, rolesColumn);

        userTableView.setPrefSize(282, 300);

        usernameColumn.setPrefWidth(200);

        VBox tableViewBox = new VBox(10);
        tableViewBox.getChildren().add(userTableView);

        comboBoxRole = new ComboBox<>();

        // Add items to the ComboBox
        ObservableList<String> items = FXCollections.observableArrayList("Admin", "Employee", "Customer");
        comboBoxRole.setItems(items);

        // Set the default value
        comboBoxRole.setValue("Admin");

        logOutButton = new Button("LogOut");
        retrieveButton = new Button("Retrieve");
        updateButton = new Button("Update");
        createButton = new Button("Create");
        deleteButton = new Button("Delete");
        showAllButton = new Button("View All");
        changeIdButton = new Button("Change Role");

        HBox buttonsDelete = new HBox(10);
        buttonsDelete.setAlignment(Pos.BOTTOM_CENTER);
        buttonsDelete.getChildren().addAll(deleteButton);

        HBox buttonsCreate = new HBox(10);
        buttonsCreate.setAlignment(Pos.BOTTOM_CENTER);
        buttonsCreate.getChildren().addAll(createButton, updateButton);

        VBox usernameBox = new VBox(10);  // Adjust the spacing (5 is just an example)
        usernameBox.setAlignment(Pos.CENTER);
        usernameBox.getChildren().addAll(usernameText, usernameArea, passwordLabel, passwordArea, buttonsCreate);

        gridPane.add(usernameBox, 3, 0, 1, 1);


        VBox retrieveBox = new VBox(10);
        retrieveBox.setAlignment(Pos.BOTTOM_CENTER);
        retrieveBox.getChildren().addAll(retrieveLabel,retrieveArea,retrieveButton,showAllButton);
        gridPane.add(retrieveBox, 3, 4, 1, 1);

        gridPane.add(changeIdLabel,3,7,1,1);
        HBox changeIdBox = new HBox(10);
        changeIdBox.setAlignment(Pos.BOTTOM_CENTER);
        changeIdBox.getChildren().addAll(comboBoxRole, changeIdButton);
        gridPane.add(changeIdBox, 3, 8 ,1 ,1 );
        gridPane.add(buttonsDelete, 0, 6, 1, 1);


        gridPane.add(actiontarget, 0, 8);

        gridPane.add(tableViewBox, 0, 0, 1, 5);

    }

    public void addRetrieveButtonListener(EventHandler<ActionEvent> retrieveButtonHandler) {
        retrieveButton.setOnAction(retrieveButtonHandler);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonHandler) {
        deleteButton.setOnAction(deleteButtonHandler);
    }

    public void addCreateButtonListener(EventHandler<ActionEvent> createButtonHnadler) {
        createButton.setOnAction(createButtonHnadler);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonHandler) {
        updateButton.setOnAction(updateButtonHandler);
    }

    public void addShowAllListener(EventHandler<ActionEvent> showAllHandler) {
        showAllButton.setOnAction(showAllHandler);
    }
    public void addRoleChangeListener(EventHandler<ActionEvent> roleChangeHandler) {
        changeIdButton.setOnAction(roleChangeHandler);
    }
    public void setUsersData(List<User> users) {
        userData = FXCollections.observableArrayList(users);
        userTableView.setItems(userData);
    }

    public User getSelectedUser(){
        return userTableView.getSelectionModel().getSelectedItem();
    }

//    public void closeAdminWindow() {
//        currentStage = (Stage) logOutButton.getScene().getWindow();
//        currentStage.close();
//    }

    public String getUsername() {
        return usernameArea.getText();
    }

    public void setActionTargetText(String text){ this.actiontarget.setText(text);}

    public String getRoleChoosen(){
        return comboBoxRole.getValue();
    }
    public String getPassword() {

        return passwordArea.getText();
    }

    public Long getId() {
        System.out.println(retrieveArea.getText());
        if(retrieveArea.getText().isEmpty()){
            return 0L;
        }
        else {
            return Long.valueOf(retrieveArea.getText());
        }

    }

    public void setUsersDataOptional(List<User> user) {
        userData = FXCollections.observableArrayList(user);
        userTableView.setItems(userData);
    }

}
