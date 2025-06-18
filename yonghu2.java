package org.example;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class yonghu2 extends JFrame {

	private JPanel contentPane;
	JTextField textField_3;
	JTextField textField_4;
	JTextField textField_5;
	JTextField textField_6;
	JTextField textField_7;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					yonghu2 frame = new yonghu2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public yonghu2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 606, 431);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("id\uFF1A");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 15));
		lblNewLabel.setBounds(140, 56, 66, 33);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\u8BE6\u7EC6\u9875\u9762");
		lblNewLabel_1.setFont(new Font("华文隶书", Font.BOLD, 20));
		lblNewLabel_1.setBounds(163, 10, 94, 36);
		contentPane.add(lblNewLabel_1);

		JLabel lblXing = new JLabel("\u578B\u53F7\uFF1A");
		lblXing.setFont(new Font("宋体", Font.PLAIN, 15));
		lblXing.setBounds(140, 115, 66, 33);
		contentPane.add(lblXing);

		JLabel label = new JLabel("\u5927\u5C0F\uFF1A");
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		label.setBounds(140, 176, 66, 33);
		contentPane.add(label);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBounds(0, 0, 590, 392);
		contentPane.add(panel);

		JLabel label_1 = new JLabel("id\uFF1A");
		label_1.setFont(new Font("宋体", Font.PLAIN, 15));
		label_1.setBounds(140, 56, 66, 33);
		panel.add(label_1);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(227, 62, 94, 21);
		panel.add(textField_3);

		JLabel label_2 = new JLabel("\u8BE6\u7EC6\u9875\u9762");
		label_2.setFont(new Font("华文隶书", Font.BOLD, 20));
		label_2.setBounds(163, 10, 94, 36);
		panel.add(label_2);

		JLabel label_3 = new JLabel("\u578B\u53F7\uFF1A");
		label_3.setFont(new Font("宋体", Font.PLAIN, 15));
		label_3.setBounds(140, 115, 66, 33);
		panel.add(label_3);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(227, 121, 94, 21);
		panel.add(textField_4);

		JLabel label_4 = new JLabel("\u5927\u5C0F\uFF1A");
		label_4.setFont(new Font("宋体", Font.PLAIN, 15));
		label_4.setBounds(140, 176, 66, 33);
		panel.add(label_4);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(227, 182, 94, 21);
		panel.add(textField_5);

		JLabel label_5 = new JLabel("\u989C\u8272\uFF1A");
		label_5.setFont(new Font("宋体", Font.PLAIN, 15));
		label_5.setBounds(140, 234, 66, 33);
		panel.add(label_5);

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(227, 240, 94, 21);
		panel.add(textField_6);

		JLabel label_6 = new JLabel("\u6570\u91CF\uFF1A");
		label_6.setFont(new Font("宋体", Font.PLAIN, 15));
		label_6.setBounds(140, 295, 66, 33);
		panel.add(label_6);

		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(227, 301, 94, 21);
		panel.add(textField_7);

		JButton button = new JButton("\u8D2D\u4E70");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "购买成功！");
				yonghu2.this.dispose();
			}
		});
		button.setFont(new Font("宋体", Font.BOLD, 12));
		button.setBounds(455, 349, 110, 33);
		panel.add(button);
	}
}
