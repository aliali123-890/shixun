package org.example;

import org.example.ManufacturerUI;
import org.example.leibie;
import org.example.neibuyemian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class guanliyemian extends JFrame {

	private JPanel contentPane;
	private JDesktopPane table = null;

	// 客服聊天相关变量
	private ServerSocket serverSocket;
	private boolean isServerRunning = false;
	private JFrame chatFrame;
	private JTextArea chatArea;

	// 多客户端支持
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private ConcurrentHashMap<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>();
	private AtomicInteger clientCounter = new AtomicInteger(0);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					guanliyemian frame = new guanliyemian();
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
	public guanliyemian() {
		setTitle("服装管理系统主页面");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("服装数据维护");
		menu.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 35));
		menu.setIcon(null);
		menuBar.add(menu);

		JMenu menu_2 = new JMenu("服装类别管理");
		menu_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		menu.add(menu_2);

		JMenuItem menuItem = new JMenuItem("服装类别");
		menuItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leibie frame = new leibie();
				frame.setVisible(true);
			}
		});
		menu_2.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("服装厂商");
		menuItem_1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManufacturerUI frame = new ManufacturerUI();
				frame.setVisible(true);
			}
		});
		menu_2.add(menuItem_1);

		JMenu menu_3 = new JMenu("服装管理");
		menu_3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		menu.add(menu_3);

		JMenuItem menuItem_2 = new JMenuItem("服装维护");
		menuItem_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		menu_3.add(menuItem_2);

		JMenuItem mntmNewMenuItm = new JMenuItem("服装添加");
		mntmNewMenuItm.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		mntmNewMenuItm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				neibuyemian frame = new neibuyemian();
				frame.setVisible(true);
			}
		});
		menu_3.add(mntmNewMenuItm);

		// === 添加客服菜单项 ===
		JMenu serviceMenu = new JMenu("客服中心");
		serviceMenu.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		menu.add(serviceMenu);

		JMenuItem serviceItem = new JMenuItem("客服聊天");
		serviceItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		serviceItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openServiceChat();
			}
		});
		serviceMenu.add(serviceItem);

		JMenuItem mntmNewMenuItem = new JMenuItem("安全退出");
		mntmNewMenuItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "是否退出");
				if (result == 0) {
					// 关闭服务器
					stopServer();
					guanliyemian.this.dispose();
				}
			}
		});
		menu.add(mntmNewMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JDesktopPane table = new JDesktopPane();
		table.setBackground(Color.CYAN);
		contentPane.add(table, BorderLayout.CENTER);
		// 设置最大化
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	// ===== 客服功能实现 =====
	private void openServiceChat() {
		// 主控制面板（只创建一次）
		if (chatFrame == null) {
			createMainControlPanel();
		} else {
			chatFrame.setVisible(true);
			chatFrame.toFront();
		}
	}

	private void createMainControlPanel() {
		chatFrame = new JFrame("客服控制中心");
		chatFrame.setSize(600, 400);
		chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		chatFrame.setLayout(new BorderLayout());

		// 主聊天区域 - 显示系统消息
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setFont(new Font("宋体", Font.PLAIN, 16));
		chatArea.setBackground(new Color(240, 248, 255)); // 淡蓝色背景
		JScrollPane scrollPane = new JScrollPane(chatArea);
		chatFrame.add(scrollPane, BorderLayout.CENTER);

		// 客户端列表面板
		JPanel clientListPanel = new JPanel();
		clientListPanel.setLayout(new BoxLayout(clientListPanel, BoxLayout.Y_AXIS));
		clientListPanel.setBorder(BorderFactory.createTitledBorder("在线客户"));
		JScrollPane listScrollPane = new JScrollPane(clientListPanel);
		listScrollPane.setPreferredSize(new Dimension(200, 0));
		chatFrame.add(listScrollPane, BorderLayout.EAST);

		// 底部控制面板
		JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton startButton = new JButton("启动客服服务");
		startButton.setFont(new Font("宋体", Font.BOLD, 14));
		startButton.setBackground(new Color(144, 238, 144)); // 浅绿色
		startButton.addActionListener(e -> startServer(clientListPanel));
		controlPanel.add(startButton);

		JButton stopButton = new JButton("停止客服服务");
		stopButton.setFont(new Font("宋体", Font.BOLD, 14));
		stopButton.setBackground(new Color(255, 182, 193)); // 浅红色
		stopButton.addActionListener(e -> stopServer());
		controlPanel.add(stopButton);

		chatFrame.add(controlPanel, BorderLayout.SOUTH);

		// 窗口关闭监听
		chatFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopServer();
			}
		});

		appendToChat("客服系统已就绪");
		appendToChat("请点击'启动客服服务'开始接收客户消息");
		chatFrame.setVisible(true);
	}

	private void startServer(JPanel clientListPanel) {
		if (isServerRunning) {
			appendToChat("客服服务已在运行中");
			return;
		}

		try {
			// 创建服务器Socket，监听8888端口
			serverSocket = new ServerSocket(8889);
			isServerRunning = true;

			appendToChat("客服服务已启动，监听端口: 8889");
			appendToChat("等待客户连接...");

			// 启动服务器监听线程
			new Thread(() -> {
				try {
					while (isServerRunning && !serverSocket.isClosed()) {
						Socket clientSocket = serverSocket.accept();
						int clientId = clientCounter.incrementAndGet();

						// 创建客户端处理器
						ClientHandler handler = new ClientHandler(clientId, clientSocket, clientListPanel);
						activeClients.put(clientId, handler);
						threadPool.execute(handler);
					}
				} catch (IOException e) {
					if (isServerRunning) {
						appendToChat("服务器错误: " + e.getMessage());
					}
				}
			}).start();
		} catch (IOException e) {
			appendToChat("启动客服服务失败: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void stopServer() {
		if (!isServerRunning) {
			appendToChat("客服服务未运行");
			return;
		}

		try {
			isServerRunning = false;

			// 关闭所有客户端连接
			for (ClientHandler handler : activeClients.values()) {
				handler.closeConnection();
			}
			activeClients.clear();

			// 关闭服务器Socket
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}

			appendToChat("客服服务已停止");
		} catch (IOException e) {
			appendToChat("停止客服服务时出错: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void appendToChat(String message) {
		SwingUtilities.invokeLater(() -> {
			chatArea.append(message + "\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
		});
	}

	// 客户端处理器（每个客户端一个）
	private class ClientHandler implements Runnable {
		private final int clientId;
		private final Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		private JFrame chatWindow;
		private JTextArea chatArea;
		private JPanel clientPanel;
		private JPanel listPanel;

		public ClientHandler(int clientId, Socket socket, JPanel listPanel) {
			this.clientId = clientId;
			this.socket = socket;
			this.listPanel = listPanel;
			createClientPanel();
		}

		private void createClientPanel() {
			SwingUtilities.invokeLater(() -> {
				clientPanel = new JPanel(new BorderLayout());
				clientPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
				clientPanel.setBackground(new Color(230, 230, 250)); // 淡紫色

				JLabel clientLabel = new JLabel("客户 #" + clientId);
				clientLabel.setFont(new Font("宋体", Font.BOLD, 14));
				clientPanel.add(clientLabel, BorderLayout.NORTH);

				JButton openChatBtn = new JButton("打开聊天");
				openChatBtn.setFont(new Font("宋体", Font.PLAIN, 12));
				openChatBtn.addActionListener(e -> openChatWindow());
				clientPanel.add(openChatBtn, BorderLayout.SOUTH);

				listPanel.add(clientPanel);
				listPanel.revalidate();
				listPanel.repaint();

				appendToChat("客户 #" + clientId + " 已连接: " + socket.getInetAddress());
			});
		}

		private void openChatWindow() {
			if (chatWindow != null && chatWindow.isVisible()) {
				chatWindow.toFront();
				return;
			}

			chatWindow = new JFrame("与客户 #" + clientId + " 对话");
			chatWindow.setSize(500, 400);
			chatWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			chatWindow.setLayout(new BorderLayout());

			// 聊天区域
			chatArea = new JTextArea();
			chatArea.setEditable(false);
			chatArea.setFont(new Font("宋体", Font.PLAIN, 14));
			JScrollPane scrollPane = new JScrollPane(chatArea);
			chatWindow.add(scrollPane, BorderLayout.CENTER);

			// 消息发送区域
			JPanel sendPanel = new JPanel(new BorderLayout());
			JTextField messageField = new JTextField();
			JButton sendButton = new JButton("发送");
			sendButton.setFont(new Font("宋体", Font.BOLD, 12));

			sendButton.addActionListener(e -> {
				String message = messageField.getText().trim();
				if (!message.isEmpty()) {
					sendMessage(message);
					appendToChatArea("客服: " + message);
					messageField.setText("");
				}
			});

			// 添加回车键发送功能
			messageField.addActionListener(e -> {
				String message = messageField.getText().trim();
				if (!message.isEmpty()) {
					sendMessage(message);
					appendToChatArea("客服: " + message);
					messageField.setText("");
				}
			});

			sendPanel.add(messageField, BorderLayout.CENTER);
			sendPanel.add(sendButton, BorderLayout.EAST);
			chatWindow.add(sendPanel, BorderLayout.SOUTH);

			// 关闭监听
			chatWindow.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					chatWindow = null; // 释放引用
				}
			});

			chatWindow.setVisible(true);
			appendToChatArea("已连接到客服，请问有什么可以帮您？");
		}

		@Override
		public void run() {
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				out.println("客服已连接，请输入您的问题...");

				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					final String message = inputLine;
					SwingUtilities.invokeLater(() -> {
						appendToChatArea("客户 #" + clientId + ": " + message);
					});
				}
			} catch (IOException e) {
				// 连接关闭是正常情况
			} finally {
				closeConnection();
			}
		}

		private void appendToChatArea(String message) {
			if (chatArea != null) {
				chatArea.append(message + "\n");
				chatArea.setCaretPosition(chatArea.getDocument().getLength());
			}
		}

		public void sendMessage(String message) {
			if (out != null) {
				out.println(message);
			}
		}

		public void closeConnection() {
			try {
				if (out != null) out.close();
				if (in != null) in.close();
				if (socket != null) socket.close();

				activeClients.remove(clientId);

				SwingUtilities.invokeLater(() -> {
					listPanel.remove(clientPanel);
					listPanel.revalidate();
					listPanel.repaint();

					if (chatWindow != null) {
						chatWindow.dispose();
					}

					appendToChat("客户 #" + clientId + " 已断开连接");
				});
			} catch (IOException e) {
				appendToChat("关闭连接时出错: " + e.getMessage());
			}
		}
	}
}