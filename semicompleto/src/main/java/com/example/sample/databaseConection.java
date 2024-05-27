package com.example.sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class databaseConection {

    public Connection databaselink;

    public Connection getConnection (){
        String databaseName = "piezascoche";
        String databaseUser = "root";
        String databasePaswword = "root";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            //Class.forName ( "com.mysql.cj.jbc.Driver" );
            databaselink = DriverManager.getConnection ( url, databaseUser, databasePaswword );

        }catch (Exception e) {
            e.printStackTrace ();
        }
        return databaselink;
    }

}