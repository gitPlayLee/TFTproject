import javax.swing.tree.*;
import javax.swing.table.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
public class TFTjoin {
	private DataInputStream in;
	private DataOutputStream out;
	StringTokenizer line; //문자 메시지 구분자
	Socket sock;
	
	TFTjoin(Socket socket){
		sock = socket;
	}
	
	void joinPage() {
		String allMsg = null; //전체 메시지
		String sign = null; //클라이언트 신호
		String next = null; //다음 메시지
		String IDName = null; // 입력된 아이디
		
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
			if(sign.equals("IDCONTROL")) { //아이디 중복확인
				String Control = null;
				line = new StringTokenizer(next, "&&");
				Control = line.nextToken();
				IDName = Control;
				// DB의 DQL로 검증
				// 성공 : 아무거나
				// 실패 : CONTROLSUCCESS
				// Control = (SQL);
				if(Control.equals("CONTROLSUCCESS")) {
					try {
						out.writeUTF("USEDID$");
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
