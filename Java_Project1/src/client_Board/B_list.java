package client_Board;

//네트워킹 완성본..
import java.awt.*;
import java.awt.event.*;
import java.util.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class B_list extends JFrame implements ActionListener, MouseListener{
	
	Bboard_conn bc = new Bboard_conn();
	
	private int totalPost ;
	private int totalPage;
	private Hashtable ht_page = new Hashtable(); // key 값
	/*private ArrayList al_value = new ArrayList(); // 밸류값
*/	private int pno=1;
	private int alno =0;
	
	private String[] jt_cname ={"글번호","글제목","글쓴이","작성일"};
	DefaultTableModel dtm = new DefaultTableModel(null,jt_cname){
		public boolean isCellEditable(int row, int col){
			return false;
		}
	};
	
	private JTable jt = new JTable(dtm) ;
	/*private ArrayList<Bboard_post> al = new ArrayList<Bboard_post>();*/
	
	private JScrollPane s = new JScrollPane(jt);
	private JButton jbt_write = new JButton("글쓰기");
	/*private JButton jbt_search = new JButton("글 검색");*/
	private JButton jbt_refresh = new JButton("새로고침");
	private JButton jbt_pre = new JButton("◀");
	private JButton jbt_next = new JButton("▶");
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
		//1. 글쓰기 버튼 누름
		if(e.getSource()==jbt_write){ 
			Bboard_write write = new Bboard_write("글쓰기",id);
		//2. 새로고침 버튼 누름	
		}else if(e.getSource()==jbt_refresh /*|| 게시판 탭 클릭 되면.*/){	//새로고침 누르면 그 메세지 가지고 게시판 리스트 가져와야해. 
			
			setPage(receive_list());
			setTable(1);
			/*if(al.size()<11){ // 게시글이 10개 이하 일때,
			
			}else{	//게시글이 10개 보다 만을때,
			paging(al);
			set_jtable(1); // 첫번째 페이지만 뭐가 있는지 보여주면 된다. 
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
				System.out.println("몇번째 페이지??     "+e.getActionCommand());
			}
		}
	}
	public ArrayList receive_list(){
		ArrayList al = null;
		String signal = "새로고침";
		try {
			Bboard_obj bo = new Bboard_obj(Bboard_conn.getSOC_OBJ());
			System.out.println("클라이언트가 게시판 글 리스트 받으려고 서버와 연결함");
			bo.objSendSignal(signal);
			 al = bo.receiveList();
			System.out.println("게           시판 al 사이즈 "+al.size());
			Bboard_post bp1 = (Bboard_post)al.get(0);
			System.out.println("클라이언트 게시글 받음 | 	게시판의 1번째 글 : "+bp1.getId()+bp1.getTitle()+bp1.getDate());
		} catch (Exception e1) {
			System.err.println("클라이언트가 게시글 못받음.");
			e1.printStackTrace();
		}
		return al;
	}
	public void eraseTable(){
		//글삭제 해주기
		int temp = jt.getRowCount();
		System.out.println(temp +"      테이블 줄 수");
		for(int i = 0; i<temp; i++){
			dtm.removeRow(0); // 지워질때마다 위로 글이 올라가서 0번째 줄만 지운다. 
		}
	}
	public void setTable(int p){ // int p = page number.
		//글 채워넣기 
		if(!ht_page.containsKey(p)){
			JOptionPane.showMessageDialog(null, "더이상 글이 없습니다.");
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
		System.out.println("게시판 리스트의 사이즈	" + size);
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
				System.out.println("몇번째 페이지 "+tp);
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
		// 텍스트 파일 요청하는애
		String txtInfo = "@"+id+"@"+title+"@"+date+".txt";
		String txt = "";
		try {
			Bboard_obj bo = new Bboard_obj(Bboard_conn.getSOC_OBJ());
			bo.objSendSignal(txtInfo);
			txt = bo.receiveObject();
			System.out.println("클:텍스트 파일받음");
		} catch (Exception e1) {
			System.err.println("클라이언트가 텍스트 파일을 받아야 하는데 왜 안될까?");
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
				
				System.out.println("1.게시판의 글을 눌러서 글보기하려고 ");
				
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
