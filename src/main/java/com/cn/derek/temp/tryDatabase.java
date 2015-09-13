package com.cn.derek.temp;

import java.sql.*;

class tryDatabase {
 
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Class.forName("com.mysql.jdbc.Driver");
         
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/mysql",
                "root","root");
        Statement stmt =  conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from user");
         
        while (rs.next()) {
            System.out.println( rs.toString() );
            }
         
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();   
        }
        if (conn != null) {
            conn.close();   
        }
    }
 
}
