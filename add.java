package JDBC第十题;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class add extends JFrame {
	Container ContentPane =getContentPane();
	JTextArea area=new JTextArea();
	JScrollPane js=new JScrollPane(area);
	private JButton submit=new JButton("提交");
	private JButton choose=new JButton("选择图片");
	JPanel ButtonPane=new JPanel();
	String filepath=null;
	add(home h){
		super("图片插入");
		ContentPane.add(js, BorderLayout.CENTER);
		ContentPane.add(ButtonPane, BorderLayout.SOUTH);
		ButtonPane.add(choose);
		ButtonPane.add(submit);
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		submit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton jb=(JButton)e.getSource();
				run();
				h.getList();
				add.this.dispose();
			}
		});
		choose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton jbb=(JButton)e.getSource();
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG & GIF Images", "jpg", "gif");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					filepath = chooser.getSelectedFile().getAbsolutePath();  
					System.out.println(filepath);//获取绝对路径  
				}
			}
		});
	}
	
	public void run(){
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root", "");
			String []lines=area.getText().split("[ \t]");
			String path=filepath;
			FileInputStream in = new FileInputStream(path); 
			String sql="insert into photo"
					+ "(id,name,image) values(?,?,?)";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(lines[0]));
			ps.setString(2,lines[1]);
			ps.setBlob(3, in);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
