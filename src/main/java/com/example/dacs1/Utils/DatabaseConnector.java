package com.example.dacs1.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    private static String DATABASE_URL = "jdbc:mysql://localhost:3306/school_data";
    private static String DATABASE_USER = "root";
    private static String DATABASE_PASSWORD = "";

    public static Connection getConnection(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
}