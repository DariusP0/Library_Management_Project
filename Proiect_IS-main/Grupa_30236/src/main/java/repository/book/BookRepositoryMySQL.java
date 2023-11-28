package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{

    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Optional<Book> book = Optional.empty();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                book = Optional.of(getBookFromResultSet(resultSet));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return book;
    }

    /**
     *
     * How to reproduce a sql injection attack on insert statement
     *
     *
     * 1) Uncomment the lines below and comment out the PreparedStatement part
     * 2) For the Insert Statement DROP TABLE SQL Injection attack to succeed we will need multi query support to be added to our connection
     * Add to JDBConnectionWrapper the following flag after the DB_URL + schema concatenation: + "?allowMultiQueries=true"
     * 3) book.setAuthor("', '', null); DROP TABLE book; -- "); // this will delete the table book
     * 3*) book.setAuthor("', '', null); SET FOREIGN_KEY_CHECKS = 0; SET GROUP_CONCAT_MAX_LEN=32768; SET @tables = NULL; SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()); SELECT IFNULL(@tables,'dummy') INTO @tables; SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables); PREPARE stmt FROM @tables; EXECUTE stmt; DEALLOCATE PREPARE stmt; SET FOREIGN_KEY_CHECKS = 1; --"); // this will delete all tables. You are not required to know the table name anymore.
     * 4) Run the program. You will get an exception on findAll() method because the test_library.book table does not exist anymore
     */


    // ALWAYS use PreparedStatement when USER INPUT DATA is present
    // DON'T CONCATENATE Strings!
    @Override
    public boolean save(Book book) {
        String checkSql = "SELECT * FROM book WHERE author = ? AND title = ?";
        String insertSql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?)";

        try {
            // Check if the book already exists
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, book.getAuthor());
            checkStatement.setString(2, book.getTitle());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {

                int oldBookQuantity = resultSet.getInt("quantity");
                int newQuantity = oldBookQuantity + book.getQuantity();
                //Check if the quantity of the book to be added is 0;
                if (book.getQuantity() == 0) {
                    System.out.println("Quantity to add is 0. Please Check");
                    return false;
                }

                return updateQuantity(book.getAuthor(), book.getTitle(), newQuantity);

            } else {
                // Book doesn't exist, insert new record
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, book.getAuthor());
                insertStatement.setString(2, book.getTitle());
                insertStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
                insertStatement.setInt(4, book.getPrice());  // Use setInt for price
                insertStatement.setInt(5, book.getQuantity());  // Use setInt for quantity
                int rowsInserted = insertStatement.executeUpdate();
                return (rowsInserted != 1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateQuantity(String author, String title, int newQuantity) {
        String updateSql = "UPDATE book SET quantity = ? WHERE author = ? AND title = ?";

        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setInt(1, newQuantity);
            updateStatement.setString(2, author);
            updateStatement.setString(3, title);
            int rowsUpdated = updateStatement.executeUpdate();
            return (rowsUpdated != 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setPrice(Integer.parseInt(resultSet.getString("price")))
                .setQuantity(Integer.parseInt(resultSet.getString("quantity")))
                .build();
    }

}