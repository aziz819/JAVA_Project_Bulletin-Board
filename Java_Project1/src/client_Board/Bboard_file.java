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
		System.out.println("file ���� �޾ƿ�");
	}

	//1.÷������ ����.
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
			System.out.println("Ŭ���̾�Ʈ�� ÷������ ����.");
			/*dos.close();*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveFile(String extension, String file_size) {
		try{
			File dir = new  File("C:\\���� �ڹ� �ڵ�\\fixingserver");
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
			System.out.println("Ŭ���̾�Ʈ ��ǻ�Ϳ� ÷�������� �������ϴ�.");
			JOptionPane.showMessageDialog(null, "���� ��� : "+dir+"\n"+extension+"  �� ����Ǿ����ϴ�.");
		}catch(Exception e){
			/*e.printStackTrace();*/
			JOptionPane.showMessageDialog(null, "�ۼ��ڰ� ������ ÷������ �ʾҽ��ϴ�.");
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
