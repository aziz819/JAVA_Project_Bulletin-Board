package client_Board;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.ArrayList;

class Bboard_obj{
	private Socket soc_obj;
	
	public Bboard_obj(Socket soc_obj){
		this.soc_obj = soc_obj;
		System.out.println("������Ʈ ���� �޾ƿ�");
	}	
	//1. �������� �Խ��� ����Ʈ ������Ʈ�޾ƿ�
	public ArrayList receiveList(){		
		ObjectInputStream ois;
		ArrayList al = null;
		try {
			ois = new ObjectInputStream(soc_obj.getInputStream());
			al = (ArrayList)ois.readObject();
			System.out.println("�޾ƿ� al�� ������   "+al.size());
			System.out.println("Ŭ���̾�Ʈ - �Խ��� ������Ʈ �޾ƿ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return al; 
	}
	//2. �۾��⸦ ������ ������ +���� ������Ʈ�� ����.
	public void sendObject(Bboard_post bp){
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(soc_obj.getOutputStream());
			oos.writeObject(bp);
			oos.flush();
			System.out.println(soc_obj.toString()+"Ŭ:�۾��� ���ۿϷ�");
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
		System.out.println("������"+txt);
		//return string 
		/*String txt = "�۳����� ������!!";*/
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

