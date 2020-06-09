import javax.swing.tree.*;
import javax.swing.table.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
public class TFTlogin {
	private DataInputStream in;
	private DataOutputStream out;
	StringTokenizer line; //문자 메시지 구분자
	Socket sock;
	
	String loginName = null; //로그인할 아이디 저장
	
	TFTlogin(Socket socket){
		sock = socket;
	}
	
	void loginPage() {
		String allMsg = null; //전체 메시지
		String sign = null; //클라이언트 신호
		String next = null; //다음 메시지
		
		try {
			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());
		}catch(IOException e) {};
		
		while(true) {
			try {
				allMsg = in.readUTF();
			}catch(IOException e) {
				return;
			};
			
			line = new StringTokenizer(allMsg, "$");
			sign = line.nextToken();
			next = line.nextToken();
			if(sign.equals("LOGIN")) { //로그인 요구
				String ID = null;
				String PW = null;
				line = new StringTokenizer(next, "&&");
				ID = line.nextToken();
				PW = line.nextToken();
				
				//DB로  로그인 검사
				//loginName = (DB 로그인 SQL);
				// 성공 시 : 로그인 성공한 아이디
				// 실패 시 : LOGINFAIL
				
				if(!loginName.equals("LOGINFAIL")) { //로그인 성공
					loginName = loginName + " f"; //닉네임 불량 방지
					line = new StringTokenizer("loginName", " ");
					loginName = line.nextToken();
					TFTMenu mainMenu = new TFTMenu(sock);
					
					/*
					
					메인 메뉴 이동 전 추가 작업 처리
					ex) 시작 메뉴 가기전 클라이언트나 서버가 가져야할 값 처리
					
					*/
					try {
						out.writeUTF("LOGINSUCCESS$"); //로그인 성공 메시지
					}catch(IOException e) {}
					mainMenu.SearchMenu(); // 페이지 동작
					
				}else { //로그인 실패
					try {
						out.writeUTF("LOGINFAIL$");
					}catch(IOException e) {}
				}
				
			}else if(sign.equals("ENDPAGE")) { //나가기 요구
				try {
					out.writeUTF("ENDPAGE$"); // 시작 페이지 이동
				}catch(IOException e) {}
				return;
				
			}else { // 잘못된 입력 처리
				try {
					out.writeUTF("FAILMSG$");
				}catch(IOException e) {}
			}
			
		}
	}
}
