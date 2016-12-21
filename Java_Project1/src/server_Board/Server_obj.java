package server_Board;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;



class Server_obj implements Runnable{
	private ServerSocket ss ;
	Bboard_post bp1;
	Server_DB sdb1 = new Server_DB();
	private Socket c_socket;
	private BufferedReader br;
	private ArrayList al;
	private String txtf_name = "";
	private ObjectOutputStream oos;
	
	//요청받고 보내줌
	public Server_obj(int port){ // open server with port # and accepting clients 
		try {
			ss= new ServerSocket(port);
			System.out.println("오브젝트 서버 열림, 대기함..");
			while(true){
				c_socket = ss.accept();
				System.out.println("오브젝트 클라이언트 접속");
				Thread th = new Thread(this); // creating a Thread, every time client enters.
				th.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
/*		try {
			oos = new ObjectOutputStream(c_socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public void run(){
		try {
			 while(true){
				 	System.out.println("서버 오브젝트 사인 메세지 대기중");
					 	br = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
						String signal = br.readLine();
						
					if(signal.equals("새로고침")/*|| 게시판 버튼 눌리면.*/ ){ // sending the whole list.
						System.out.println("클라이언트가 서버에 게시판리스트를 요청했습니다.");
						sendObj();
						
			 		}else if((signal.charAt(0))=='@'){
							// 나도 파일이름 보내서 그걸 찾아야함. 저장 시킨 이름이랑같아야 찾음.
							System.out.println("서버파일에서 받은 텍스트 파일이름"+signal);
							txtf_name = signal;
							sendTxt(txtf_name);
					}else{
						System.out.println(signal);
					}
			 }
		} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void sendTxt(String txtf_name){
		// load the txt file matches the filename(signal) then send the obj back to the client
		
		//파일을 읽고
		File dir = new File("C:\\송희 자바 코드\\fixingserver\\저장파일");
		String fname = txtf_name;
		File f1 = new File(dir,fname);
		FileReader fr;
		Object obj ;
		String msg="";
		String txt ="";
		try {
			fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			while(true){
				msg = br.readLine();
				if (msg==null) break;
				txt+="\n"+msg;
				//***********************************************ask why?
				System.out.println("클라이언트에게 보내야할 내용"+txt);
			}
			System.out.println("1.클라이언트에게 보낸 글 내용"+txt);
			oos = new ObjectOutputStream(c_socket.getOutputStream());
			// and how
			oos.writeObject(txt);
			System.out.println("2.클라이언트에게 보낸 글 내용"+msg);
			//why is this null? when is it  out of the { }??
			oos.flush();
			br.close();
			//스트링을 오브젝트로 보내준다.
			/*obj=(Object)msg;*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void sendObj() throws Exception{ // get arraylist from the Server_DB
		System.out.println("send obj ???");
		//어레이 리스트쓴방법
		ArrayList al = sdb1.db_view_list();
		System.out.println(al.size());
		Bboard_post bp1=(Bboard_post)al.get(0);
		System.out.println("디비에서 받은 애"+bp1.getTitle()+bp1.getId()+bp1.getDate());
		oos = new ObjectOutputStream(c_socket.getOutputStream());
		System.out.println("게시판에 리스트를 보내줍니다.");
		
		oos.writeObject(al);
		/*oos.flush();*/
	}
	
	public static void main(String[] args) {
		new Server_obj(22000);
	}
}
