/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Model.User;
import static Server.MailServer.sendServerStatus;

 
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import sun.rmi.transport.Transport;

/**
 *
 * @author Khanh
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=De10_1;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //dăng ký mới
    public static boolean registerUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
           // preparedStatement.setString(3, fullName);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //kiểm tra đăng nhập
//    public static boolean authenticateUser(String username, String password) {
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
////            String query = "SELECT password FROM Users WHERE username = ?";
////            PreparedStatement preparedStatement = connection.prepareStatement(query);
////            preparedStatement.setString(1, username);
////            ResultSet resultSet = preparedStatement.executeQuery();
//            
//            if (User.getUserByUsername(username, password) != null) {
//                String storedPassword = resultSet.getString("password");
//                return storedPassword.equals(password);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static void sendEmail(String recipient, String subject, String content, byte is_cc,byte is_bcc, byte addEmail, String file_name) throws IOException {
        try (Connection connection = connect()) {
            String query = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, recipient);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Gửi email cho người dùng
                sendEmailToRecipient(recipient, subject, content, is_cc, is_bcc,addEmail, file_name);
                System.out.println("Email đã được gửi cho người dùng: " + recipient);
                sendServerStatus("Email đã được gửi cho người dùng: " + recipient);
            } else {
                System.out.println("Người dùng không tồn tại trong cơ sở dữ liệu. Không thể gửi email.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    private static void sendEmailToRecipient(String recipient, String subject, String content, byte is_cc, byte is_bcc, byte addEmail, String file_name) throws IOException, SQLException {
        CallableStatement callableStatement = null;
        int id = 0;
        try {

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String insertSql = "Select user_id from Account WHERE username = ?";
            // Tạo một đối tượng PreparedStatement để thực hiện câu SQL
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);

            // Thiết lập các tham số cho câu SQL
            preparedStatement.setString(1, recipient);

            // Thực hiện câu SQL INSERT
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bản ghi
                id = resultSet.getInt("user_id");

            }

           

            String procedureCall = "{call sp_InsertEmail(?, ?, ?, ?, ?, ?, ?, ?)}";
            callableStatement = connection.prepareCall(procedureCall);

            // Đặt các tham số cho Procedure (nếu có)
            callableStatement.setString(1, subject);
            callableStatement.setString(2, content);
            callableStatement.setInt(3, UserManager.getUserByIp(MailServer.clientSocket.getInetAddress().getHostAddress()).getUserId());
            callableStatement.setInt(4, id);           
            callableStatement.setByte(5, is_cc);
            callableStatement.setByte(6, is_bcc);
            callableStatement.setString(7, file_name);
            callableStatement.setByte(8, addEmail);

            // Thực hiện Procedure
            callableStatement.execute();

            // Lấy kết quả nếu có
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (callableStatement != null) {
                callableStatement.close();
            }
        }
    }

    //phản hồi mail
    
}
