package com.prealpha.tyrc.server.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTextPane;

import com.prealpha.tyrc.server.Server;
import com.prealpha.tyrc.server.ServerMessageHandler;
import com.prealpha.tyrc.shared.Message;
import com.prealpha.tyrc.shared.Message.Type;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerUI implements ServerMessageHandler{
	
	private DefaultListModel model = new DefaultListModel();
	private ServerUI t = this;

	private Server server;
	
	private JFrame frame;
	private JTextField textField;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI window = new ServerUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.NORTH);
		
		textField = new JTextField();
		splitPane.setLeftComponent(textField);
		splitPane.setDividerLocation(100);
		textField.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server = new Server(Integer.parseInt(textField.getText()),t);
			}
		});
		splitPane.setRightComponent(btnStart);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerLocation(100);
		frame.getContentPane().add(splitPane_1, BorderLayout.CENTER);
		
		JList list = new JList(this.model);
		splitPane_1.setLeftComponent(list);
		
		textPane = new JTextPane();
		splitPane_1.setRightComponent(textPane);
	}

	@Override
	public void dealWithIt(Message m) {
		textPane.setText(textPane.getText()+m.type+" "+m.name+": "+m.message+"\n");
		if(m.type==Type.JOIN){
			this.model.addElement(m.name);
		}
		if(m.type==Type.DISCONNECT){
			this.model.removeElement(m.name);
		}
	}
}
