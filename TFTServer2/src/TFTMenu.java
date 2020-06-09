//페이지 처리 : 검색 창, 결과 목록 창, 상세 결과 창
//
//

import javax.swing.tree.*;
import javax.swing.table.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
public class TFTMenu {
	private DataInputStream in;
	private DataOutputStream out;
	StringTokenizer line; //문자 메시지 구분자
	Socket sock;
	
	ArrayList<String[]> saveList;
	
	TFTMenu(Socket socket){
		sock = socket;
	}
	
	void SearchMenu() {
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
			if(sign.equals("SearchOn")) { //검색 요구
				String searchName = null; //검색 아이디
				boolean result = false; // 검색 성공 조건 - 필요에 따라 형태 바꾸기
				line = new StringTokenizer(next, "&&");
				searchName = line.nextToken(); //검색하고자 하는 닉네임
				
				//API로 해당 이름을 검색
				
				
				//여기까지 코드 작성 - 생성된 인스턴스 객체
				
				
				if(result == true) { // 검색에 성공했을 경우  - if 조건은 재설정
					String searchMsg = null;
					int matchNum = 0; // 몇 번의 경기 리스트 가져오는지
					
					
					// matchNum = ??(인스턴스의 리스트 길이 가져오기- 몇 줄?)
					
					try {
						// searchMsg에 저장된 값 변경
						//searchMsg = "SEARCHOK$" + 소환사 이름 필드 + "&&" + 티어 정보);
						out.writeUTF(searchMsg); // 검색 성공 알림
					}catch(IOException e) {}
					/*
					for(int i = 0; i < matchNum; i ++){
						if(int == matchNum-1)
						{
							try {
								searchMsg = "FAINALLMSG$" + 정보 1 + "&&" + 정보2 + "&&" + 정보3 + .... );
								out.writeUTF(searchMsg); // 마지막
							}catch(IOException e) {}
						}else{
							try {
								searchMsg = "NEWMSG$" + 정보 1 + "&&" + 정보2 + "&&" + 정보3 + .... );
								out.writeUTF(searchMsg); // 계속
							}catch(IOException e) {}
						
						}
					}
					*/
				}else { //검색에 실패했을 경우
					try {
						out.writeUTF("SEARCHFAIL$"); // 검색 실패 메시지
					}catch(IOException e) {}
				}
				
			}else if(sign.equals("DATASAVEON")) { //전적 저장 요구
				String save = null;
				line = new StringTokenizer(next, "&&");
				/*
				데이터 저장에 필요한 필드 만들기
				필드 명 = next.nextToken(); <- 활용
				
				저장하기 DB 작성
				save = 결과 저장
				예외 처리 => save = FAIL;
				*/
				if(save.equals("SAVE")) { // 저장 성공
					try {
						out.writeUTF("DATASAVE$"); //저장 성공 메시지
					}catch(IOException e) {}
					
				}else { //저장 실패
					try {
						out.writeUTF("SAVEFAIL$"); // 저장 실패 메시지
					}catch(IOException e) {}
				}
				
				//저장된 데이터 불러오기는 수정 필요 == 데이터 보낼 방법과 저장 방법 고민 중
			}else if(sign.equals("SAVELOAD")) { //저장된 데이터 불러오기
				saveList = new ArrayList<String[]>(); //<-이중 리스트 활용하기
				/*
				DB에서 불러올 데이터를 저장할 필드 구성
				가능하면 한 경기 씩 가져와서 리스트에 저장하는 걸 추천
				불가능하면 전부 가져와서 tokenuzer을 활용해서 잘라서 순서대로 넣기
				saveList.add(new String[]{필드, 필드, 필드, 필드 ...})
				
				*/
				
				/* ---- 여긴 수정 필요 ----
				for(int i = 0; i < matchNum; i ++){
					if(int == matchNum-1)
					{
						try {
							searchMsg = "FAINALLMSG$" + 정보 1 + "&&" + 정보2 + "&&" + 정보3 + .... );
							out.writeUTF(searchMsg); // 마지막
						}catch(IOException e) {}
					}else{
						try {
							searchMsg = "NEWMSG$" + 정보 1 + "&&" + 정보2 + "&&" + 정보3 + .... );
							out.writeUTF(searchMsg); // 계속
						}catch(IOException e) {}
					
					}
				}
				*/
				
				try {
					out.writeUTF("SAVEPAGE$"); // 저장된 페이지로
				}catch(IOException e) {}
				return;
				
			}else if(sign.equals("ENDPAGE")) { //나가기 요구
				try {
					out.writeUTF("ENDPAGE$"); // 이전 페이지 이동
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
