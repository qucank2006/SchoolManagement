package com.example.dacs1.Test;

import com.example.dacs1.Utils.DatabaseConnector;

import java.sql.Connection;

public class CSDL {
    public static void main(String[] args) {
        Connection con = DatabaseConnector.getConnection();
        if (con != null) {
            System.out.println("Kết nối CSDL thành công!");
        } else {
            System.out.println("Kết nối CSDL thất bại!");
        }
    }

}
