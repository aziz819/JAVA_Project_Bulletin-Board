package server_Board;

import java.io.*;
import java.net.*;



public class Server_obj_r implements Runnable{
	Bboard_post bp1;
	Server_DB sdb1 = new Server_DB();
	ServerSocket ss;
	Socket c_socket;
	//portnumber 21000
	public Server_obj_r(){ // �׳� �޴¾�
		try {
			ss= new ServerSocket(21000);
			System.out.println("������Ʈ ���� ����, �����..");
			while(true){
				c_socket = ss.accept();
				// ������ ���ο� ��Ĺ�� ����ɶ����� ����ϰ� ����..
				System.out.println("������Ʈ Ŭ���̾�Ʈ ����");
				//�����̵Ǹ� �Ȳ����� ��� �����尡 ���ư�..
				if(c_socket==null) break;
				Thread th = new Thread(this); // creating a Thread, every time client enters.
				th.start();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receiveObj(){ //receiving obj from client. will be started in run.
			//Ŭ���̾�Ʈ���� �۳��� �ް� �����ϴ� �޼ҵ� ����
			// ������ ���̽��� ������ ���̵� ��¥ �߰�.
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(c_socket.getInputStream());
				System.out.println("ois ���� ����??"+ois);
				Object obj = ois.readObject();
				System.out.println(c_socket.toString());
				bp1 = (Bboard_post)obj;
				bp1.disp();
				System.out.println(bp1.getNo()+"���� �ڵ带���캸��.. 3737���� 7373����");
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
			File dir = new File("C:\\���� �ڹ� �ڵ�\\fixingserver\\��������");
			String file_name = "@"+bp1.getId()+"@"+bp1.getTitle()+"@"+bp1.getDate()+".txt";
			
			System.out.println(file_name);
			
			File f_txt = new File(dir, file_name);
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f_txt)));
			pw.println(bp1.getTa());
			pw.flush();
			
			System.out.println(bp1.getTa()+"�� �ؽ�Ʈ ���Ϸ� �����Ͽ����ϴ�.");
	}
	public void deletePost(Bboard_post bp1){
/*		 String parts[] = bp1.getExtension().split(".");
		 String attfname=parts[0];*/
		String dir = "C:\\���� �ڹ� �ڵ�\\fixingserver\\��������\\";
		String f_att = bp1.getId()+"@"+bp1.getTitle()+"@"+bp1.getDate()+"@"+bp1.getExtension();/*attfname*/
		String f_txt = "@"+bp1.getId()+"@"+bp1.getTitle()+"@"+bp1.getDate()+".txt";
		
		System.out.println("attaced file : "+f_att);
		System.out.println("txt file : "+f_txt);
		
		// attach file ���� �����??
		File fa= new File(dir+f_att);
		File ft= new File(dir+f_txt);
		boolean fab =fa.delete(); 
		boolean ftb =ft.delete();
		if(fab){
			System.out.println(bp1.getId()+" ���� ÷�������� ���� �Ǿ����ϴ�.");
		}else if(ftb){
			System.out.println(bp1.getId()+" ���� ���� ���� �Ǿ����ϴ�.");
		}else{
			System.out.println("�� ������ ���� �Ͽ����ϴ�.");
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
