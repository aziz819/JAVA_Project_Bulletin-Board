package server_Board;

import java.io.Serializable;

public class Bboard_post implements Serializable{
	int no;
	int pno;
	String title;
	String id;
	String ta;
	String date;
	String extension;
	
	public Bboard_post(int pno,String title,String id,String date){ // constructor used by db
		this.pno=pno;
		this.title=title;
		this.id=id;
		this.date=date;
	}
	public Bboard_post(String title,String id, String date){
		this.title = title;
		this.id = id;
		this.date = date;
	}
	public Bboard_post(String title,String id, String ta, String date){
		this.title=title;
		this.id = id;
		this.ta=ta;
		this.date=date;
	}
	
	
	public String getExtension(){
		return extension;
	}
	public void setExtension(String extension){
		this.extension=extension;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTa() {
		return ta;
	}
	public void setTa(String ta) {
		this.ta = ta;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getPno() {
		return pno;
	}
	public void setPno(int pno) {
		this.pno = pno;
	}
	public void disp(){
		System.out.println("글번호 : "+no+"아이디 : "+id+"  제목 : "+title +"  날짜 : "+date+ "  글내용: " + ta);
	}

}