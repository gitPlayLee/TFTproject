import javax.swing.tree.*;
import javax.swing.table.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Map;
public class ServerOn {
	private ServerSocket SSock;
	private Socket Sock;
	
	//DB 선언해주기
	
	public void SetServer() {
		try {
			SSock = new ServerSocket(9999);
		}catch(IOException e) {}
		
		while(true) {
			System.out.println("대기중.......");
			try {
				Sock = SSock.accept(); // 클라이언트가 접촉할 때까지 대기
				System.out.println("클라이언트 접속");
			}catch(IOException r) {}
			System.out.println(Sock.getInetAddress() + " 에서 접속했습니다.");
			
			TFTstart startPage = new TFTstart(Sock);
			startPage.start();
		}
	}
	
	public static void main(String[] args) {
		ServerOn Server = new ServerOn();
		Server.SetServer();
	}

}
