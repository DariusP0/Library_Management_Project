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

public class CustomerView {

    private final User user;
    private Button viewAllBooksButton;
    private Button buyButton;
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
    private Label customerContentLabel;
    private TableView<Book> selectedBookTableView;
    private TableColumn<Book, Long> selectedBookIdColumn;
    private TableColumn<Book, String> selectedBookTitleColumn;
    private TableColumn<Book, String> selectedBookAuthorColumn;
    private TableColumn<Book, LocalDate> selectedBookDateColumn;
    private TableColumn<Book, Integer> selectedBookPriceColumn;
    private TableColumn<Book, Integer> selectedBookQuantityColumn;
    private ObservableList<Book> selectedBooksData;
    public CustomerView(User user) {
        this.user = user;

        Stage stage = new Stage();
        stage.setTitle("Customer View");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        initializeSceneTitle(gridPane);
        initializeFields(gridPane);
        initializeSelectedBookTableView(gridPane);
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
    //Initialize BoughtBooks/Cart table
    private void initializeSelectedBookTableView(GridPane gridPane) {
        selectedBookTableView = new TableView<>();
        selectedBookIdColumn = new TableColumn<>("ID");
        selectedBookTitleColumn = new TableColumn<>("Title");
        selectedBookAuthorColumn = new TableColumn<>("Author");
        selectedBookDateColumn = new TableColumn<>("Date");
        selectedBookPriceColumn = new TableColumn<>("Price");
        selectedBookQuantityColumn = new TableColumn<>("Quantity");

        // Table columns values for selected book
        selectedBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        selectedBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        selectedBookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        selectedBookDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        selectedBookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        selectedBookQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Add columns to the TableView
        selectedBookTableView.getColumns().addAll(
                selectedBookIdColumn,
                selectedBookTitleColumn,
                selectedBookAuthorColumn,
                selectedBookDateColumn,
                selectedBookPriceColumn,
                selectedBookQuantityColumn
        );


        VBox selectedBookTableViewBox = new VBox(10);
        selectedBookTableViewBox.getChildren().add(selectedBookTableView);


        gridPane.add(selectedBookTableViewBox, 0, 6, 2, 1);

        selectedBookTableView.setVisible(false);
    }

    // Set selected books data to the selectedBookTable
    public void setSelectedBookData(List<Book> selectedBooks) {
        selectedBooksData = FXCollections.observableArrayList(selectedBooks);
        selectedBookTableView.setItems(selectedBooksData);
        selectedBookTableView.setVisible(true);
    }


    //Initial view for customer
    private void initializeFields(GridPane gridPane) {
        viewAllBooksButton = new Button("View All Books");
        buyButton = new Button("Buy");

        //Quantity Label
        quantityLabel = new Label("Quantity:");
        quantityTextField = new TextField();
        quantityTextField.setPrefWidth(50);

        //Price Label
        priceLabel = new Label("Price:");
        priceTextField = new TextField();
        priceTextField.setPrefWidth(50);
        priceTextField.setEditable(false);

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
        viewAllBooksHBox.getChildren().add(viewAllBooksButton);
        gridPane.add(viewAllBooksHBox, 1, 4);
        
        HBox buyButtonHBox = new HBox(10);
        buyButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        buyButtonHBox.getChildren().addAll(buyButton, quantityLabel, quantityTextField, priceLabel, priceTextField);
        gridPane.add(buyButtonHBox, 0, 4);

        gridPane.add(tableViewBox, 0, 5, 2, 1);

        customerContentLabel = new Label("This is the customer view.");

    }

    // Get the quantity from textField
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

    public void addBuyButtonListener(EventHandler<ActionEvent> BuyButtonListener) {
        buyButton.setOnAction(BuyButtonListener);
    }
}
