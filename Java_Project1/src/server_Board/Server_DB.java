package server_Board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;




public class Server_DB {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private int postnumber;
	private int rn ;
	
	public Server_DB(){
		db_connect();
		if(getRowNum()==0){
			postnumber=1;
		}else{
		postnumber = getPostNum()+1;
		}
	}
	
	public void db_connect(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("driver connected");
		} catch (ClassNotFoundException e) {
			System.err.println("cannot find the driver");
		}
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe	";
		String user = "web01";
		String pass = "web01";
		try{
			con= DriverManager.getConnection(url,user,pass);
			System.out.println("oracle db connected");
		}catch(SQLException e){
			System.err.println("oracle db connection failed");
		}
	}
	public int getPostNum(){
		int pn=0;
		String sql ="select * from pnum";
		try {
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				pn=rs.getInt("postnum");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return  pn;
	}
	public int getRowNum(){
		Statement stmt;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery("select * from board_list");
			rs.last();
			rn = rs.getRow();
			rs.beforeFirst();
			System.out.println("row number???? : "+rn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rn;
	}
	public synchronized void db_insert(String title, String id, String date) {
		
		String sql = "insert into board_list values (?,?,?,?)";
		/*board_list_seq.nextval*/
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, postnumber);
			System.out.println(postnumber + "     post number");
			ps.setString(2, title);
			ps.setString(3, id);
			ps.setString(4, date);
			int res = ps.executeUpdate();
			if(res>0){
				System.out.println(id+"���� �Խ��� ���� �ۼ��ϼ˽��ϴ�.");
				// ���̺� �� ����Ʈ �ѹ� ������.
				String sql2="update pnum set postnum = ?";
				try{	// saving number to the db for when restarting the server.
					ps=con.prepareStatement(sql2);
					ps.setInt(1,postnumber	);
					int res2=ps.executeUpdate();
					if(res2>0){
						System.out.println("postnumber�� ���������� ������.");
					}else{
						System.out.println("��������");
					}
				}catch(SQLException e){
					e.printStackTrace();	
				}
				
				postnumber++;
			}else{
				System.out.println(id+"���� �Խñ� ��� ���� �߽��ϴ�.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*ps.setString(1, number);*/
		
	}
/*	public void db_update(String title, String id, String date){
		// �� ��� �ʵ常 ���� �ϴ� ���?
		String sql = "update into board_list values (0,?,?,?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, number);
			ps.setString(1, title);
			ps.setString(2, id);
			ps.setString(3, date);
			int res = ps.executeUpdate();
			if(res>0){
				System.out.println(id+"���� �Խ��� ���� �����ϼ˽��ϴ�.");
			}else{
				System.out.println(id+"���� �Խñ� ���� ���� �߽��ϴ�.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	public void db_delete(String title, String id, String date) {
		// ��ü ���� ���� �ϴ� ���?

		try {
			String sql = "delete from board_list where title =? and id =? and date1 =?";
			ps = con.prepareStatement(sql);
			/*ps.setString(1, number);*/
			ps.setString(1, title);
			ps.setString(2, id);
			ps.setString(3, date);
			int res= ps.executeUpdate();
			if(res>0){
				System.out.println(id+"���� �Խ��� ���� �����ϼ˽��ϴ�.");
				String sql2="update pnum set postnum = ?";
				try{	// saving number to the db for when restarting the server.
					ps=con.prepareStatement(sql2);
					ps.setInt(1,postnumber	);
					int res2=ps.executeUpdate();
					if(res2>0){
						System.out.println("postnumber�� ���������� ������.");
					}else{
						System.out.println("���ҽ���");
					}
				}catch(SQLException e){
					e.printStackTrace();	
				}
				postnumber--;
			}else{
				System.out.println(id+"���� �Խñ� ���� ���� �߽��ϴ�.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList db_view_list(){
		String sql ="select * from board_list order by no asc";
		ArrayList al = new ArrayList();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			System.out.println("rs����?"+rs);
			while(rs.next()){
				int no = rs.getInt(1);
				String title = rs.getString(2);
				String id = rs.getString(3);
				String date = rs.getString(4);
				//al.get(index)
				Bboard_post bp = new Bboard_post(no,title,id,date);
				al.add(bp);
				System.out.println(bp.getNo()+"@"+bp.getTitle()+"@"+bp.getId()+"@"+bp.getDate()+"@");
			}
		} catch (Exception e) {
			System.err.println("�Խ��� ��� �ҷ����� ����");
			e.printStackTrace();
		}
		return al;
	}

}
/*ht.put(no, bp); // �ؽ� ���̺� ��� ���ڵ带 �۹�ȣ�� ����ִ´�. 
	Enumeration enu = ht.keys();
	ht.get(1);
		while(ht.keys().hasMoreElements()){
		Integer key_no = (Integer)enu.nextElement();
		Bboard_post  bp_load = (Bboard_post)ht.get(key_no);
		bp_load.disp();
	}*/
/*	public static void main(String[] args) {
		Server_DB sdb= new Server_DB();
		sdb.db_view_list();
	}
}
*/
