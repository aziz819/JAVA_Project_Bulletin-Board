package server_Board;

import java.io.*;
import java.net.*;

public class Server_File implements Runnable {
	private ServerSocket ss;
	private String file_name = "";
	private Socket c_soc;
	private BufferedReader br;
	private String signal = "";
	private String attfile_name="";
	
	//��û�ް� ������
	public Server_File(int port) {
		try {
			ss = new ServerSocket(port);
			System.out.println("���� ���� ���� ����, �����..");
			while(true){
				c_soc = ss.accept();
				Thread th = new Thread(this);
				th.start();
				System.out.println("���� ���� Ŭ���̾�Ʈ ����");
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	public void run() {
		try {
			while (true) {
				System.out.println("�������� �޼��� �����");
				//���۰� ��� �޼����� �����鼭 ���������� �׿� �´� �޼ҵ� ��������.
				br = new BufferedReader(new InputStreamReader(c_soc.getInputStream()));
				String signal = br.readLine();
				System.out.println(signal+"�������Ͽ��� ���� �ñ׳�");
				if(signal.charAt(0)=='#'){
					// send the attached file back to the client.
					attfile_name=signal;
					String[] parts =signal.split("#");
					attfile_name = parts[1];
					System.out.println("÷������ �̸� : "+attfile_name);
					sendFile(attfile_name);
				}/*else{//Ŭ���̾�Ʈ�� ÷�������� ������ ���
					//�굵 ���� ���� ���� �̸��������� �װ� �����Ŵ.
					file_name = signal;
					System.out.println(file_name);
				}*/
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendFile(String attfile_name){
		File dir = new File("C:\\���� �ڹ� �ڵ�\\fixingserver\\��������");
		File f1 = new File(dir,attfile_name);
		int f1_size = (int) f1.length(); 
		/*System.out.println(f1.getPath()+);*/
		FileInputStream fis;
		try {
			fis= new FileInputStream(f1);
			DataOutputStream dos = new DataOutputStream(c_soc.getOutputStream());
			byte[] buffer = new byte[1024];
			int r1;
			while((r1=fis.read(buffer))>0){
				dos.write(buffer);
				System.out.println("������ ������ �󸶳� �о�Դ�? : "+r1);
			}
			dos.flush();
			System.out.println("server sent the attached file to the client");
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
/*	public void fileLoad(String txtf_name){
		//������ �а�
		// how to send the binary file.. you dont have to read it! just find it and send!!
		//**************************************************************************
		File dir = new File("D:\\����� ������Ʈ SHK\\project");
		String fname = txtf_name;
		
		File f1 = new File(dir,fname);
		FileReader fr;
		try {
			fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			while(true){
				String msg = br.readLine();
				if (msg==null) break;
				System.out.println(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//������ ������
	}*/

	public static void main(String[] args) {
		new Server_File(30000);
	}
}