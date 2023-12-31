/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import static ClientMail.frmMailClient.re;
import Model.EmailData;
import Model.LogData;
import static Server.DatabaseManager.connect;
import static Server.MailServer.sendServerStatus;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class frmServer extends javax.swing.JFrame {

    /**
     * Creates new form frmServer
     */
    public frmServer() {
        initComponents();
    }

    public void updateServerStatus(String message) {
        txtThongbao.append(message + "\n");
    }

    private DefaultTableModel loaddata() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel();

        try {
            Connection connection = DatabaseManager.connect();
            String query = "SELECT * FROM InsertLog";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Tạo cột cho tableModel bằng cơ sở dữ liệu
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
            }
            jTable1.setModel(tableModel);
            // Lặp qua kết quả truy vấn và thêm dữ liệu vào tableModel
            while (resultSet.next()) {
                Object[] rowData = new Object[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                tableModel.addRow(rowData);
            }

            // Đóng tài nguyên
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(frmServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableModel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtThongbao = new javax.swing.JTextArea();
        btnBan = new javax.swing.JButton();
        btnunban = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setText("MANAGE EMAIL INFORMATION");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "email_id", "Username", "TimeIn", "Timeout"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        txtThongbao.setColumns(20);
        txtThongbao.setRows(5);
        jScrollPane2.setViewportView(txtThongbao);

        btnBan.setText("Ban");
        btnBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanActionPerformed(evt);
            }
        });

        btnunban.setText("Unban");
        btnunban.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnunbanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnBan, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnunban)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(180, 180, 180))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBan)
                    .addComponent(btnunban))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        try {
            btnBan.setVisible(false);
            btnunban.setVisible(false);
            loaddata();
        } catch (IOException ex) {
            Logger.getLogger(frmServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowOpened

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            Connection connection = DatabaseManager.connect();
            int selectedRow = jTable1.getSelectedRow();
            int usernameColumnIndex = 1; // Chỉ mục cột chứa username

            if (selectedRow >= 0) {
                String username = (String) jTable1.getValueAt(selectedRow, usernameColumnIndex);
                String query = "select is_ban from Account where username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet1 = preparedStatement.executeQuery();

                boolean isBanned = false; // Mặc định người dùng không bị cấm
                if (resultSet1.next()) {
                    isBanned = resultSet1.getBoolean("is_ban");
                }

                if (isBanned) {
                    btnunban.setVisible(true);
                    btnBan.setVisible(false);

                } else {
                    btnBan.setVisible(true);
                    btnunban.setVisible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanActionPerformed
        try {
            Connection connection = DatabaseManager.connect();
            int selectedRow = jTable1.getSelectedRow();
            int usernameColumnIndex = 1; // Chỉ mục cột chứa username

            if (selectedRow >= 0) {
                String username = (String) jTable1.getValueAt(selectedRow, usernameColumnIndex);
                int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn ban?", "Xác nhận ban", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String query = "UPDATE Account\n"
                            + "SET is_ban = 1\n"
                            + "WHERE username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        // Cập nhật thành công
                        String message = "Cấm thành công user: " + username;
                        JOptionPane.showMessageDialog(this, message); // Sử dụng showMessageDialog để hiển thị thông báo
                        txtThongbao.append(message + "\n");
                    }
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnBanActionPerformed

    private void btnunbanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnunbanActionPerformed
        try {
            Connection connection = DatabaseManager.connect();
            int selectedRow = jTable1.getSelectedRow();
            int usernameColumnIndex = 1; // Chỉ mục cột chứa username

            if (selectedRow >= 0) {
                String username = (String) jTable1.getValueAt(selectedRow, usernameColumnIndex);
                int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn gỡ cấm?", "Xác nhận gỡ cấm", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String query = "UPDATE Account\n"
                            + "SET is_ban = 0\n"
                            + "WHERE username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        // Cập nhật thành công
                        String message = "Gỡ cấm thành công user: " + username;
                        JOptionPane.showMessageDialog(this, message); // Sử dụng showMessageDialog để hiển thị thông báo
                        txtThongbao.append(message + "\n");
                    }

                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnunbanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBan;
    private javax.swing.JButton btnunban;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtThongbao;
    // End of variables declaration//GEN-END:variables
}
