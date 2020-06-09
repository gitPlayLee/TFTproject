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
		String TFTName = null; // 입력된 TFT 닉네임
		String PWdate = null; // 입력된 비밀번호
		
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
			if(sign.equals("JOINSTART")) { //입력 아이디와 TFT 아이디 입력 확인
				String IDControl = null;
				String TFTControl = null;
				line = new StringTokenizer(next, "&&");
				IDControl = line.nextToken();
				PWdate = line.nextToken();
				TFTName = line.nextToken();
				
				IDName = IDControl; //아이디 보관
				TFTName = TFTControl; //닉네임 보관
				
				// DB의 SQL로 검증
				// 성공 : 아무거나 or 입력한 아이디 명
				// 실패 : CONTROLSUCCESS
				// IDControl = (SQL);
				
				if(IDControl.equals("CONTROLSUCCESS")) {
					try {
						out.writeUTF("USEDID$"); //중복 아이디 
					}catch(IOException e) {}
				}else {
					try {
						out.writeUTF("USEPLAY$"); //사용 가능
					}catch(IOException e) {}
				}
				
				// API를 통한 검증
				// 성공 : 아무거나 or 입
				// 실패 : FAILNICK
				// TFTControl = API 결과 값 저장
				
				if(TFTControl.equals("FAILNICK")) {
					try {
						out.writeUTF("FAILNICK$"); //닉네임 찾기 실패
					}catch(IOException e) {}
					
				}else {
					try {
						out.writeUTF("CLEARNICK$"); //닉네임 찾기 성공
					}catch(IOException e) {}
				}
				
				// 가입 작업 종료 메시지
				try {
					out.writeUTF("JOBCLEAR$"); //확인 명령
				}catch(IOException e) {}
				
				
			}else if(sign.equals("TERMOK")) { //가입 절차 진행
				String JOINCUT = "JOINFALE";
				// 아이디 : IDName
				// 비밀번호 : PWdate
				// TFT닉네임 : TFTName
				
				//DB에 저장 SQL 작성
				// 성공 : JOINOK
				// 실패 : JOINFALE
				
				if(JOINCUT.equals("JOINOK")) { //가입 성공
					try {// 가입 성공
						out.writeUTF("JOINSUCCESS$"); //가입 성공 전달
					}catch(IOException e) {}
					
				}else { // 가입 실패 - DB 문제
					try {
						out.writeUTF("JOINFALE$"); //가입 실패 전달
					}catch(IOException e) {}
				}
				
			}else if(sign.equals("ENDPAGE")) { //나가기 요구
				/*
				try {
					out.writeUTF("ENDPAGE$"); // 시작 페이지 이동
				}catch(IOException e) {}
				*/
				return;
				
			}else { // 잘못된 입력 처리
				try {
					out.writeUTF("FAILMSG$");
				}catch(IOException e) {}
			}
			
		}
		
	}
	
}
