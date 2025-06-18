package org.example;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.apache.ibatis.session.SqlSession;
import org.example.entity.yifuku;
import org.example.entity.yifuku;
import org.example.mapper.yifukumapper;
import org.example.mapper.yifukumapper;
import org.example.util.MyBatisUtil;

public class neibuyemian extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1; // 型号
	private JTextField textField_2; // 大小
	private JTextField textField_3; // 颜色
	private JTextField textField_4; // 数量
	private JTextField textField;   // ID

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					neibuyemian frame = new neibuyemian();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public neibuyemian() {
		setTitle("服装管理");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 476, 527);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("服装管理");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 27));
		lblNewLabel.setBounds(160, 31, 142, 87);
		contentPane.add(lblNewLabel);

		// 入库按钮
		JButton button = new JButton("服装入库");
		button.setFont(new Font("宋体", Font.PLAIN, 15));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClothing();
			}
		});
		button.setBounds(308, 390, 116, 54);
		contentPane.add(button);

		JLabel label_1 = new JLabel("型号：");
		label_1.setFont(new Font("宋体", Font.PLAIN, 15));
		label_1.setBounds(115, 170, 66, 33);
		contentPane.add(label_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(257, 176, 94, 21);
		contentPane.add(textField_1);

		JLabel label_2 = new JLabel("大小：");
		label_2.setFont(new Font("宋体", Font.PLAIN, 15));
		label_2.setBounds(115, 213, 66, 33);
		contentPane.add(label_2);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(257, 219, 94, 21);
		contentPane.add(textField_2);

		JLabel label_3 = new JLabel("颜色：");
		label_3.setFont(new Font("宋体", Font.PLAIN, 15));
		label_3.setBounds(115, 262, 66, 33);
		contentPane.add(label_3);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(257, 268, 94, 21);
		contentPane.add(textField_3);

		JLabel label_4 = new JLabel("数量：");
		label_4.setFont(new Font("宋体", Font.PLAIN, 15));
		label_4.setBounds(115, 311, 66, 33);
		contentPane.add(label_4);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(257, 317, 94, 21);
		contentPane.add(textField_4);

		// 出库按钮
		JButton button_1 = new JButton("服装出库");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteClothing();
			}
		});
		button_1.setFont(new Font("宋体", Font.PLAIN, 15));
		button_1.setBounds(37, 390, 116, 54);
		contentPane.add(button_1);

		JLabel lblId = new JLabel("ID：");
		lblId.setFont(new Font("宋体", Font.PLAIN, 15));
		lblId.setBounds(115, 128, 66, 33);
		contentPane.add(lblId);

		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(257, 134, 94, 21);
		contentPane.add(textField);
	}

	// 添加服装（入库操作）
	private void addClothing() {
		try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
			yifukumapper mapper = sqlSession.getMapper(yifukumapper.class);

			yifuku yifuku = new yifuku();
			yifuku.set型号(textField_1.getText());
			yifuku.set大小(textField_2.getText());
			yifuku.set颜色(textField_3.getText());
			yifuku.set数量(textField_4.getText());

			int result = mapper.insertyifuku(yifuku);

			if (result > 0) {
				JOptionPane.showMessageDialog(this, "入库成功!");
				clearFields();
			} else {
				JOptionPane.showMessageDialog(this, "入库失败!");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "数量必须是数字!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "操作失败: " + e.getMessage());
		}
	}

	// 删除服装（出库操作）
	private void deleteClothing() {
		String idStr = textField.getText();
		if (idStr == null || idStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "请输入ID!");
			return;
		}

		try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
			yifukumapper mapper = sqlSession.getMapper(yifukumapper.class);

			int id = Integer.parseInt(idStr);
			int result = mapper.deleteyifukuById(id);

			if (result > 0) {
				JOptionPane.showMessageDialog(this, "出库成功!");
				textField.setText(""); // 清空ID字段
			} else {
				JOptionPane.showMessageDialog(this, "未找到对应记录!");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "ID必须是数字!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "操作失败: " + e.getMessage());
		}
	}

	// 清空输入字段
	private void clearFields() {
		textField_1.setText("");
		textField_2.setText("");
		textField_3.setText("");
		textField_4.setText("");
	}
}