package server_Board;

import java.io.*;
import java.net.*;



public class Server_obj_r implements Runnable{
	Bboard_post bp1;
	Server_DB sdb1 = new Server_DB();
	ServerSocket ss;
	Socket c_socket;
	//portnumber 21000
	public Server_obj_r(){ // 그냥 받는애
		try {
			ss= new ServerSocket(21000);
			System.out.println("오브젝트 서버 열림, 대기함..");
			while(true){
				c_socket = ss.accept();
				// 위에서 새로운 소캣이 연결될때까지 대기하고 있음..
				System.out.println("오브젝트 클라이언트 접속");
				//연결이되면 안끊나고 계속 쓰레드가 돌아감..
				if(c_socket==null) break;
				Thread th = new Thread(this); // creating a Thread, every time client enters.
				th.start();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receiveObj(){ //receiving obj from client. will be started in run.
			//클라이언트에서 글내용 받고 저장하는 메소드 실행
			// 데이터 베이스에 글제목 아이디 날짜 추가.
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(c_socket.getInputStream());
				System.out.println("ois 값이 뭘까??"+ois);
				Object obj = ois.readObject();
				System.out.println(c_socket.toString());
				bp1 = (Bboard_post)obj;
				bp1.disp();
				System.out.println(bp1.getNo()+"들어온 코드를살펴보자.. 3737삭제 7373수정");
				if(bp1.getNo()==3737){
					deletePost(bp1);
				}else if(bp1.getNo()==7373){
				savePost();
				}else{
					savePost();
				sdb1.db_insert(bp1.getTitle(), bp1.getId(), bp1.getDate());
				}
			} catch (Exception e) {
				/*e.printStackTrace();*/
			}
	}
	public void savePost() throws Exception{// save the post as txt file on server's harddrive.
			File dir = new File("C:\\송희 자바 코드\\fixingserver\\저장파일");
			String file_name = "@"+bp1.getId()+"@"+bp1.getTitle()+"@"+bp1.getDate()+".txt";
			
			System.out.println(file_name);
			
			File f_txt = new File(dir, file_name);
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f_txt)));
			pw.println(bp1.getTa());
			pw.flush();
			
			System.out.println(bp1.getTa()+"을 텍스트 파일로 저장하였습니다.");
	}
	public void deletePost(Bboard_post bp1){
/*		 String parts[] = bp1.getExtension().split(".");
		 String attfname=parts[0];*/
		String dir = "C:\\송희 자바 코드\\fixingserver\\저장파일\\";
		String f_att = bp1.getId()+"@"+bp1.getTitle()+"@"+bp1.getDate()+"@"+bp1.getExtension();/*attfname*/
		String f_txt = "@"+bp1.getId()+"@"+bp1.getTitle()+"@"+bp1.getDate()+".txt";
		
		System.out.println("attaced file : "+f_att);
		System.out.println("txt file : "+f_txt);
		
		// attach file 삭제 어떻게해??
		File fa= new File(dir+f_att);
		File ft= new File(dir+f_txt);
		boolean fab =fa.delete(); 
		boolean ftb =ft.delete();
		if(fab){
			System.out.println(bp1.getId()+" 님의 첨부파일이 삭제 되었습니다.");
		}else if(ftb){
			System.out.println(bp1.getId()+" 님의 글이 삭제 되었습니다.");
		}else{
			System.out.println("글 삭제를 실패 하였습니다.");
		}
		//DB
		Server_DB sdb = new Server_DB();
		try {
			sdb.db_delete(bp1.getTitle(),bp1.getId(),bp1.getDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true){
			receiveObj();
		}
	}
	public static void main(String[] args) {
		new Server_obj_r();
	}
}
