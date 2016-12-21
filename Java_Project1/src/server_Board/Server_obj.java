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
	
	//��û�ް� ������
	public Server_obj(int port){ // open server with port # and accepting clients 
		try {
			ss= new ServerSocket(port);
			System.out.println("������Ʈ ���� ����, �����..");
			while(true){
				c_socket = ss.accept();
				System.out.println("������Ʈ Ŭ���̾�Ʈ ����");
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
				 	System.out.println("���� ������Ʈ ���� �޼��� �����");
					 	br = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
						String signal = br.readLine();
						
					if(signal.equals("���ΰ�ħ")/*|| �Խ��� ��ư ������.*/ ){ // sending the whole list.
						System.out.println("Ŭ���̾�Ʈ�� ������ �Խ��Ǹ���Ʈ�� ��û�߽��ϴ�.");
						sendObj();
						
			 		}else if((signal.charAt(0))=='@'){
							// ���� �����̸� ������ �װ� ã�ƾ���. ���� ��Ų �̸��̶����ƾ� ã��.
							System.out.println("�������Ͽ��� ���� �ؽ�Ʈ �����̸�"+signal);
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
		
		//������ �а�
		File dir = new File("C:\\���� �ڹ� �ڵ�\\fixingserver\\��������");
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
				System.out.println("Ŭ���̾�Ʈ���� �������� ����"+txt);
			}
			System.out.println("1.Ŭ���̾�Ʈ���� ���� �� ����"+txt);
			oos = new ObjectOutputStream(c_socket.getOutputStream());
			// and how
			oos.writeObject(txt);
			System.out.println("2.Ŭ���̾�Ʈ���� ���� �� ����"+msg);
			//why is this null? when is it  out of the { }??
			oos.flush();
			br.close();
			//��Ʈ���� ������Ʈ�� �����ش�.
			/*obj=(Object)msg;*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void sendObj() throws Exception{ // get arraylist from the Server_DB
		System.out.println("send obj ???");
		//��� ����Ʈ�����
		ArrayList al = sdb1.db_view_list();
		System.out.println(al.size());
		Bboard_post bp1=(Bboard_post)al.get(0);
		System.out.println("��񿡼� ���� ��"+bp1.getTitle()+bp1.getId()+bp1.getDate());
		oos = new ObjectOutputStream(c_socket.getOutputStream());
		System.out.println("�Խ��ǿ� ����Ʈ�� �����ݴϴ�.");
		
		oos.writeObject(al);
		/*oos.flush();*/
	}
	
	public static void main(String[] args) {
		new Server_obj(22000);
	}
}
