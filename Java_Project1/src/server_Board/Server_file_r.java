package server_Board;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server_file_r implements Runnable {
	ServerSocket ss;
	Socket c_socket;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	private String date = sdf.format(new Date());
	
	public Server_file_r(){ // 그냥 받는애
		try {
			ss= new ServerSocket(31000);
			System.out.println("파일 리시빙 서버 열림, 대기함..");
			while(true){
				c_socket = ss.accept();
				System.out.println("파일 리시빙 클라이언트 접속");
				if(c_socket==null) break;
					Thread th = new Thread(this); // creating a Thread, every time client enters.
					th.start();
				/*receiveFile();*/
			}
		} catch (Exception e) {;
			e.printStackTrace();
		}
	}
	public void receiveFile() throws IOException {
		//첨부파일 이름 받으려함.
		BufferedReader br = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
		System.out.println("br 값  "+br); // 쓰레기값 생성하고 있어 계속 
		String msg = br.readLine();
		if(msg.indexOf("#")==-1) return; // 메세지에서 #이 몇번째 있는지 찾고 없으면(-1) 브레이크로 나가.
		System.out.println("msg="+msg);
		
		String split[] = msg.split("#");
		
		System.out.println(split[0]);
		//첨부파일 받으려함
		File dir = new File("C:\\송희 자바 코드\\fixingserver\\저장파일");
		File attFile = new File(dir,split[0]);
		DataInputStream dis = new DataInputStream(c_socket.getInputStream());
		FileOutputStream fos = new FileOutputStream(attFile);
		byte[] buffer = new byte[1024];
		
		int filesize = Integer.parseInt(split[1]); // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read=dis.read(buffer,0,Math.min(buffer.length, remaining)))>0) {
			System.out.println(1);
			totalRead += read;
			System.out.println(2);
			remaining  -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
			System.out.println(3);
			
		}
		System.out.println("while 빠저나오니?");
		fos.flush();
		fos.close();
	}
	@Override
	public void run() {
		try {
			while(true){
			receiveFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Server_file_r();
	}
}
