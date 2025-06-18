package org.example;

import org.example.entity.yifuku;
import org.example.mapper.yifukumapper;
import org.example.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ManufacturerUI extends JFrame {

    private JComboBox<String> manufacturerComboBox;
    private JTable dataTable;
    private final Map<String, String> manufacturerMap = new HashMap<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManufacturerUI frame = new ManufacturerUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManufacturerUI() {
        initializeManufacturerMapping();
        initializeUI();
    }

    private void initializeManufacturerMapping() {
        // 建立厂商与型号后缀的映射关系
        manufacturerMap.put("厂商A", "110");
        manufacturerMap.put("厂商B", "120");
        manufacturerMap.put("厂商C", "130");
        manufacturerMap.put("厂商D", "140");
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("服装厂商管理系统");
        setSize(890, 835);
        getContentPane().setLayout(new BorderLayout());

        // 顶部控制面板
        JPanel controlPanel = new JPanel();
        JLabel label = new JLabel("选择厂商：");
        label.setFont(new Font("宋体", Font.PLAIN, 24));
        controlPanel.add(label);

        manufacturerComboBox = new JComboBox<>();
        manufacturerComboBox.setFont(new Font("宋体", Font.PLAIN, 24));
        manufacturerComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"厂商A", "厂商B", "厂商C", "厂商D"}));
        controlPanel.add(manufacturerComboBox);

        JButton refreshButton = new JButton("刷新数据");
        refreshButton.setFont(new Font("宋体", Font.PLAIN, 27));
        refreshButton.addActionListener(this::loadManufacturerData);
        controlPanel.add(refreshButton);

        getContentPane().add(controlPanel, BorderLayout.NORTH);

        dataTable = new JTable();
        dataTable.setFont(new Font("宋体", Font.PLAIN, 19));
        JScrollPane scrollPane = new JScrollPane(dataTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void loadManufacturerData(ActionEvent event) {
        String selectedManufacturer = (String) manufacturerComboBox.getSelectedItem();
        if (selectedManufacturer == null) return;

        String manufacturerCode = manufacturerMap.get(selectedManufacturer);
        if (manufacturerCode == null) {
            JOptionPane.showMessageDialog(this, "未找到对应厂商编码");
            return;
        }

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            yifukumapper mapper = sqlSession.getMapper(yifukumapper.class);
            List<yifuku> yifukuList = mapper.selectByManufacturerCode(manufacturerCode);

            DefaultTableModel model = new DefaultTableModel();
            // 设置列头
            model.addColumn("ID");
            model.addColumn("型号");
            model.addColumn("大小");
            model.addColumn("颜色");
            model.addColumn("数量");

            // 填充数据
            for (yifuku yifuku : yifukuList) {
                Vector<Object> row = new Vector<>();
                row.add(yifuku.getId());
                row.add(yifuku.get型号());
                row.add(yifuku.get大小());
                row.add(yifuku.get颜色());
                row.add(yifuku.get数量());
                model.addRow(row);
            }

            dataTable.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据加载失败: " + ex.getMessage());
        }
    }
}