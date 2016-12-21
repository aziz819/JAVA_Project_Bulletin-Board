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
	
	//요청받고 보내줌
	public Server_File(int port) {
		try {
			ss = new ServerSocket(port);
			System.out.println("파일 샌딩 서버 열림, 대기중..");
			while(true){
				c_soc = ss.accept();
				Thread th = new Thread(this);
				th.start();
				System.out.println("파일 샌딩 클라이언트 접속");
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	public void run() {
		try {
			while (true) {
				System.out.println("서버파일 메세지 대기중");
				//버퍼가 계속 메세지를 받으면서 받을때마다 그에 맞는 메소드 실행해줌.
				br = new BufferedReader(new InputStreamReader(c_soc.getInputStream()));
				String signal = br.readLine();
				System.out.println(signal+"서버파일에서 받은 시그널");
				if(signal.charAt(0)=='#'){
					// send the attached file back to the client.
					attfile_name=signal;
					String[] parts =signal.split("#");
					attfile_name = parts[1];
					System.out.println("첨부파일 이름 : "+attfile_name);
					sendFile(attfile_name);
				}/*else{//클라이언트가 첨부파일을 보낼때 사용
					//얘도 위와 같이 파일 이름을보내서 그걸 저장시킴.
					file_name = signal;
					System.out.println(file_name);
				}*/
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendFile(String attfile_name){
		File dir = new File("C:\\송희 자바 코드\\fixingserver\\저장파일");
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
				System.out.println("서버가 파일을 얼마나 읽어왔니? : "+r1);
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
		//파일을 읽고
		// how to send the binary file.. you dont have to read it! just find it and send!!
		//**************************************************************************
		File dir = new File("D:\\취업반 프로젝트 SHK\\project");
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
		//파일을 보내줌
	}*/

	public static void main(String[] args) {
		new Server_File(30000);
	}
}