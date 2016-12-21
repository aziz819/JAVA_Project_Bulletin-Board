package server_Board;

public class Server_main extends Thread {
	public static void main(String[] args) {
		new Server_main().start();
		Server_obj sobj = new Server_obj(21000); 
	}
	public void run(){
		Server_File sf = new Server_File(30000);
	}
}
