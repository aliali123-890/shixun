package org.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.entity.Clothing;
import org.example.mapper.ClothingMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class leibie extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> comboBox;
    private JTable table;
    private final Map<String, String> categoryMap = new HashMap<>();
    private SqlSessionFactory sqlSessionFactory; // MyBatis会话工厂

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                leibie frame = new leibie();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "程序启动失败: " + e.getMessage());
            }
        });
    }

    public leibie() {
        initializeMyBatis(); // 初始化MyBatis
        initializeCategoryMapping();
        initializeUI();
    }

    // 初始化MyBatis配置
    private void initializeMyBatis() {
        try {
            // 加载MyBatis配置文件
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "MyBatis初始化失败: " + e.getMessage());
            System.exit(1); // 初始化失败退出程序
        }
    }

    private void initializeCategoryMapping() {
        // 建立分类与型号前缀的映射关系
        categoryMap.put("裤子", "A");
        categoryMap.put("裙子", "B");
        categoryMap.put("短袖", "C");
        categoryMap.put("卫衣", "D");
        categoryMap.put("背心", "E");
        categoryMap.put("工装裤", "F");
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 867, 893);
        setTitle("服装类别管理系统");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // 顶部面板
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        JLabel label = new JLabel("选择类别：");
        label.setFont(new Font("宋体", Font.PLAIN, 24));
        panel.add(label);

        comboBox = new JComboBox<>();
        comboBox.setFont(new Font("宋体", Font.PLAIN, 24));
        comboBox.setModel(new DefaultComboBoxModel<>(
                new String[] {"裤子", "裙子", "短袖", "卫衣", "背心", "工装裤"}));
        panel.add(comboBox);

        JButton button = new JButton("刷新");
        button.setFont(new Font("宋体", Font.PLAIN, 27));
        button.addActionListener(this::refreshAction);
        panel.add(button);

        // 表格区域
        table = new JTable();
        table.setFont(new Font("宋体", Font.PLAIN, 19));
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void refreshAction(ActionEvent e) {
        String selectedCategory = (String) comboBox.getSelectedItem();
        if (selectedCategory == null) return;

        String typePrefix = categoryMap.get(selectedCategory);
        if (typePrefix == null) {
            JOptionPane.showMessageDialog(this, "未找到对应分类编码");
            return;
        }

        // 使用try-with-resources确保SqlSession正确关闭
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ClothingMapper clothingMapper = sqlSession.getMapper(ClothingMapper.class);
            List<Clothing> clothingList = clothingMapper.selectByTypePrefix(typePrefix);

            // 创建表格模型
            DefaultTableModel model = new DefaultTableModel();

            // 添加列头
            model.addColumn("ID");
            model.addColumn("型号");
            model.addColumn("大小");
            model.addColumn("颜色");
            model.addColumn("数量");
            // 添加数据行
            for (Clothing clothing : clothingList) {
                Vector<Object> row = new Vector<>();
                row.add(clothing.getId());
                row.add(clothing.get型号());
                row.add(clothing.get大小());
                row.add(clothing.get颜色());
                row.add(clothing.get数量());
                model.addRow(row);
            }

            table.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据查询失败: " + ex.getMessage());
        }
    }
}