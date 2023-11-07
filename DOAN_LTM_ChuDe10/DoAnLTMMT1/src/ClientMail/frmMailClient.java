/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientMail;

import ClientMail.frmSend;
import Model.EmailData;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalTime;

/**
 *
 * @author Khanh
 */
public class frmMailClient extends javax.swing.JFrame {

    /**
     * Creates new form frmMailClient
     */
    public frmMailClient() {
        initComponents();
        setExtendedState(this.MAXIMIZED_BOTH);
        setTitle("Email");
    }
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static String nguoinhan;
    public static String re;
    List<EmailData> Data;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablethu = new javax.swing.JTable();
        txtNguoigui = new javax.swing.JTextField();
        txtTieude = new javax.swing.JTextField();
        btnTraloi = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jlablenguoigui = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSoanthu2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnHopThuDen = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnThudagui = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnThurac = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnThungrac = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnDangxuat = new javax.swing.JButton();
        txtNguoinhan = new javax.swing.JTextField();
        jlabenguoinhan = new javax.swing.JLabel();
        btnXoa = new javax.swing.JButton();
        txtduongdan = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        txtNoidung = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 204, 204));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("EMAIL");

        tablethu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tablethu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Time", "Sender", "Title", "Content"
            }
        ));
        tablethu.setUpdateSelectionOnSort(false);
        tablethu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablethuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablethu);

        txtNguoigui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNguoiguiActionPerformed(evt);
            }
        });

        btnTraloi.setText("Reply");
        btnTraloi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraloiActionPerformed(evt);
            }
        });

        jLabel2.setText("Title:");
        jLabel2.setEnabled(false);

        jlablenguoigui.setText("Sender:");
        jlablenguoigui.setEnabled(false);

        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);

        btnSoanthu2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSoanthu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientMail/but.png"))); // NOI18N
        btnSoanthu2.setText("Send email");
        btnSoanthu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSoanthu2ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSoanthu2);
        jToolBar1.add(jSeparator1);

        btnHopThuDen.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHopThuDen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientMail/nhan.png"))); // NOI18N
        btnHopThuDen.setText("Inbox");
        btnHopThuDen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHopThuDenActionPerformed(evt);
            }
        });
        jToolBar1.add(btnHopThuDen);
        jToolBar1.add(jSeparator5);

        btnThudagui.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThudagui.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientMail/send.png"))); // NOI18N
        btnThudagui.setText("Sent");
        btnThudagui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThudaguiActionPerformed(evt);
            }
        });
        jToolBar1.add(btnThudagui);
        jToolBar1.add(jSeparator3);

        btnThurac.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThurac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientMail/spam.png"))); // NOI18N
        btnThurac.setText("Spam");
        btnThurac.setActionCommand("");
        btnThurac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThuracActionPerformed(evt);
            }
        });
        jToolBar1.add(btnThurac);
        jToolBar1.add(jSeparator4);

        btnThungrac.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThungrac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientMail/xoa.png"))); // NOI18N
        btnThungrac.setText("Bin");
        btnThungrac.setActionCommand("");
        btnThungrac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThungracActionPerformed(evt);
            }
        });
        jToolBar1.add(btnThungrac);
        jToolBar1.add(jSeparator2);

        btnDangxuat.setBackground(new java.awt.Color(255, 255, 255));
        btnDangxuat.setText("Logout");
        btnDangxuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangxuatActionPerformed(evt);
            }
        });

        jlabenguoinhan.setText("Recipient:");
        jlabenguoinhan.setEnabled(false);

        btnXoa.setText("Delete");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClientMail/Image/icons8-email-50.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDangxuat)
                        .addGap(32, 32, 32)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTraloi, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoidung)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jlabenguoinhan)
                                    .addComponent(jlablenguoigui, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNguoinhan, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(txtTieude)
                                    .addComponent(txtNguoigui)))
                            .addComponent(txtduongdan))))
                .addGap(98, 98, 98))
            .addGroup(layout.createSequentialGroup()
                .addGap(341, 341, 341)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNguoinhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlabenguoinhan)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNguoigui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlablenguoigui))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTieude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNoidung, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtduongdan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTraloi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDangxuat, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDangxuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangxuatActionPerformed
        try {

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            LocalTime currentTime = LocalTime.now();
            String time = currentTime.toString();
            outputStream.writeUTF("logout");
            outputStream.writeUTF(frmHome.EM);
            outputStream.writeUTF(time);
            outputStream.close();
            this.setVisible(false);
            frmHome m = new frmHome();
            m.setVisible(true);
        } catch (Exception e) {
        }

    }//GEN-LAST:event_btnDangxuatActionPerformed

    private void clean() {
        txtNguoigui.setText("");
        txtNguoinhan.setText("");
        txtNoidung.setText("");
        txtTieude.setText("");
    }

    private void loadRead(boolean a){
        jlablenguoigui.setVisible(a);
        txtNguoigui.setVisible(a);
        jLabel2.setVisible(a);
        txtTieude.setVisible(a);
        jlabenguoinhan.setVisible(a);
        txtNguoinhan.setVisible(a);
        txtNoidung.setVisible(a);
        txtduongdan.setVisible(a);
        btnTraloi.show(a);
        btnCancel.setVisible(a);
    }
    private void loaddata(List<EmailData> Data, String a) throws IOException {
        try {
            btnTraloi.setVisible(false);
            btnXoa.setVisible(false);
            DefaultTableModel tableModel = new DefaultTableModel();

            String[] columnNames = {"Thời gian", a, "Tiêu đề", "Nội dung"};
            for (int i = 0; i < 4; i++) { //chỉ load lên 4 cột thời gian, người gửi, tiêu đề, nội dung
                tableModel.addColumn(columnNames[i]);
            }
            tablethu.setModel(tableModel);

            for (EmailData data : Data) {
                Object[] rowData = {
                    data.getTimestamp(),
                    data.getUsername(),
                    data.getSubject(),
                    data.getBody()
                };
                tableModel.addRow(rowData);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //mặc định khi load form sẽ hiển thị hộp thư đến
        try {
            clean();
            loadRead(false);
            re = "";
            nguoinhan = "";

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            outputStream.writeUTF("hopthuden");
            outputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Data = (List<EmailData>) objectInputStream.readObject();

            loaddata(Data, "Người gửi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_formWindowOpened

    private void btnTraloiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraloiActionPerformed
        frmSend frmsend = new frmSend();
        frmsend.setVisible(true);
        nguoinhan = txtNguoigui.getText();
        this.setVisible(false);
    }//GEN-LAST:event_btnTraloiActionPerformed

    private void btnSoanthu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSoanthu2ActionPerformed
        frmSend f = new frmSend();
        this.setVisible(false);
        nguoinhan = "";
        re = "";
        f.setVisible(true);
    }//GEN-LAST:event_btnSoanthu2ActionPerformed

    private void btnThudaguiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThudaguiActionPerformed
        try {
            clean();

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            outputStream.writeUTF("thudagui");
            outputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Data = (List<EmailData>) objectInputStream.readObject();

            loaddata(Data, "Người nhận");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThudaguiActionPerformed

    private void btnHopThuDenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHopThuDenActionPerformed
        try {
            clean();

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            outputStream.writeUTF("hopthuden");
            outputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Data = (List<EmailData>) objectInputStream.readObject();

            loaddata(Data, "Người gửi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnHopThuDenActionPerformed

    private void tablethuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablethuMouseClicked
        try {
            loadRead(true);

            System.out.println(Data.get(tablethu.getSelectedRow()).getId());
            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            int ID = Data.get(tablethu.getSelectedRow()).getId();
            outputStream.writeUTF("laycc");
            outputStream.writeInt(ID);

            outputStream.flush();

            String listNguoiNhan = "";
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            List<EmailData> emailDataList2 = (List<EmailData>) objectInputStream.readObject();

            for (EmailData emailData : emailDataList2) {

                listNguoiNhan += ", " + emailData.getBody();
            }
            txtNguoinhan.setText(listNguoiNhan.replaceFirst(", ", ""));
            String savePath = "";
            String messageType = inputStream.readUTF();
            if ("attachment".equals(messageType)) {
                // Nếu là tin nhắn có tệp đính kèm, nhận và lưu tệp
                String fileName = inputStream.readUTF();

                // Lưu tệp đính kèm
                savePath = "src\\FileServer\\" + fileName;

                try (FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Attachment saved at: " + savePath);
                }
            } else if ("no_attachment".equals(messageType)) {
                System.out.println("Mail này không có file");
            }

            DefaultTableModel tableModel = (DefaultTableModel) tablethu.getModel();
            int row = tablethu.rowAtPoint(evt.getPoint());

            Object[] data = new Object[4];

            for (int i = 0; i < 4; i++) {
                data[i] = tableModel.getValueAt(row, i);
            }

            re = "re:" + data[2].toString();

            txtNguoigui.setText(data[1].toString());
            txtTieude.setText(data[2].toString());
            txtNoidung.setText(data[3].toString());
            txtduongdan.setText(savePath);
            if (tableModel.getColumnName(1) == "Người nhận") {
                jlablenguoigui.setText("Người nhận");
                jlabenguoinhan.setVisible(false);
                txtNguoinhan.setVisible(false);
                btnTraloi.setText("Gửi tiếp");
            } else {
                btnTraloi.setText("Trả lời");
                jlabenguoinhan.setVisible(true);
                txtNguoinhan.setVisible(true);
                jlablenguoigui.setText("Người gửi");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tablethuMouseClicked

    private void btnThuracActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThuracActionPerformed
        try {
            clean();

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            outputStream.writeUTF("thurac");
            outputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Data = (List<EmailData>) objectInputStream.readObject();

            loaddata(Data, "Người gửi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThuracActionPerformed

    private void btnThungracActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThungracActionPerformed
        try {
            clean();

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            outputStream.writeUTF("thungrac");
            outputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Data = (List<EmailData>) objectInputStream.readObject();

            loaddata(Data, "Người nhận");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThungracActionPerformed

    private void txtNguoiguiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNguoiguiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNguoiguiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        try {

            System.out.println(Data.get(tablethu.getSelectedRow()).getId());
            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            int ID = Data.get(tablethu.getSelectedRow()).getId();
            System.out.println(Data.get(tablethu.getSelectedRow()).getId());

            int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                try {
                    // Thực hiện xóa ở đây
                    outputStream.writeUTF("delete");
                    outputStream.writeInt(ID);

                    outputStream.flush();

                    //nhận kết quả từ server                      
                    String response = inputStream.readUTF();
                    JOptionPane.showMessageDialog(this, response);

                    clean();
                } catch (IOException ex) {
                    Logger.getLogger(frmMailClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e) {
        }

    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        loadRead(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try {

            Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            LocalTime currentTime = LocalTime.now();
            String time = currentTime.toString();
            outputStream.writeUTF("logout");
            outputStream.writeUTF(frmHome.EM);
            outputStream.writeUTF(time);
            outputStream.close();
            this.setVisible(false);
            frmHome m = new frmHome();
            m.setVisible(true);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(frmMailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMailClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDangxuat;
    private javax.swing.JButton btnHopThuDen;
    private javax.swing.JButton btnSoanthu2;
    private javax.swing.JButton btnThudagui;
    private javax.swing.JButton btnThungrac;
    private javax.swing.JButton btnThurac;
    private javax.swing.JButton btnTraloi;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel jlabenguoinhan;
    private javax.swing.JLabel jlablenguoigui;
    private javax.swing.JTable tablethu;
    private javax.swing.JTextField txtNguoigui;
    private javax.swing.JTextField txtNguoinhan;
    private javax.swing.JTextField txtNoidung;
    private javax.swing.JTextField txtTieude;
    private javax.swing.JTextField txtduongdan;
    // End of variables declaration//GEN-END:variables
}
