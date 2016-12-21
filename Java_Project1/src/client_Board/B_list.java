package client_Board;

//��Ʈ��ŷ �ϼ���..
import java.awt.*;
import java.awt.event.*;
import java.util.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class B_list extends JFrame implements ActionListener, MouseListener{
	
	Bboard_conn bc = new Bboard_conn();
	
	private int totalPost ;
	private int totalPage;
	private Hashtable ht_page = new Hashtable(); // key ��
	/*private ArrayList al_value = new ArrayList(); // �����
*/	private int pno=1;
	private int alno =0;
	
	private String[] jt_cname ={"�۹�ȣ","������","�۾���","�ۼ���"};
	DefaultTableModel dtm = new DefaultTableModel(null,jt_cname){
		public boolean isCellEditable(int row, int col){
			return false;
		}
	};
	
	private JTable jt = new JTable(dtm) ;
	/*private ArrayList<Bboard_post> al = new ArrayList<Bboard_post>();*/
	
	private JScrollPane s = new JScrollPane(jt);
	private JButton jbt_write = new JButton("�۾���");
	/*private JButton jbt_search = new JButton("�� �˻�");*/
	private JButton jbt_refresh = new JButton("���ΰ�ħ");
	private JButton jbt_pre = new JButton("��");
	private JButton jbt_next = new JButton("��");
	private JButton jbt_number[] = new JButton[5];
	private JPanel jp1 = new JPanel();
	private JPanel jp2 = new JPanel();
	private JPanel jp3 = new JPanel();
	private JPanel jp4 = new JPanel();
	private String id ;
	private String signal1 = "";
	
	private int pos=1;
	private ArrayList re_al = new ArrayList();
	
	public B_list(String title, String id){
		super(title);
		this.id=id;
		this.init();
		this.start();
		super.setSize(750, 500);
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
		
		jt.setPreferredSize(new Dimension(500,400));
		jt.setVisible(true);
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		jt.setCellSelectionEnabled(false);
		/*s.setViewportView(jt);*/
		/*con.add("North",ne)*/
		con.add("Center", s);
		con.add("South",jp1);
		
		
		jp1.setLayout(new BorderLayout());
		jp1.add("Center", jp2);
		jp1.add("East", jp3);
		jp3.setLayout(new GridLayout(1,2));
		jp3.add(jbt_write);
		jp3.add(jbt_refresh);
		jp2.setLayout(new BorderLayout());
		
		
		jp2.add("West", jbt_pre);
		jp2.add("East", jbt_next);
		jp2.add("Center", jp4);
		jp4.setLayout(new GridLayout(1,10));
		for(int i = 0; i <5; i++){
			int n= i+1;
			jbt_number[i] = new JButton(String.valueOf(n));
			jbt_number[i].addActionListener(this);
			jp4.add(jbt_number[i]);
		}
	}
	public void start(){
		jbt_write.addActionListener(this);
		jbt_refresh.addActionListener(this);
		jt.addMouseListener(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jbt_pre.addActionListener(this);
		jbt_next.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e){
		//1. �۾��� ��ư ����
		if(e.getSource()==jbt_write){ 
			Bboard_write write = new Bboard_write("�۾���",id);
		//2. ���ΰ�ħ ��ư ����	
		}else if(e.getSource()==jbt_refresh /*|| �Խ��� �� Ŭ�� �Ǹ�.*/){	//���ΰ�ħ ������ �� �޼��� ������ �Խ��� ����Ʈ �����;���. 
			
			setPage(receive_list());
			setTable(1);
			/*if(al.size()<11){ // �Խñ��� 10�� ���� �϶�,
			
			}else{	//�Խñ��� 10�� ���� ������,
			paging(al);
			set_jtable(1); // ù��° �������� ���� �ִ��� �����ָ� �ȴ�. 
			}*/
			
		}else if(e.getSource()==jbt_pre){
			for(int i =0; i<5;i++){
				if(Integer.parseInt(jbt_number[i].getText())>5){
					int n = (Integer.parseInt(jbt_number[i].getText()))-5;
					jbt_number[i].setText(String.valueOf(n));
				}
			}
		}else if(e.getSource()==jbt_next){
			for(int i =0; i<5;i++){
				int n = (Integer.parseInt(jbt_number[i].getText()))+5;
				jbt_number[i].setText(String.valueOf(n));
			}
		}for(int i=0;i<5;i++){
			if(e.getSource()==jbt_number[i]){
				setTable(Integer.parseInt(e.getActionCommand()));
				System.out.println("���° ������??     "+e.getActionCommand());
			}
		}
	}
	public ArrayList receive_list(){
		ArrayList al = null;
		String signal = "���ΰ�ħ";
		try {
			Bboard_obj bo = new Bboard_obj(Bboard_conn.getSOC_OBJ());
			System.out.println("Ŭ���̾�Ʈ�� �Խ��� �� ����Ʈ �������� ������ ������");
			bo.objSendSignal(signal);
			 al = bo.receiveList();
			System.out.println("��           ���� al ������ "+al.size());
			Bboard_post bp1 = (Bboard_post)al.get(0);
			System.out.println("Ŭ���̾�Ʈ �Խñ� ���� | 	�Խ����� 1��° �� : "+bp1.getId()+bp1.getTitle()+bp1.getDate());
		} catch (Exception e1) {
			System.err.println("Ŭ���̾�Ʈ�� �Խñ� ������.");
			e1.printStackTrace();
		}
		return al;
	}
	public void eraseTable(){
		//�ۻ��� ���ֱ�
		int temp = jt.getRowCount();
		System.out.println(temp +"      ���̺� �� ��");
		for(int i = 0; i<temp; i++){
			dtm.removeRow(0); // ������������ ���� ���� �ö󰡼� 0��° �ٸ� �����. 
		}
	}
	public void setTable(int p){ // int p = page number.
		//�� ä���ֱ� 
		if(!ht_page.containsKey(p)){
			JOptionPane.showMessageDialog(null, "���̻� ���� �����ϴ�.");
		}else{
		eraseTable();
			ArrayList<Bboard_post> al_value1 = (ArrayList<Bboard_post>)ht_page.get(p);
				/*for(Bboard_post bpb : al_value1){
					String[] row ={String.valueOf(bpb.getPno()),bpb.getTitle(),bpb.getId(),bpb.getDate()};
					dtm.addRow(row);
			}*/
			for(int i=al_value1.size()-1;i>=0;i--){
				String []input ={String.valueOf(al_value1.get(i).getPno()), al_value1.get(i).getTitle(), 
						al_value1.get(i).getId(), al_value1.get(i).getDate()};
				dtm.addRow(input);	
			}
		}
	}	
	public void setPage(ArrayList al){
		pos=1;
		int size = al.size(); 
		System.out.println("�Խ��� ����Ʈ�� ������	" + size);
		int st = 0; 		int end = 10;
		int tp = (size/end)+1;
		while(true){
			ArrayList al1= new ArrayList();
			if(end<size){}
			else end=size;
			for(int i=st;i<end;i++){
				System.out.println(i+" ");
				Bboard_post bp = (Bboard_post) al.get(i);
				bp.setPno(pos);
				al1.add(bp);
				System.out.println("���° ������ "+tp);
				ht_page.put(tp, al1);
				pos++;
			}
			tp--;
			System.out.println();
			if(end==size)break;
			st=end; end+=10;
		}
	}
	
	public String post_view(String id ,String title, String date){
		// �ؽ�Ʈ ���� ��û�ϴ¾�
		String txtInfo = "@"+id+"@"+title+"@"+date+".txt";
		String txt = "";
		try {
			Bboard_obj bo = new Bboard_obj(Bboard_conn.getSOC_OBJ());
			bo.objSendSignal(txtInfo);
			txt = bo.receiveObject();
			System.out.println("Ŭ:�ؽ�Ʈ ���Ϲ���");
		} catch (Exception e1) {
			System.err.println("Ŭ���̾�Ʈ�� �ؽ�Ʈ ������ �޾ƾ� �ϴµ� �� �ȵɱ�?");
			e1.printStackTrace();
		}
		return txt;
	}
	public void mouseClicked(MouseEvent e) {
		boolean check = (jt.getSelectedColumn()==1);
		if(e.getSource()==jt){
			int row = jt.getSelectedRow();
			int column = jt.getSelectedColumn();
			System.out.println("row "+row+" column "+column);
			if(check){
				String title = (String) jt.getValueAt(row,column);
				String id =(String)jt.getValueAt(row, column+1);
				String date = (String)jt.getValueAt(row, column+2);
				
				Bboard_post jtbp = new Bboard_post(title,id,date);
				
				String txtRec = post_view(id, title,date);
				
				System.out.println("1.�Խ����� ���� ������ �ۺ����Ϸ��� ");
				
				Bboard_view bv = new Bboard_view(txtRec,jtbp,id);
			}
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	


	public static void main(String[] args) {
		B_list list = new B_list("Post List","admin");
	}
}
