/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
 *
 * @author Khanh
 */
//tạo ra socket để tái sử dụng
public class SocketManager {   
     private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public SocketManager(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public String sendRequest(String requestType, String email, String password, String time) throws IOException {
        outputStream.writeUTF(requestType);
        outputStream.writeUTF(email);
        outputStream.writeUTF(password);
        outputStream.writeUTF(time);
        outputStream.flush();

        return inputStream.readUTF();
    }

    public void close() throws IOException {
        socket.close();
    }
}
