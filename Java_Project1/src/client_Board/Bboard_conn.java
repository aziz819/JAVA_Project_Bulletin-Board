package client_Board;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Bboard_conn {
	private static Socket SOCKET_CLIENT;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	static{
		try {
			SOCKET_CLIENT = new Socket("localhost",20000);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Bboard_conn(){
			try {
				ois = new ObjectInputStream(SOCKET_CLIENT.getInputStream());
				oos = new ObjectOutputStream(SOCKET_CLIENT.getOutputStream());
			} catch (IOException e) {
				System.out.println("connection stream err :");
				e.printStackTrace();
			}
	}	
	public static Socket getSOCKET_CLIENT() {
		return SOCKET_CLIENT;
	}
	public static void setSOCKET_CLIENT(Socket sOCKET_CLIENT) {
		SOCKET_CLIENT = sOCKET_CLIENT;
	}
	public ObjectInputStream getOis() {
		return ois;
	}
	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}
	public ObjectOutputStream getOos() {
		return oos;
	}
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
}
