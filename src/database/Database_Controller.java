package database;

import java.sql.*;

public class Database_Controller
{
    public static void initializeTables(int numOfTable) throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");

        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Table.db");
        Statement statement = connStream.createStatement();

        for (int i = 1; i <= numOfTable; i++)
            statement.execute("CREATE TABLE IF NOT EXISTS table_" + i
                    +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, Item_Num INTEGER)");

        connStream.close();
        statement.close();
    }

    public static void initializePickup(int numOfTable) throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");

        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS Customers (name String,phoneNumber String)");

        connStream.close();
        statement.close();
    }

    public static void createPickupOrder(String name, String phoneNumber) throws SQLException, ClassNotFoundException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        name = name.replaceAll("\\W", "_");

        statement.execute("INSERT INTO Customers (name, phoneNumber) " +
                "VALUES ('" + name +"','" + phoneNumber +"')");

        statement.execute("CREATE TABLE IF NOT EXISTS " + name
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Item_Num INTEGER)");

        connStream.close();
        statement.close();
    }


    public static void addItemToPickupOrder(String name, int itemNum) throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        name = name.replaceAll("\\W", "_");

        statement.execute("INSERT INTO " + name + " (Item_Num) VALUES (" + itemNum + ");");

        connStream.close();
        statement.close();
    }

    public static void deleteItemFromOrder(String name, int ID)
            throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        statement.execute("DELETE FROM " + name + " WHERE ID="  + ID + ";");
        statement.execute("CREATE TABLE tmp (ID INTEGER PRIMARY KEY AUTOINCREMENT, Item_Num INTEGER)");
        statement.execute("INSERT INTO tmp (Item_Num) SELECT Item_Num FROM " + name);
        statement.execute("DROP TABLE " + name);
        statement.execute("ALTER TABLE tmp RENAME TO " + name);

        connStream.close();
        statement.close();
    }

    public static void deleteOrder(String cusName)
            throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        statement.execute("DELETE FROM Customers WHERE name = '"+ cusName +"'");
         statement.execute("DROP TABLE " + cusName);

        connStream.close();
        statement.close();
    }

    public static String getPhoneNumber(String name)
            throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();


        ResultSet rs = statement.executeQuery("SELECT phoneNumber FROM Customers WHERE name = '" + name + "'");

        String tmp = rs.getString("phoneNumber");

        connStream.close();
        statement.close();
        rs.close();

        return tmp;
    }

    public static String[] getPickupOrders() throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        ResultSet count = statement.executeQuery("SELECT COUNT(*) AS total FROM Customers");

        String[] items = new String[count.getInt("total")];

        ResultSet rs = statement.executeQuery("SELECT name FROM Customers");

        int i = 0;

        while (rs.next())
        {
            items[i] = rs.getString("name");
            i++;
        }

        connStream.close();
        statement.close();

        return items;
    }

    public static int[] getItemOfOrder(String name) throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Pickup.db");
        Statement statement = connStream.createStatement();

        ResultSet count = statement.executeQuery("SELECT COUNT(*) AS total FROM " + name);

        int[] items = new int[count.getInt("total")];

        ResultSet rs = statement.executeQuery("SELECT Item_Num FROM "  + name);

        int i = 0;

        while (rs.next())
        {
            items[i] = rs.getInt("Item_Num");
            i++;
        }

        connStream.close();
        statement.close();

        return items;
    }

    public static void addItemToTable(int tableNum, int itemNum)
            throws SQLException
    {

        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Table.db");
        Statement statement = connStream.createStatement();

        statement.execute("INSERT INTO table_" + tableNum + " (Item_Num) VALUES (" + itemNum + ");");

        connStream.close();
        statement.close();
    }

    public static void deleteItemFromTable(int tableNum, int ID)
            throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Table.db");
        Statement statement = connStream.createStatement();

        statement.execute("DELETE FROM table_" + tableNum + " WHERE ID="  + ID + ";");
        statement.execute("CREATE TABLE tmp (ID INTEGER PRIMARY KEY AUTOINCREMENT, Item_Num INTEGER)");
        statement.execute("INSERT INTO tmp (Item_Num) SELECT Item_Num FROM table_" + tableNum);
        statement.execute("DROP TABLE table_" + tableNum);
        statement.execute("ALTER TABLE tmp RENAME TO table_" + tableNum);

        connStream.close();
        statement.close();
    }

    public static void clearTable(int tableNum)
            throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Table.db");
        Statement statement = connStream.createStatement();

        statement.execute("DROP TABLE table_" + tableNum);

        statement.execute("CREATE TABLE table_" + tableNum
                                +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, Item_Num INTEGER)");

        connStream.close();
        statement.close();
    }

    public static int[] getItemOfTable (int tableNum)
            throws SQLException
    {
        Connection connStream = DriverManager.getConnection
                ("jdbc:sqlite:src\\database\\Table.db");
        Statement statement = connStream.createStatement();

        ResultSet count = statement.executeQuery("SELECT COUNT(*) AS total FROM table_" + tableNum);

        int[] items = new int[count.getInt("total")];

        ResultSet rs = statement.executeQuery("SELECT Item_Num FROM table_" + tableNum);

        int i = 0;

        while (rs.next())
        {
            items[i] = rs.getInt("Item_Num");
            i++;
        }

        connStream.close();
        statement.close();

        return items;
    }
}
