/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Model.EmailData;
import Model.Ceasar;
import Model.User;
import static Server.DatabaseManager.connect;

//import java.awt.List;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Khanh
 */
public class MailServer {

    private static final int SERVER_PORT = 1234;
    private static Ceasar ceasar = new Ceasar();
    private static final int DEFAULT_MAILBOX_SIZE_MB = 100; // Dung lượng mặc định cho hộp thư

    public static Socket clientSocket;
    private static frmServer serverForm = new frmServer();

    public static void sendServerStatus(String message) {
        if (serverForm != null) {
            serverForm.updateServerStatus(message);
        } else {
            //  serverForm.updateServerStatus(message);
        }
    }

    public static void main(String[] args) throws SQLException {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            // Tạo và hiển thị frm mới
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {

                    serverForm.setVisible(true);
                }
            });
            sendServerStatus("Server đã được mở. Đang đợi client .... (PORT = 1234)");
            while (true) {
                clientSocket = serverSocket.accept();
//                System.out.println("Client đã kết nối: " + clientSocket.getInetAddress().getHostAddress());

                // Xử lý yêu cầu đăng nhập của client
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) throws SQLException {
        //lấy địa chỉ ip của client
            InetAddress clientAddress = clientSocket.getInetAddress();
            String IP_CLIENT = clientAddress.getHostAddress();
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            String requestType = inputStream.readUTF();

            if ("login".equals(requestType)) {
                sendServerStatus("\n" + LocalTime.now() + ":");
                sendServerStatus("\nVừa có một client vừa gửi yêu cầu ĐĂNG NHẬP từ " + IP_CLIENT + "..");
                String email = inputStream.readUTF();
                String password = inputStream.readUTF();
                String time = inputStream.readUTF();

                System.out.println("Client đã kết nối: " + clientSocket.getInetAddress().getHostAddress() + " Là: " + email + " vào lúc " + time);
                UserManager.addUser(User.getUserByUsername(email, password), clientSocket.getInetAddress().getHostAddress());

                // Thực hiện truy vấn để lấy giá trị is_ban cho người dùng
                Connection connection = DatabaseManager.connect();
                String query0 = "SELECT is_ban FROM Account WHERE username = ?";
                PreparedStatement preparedStatement0 = connection.prepareStatement(query0);
                preparedStatement0.setString(1, email);
                ResultSet resultSet1 = preparedStatement0.executeQuery();

                boolean isBanned = false; // Mặc định người dùng không bị cấm
                if (resultSet1.next()) {
                    isBanned = resultSet1.getBoolean("is_ban");
                }

                if (UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()) != null) {
                    if (isBanned) {
                        // Người dùng đã đăng nhập nhưng bị cấm
                        outputStream.writeUTF("Tài khoản của bạn đã bị cấm.");
                    } else {
                        outputStream.writeUTF("Đăng Nhập Thành Công");
                        sendServerStatus("Client đã kết nối: " + clientSocket.getInetAddress().getHostAddress() + " Là: " + email + " vào lúc " + time);
                        //ghi login
                        int id = 0;

                        String query = "select user_id from Account where username = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, email);

                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            // Lấy dữ liệu từ các cột của bản ghi
                            id = resultSet.getInt("user_id");
                        }

                        String query1 = "INSERT INTO InsertLog (user_id, username, time_in) VALUES (?, ?, ?)";
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                        preparedStatement1.setInt(1, id); // user_id
                        preparedStatement1.setString(2, email); // username
                        preparedStatement1.setTimestamp(3, timestamp); // time_out

                        int rowsInserted = preparedStatement1.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Thêm bản ghi vào bảng InsertLog  thành công.");
                            sendServerStatus("Thêm bản ghi vào bảng InsertLog đăng nhập thành công.");
                            sendServerStatus("\n" + LocalTime.now() + ":");
                            sendServerStatus("\n\tYêu cầu đăng nhập THÀNH CÔNG của " + email+ ": " + IP_CLIENT + "..");
                        } else {
                            System.out.println("Thêm bản ghi vào bảng InsertLog không thành công.");
                            sendServerStatus("\n" + LocalTime.now() + ":");
                            sendServerStatus("\n\tYêu cầu đăng nhập THẤT BẠI " + IP_CLIENT + "..");
                        }
                    }
                } else {
                    outputStream.writeUTF("Đăng Nhập Thất Bại");
                }

            } else if ("REGISTER".equals(requestType)) {
                String email = inputStream.readUTF();
                String password = inputStream.readUTF();
                // String fullName = inputStream.readUTF();
                // Xử lý đăng ký
                boolean registrationSuccessful = DatabaseManager.registerUser(email, password);

                // Gửi kết quả đăng ký tới client
                if (registrationSuccessful) {
                    //userQuotas.put(email, DEFAULT_MAILBOX_SIZE_MB);
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                    output.println("SUCCESS");
                    sendServerStatus("\n" + LocalTime.now() + ":");
                    sendServerStatus("\n\tYêu cầu đăng ký THÀNH CÔNG của " + email + ": " + IP_CLIENT + "..");
                } else {
                    outputStream.writeUTF("Đăng ký thất bại.");
                }
            } else if ("guimail".equals(requestType)) {
                String recipient = inputStream.readUTF();
                String subject = inputStream.readUTF();
                String role = inputStream.readUTF();
                String[] allRec = recipient.split(",");

                byte cc = 0;
                byte bcc = 0;
                byte addEmail = 1;

                String content = inputStream.readUTF();

                //mahoa
                String messageType = inputStream.readUTF();
                String savePath = "";
                if ("attachment".equals(messageType)) {
                    // The client is sending an attachment
                    String fileName = inputStream.readUTF();
                    long attachmentLength = inputStream.readLong();

                    // Define a directory where you want to save the attachment
                    savePath = "src\\FileServer\\" + fileName;

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    try (FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
                        while (attachmentLength > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, attachmentLength))) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                            attachmentLength -= bytesRead;
                        }
                    }
                } else if ("no_attachment".equals(messageType)) {
                    savePath = "";
                }
                String message = UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId() + ":" + ceasar.Encrypt(content, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId()) + "@";;
                if (allRec.length >= 1) {
                    if ("CC".equals(role)) {
                        cc = 1;
                    } else if ("BCC".equals(role)) {
                        bcc = 1;
                    }else{
                        cc = 0;
                        bcc = 0;
                    }

                    for (int i = 0; i < allRec.length; i++) {

                        //ma hoa
                        Connection connection = connect();
                        String query = "Select user_id from Account WHERE username = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, allRec[i]);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        // Thực hiện câu SQL INSERT                      
                        while (resultSet.next()) {
                            // Lấy dữ liệu từ các cột của bản ghi
                            int id = resultSet.getInt("user_id");
                            message = message + id + ":" + ceasar.Encrypt(content, id) + "@";
                        }
                        connection.close();
                    }
                    for (int i = 0; i < allRec.length; i++) {
                        if (i == 0) {
                            addEmail = 1;
                        } else {
                            addEmail = 0;
                        }
                        // Gửi email cho người nhận
                        System.out.println(allRec[i]);
                        DatabaseManager.sendEmail(allRec[i], subject, message, cc, bcc, addEmail, savePath);

                        // Gửi kết quả thành công cho client
                        outputStream.writeUTF("Gửi email thành công.");
                    }
                }

            } else if ("hopthuden".equals(requestType)) {
                try {
                    // Kết nối đến cơ sở dữ liệu
                    Connection connection = DatabaseManager.connect();

                    // Tạo câu lệnh truy vấn
                    String query = "select e.email_id, e.timestamp, u.username, e.subject, e.body from Emails e join Recipients r on e.email_id = r.email_id join Account u on u.user_id = e.sender_id where r.user_id = ? and r.is_delete !=1 and e.email_id Not in (Select email_id from EmailTags where tag_id = 3 group by email_id) order by e.timestamp desc";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    // Đặt giá trị cho tham số
                    preparedStatement.setInt(1, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId());

                    // Thực hiện truy vấn
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Tạo danh sách để lưu trữ dữ liệu
                    List<EmailData> emailDataList = new ArrayList<>();

                    while (resultSet.next()) {

                        EmailData emailData = new EmailData();
                        emailData.setId(resultSet.getInt("email_id"));
                        emailData.setTimestamp(resultSet.getTimestamp("timestamp"));
                        emailData.setUsername(resultSet.getString("username"));
                        emailData.setSubject(resultSet.getString("subject"));
                        String ciphertextString = resultSet.getString("body");
//                        String[] allRec = ciphertextString.split("@");
//                        for (int i = 0; i < allRec.length; i++) {
//                            System.out.println(allRec[i]);
//                            if (i == 1) {
//                                String decryptedMessage = ceasar.Encrypt(allRec[i], -(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId()));
//                                emailData.setBody(decryptedMessage);
//                            }
//                        }
                        String[] messageParts = ciphertextString.split("@");
                        for (String part : messageParts) {
                            if (part.contains(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId() + ":")) {
                                String[] messageValue = part.split(":");
                                if (messageValue.length == 2) {
                                    String decryptedMessage = ceasar.Encrypt(messageValue[1], -(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId()));
                                    emailData.setBody(decryptedMessage);
                                }
                            }
                        }

                        //emailData.setBody(resultSet.getString("body"));
                        emailDataList.add(emailData);
                    }

                    // Gửi danh sách dữ liệu cho client
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(emailDataList);

                    // Đóng kết nối và tài nguyên
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            } else if ("thudagui".equals(requestType)) {
                try {
                    // Kết nối đến cơ sở dữ liệu
                    Connection connection = DatabaseManager.connect();

                    // Tạo câu lệnh truy vấn
                    String query = "SELECT e.timestamp,e.email_id,u.username,  e.subject, e.body\n"
                            + "	FROM Recipients r\n"
                            + "INNER JOIN Account u ON r.user_id = u.user_id\n"
                            + "inner join Emails e ON e.email_id = r.email_id\n"
                            + "WHERE r.email_id IN (SELECT e.email_id FROM Emails e WHERE sender_id = ? and e.email_id Not in (Select email_id from EmailTags where tag_id = 3 group by email_id))order by e.timestamp desc";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    // Đặt giá trị cho tham số
                    preparedStatement.setInt(1, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId());

                    // Thực hiện truy vấn
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Tạo danh sách để lưu trữ dữ liệu
                    List<EmailData> emailDataList = new ArrayList<>();

                    while (resultSet.next()) {
                        EmailData emailData = new EmailData();
                        emailData.setId(resultSet.getInt("email_id"));
                        emailData.setTimestamp(resultSet.getTimestamp("timestamp"));
                        emailData.setUsername(resultSet.getString("username"));
                        emailData.setSubject(resultSet.getString("subject"));
                        String ciphertextString = resultSet.getString("body");

                        String[] messageParts = ciphertextString.split("@");
                        for (String part : messageParts) {
                            if (part.contains(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId() + ":")) {
                                String[] messageValue = part.split(":");
                                if (messageValue.length == 2) {
                                    String decryptedMessage = ceasar.Encrypt(messageValue[1], -(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId()));
                                    emailData.setBody(decryptedMessage);
                                }
                            }
                        }
                        emailDataList.add(emailData);
                    }

                    // Gửi danh sách dữ liệu cho client
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(emailDataList);

                    // Đóng kết nối và tài nguyên
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            } else if ("thurac".equals(requestType)) {
                try {
                    // Kết nối đến cơ sở dữ liệu
                    Connection connection = DatabaseManager.connect();

                    // Tạo câu lệnh truy vấn
                    String query = "select e.timestamp,e.email_id, u.username, e.subject, e.body from Emails e join Recipients r on e.email_id = r.email_id join Account u on u.user_id = e.sender_id join EmailTags t on t.email_id = e.email_id where r.user_id = ? and t.tag_id =2 and e.email_id Not in (Select email_id from EmailTags where tag_id = 3 group by email_id) order by e.timestamp desc";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    // Đặt giá trị cho tham số
                    preparedStatement.setInt(1, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId());

                    // Thực hiện truy vấn
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Tạo danh sách để lưu trữ dữ liệu
                    List<EmailData> emailDataList = new ArrayList<>();

                    while (resultSet.next()) {
                        EmailData emailData = new EmailData();
                        emailData.setId(resultSet.getInt("email_id"));
                        emailData.setTimestamp(resultSet.getTimestamp("timestamp"));
                        emailData.setUsername(resultSet.getString("username"));
                        emailData.setSubject(resultSet.getString("subject"));
                        String ciphertextString = resultSet.getString("body");

                        String[] messageParts = ciphertextString.split("@");
                        for (String part : messageParts) {
                            if (part.contains(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId() + ":")) {
                                String[] messageValue = part.split(":");
                                if (messageValue.length == 2) {
                                    String decryptedMessage = ceasar.Encrypt(messageValue[1], -(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId()));
                                    emailData.setBody(decryptedMessage);
                                }
                            }
                        }
                        //emailData.setBody(resultSet.getString("body"));
                        emailDataList.add(emailData);
                    }

                    // Gửi danh sách dữ liệu cho client
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(emailDataList);

                    // Đóng kết nối và tài nguyên
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            } else if ("laycc".equals(requestType)) {
                try {
                    int IP = inputStream.readInt();
                    // Kết nối đến cơ sở dữ liệu
                    Connection connection = DatabaseManager.connect();

                    // Tạo câu lệnh truy vấn
                    String query = "SELECT distinct u.username, e.file_name FROM Recipients r JOIN Account u ON r.user_id = u.user_id join EmailAttachments a ON a.email_id = r.email_id,Attachment e where (r.email_id = ? and e.email_attachment_id = A.email_attachment_id)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    // Đặt giá trị cho tham số
                    preparedStatement.setInt(1, IP);

                    // Thực hiện truy vấn
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Tạo danh sách để lưu trữ dữ liệu
                    List<EmailData> emailDataList = new ArrayList<>();

                    while (resultSet.next()) {

                        EmailData emailData = new EmailData();
                        emailData.setBody(resultSet.getString("username"));
                        emailData.setAttachmentFileName(resultSet.getString("file_name"));
                        emailDataList.add(emailData);
                    }

                    // Gửi danh sách dữ liệu cho client
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(emailDataList);

                    // Đóng kết nối và tài nguyên
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();

                    if (!emailDataList.isEmpty() && emailDataList.get(0).getAttachmentFileName() != null) {
                        String attachmentFile = emailDataList.get(0).getAttachmentFileName();
                        File file = new File(attachmentFile);
                        if (file.exists()) {
                            outputStream.writeUTF("attachment"); // Indicate that an attachment is being sent
                            outputStream.writeUTF(file.getName()); // Send the file name to the client
                            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                // Read the file and write it to the output stream
                                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                            }
                        } else {
                            outputStream.writeUTF("no_attachment");
                        }
                    } else {
                        outputStream.writeUTF("no_attachment");
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }

            } else if ("delete".equals(requestType)) {
                int IP = inputStream.readInt();
                CallableStatement callableStatement = null;
                Connection connection = connect();
                String procedureCall = "{call sp_DeleteEmail(?, ?)}";
                callableStatement = connection.prepareCall(procedureCall);
                // Đặt các tham số cho Procedure (nếu có)
                callableStatement.setInt(1, IP);
                callableStatement.setInt(2, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId());

                int resulset = callableStatement.executeUpdate();
                if (resulset > 0) {
                    outputStream.writeUTF("Thành Công");
                } else {
                    outputStream.writeUTF("Thất Bại");
                }
            } else if ("thungrac".equals(requestType)) {
                try {
                    // Kết nối đến cơ sở dữ liệu
                    Connection connection = DatabaseManager.connect();

                    // Tạo câu lệnh truy vấn
                    String query = "SELECT e.timestamp,e.email_id,u.username,  e.subject, e.body\n"
                            + "                           	FROM Recipients r\n"
                            + "                           INNER JOIN Account u ON r.user_id = u.user_id\n"
                            + "                           inner join Emails e ON e.email_id = r.email_id\n"
                            + "                           WHERE (u.user_id = ? and r.is_delete = 1) or (e.sender_id = ? and r.email_id IN (SELECT e.email_id FROM Emails e WHERE e.email_id in (Select email_id from EmailTags where tag_id = 3 group by email_id)))order by e.timestamp desc";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    // Đặt giá trị cho tham số
                    preparedStatement.setInt(1, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId());
                    preparedStatement.setInt(2, UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId());

                    // Thực hiện truy vấn
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Tạo danh sách để lưu trữ dữ liệu
                    List<EmailData> emailDataList = new ArrayList<>();

                    while (resultSet.next()) {
                        EmailData emailData = new EmailData();
                        emailData.setId(resultSet.getInt("email_id"));
                        emailData.setTimestamp(resultSet.getTimestamp("timestamp"));
                        emailData.setUsername(resultSet.getString("username"));
                        emailData.setSubject(resultSet.getString("subject"));
                        String ciphertextString = resultSet.getString("body");

                        String[] messageParts = ciphertextString.split("@");
                        for (String part : messageParts) {
                            if (part.contains(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId() + ":")) {
                                String[] messageValue = part.split(":");
                                if (messageValue.length == 2) {
                                    String decryptedMessage = ceasar.Encrypt(messageValue[1], -(UserManager.getUserByIp(clientSocket.getInetAddress().getHostAddress()).getUserId()));
                                    emailData.setBody(decryptedMessage);
                                }
                            }
                        }
                        emailDataList.add(emailData);
                    }

                    // Gửi danh sách dữ liệu cho client
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(emailDataList);

                    // Đóng kết nối và tài nguyên
                    resultSet.close();

                    preparedStatement.close();
                    connection.close();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            } else if ("logout".equals(requestType)) {
                try {
                    int id = 0;
                    String email = inputStream.readUTF();
                    String time = inputStream.readUTF();
                    Connection connection = DatabaseManager.connect();
                    String query = "select user_id from Account where username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, email);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        // Lấy dữ liệu từ các cột của bản ghi
                        id = resultSet.getInt("user_id");
                    }

                    String query1 = "INSERT INTO InsertLog (user_id, username, time_out) VALUES (?, ?, ?)";
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                    preparedStatement1.setInt(1, id); // user_id
                    preparedStatement1.setString(2, email); // username
                    preparedStatement1.setTimestamp(3, timestamp); // time_out

                    int rowsInserted = preparedStatement1.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Thêm bản ghi vào bảng InsertLog thành công.");
                        sendServerStatus("Thêm bản ghi vào bảng InsertLog đăng xuất thành công.");
                    } else {
                        System.out.println("Thêm bản ghi vào bảng InsertLog đăng xuất không thành công.");
                        sendServerStatus("Thêm bản ghi vào bảng InsertLog đăng xuất không thành công.");
                    }

                    System.out.println("Client : " + email + " đã ngắt kết nối vào lúc " + time);
                    sendServerStatus("Client : " + email + " đã ngắt kết nối vào lúc " + time);
                } catch (Exception e) {
                }
            }

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
