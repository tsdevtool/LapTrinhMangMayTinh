/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.soap.SOAPBinding;
/**
 * 
 *
 * @author Khanh
 */
public class UserManager {
//       private static List<User> userList = new ArrayList<>();
    private static Map<String, User> userByIp = new HashMap<>();

    public static void addUser(User user, String ip) {
//        userList.add(user);
        userByIp.put(ip, user);
    }

    public static User getUserByIp(String ip) {
        return userByIp.get(ip);
    }

    
    public static boolean registerUser(String username, String password) {
        try (Connection connection = DatabaseManager.connect()) {
            String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}
