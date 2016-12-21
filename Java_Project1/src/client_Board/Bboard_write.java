package client_Board;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import client_Board.Bboard_file;



class Bboard_write extends JFrame implements ActionListener{
	/*String id = "songhee";*/
	
	private JLabel lb_title= new JLabel("���� : ", JLabel.RIGHT);
	private JTextField jtf_title = new JTextField();
	
	private JLabel lb_attach= new JLabel("÷������: ", JLabel.RIGHT);
	private JTextField jtf_attach = new JTextField();
	private JButton jbt_attach = new JButton("÷�� ���� ã��");
	
	private JCheckBox jbt_notice = new JCheckBox("notice");
	private JButton jbt_write =	new JButton("Ȯ��");
	private JButton jbt_cancel = new JButton("���");
	
	//�ѱ۷� �ۼ��� ������ ��ġ�� ���??
	private TextArea ta = new TextArea(); //��� ������ ????
	
	private JPanel jp1 = new JPanel();
	private JPanel jp2 = new JPanel();
	private JPanel jp3 = new JPanel();
	private JPanel jp4 = new JPanel();
	
	private JPanel jp5 = new JPanel();
	
	// title, id, ect
	private String title;
	private String textarea;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
	private String date = sdf.format(new Date());
	
	private String id;
	public Bboard_write(String title,String id){
		super(title);
		this.id=id;
		this.init();
		this.start();
		super.setSize(600, 500);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)(screen.getWidth()/2 - super.getWidth()/2);
		int y = (int)(screen.getHeight()/2 - super.getHeight()/2);
		super.setLocation(x, y);
		super.setResizable(false);
		super.setVisible(true);
	}
	public void init(){
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add("North",	jp1);
		con.add("Center",ta);
		con.add("South",jp3);
		
		jp1.setLayout(new GridLayout(2,1));
		
		jp1.add(jp4);
		jp4.setLayout(new BorderLayout());
		jp4.add("West",lb_title);
		jp4.add("Center", jtf_title);
		
		
		jp1.add(jp5);
		jp5.setLayout(new BorderLayout());
		jp5.add("West", lb_attach);
		jp5.add("Center", jtf_attach);
		jp5.add("East", jbt_attach);
		
		
		jp3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jp3.add(jbt_notice);
		// only admin can check the box.
		if(id.equals("admin")){
			jbt_notice.setEnabled(true);
		}else{
			jbt_notice.setEnabled(false);
		}
		
		jp3.add(jbt_write);
		jp3.add(jbt_cancel);
	}
	public void start(){
		jbt_attach.addActionListener(this);
		jbt_write.addActionListener(this);
		jbt_cancel.addActionListener(this);
	}
	
	private JFileChooser jfc = new JFileChooser(".");
	private File attachedFile;
	private String filename ;
	private String filedir;
	private String file_name;
	Bboard_obj bo = new Bboard_obj(Bboard_conn.getSOC_OBJ_R());
	//1.÷������ ������
	public void chooseFile() {
		int res = jfc.showOpenDialog(null);
		attachedFile = jfc.getSelectedFile();
		if(res==0&& attachedFile!=null){
			filename = attachedFile.getName();
			filedir = attachedFile.getPath();
		}else{
			System.out.println("������ ���� �ʾ� â�� �ݽ��ϴ�.");
		}
	}
	private String signal="";
	private String attInfo = "";
	public void actionPerformed(ActionEvent e) {
		//1.÷������
		if(e.getSource()==jbt_attach){
			// import the file class , 
			title = jtf_title.getText();
				//���ϼ���
			chooseFile();
			String file_name = id+"@"+title+"@"+date+"@"+filename+"#"+attachedFile.length();
			/*String file_name1 =id+"@"+title+"@"+date+"@"+filename;*/
			signal = file_name;			
			jtf_attach.setText(file_name);
			//chekced!!
	/*		String[] parts = file_name.split(".");
			String extension = parts[1];
			System.out.println("Ȯ���ڸ�"+extension);*/
			attInfo = "\n\n\n\n\n\n\n\n\n\n\n\n "
					+ "÷������ : "+file_name+"\n";
			
			Bboard_file bf = new Bboard_file(Bboard_conn.getSOC_FILE_R());
			bf.fileSendSignal(signal);// signal����
			bf.sendFile(filedir); 			// ���� ����
			
		//2. �۾��� ��ư ����
		}else if(e.getSource()==jbt_write){
			
			title = jtf_title.getText();
			ta.append(attInfo);
			textarea = ta.getText();
			
			Bboard_post bp = new Bboard_post(title,id,textarea,date);
			bp.disp();

			/*String signal = "Ŭ:���ۼ�";*/
			//������ �������� �ñ׳� �Ⱥ����� �׳� �޾�!
			/*Bboard_str bs = new Bboard_str(Bboard_conn.getSOC_OBJ_R()); */
			/*bs.objSendSignal(signal);	*///�ñ׳� ����
			bo.sendObject(bp);			// �۾��� ����.
			System.out.println(bp.toString());
			JOptionPane.showMessageDialog(this, "���� ���� �Ͽ����ϴ�.");
			//checked!!
			
			/*this.setVisible(false);*/
			this.dispose();
		}else if(e.getSource()==jbt_cancel){
			int res =JOptionPane.showConfirmDialog(this, "��� �Ͻðڽ��ϱ�?","���",JOptionPane.YES_NO_OPTION);
			if(res==0) this.dispose();
		}
	}
/*	
	public static void main(String[] args) {
		new Bboard_write("�۾���");
	}*/
}

