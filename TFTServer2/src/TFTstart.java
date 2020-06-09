import javax.swing.tree.*;
import javax.swing.table.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
public class TFTstart extends Thread{
	private DataInputStream in;
	private DataOutputStream out;
	StringTokenizer line; //문자 메시지 구분자
	Socket sock;
	
	TFTstart(Socket socket){
		sock = socket;
	}
	
	@Override
	public void run() {
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
				System.out.println("스레드 종료");
				return;
			};
			line = new StringTokenizer(allMsg, "$");
			sign = line.nextToken();
			next = line.nextToken();
			//US : 등록 유저 명령어 구분   DE : 비등록 유저 명령어 구분
			if(sign.equals("USLOGINMOVE")) { //로그인 페이지로 이동
				try {
					out.writeUTF("USLOGINMOVE$"); // 로그인 메시지
				}catch(IOException e) {}
				TFTlogin LoginPages = new TFTlogin(sock);
				LoginPages.loginPage();
				
			}else if(sign.equals("USJOINMOVE")) { //회원가입 페이지로 이동
				try {
					out.writeUTF("USJOINMOVE$"); // 회원가입 메시지
				}catch(IOException e) {}
				TFTjoin JoinPage = new TFTjoin(sock);
				JoinPage.joinPage();
				
			}else if(sign.equals("DESTARTMOVE")) { //로그인 없이 시작
				try {
					out.writeUTF("DESTARTMOVE$"); //바로 시작 메시지
				}catch(IOException e) {}
				TFTMenu mainMenu = new TFTMenu(sock);
				mainMenu.SearchMenu();
				
			}else {
				try {
					out.writeUTF("FAILMSG$");
				}catch(IOException e) {}
			}
			
		}
	}
}
