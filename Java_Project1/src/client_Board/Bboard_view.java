
package client_Board;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Bboard_view extends JFrame implements ActionListener{
	
	
	/*String id = "songhee"; "songhee1";*/
	/*String id1= "songhee1";*/
	Bboard_file bf= new Bboard_file(Bboard_conn.getSOC_FILE());
	Bboard_obj bo = new Bboard_obj(Bboard_conn.getSOC_OBJ_R());
	Bboard_post view_bp ;
	private JTextArea jta = new JTextArea();
	private JScrollPane jscp = new JScrollPane(jta);
	
	private JPanel jp1 = new JPanel();
	private JPanel jp2 = new JPanel();
	private JPanel jp3 = new JPanel();
	private JPanel jp4 = new JPanel();
	
/*	private JTextField jtf_title = new JTextField();
	private JTextField jtf_id = new JTextField();
	private JTextField jtf_date = new JTextField();*/
	
	private JLabel jlb_title ;
	private JLabel jlb_id ;
	private JLabel jlb_date ;
	
	private JButton jbt_edit =	new JButton("����");
	private JButton jbt_delete = new JButton("����");
	private JButton jbt_cancel = new JButton("���");
	/*private JButton jbt_list =	new JButton("���");*/
/*	private JButton jbt_pre = new JButton("������");
	private JButton jbt_next =	new JButton("������");*/
	private JButton jbt_attchedfile = new JButton("÷������ �ٿ�ε�");
	private String txt ;
	private String extension;
	private String file_size;
	private String id ;
	
	public Bboard_view(String txtRec,Bboard_post bp,String id){
		super("�ۺ���");
		this.id=id;
		view_bp = bp;
		this.init();
		this.start();
		super.setSize(600, 500);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)(screen.getWidth()/2 - super.getWidth()/2);
		int y = (int)(screen.getHeight()/2 - super.getHeight()/2);
		super.setLocation(x, y);
		super.setResizable(false);
		super.setVisible(true);
/*		jtf_title.setText(view_bp.getTitle());
		jtf_id.setText(view_bp.getId());
		jtf_date.setText(view_bp.getDate());*/
		txt= txtRec;
		String[] parts = txt.split("@");
		String ext_size = parts[(parts.length-1)];
		if((ext_size.indexOf("#"))!=-1){
		String[] parts2 = ext_size.split("#");
		extension = parts2[0];
		file_size = parts2[1];
		System.out.println("������� �ͽ��ټ�   :" +extension +file_size );
		}
		jta.setText(txtRec);
	}
	public void init(){
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add("Center",jscp);
		con.add("North",jp1);
		con.add("South", jp2);
		jp1.setLayout(new GridLayout(1,6));
		
		//�޾ƿ� ��Ʈ������ �����ֱ�!!
		jlb_title = new JLabel("���� :   "+view_bp.title);
		jlb_id = new JLabel("�۾��� :   "+view_bp.id);
		jlb_date = new JLabel("�ۼ��� :   "+view_bp.date);
		
	
		// how to change ?? to look better.
		jp1.add(jlb_title);
/*		jp1.add(jtf_title);*/
		jp1.add(jlb_date);
/*		jp1.add(jtf_date);*/
		jp1.add(jlb_id);
/*		jp1.add(jtf_id);*/
		
		
		jp2.setLayout(new GridLayout(1,2));
		
		jp2.add(jp3);
		jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp3.add(jbt_attchedfile);
/*		jp3.add(jbt_pre);
		jp3.add(jbt_next);*/
		
		jp2.add(jp4);
		
		jp4.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jp4.add(jbt_edit);
		jp4.add(jbt_delete);
		jp4.add(jbt_cancel);
		/*jp4.add(jbt_list);*/
	}
	public void start(){
/*		jbt_pre.addActionListener(this);
		jbt_next.addActionListener(this);*/
		jbt_edit.addActionListener(this);
		jbt_delete.addActionListener(this);
		jbt_cancel.addActionListener(this);
		jbt_attchedfile.addActionListener(this);
		/*jbt_list.addActionListener(this);*/
	}
	
	private JFileChooser jfc = new JFileChooser();
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jbt_attchedfile){

			String signal = "#"+view_bp.getId()+"@"+view_bp.getTitle()+"@"+view_bp.getDate()+"@"+extension;
			System.out.println("extension : "+ extension +" file size : "+file_size);
			bf.fileSendSignal(signal);
/*			int res =jfc.showSaveDialog(null);
			File savedfile = jfc.getSelectedFile();
			if(res==0&& savedfile!=null){*/
			bf.receiveFile(extension, file_size);
			/*}*/
		}else if(e.getSource()==jbt_edit){
			if(id.equals(view_bp.getId()) || id.equals("admin")){
				int res = JOptionPane.showConfirmDialog(null, "���� ���� �Ͻðڽ��ϱ�?","����",JOptionPane.YES_NO_OPTION);
				if(res==JOptionPane.YES_OPTION){
					// send the info to the server using object
					
					txt=jta.getText();
					view_bp.setTa(txt);
					view_bp.setNo(7373); // 7373 ����
					bo.sendObject(view_bp);
					System.out.println("view_bp �ۼ������� ������ ����.");
					this.dispose();
				}
			}else{
				JOptionPane.showMessageDialog(null, "������ ��� ���� �� �� �����ϴ�.");
			}
		}else if(e.getSource()==jbt_delete){
			if(id.equals(view_bp.getId()) || id.equals("admin")){
				int res = JOptionPane.showConfirmDialog(null, "���� ���� �Ͻðڽ��ϱ�?","����",JOptionPane.YES_NO_OPTION);
				if(res==JOptionPane.YES_OPTION){
					//send the info to the server using object
					/*post_delete();*/
					view_bp.setNo(3737); //3737 ����
					view_bp.setExtension(extension);
					bo.sendObject(view_bp);
					
					this.dispose();
				}
			}else{
				JOptionPane.showMessageDialog(this, "������ ��� ���� �Ҽ�  �����ϴ�.");
			}
		}else if(e.getSource()==jbt_cancel){
			int res =JOptionPane.showConfirmDialog(null, "���� ��� �Ͻðڽ��ϱ�?","���", JOptionPane.YES_NO_OPTION);
			if(res==JOptionPane.YES_OPTION){
				JOptionPane.showMessageDialog(null, "�ǵ��ư��ϴ�.");
				this.dispose();
			}
		}
	}
	
}

