package Controllers;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
//Класс взаимодействия с БД
public class NoteDB {
    private static final String url = "jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection connection;

    public static void dbConnect() throws SQLException {
        try{
            connection = DriverManager.getConnection(url,user,password);
            connection.setCatalog("notedatabase");
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        }
    }

    public static void dbDisconnect() throws SQLException {
        try{
            if(!connection.isClosed())
            connection.close();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            throw sqlEx;
        }
    }

    //проверяет есть ли бд а, если нет, то создает
    public static void dbCreateDB() throws SQLException{
        ResultSet rs = null;
        Statement stmt = null;
        connection = DriverManager.getConnection(url,user,password);
        try {
            rs = connection.getMetaData().getCatalogs();
            boolean flag = false;
            while (rs.next()) {
                if (rs.getString(1).equals("notedatabase")) flag = true;
            }
            if (!flag) {
                stmt = connection.createStatement();
                stmt.executeUpdate("create database notedatabase");
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw sqlEx;
        }
        finally{
            if(stmt != null)
                stmt.close();
            if(rs != null)
                rs.close();
            dbDisconnect();
        }
    }

    //проверяет есть ли таблица, если нет, то создает
    public static void dbCreateTable() throws SQLException{
        ResultSet rs = null;
        Statement stmt = null;
        try{
            dbConnect();
            rs = connection.getMetaData().getTables(null,null,"notes",null);
            if (!rs.next()){
                String sql = "CREATE TABLE `notes` (\n" +
                        "  `id` int(30) NOT NULL AUTO_INCREMENT,\n" +
                        "  `note` varchar(100) NOT NULL,\n" +
                        "  `creationDate` datetime NOT NULL,\n" +
                        "  PRIMARY KEY (`Id`)\n" +
                        ")";
                executeUpdate(sql);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw sqlEx;
        }
        finally{
            if(stmt != null)
                stmt.close();
            if(rs != null)
                rs.close();
            dbDisconnect();
        }
    }

    //метод выполняющий executeQuery
    public static ResultSet executeQuery(String sqlQuery) throws  SQLException{
        Statement stmt = null;
        ResultSet rs = null;
        CachedRowSet crs = null;
        try{
            dbConnect();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sqlQuery);
            crs = new CachedRowSetImpl();
            crs.populate(rs);
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println("Problem with query operation" + sqlEx);
            throw sqlEx;
        }
        finally {
            if(stmt != null){
                stmt.close();
            }
            if(rs != null){
                rs.close();
            }
            dbDisconnect();
        }
        return crs;
    }
    //метод выполняющий executeUpdate
    public static void executeUpdate(String sql) throws SQLException{
        Statement stmt = null;
        try{
            dbConnect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println("Problem with updating " + sqlEx);
            throw sqlEx;
        }
        finally {
            if (stmt!=null)
                stmt.close();
            dbDisconnect();
        }
    }
}
