package repository;

import model.*;
import model.builder.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL<T> implements BookRepository<T> {

    private final Connection connection;

    public BookRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM book;";

        List<T> books = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<T> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Optional<T> book = Optional.empty();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public boolean save(T book) {
        String sql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";
        int rowsInserted = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            if (book instanceof Book) {
                Book castedBook = (Book) book;

                preparedStatement.setString(1, castedBook.getAuthor());
                preparedStatement.setString(2, castedBook.getTitle());
                preparedStatement.setDate(3, java.sql.Date.valueOf(castedBook.getPublishedDate()));
                preparedStatement.setNull(4, Types.VARCHAR);
                preparedStatement.setNull(5, Types.VARCHAR);

                if (book instanceof EBook) {
                    preparedStatement.setString(5, ((EBook) book).getFormat());
                } else if (book instanceof AudioBook) {
                    preparedStatement.setString(4, ((AudioBook) book).getLength());
                }

                rowsInserted = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return rowsInserted == 1;
    }


    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private T getBookFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.getString("runtime") != null) {
            return (T) new AudioBookBuilder()
                    .setLength("runtime")
                    .setId(resultSet.getLong("id"))
                    .setTitle(resultSet.getString("title"))
                    .setAuthor(resultSet.getString("author"))
                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())

                    .build();
        }else if (resultSet.getString("format") != null) {
            return (T) new EBookBuilder()
                    .setId(resultSet.getLong("id"))
                    .setTitle(resultSet.getString("title"))
                    .setAuthor(resultSet.getString("author"))
                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                    .setFormat(resultSet.getString("format"))
                    .build();

        } else{
            return (T) new BookBuilder()
                    .setId(resultSet.getLong("id"))
                    .setTitle(resultSet.getString("title"))
                    .setAuthor(resultSet.getString("author"))
                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                    .build();
        }
    }
}
