package JDBC第十题;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class home extends JFrame {
	Vector clounmnames = new Vector();
	Vector rowdata = new Vector();
	DefaultTableModel model = new DefaultTableModel();
	Container ContentPane = getContentPane();
	JPanel ButtonPane = new JPanel();
	JTable table = null;
	JButton add = new JButton("图片插入");
	BufferedImage buffimg = null;

	public void getList() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root", "");
			String sql = "select * from photo";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Vector hang = new Vector();
				hang.add(rs.getInt(1));
				hang.add(rs.getString(2));
				// hang.add(ImageIO.read(rs.getBlob(3).getBinaryStream()));
				// hang.add(rs.getBinaryStream("image"));
				model.addRow(hang);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	home() {
		super("图片插入与查询");
		clounmnames.add("编号");
		clounmnames.add("名字");
		// clounmnames.add("图片");
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(400);
		splitPane.setResizeWeight(1);
		JPanel imgPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (buffimg != null) {
					this.setPreferredSize(new Dimension(buffimg.getWidth(), buffimg.getHeight()));
					g.drawImage(buffimg, 0, 0, null);
				}
			}
		};
		splitPane.setRightComponent(new JScrollPane(imgPanel));
		model.setColumnIdentifiers(clounmnames);
		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int row =table.getSelectedRow();
				int id = (int) table.getValueAt(row, 0);
				Connection con;
				try {
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root", "");
					String sql = "select image from photo where id=?";
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setInt(1, id);
					ResultSet rs = ps.executeQuery();
					rs.next();
					buffimg = ImageIO.read(rs.getBlob(1).getBinaryStream());
					imgPanel.repaint();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			}

		});
		// table.getColumnModel().getColumn(2).setCellRenderer(new
		// ImageCellRenderer());
		getList();
		JScrollPane js = new JScrollPane(table);
		splitPane.setLeftComponent(js);
		ContentPane.add(splitPane, BorderLayout.CENTER);
		ContentPane.add(ButtonPane, BorderLayout.SOUTH);
		ButtonPane.add(add);
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);

		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton jb = (JButton) e.getSource();
				new add(home.this);
			}
		});
	}

	public static void main(String[] args) {
		new home();

	}
}

class ImageCellRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon((Image) value));
		return label;
	}
}