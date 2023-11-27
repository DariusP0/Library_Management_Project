import controller.LoginController;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import view.CustomerView;
import view.LoginView;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static database.Constants.Schemas.PRODUCTION;
import static database.Constants.Schemas.TEST;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Connection connection = new JDBConnectionWrapper(TEST).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        final AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository,
                rightsRolesRepository);

        final LoginView loginView = new LoginView(primaryStage);

        final UserValidator userValidator = new UserValidator(userRepository);

        final BookRepository bookRepository = new BookRepositoryMySQL(connection);

        final BookService bookService = new BookServiceImpl(bookRepository);

        new LoginController(loginView, authenticationService, userValidator, bookService);
        //test update quantity
//        BookRepository b1 = new BookRepositoryMySQL(connection);
//        Book book = new BookBuilder()
//                .setTitle("Harry Potter")
//                .setAuthor("J.K. Rowling")
//                .setPublishedDate(LocalDate.of(2010, 7, 3))
//                .setQuantity(10)
//                .setPrice(120)
//                .build();
//        BookService bookService = new BookServiceImpl(b1);
//        bookService.findAll();
    }
}
