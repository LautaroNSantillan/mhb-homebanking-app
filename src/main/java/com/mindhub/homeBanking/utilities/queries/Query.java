package com.mindhub.homeBanking.utilities.queries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

//@Service
public class Query {
    @Autowired
    PasswordEncoder pwdEncoder;

    String jdbcURL = "jdbc:postgresql://localhost:5432/homebanking";
    String usernameDB = "homebankingapp";
    String pwdDB = "homebankingapp";
    @Autowired
    DataSource dataSource;
    Connection connection = null;

    public void connectToDatabase() {
        try {
            //connection = DriverManager.getConnection(jdbcURL, usernameDB, pwdDB);
            connection = dataSource.getConnection();
            System.out.println("Connected to DB");

            // connection.close();
        } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow() {
        String sql = "INSERT INTO client (first_name, last_name, email, password)"
                + "VALUES('PrimerUsuario', 'xxx', 'xxxx@gmail.com', '12345')";

        try {
            Statement statement = connection.createStatement();

            int rowsInserted = statement.executeUpdate(sql);
            if (rowsInserted > 0) {
                System.out.println("Row inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String fname, String lname, String email, String pwd) {
        String sql = "INSERT INTO client (first_name, last_name, email, password)"
                + "VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1,fname );
            statement.setString(2,lname );
            statement.setString(3,email );
            statement.setString(4, pwdEncoder.encode(pwd));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Client added");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fetchClients() {
        String sql = "SELECT * FROM  client ORDER BY id";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String id = result.getString(1);
                String fname = result.getString(2);
                String lname = result.getString(3);
                String email = result.getString(4);
                String pwd = result.getString(5);

                System.out.println(
                        "ID : " + id
                        + " | FIRST_NAME : " + fname
                        + " | LAST_NAME : " + lname
                        + " | EMAIL : " + email
                        + " | PASSWORD " + pwd

                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fetchMickeys() {
        String sql = "SELECT * FROM  client WHERE first_name='Mickey'";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String id = result.getString(1);
                String fname = result.getString(2);
                String lname = result.getString(3);
                String email = result.getString(4);
                String pwd = result.getString(5);

                System.out.println(
                        "ID : " + id
                                + " | FIRST_NAME : " + fname
                                + " | LAST_NAME : " + lname
                                + " | EMAIL : " + email
                                + " | PASSWORD " + pwd

                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void numberOfMickeys() {
        String sql = "SELECT * FROM  client WHERE first_name='Mickey'";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String id = result.getString(1);
                String fname = result.getString(2);
                String lname = result.getString(3);
                String email = result.getString(4);
                String pwd = result.getString(5);

                System.out.println(
                        "ID : " + id
                                + " | FIRST_NAME : " + fname
                                + " | LAST_NAME : " + lname
                                + " | EMAIL : " + email
                                + " | PASSWORD " + pwd

                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fetchMelbaAndMickeys() {
        String sql = "SELECT * FROM  client WHERE first_name IN ('Mickey', 'Melba')";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String id = result.getString(1);
                String fname = result.getString(2);
                String lname = result.getString(3);
                String email = result.getString(4);
                String pwd = result.getString(5);

                System.out.println(
                        "ID : " + id
                                + " | FIRST_NAME : " + fname
                                + " | LAST_NAME : " + lname
                                + " | EMAIL : " + email
                                + " | PASSWORD " + pwd

                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fetchAdmins() {
        String sql = "SELECT * FROM  client WHERE email LIKE '%@admin.mindhub'";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String id = result.getString(1);
                String fname = result.getString(2);
                String lname = result.getString(3);
                String email = result.getString(4);
                String pwd = result.getString(5);

                System.out.println(
                        "ID : " + id
                                + " | FIRST_NAME : " + fname
                                + " | LAST_NAME : " + lname
                                + " | EMAIL : " + email
                                + " | PASSWORD " + pwd

                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void distinctNames() {
        String sql = "SELECT DISTINCT first_name FROM client";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String name = result.getString(1);
                System.out.println("NAME : " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void countNames() {
        String sql = "SELECT  first_name, COUNT(*) FROM client GROUP BY first_name";
        try {
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(sql);

            while (result.next()){
                String name = result.getString(1);
                String count = result.getString(2);
                System.out.println("NAME : " + name + " | COUNT : " + count );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
