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
import javafx.stage.Stage;
import model.Book;
import model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class EmployeeView {

    private final User user;
    private Button viewAllBooksButton;
    private Button sellButton, createButton, updateButton, retrieveButton, deleteButton;
    private Label welcomeLabel;
    private Label quantityLabel;
    private TextField quantityTextField;
    private Label priceLabel;
    private TextField priceTextField;
    private TableView<Book> bookTableView;
    private TableColumn<Book, Long> idColumn;
    private TableColumn<Book, String> titleColumn;
    private TableColumn<Book, String> authorColumn;
    private TableColumn<Book, LocalDate> dateColumn;
    private TableColumn<Book, Integer> priceColumn;
    private TableColumn<Book, Integer> quantityColumn;
    private ObservableList<Book> booksData;
    private Label idLabel, authorLabel, titleLabel, dateLabel;
    private TextField idTextField, authorTextField, titleTextField,dateTextField;
    public EmployeeView(User user) {
        this.user = user;

        Stage stage = new Stage();
        stage.setTitle("Customer View");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        initializeSceneTitle(gridPane);
        initializeFields(gridPane);

        Scene scene = new Scene(gridPane, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }
    private void initializeSceneTitle(GridPane gridPane) {
        welcomeLabel = new Label("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 16));
        welcomeLabel.setAlignment(Pos.TOP_CENTER);
        gridPane.add(welcomeLabel, 0, 0, 2, 1);
    }



    private void initializeFields(GridPane gridPane) {
        viewAllBooksButton = new Button("View All Books");
        sellButton = new Button("Sell");
        createButton = new Button("Create");
        updateButton = new Button("Update");
        retrieveButton = new Button("Retrieve");
        deleteButton = new Button("Delete");

        idLabel = new Label("ID:");
        idTextField = new TextField();
        idTextField.setPrefWidth(100);

        authorLabel = new Label("Author:");
        authorTextField = new TextField();
        authorTextField.setPrefWidth(100);

        titleLabel = new Label("Title:");
        titleTextField = new TextField();
        titleTextField.setPrefWidth(100);


        //Quantity Label
        quantityLabel = new Label("Quantity:");
        quantityTextField = new TextField();
        quantityTextField.setPrefWidth(100);


        //Price Label
        priceLabel = new Label("Price:");
        priceTextField = new TextField();
        priceTextField.setPrefWidth(100);

        dateLabel = new Label("Published Date:");
        dateTextField = new TextField();
        dateTextField.setPrefWidth(100);


        HBox fieldsBox = new HBox();
        fieldsBox.setSpacing(15);
        fieldsBox.setAlignment(Pos.BOTTOM_LEFT);
        fieldsBox.getChildren().addAll(idLabel, idTextField, authorLabel, authorTextField, titleLabel,titleTextField,dateLabel, dateTextField, priceLabel,priceTextField,quantityLabel,quantityTextField);
        gridPane.add(fieldsBox, 0, 4);

        //All books table init
        bookTableView = new TableView<>();
        idColumn = new TableColumn<>("ID");
        titleColumn = new TableColumn<>("Title");
        authorColumn = new TableColumn<>("Author");
        dateColumn = new TableColumn<>("Date");
        priceColumn = new TableColumn<>("Price");
        quantityColumn = new TableColumn<>("Quantity");


        //Table columns values
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        // Add columns to the TableView
        bookTableView.getColumns().addAll(idColumn, titleColumn, authorColumn, dateColumn, priceColumn, quantityColumn);

        // Create a VBox to hold the TableView
        VBox tableViewBox = new VBox(10);
        tableViewBox.getChildren().add(bookTableView);


        // Handle button actions + add buttons to view

        HBox viewAllBooksHBox = new HBox(10);
        viewAllBooksHBox.setAlignment(Pos.BOTTOM_RIGHT);
        viewAllBooksHBox.getChildren().addAll(viewAllBooksButton,createButton,updateButton,deleteButton,retrieveButton, sellButton);
        gridPane.add(viewAllBooksHBox, 1, 4);



        gridPane.add(tableViewBox, 0, 5, 2, 1);

    }

    // Get the quantity from textField

    public String getIdText(){return idTextField.getText();}
    public String getAuthorText(){return authorTextField.getText();}
    public String getTitleText(){return titleTextField.getText();}
    public String getDate(){return dateTextField.getText();}
    public String getPrice(){return priceTextField.getText();}
    public String getQuantityText(){
        return quantityTextField.getText();
    }

    public void setPriceText(int price){
        priceTextField.setText(String.valueOf(price));
    }
    public void setBooksData(List<Book> books) {
        booksData = FXCollections.observableArrayList(books);
        bookTableView.setItems(booksData);
    }

    //Get a book selected by clicking
    public Book getSelectedBook() {
        return bookTableView.getSelectionModel().getSelectedItem();
    }

    public void addViewAllButtonListener(EventHandler<ActionEvent> ViewAllButtonListener) {
        viewAllBooksButton.setOnAction(ViewAllButtonListener);
    }
    public void addSellButtonListener(EventHandler<ActionEvent> SellButtonListener){
        sellButton.setOnAction(SellButtonListener);
    }
    public void addCreateButtonListener(EventHandler<ActionEvent> CreateButtonListener){
        createButton.setOnAction(CreateButtonListener);
    }
    public void addRetrieveButtonListener(EventHandler<ActionEvent> RetrieveButtonListener){
        retrieveButton.setOnAction(RetrieveButtonListener);
    }
    public void addUpdateButtonListener(EventHandler<ActionEvent> UpdateButtonListener){
        updateButton.setOnAction(UpdateButtonListener);
    }
    public void addDeleteButtonListener(EventHandler<ActionEvent> DeleteButtonListener){
        deleteButton.setOnAction(DeleteButtonListener);
    }

//    public void addBuyButtonListener(EventHandler<ActionEvent> BuyButtonListener) {
//        buyButton.setOnAction(BuyButtonListener);
//    }
}