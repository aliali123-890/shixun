package org.example;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class zhuce extends JFrame {
    // 数据库基础配置
    private static final String DB_BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JRadioButton radioButton;
    private JRadioButton radioButton_1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                zhuce frame = new zhuce();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public zhuce() {
        initializeUI1();
        setupComponents1();
    }
    private void initializeUI1() {
        setDefaultCloseOperation(zhuce.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 693, 453);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 账号标签
        JLabel lblAccount = new JLabel("账号：");
        lblAccount.setFont(new Font("宋体", Font.BOLD, 20));
        lblAccount.setBounds(101, 30, 80, 20);
        contentPane.add(lblAccount);

        // 账号输入框
        textField = new JTextField();
        textField.setBounds(339, 28, 200, 25);
        contentPane.add(textField);

        // 密码标签
        JLabel lblPassword = new JLabel("密码：");
        lblPassword.setFont(new Font("宋体", Font.BOLD, 20));
        lblPassword.setBounds(101, 94, 80, 20);
        contentPane.add(lblPassword);

        // 密码输入框
        textField_1 = new JTextField();
        textField_1.setBounds(339, 92, 200, 25);
        contentPane.add(textField_1);

        // 姓名标签
        JLabel lblName = new JLabel("姓名：");
        lblName.setFont(new Font("宋体", Font.BOLD, 20));
        lblName.setBounds(101, 161, 80, 20);
        contentPane.add(lblName);

        // 姓名输入框
        textField_2 = new JTextField();
        textField_2.setBounds(339, 159, 200, 25);
        contentPane.add(textField_2);

        // 用户单选按钮
        radioButton = new JRadioButton("普通用户");
        radioButton.setFont(new Font("宋体", Font.BOLD, 18));
        radioButton.setBounds(50, 230, 131, 25);
        contentPane.add(radioButton);

        // 管理员单选按钮
        radioButton_1 = new JRadioButton("管理员");
        radioButton_1.setFont(new Font("宋体", Font.BOLD, 18));
        radioButton_1.setBounds(457, 230, 100, 25);
        contentPane.add(radioButton_1);

        // 注册按钮
        JButton btnRegister = new JButton("立即注册");
        btnRegister.setFont(new Font("宋体", Font.PLAIN, 21));
        btnRegister.setBounds(217, 295, 207, 49);
        btnRegister.addActionListener(this::handleRegistration);
        contentPane.add(btnRegister);

    }

    // 设置单选按钮组
    private void setupComponents1() {
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(radioButton);
        roleGroup.add(radioButton_1);
    }

    // 初始化界面
    private void initializeUI11() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 组件初始化代码保持不变...
        // [这里保留原有界面组件初始化代码]
    }

    // 设置单选按钮组
    private void setupComponents() {
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(radioButton);
        roleGroup.add(radioButton_1);
    }

    // 处理注册事件
    private void handleRegistration(ActionEvent e) {
        String zhanghao = textField.getText().trim();
        String mima = textField_1.getText().trim();
        String xingming = textField_2.getText().trim();

        if (!validateInput(zhanghao, mima, xingming)) return;

        try {
            if (radioButton.isSelected()) {
                insertData("zuce", "zuce", zhanghao, mima, xingming);
            } else if (radioButton_1.isSelected()) {
                insertData("guanliyuanzuce", "guanliyuanzuce", zhanghao, mima, xingming);
            }
            JOptionPane.showMessageDialog(this, "注册成功！");
            this.dispose();
        } catch (SQLException ex) {
            handleDatabaseError(ex);
        }
    }

    // 输入验证方法
    private boolean validateInput(String zhanghao, String mima, String xingming) {
        if (zhanghao.isEmpty() || mima.isEmpty() || xingming.isEmpty()) {
            showError("所有字段必须填写！");
            return false;
        }
        if (!radioButton.isSelected() && !radioButton_1.isSelected()) {
            showError("请选择用户类型！");
            return false;
        }
        return true;
    }

    // 数据库插入方法
    private void insertData(String dbName, String tableName,
                            String zhanghao, String mima, String xingming) throws SQLException {
        String url = DB_BASE_URL + dbName + "?useSSL=false&serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO " + tableName + " (zhanghao, mima, xingming) VALUES (?, ?, ?)")) {

            pstmt.setString(1, zhanghao);
            pstmt.setString(2, mima);
            pstmt.setString(3, xingming);

            pstmt.executeUpdate();
        }
    }

    // 错误处理方法
    private void handleDatabaseError(SQLException ex) {
        if (ex.getErrorCode() == 1062) {
            showError("账号已存在！");
        } else {
            showError("数据库错误：" + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}