package com.docn.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.docn.qrcode.QRCodeUtil;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField content;

	private JLabel qrCodePic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
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
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(78, 70, 388, 40);
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setText("往里拖图片");
		contentPane.add(textField);

		JLabel lblNewLabel = new JLabel("嵌入图片：");
		lblNewLabel.setBounds(10, 69, 76, 40);
		contentPane.add(lblNewLabel);

		JLabel label = new JLabel("文字内容：");
		label.setBounds(10, 10, 106, 40);
		contentPane.add(label);

		content = new JTextField();
		content.setText("http://ke.do.cn");
		content.setColumns(10);
		content.setBounds(78, 10, 388, 40);

		Document dt = content.getDocument();
		dt.addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				initCodePic();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				initCodePic();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				initCodePic();
			}
		});

		contentPane.add(content);

		qrCodePic = new JLabel("二维码图片",JLabel.CENTER);
		qrCodePic.setBounds(20, 120, 446, 280);
		contentPane.add(qrCodePic);

		drag();
		initCodePic();
	}

	public void drag() {
		new DropTarget(textField, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {

			public void drop(DropTargetDropEvent dtde) {
				try {

					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						List<File> list = (List<File>) (dtde.getTransferable()
								.getTransferData(DataFlavor.javaFileListFlavor));

						String temp = "";
						for (File file : list) {
							temp += file.getAbsolutePath();
							// JOptionPane.showMessageDialog(null, temp);
							dtde.dropComplete(true);
							textField.setText(temp);

							initCodePic();

						}
					}

					else {
						dtde.rejectDrop();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

	}

	private void initCodePic() {

		String contentStr = content.getText();
		String imagePath = textField.getText();

		if (contentStr == null || "".equals(contentStr.trim())) {
			return;
		}
		if (imagePath == null || "往里拖图片".equals(imagePath.trim())) {
			imagePath = "";
		}

		try {
			qrCodePic.setText("");
			BufferedImage image = QRCodeUtil.createImage(contentStr, imagePath, true);
			qrCodePic.setIcon(new ImageIcon(image));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "未知错误,检查拖入的图片"+e.toString());
		}
	}
}
