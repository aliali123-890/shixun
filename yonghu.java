package org.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.entity.yifuku;
import org.example.mapper.yifukumapper;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class yonghu extends JFrame {

	private static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			String resource = "mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "MyBatis初始化失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private JTextField textField;

	// 客服聊天相关变量
	private Socket chatSocket;
	private PrintWriter out;
	private BufferedReader in;
	private JFrame chatFrame;
	private JTextArea chatArea;
	private JTextField messageField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					yonghu frame = new yonghu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public yonghu() {
		setTitle("用户查询页面");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 869, 732);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton button = new JButton("查询衣裤库");
		button.setFont(new Font("宋体", Font.PLAIN, 24));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (SqlSession session = sqlSessionFactory.openSession()) {
					yifukumapper mapper = session.getMapper(yifukumapper.class);
					List<yifuku> yifukuList = mapper.selectAll();

					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.setRowCount(0);

					for (yifuku yifuku : yifukuList) {
						Vector<Object> row = new Vector<>();
						row.add(yifuku.getId());
						row.add(yifuku.get型号());
						row.add(yifuku.get大小());
						row.add(yifuku.get颜色());
						row.add(yifuku.get数量());
						model.addRow(row);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button.setBounds(20, 28, 168, 47);
		contentPane.add(button);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(75, 85, 772, 190);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				yonghu2 frame = new yonghu2();
				frame.setVisible(true);
				int n = table.getSelectedRow();
				if (n >= 0) {
					frame.textField_3.setText(table.getValueAt(n, 0).toString());
					frame.textField_4.setText(table.getValueAt(n, 1).toString());
					frame.textField_5.setText(table.getValueAt(n, 2).toString());
					frame.textField_6.setText(table.getValueAt(n, 3).toString());
					frame.textField_7.setText(table.getValueAt(n, 4).toString());
				}
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
				new Object[][] {
						{null, null, null, null, null},
				},
				new String[] {
						"id", "型号", "大小", "颜色", "数量"
				}
		));

		JLabel lblNewLabel = new JLabel("查询页面");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 21));
		lblNewLabel.setBounds(369, 15, 93, 40);
		contentPane.add(lblNewLabel);

		JLabel label = new JLabel("具体查询");
		label.setFont(new Font("宋体", Font.BOLD, 21));
		label.setBounds(369, 275, 93, 40);
		contentPane.add(label);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(75, 413, 772, 153);
		contentPane.add(scrollPane_1);

		table_1 = new JTable();
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				yonghu2 frame = new yonghu2();
				frame.setVisible(true);
				int n = table_1.getSelectedRow();
				if (n >= 0) {
					frame.textField_3.setText(table_1.getValueAt(n, 0).toString());
					frame.textField_4.setText(table_1.getValueAt(n, 1).toString());
					frame.textField_5.setText(table_1.getValueAt(n, 2).toString());
					frame.textField_6.setText(table_1.getValueAt(n, 3).toString());
					frame.textField_7.setText(table_1.getValueAt(n, 4).toString());
				}
			}
		});
		scrollPane_1.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
				new Object[][] {
						{null, null, null, null, null},
				},
				new String[] {
						"id", "型号", "大小", "颜色", "数量"
				}
		));

		JButton button_1 = new JButton("搜索");
		button_1.setFont(new Font("宋体", Font.PLAIN, 24));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String xinghao = textField.getText().trim();
				if (xinghao.isEmpty()) {
					JOptionPane.showMessageDialog(null, "请输入型号进行搜索", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}

				try (SqlSession session = sqlSessionFactory.openSession()) {
					yifukumapper mapper = session.getMapper(yifukumapper.class);
					List<yifuku> yifukuList = mapper.selectByXinghao(xinghao);

					DefaultTableModel model = (DefaultTableModel) table_1.getModel();
					model.setRowCount(0);

					for (yifuku yifuku : yifukuList) {
						Vector<Object> row = new Vector<>();
						row.add(yifuku.getId());
						row.add(yifuku.get型号());
						row.add(yifuku.get大小());
						row.add(yifuku.get颜色());
						row.add(yifuku.get数量());
						model.addRow(row);
					}

					if (yifukuList.isEmpty()) {
						JOptionPane.showMessageDialog(null, "未找到匹配的型号", "提示", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "搜索失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_1.setBounds(679, 320, 128, 56);
		contentPane.add(button_1);

		JLabel label_1 = new JLabel("选择：");
		label_1.setFont(new Font("宋体", Font.BOLD, 22));
		label_1.setBounds(152, 354, 93, 22);
		contentPane.add(label_1);

		textField = new JTextField();
		textField.setBounds(260, 339, 389, 37);
		contentPane.add(textField);
		textField.setColumns(10);

		// 添加联系客服按钮
		JButton contactButton = new JButton("联系客服");
		contactButton.setFont(new Font("宋体", Font.PLAIN, 24));
		contactButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openChatWindow();
			}
		});
		contactButton.setBounds(680, 28, 168, 47);
		contentPane.add(contactButton);
	}

	// ===== 客服聊天功能方法 =====
	private void openChatWindow() {
		chatFrame = new JFrame("联系客服");
		chatFrame.setSize(500, 400);
		chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		chatFrame.setLayout(new BorderLayout());

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setFont(new Font("宋体", Font.PLAIN, 16));
		JScrollPane scrollPane = new JScrollPane(chatArea);
		chatFrame.add(scrollPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		messageField = new JTextField();
		messageField.setFont(new Font("宋体", Font.PLAIN, 16));
		messageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		bottomPanel.add(messageField, BorderLayout.CENTER);

		JButton sendButton = new JButton("发送");
		sendButton.setFont(new Font("宋体", Font.BOLD, 16));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		bottomPanel.add(sendButton, BorderLayout.EAST);

		chatFrame.add(bottomPanel, BorderLayout.SOUTH);

		// 启动连接线程
		new Thread(new ConnectToServer()).start();

		chatFrame.setVisible(true);
	}

	private void sendMessage() {
		String message = messageField.getText().trim();
		if (!message.isEmpty()) {
			appendToChat("您: " + message);
			if (out != null) {
				out.println(message);
			}
			messageField.setText("");
		}
	}

	private void appendToChat(String message) {
		SwingUtilities.invokeLater(() -> {
			chatArea.append(message + "\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
		});
	}

	// 连接客服服务器的线程
	private class ConnectToServer implements Runnable {
		@Override
		public void run() {
			try {
				// 连接到本地服务器的8888端口
				chatSocket = new Socket("localhost", 8889);
				out = new PrintWriter(chatSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

				appendToChat("系统: 已连接到客服服务");

				// 启动消息接收线程
				new Thread(new ReceiveMessages()).start();
			} catch (ConnectException e) {
				appendToChat("系统: 无法连接到客服，请稍后再试");
			} catch (IOException e) {
				e.printStackTrace();
				appendToChat("系统: 连接出错: " + e.getMessage());
			}
		}
	}

	// 接收服务器消息的线程
	private class ReceiveMessages implements Runnable {
		@Override
		public void run() {
			try {
				String message;
				while ((message = in.readLine()) != null) {
					appendToChat("客服: " + message);
				}
			} catch (SocketException e) {
				appendToChat("系统: 与客服的连接已断开");
			} catch (IOException e) {
				e.printStackTrace();
				appendToChat("系统: 接收消息出错: " + e.getMessage());
			} finally {
				try {
					if (chatSocket != null) chatSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}