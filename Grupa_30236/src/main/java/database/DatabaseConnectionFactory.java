package database;

public class DatabaseConnectionFactory {
    private static final String SCHEMA = "test";
    private static final String TEST_SCHEMA = "test";

    public static JDBConnectionWrapper getConnectionWrapper(boolean test){
        if(test){
            return new JDBConnectionWrapper(TEST_SCHEMA);
        }else{
            return new JDBConnectionWrapper(SCHEMA);
        }
    }
}
