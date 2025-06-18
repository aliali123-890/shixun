package org.example;

import org.example.guanliyemian;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class zuce extends JFrame {
    // 数据库配置
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = "123456";

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JRadioButton radioButton;
    private JRadioButton radioButton_1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                zuce frame = new zuce();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public zuce() {
        setTitle("\u8863\u670D\u5E93\u7BA1\u7406");
        initComponents();
        setupDatabase();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 账号标签
        JLabel label = new JLabel("账号：");
        label.setFont(new Font("宋体", Font.BOLD, 23));
        label.setBounds(98, 84, 101, 57);
        contentPane.add(label);

        // 账号输入框
        textField = new JTextField();
        textField.setBounds(234, 84, 318, 41);
        contentPane.add(textField);
        textField.setColumns(10);

        // 密码标签
        JLabel label_1 = new JLabel("密码：");
        label_1.setFont(new Font("宋体", Font.BOLD, 23));
        label_1.setBounds(98, 168, 121, 40);
        contentPane.add(label_1);

        // 密码输入框
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(234, 169, 318, 43);
        contentPane.add(textField_1);

        // 登录按钮
        JButton button = new JButton("登录");
        button.setFont(new Font("宋体", Font.PLAIN, 21));
        button.addActionListener(e -> handleLogin());
        button.setBounds(97, 301, 121, 57);
        contentPane.add(button);

        // 注册按钮
        JButton button_1 = new JButton("注册");
        button_1.setFont(new Font("宋体", Font.PLAIN, 21));
        button_1.addActionListener(e -> {
            org.example.zhuce frame = new org.example.zhuce();
            frame.setVisible(true);
        });
        button_1.setBounds(410, 301, 129, 57);
        contentPane.add(button_1);

        // 用户类型单选按钮
        radioButton = new JRadioButton("\u666E\u901A\u7528\u6237");
        radioButton.setFont(new Font("宋体", Font.BOLD, 18));
        radioButton.setBounds(97, 252, 121, 23);
        contentPane.add(radioButton);

        radioButton_1 = new JRadioButton("管理员");
        radioButton_1.setFont(new Font("宋体", Font.BOLD, 18));
        radioButton_1.setBounds(410, 252, 121, 23);
        contentPane.add(radioButton_1);

        // 设置单选按钮组
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton_1);

        JLabel lblNewLabel = new JLabel("\u5EFA\u82F1\u5927\u7AE5");
        lblNewLabel.setFont(new Font("华文隶书", Font.PLAIN, 28));
        lblNewLabel.setBounds(282, 15, 163, 40);
        contentPane.add(lblNewLabel);
    }

    private void setupDatabase() {
        // 创建数据库（如果不存在）
        String[] dbs = {"zuce", "guanliyuanzuce"};
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            for (String db : dbs) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "数据库初始化失败：" + ex.getMessage());
        }
    }

    private void handleLogin() {
        String account = textField.getText().trim();
        String password = textField_1.getText().trim();

        if (account.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "账号密码不能为空");
            return;
        }

        if (!radioButton.isSelected() && !radioButton_1.isSelected()) {
            JOptionPane.showMessageDialog(this, "请选择用户类型");
            return;
        }

        try {
            boolean loginSuccess = false;
            if (radioButton.isSelected()) {
                loginSuccess = validateUser("zuce", "zuce", account, password);
            } else {
                loginSuccess = validateUser("guanliyuanzuce", "guanliyuanzuce", account, password);
            }

            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, "登录成功！");


                if(loginSuccess == validateUser("zuce", "zuce", account, password)){
                   org.example.yonghu yonghu =new org.example.yonghu();
                    yonghu.setLocationRelativeTo(null);
                    org.example.yonghu frame = new org.example.yonghu();
                    frame.setVisible(true);
                }
                if(loginSuccess == validateUser("guanliyuanzuce", "guanliyuanzuce", account, password)){
                    guanliyemian frame = new guanliyemian();
                    frame.setVisible(true);}

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "账号或密码错误");
            }
        } catch (SQLException ex) {
            handleDatabaseError(ex);
        }
    }

    private boolean validateUser(String dbName, String tableName,
                                 String account, String password) throws SQLException {
        String url = DB_URL + dbName + "?useSSL=false&serverTimezone=UTC";
        String sql = "SELECT * FROM " + tableName + " WHERE zhanghao = ? AND mima = ?";

        try (Connection conn = DriverManager.getConnection(url, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void handleDatabaseError(SQLException ex) {
        String errorMsg;
        switch (ex.getErrorCode()) {
            case 1045:
                errorMsg = "数据库连接失败：用户名或密码错误";
                break;
            case 1049:
                errorMsg = "数据库不存在";
                break;
            case 1146:
                errorMsg = "数据表不存在";
                break;
            default:
                errorMsg = "数据库错误：" + ex.getMessage();
        }
        JOptionPane.showMessageDialog(this, errorMsg, "错误", JOptionPane.ERROR_MESSAGE);
    }
}
