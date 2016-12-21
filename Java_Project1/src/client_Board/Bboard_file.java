package client_Board;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

class Bboard_file{
	private Socket soc_file;
	
	public Bboard_file(Socket soc_file){
		this.soc_file = soc_file;
		System.out.println("file 소켓 받아옴");
	}

	//1.첨부파일 보냄.
	public void sendFile(String filedir){
		FileInputStream fis;
		try {
			fis = new FileInputStream(filedir);
			DataOutputStream dos = new DataOutputStream(soc_file.getOutputStream());
			byte[] buffer = new byte[1024];
			int res ;
			while(fis.read(buffer)>0){
				dos.write(buffer);
			}
			dos.flush();
			fis.close();
			System.out.println("클라이언트가 첨부파일 보내.");
			/*dos.close();*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveFile(String extension, String file_size) {
		try{
			File dir = new  File("C:\\송희 자바 코드\\fixingserver");
			File atcFile = new File(dir,extension);
			DataInputStream dis = new DataInputStream(soc_file.getInputStream());
			FileOutputStream fos = new FileOutputStream(atcFile);
			byte[] buffer = new byte[1024];
			int filesize = Integer.parseInt(file_size.trim());
			System.out.println("file siezeeeeeeeeeeeeeeeeee"+file_size);
			int read = 0;
			int totalRead=0;
			int remaining = filesize;
			while((read=dis.read(buffer,0,Math.min(buffer.length, remaining)))>0){
				totalRead+= read;
				remaining -= read;
				System.out.println("read "+ totalRead +"bytes.");
				fos.write(buffer, 0, read);
			}
			fos.close();
			System.out.println("클라이언트 컴퓨터에 첨부파일이 저장됬습니다.");
			JOptionPane.showMessageDialog(null, "저장 경로 : "+dir+"\n"+extension+"  이 저장되었습니다.");
		}catch(Exception e){
			/*e.printStackTrace();*/
			JOptionPane.showMessageDialog(null, "작성자가 파일을 첨부하지 않았습니다.");
		}
	}

	public void fileSendSignal(String signal){
		try {
			PrintWriter pw = new PrintWriter(soc_file.getOutputStream());
			pw.println(signal);
			System.out.println(signal);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
