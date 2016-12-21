package client_Board;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.ArrayList;

class Bboard_obj{
	private Socket soc_obj;
	
	public Bboard_obj(Socket soc_obj){
		this.soc_obj = soc_obj;
		System.out.println("오브젝트 소켓 받아옴");
	}	
	//1. 서버에서 게시판 리스트 오브젝트받아옴
	public ArrayList receiveList(){		
		ObjectInputStream ois;
		ArrayList al = null;
		try {
			ois = new ObjectInputStream(soc_obj.getInputStream());
			al = (ArrayList)ois.readObject();
			System.out.println("받아온 al의 사이즈   "+al.size());
			System.out.println("클라이언트 - 게시판 오브젝트 받아옴");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return al; 
	}
	//2. 글쓰기를 했을때 글정보 +글을 오브젝트로 보냄.
	public void sendObject(Bboard_post bp){
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(soc_obj.getOutputStream());
			oos.writeObject(bp);
			oos.flush();
			System.out.println(soc_obj.toString()+"클:글쓴것 전송완료");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receiveObject(){
		ObjectInputStream ois;
		String txt = "";
		try {
			ois = new ObjectInputStream(soc_obj.getInputStream());
			Object obj = ois.readObject();
			txt = (String)obj;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("받은애"+txt);
		//return string 
		/*String txt = "글내용을 리턴해!!";*/
		return txt;
	}

	public void objSendSignal(String signal){
		try {
			PrintWriter pw = new PrintWriter(soc_obj.getOutputStream());
			pw.println(signal);
			System.out.println(signal);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}

