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
	
	TFTMenu(Socket socket){
		sock = socket;
	}
	
	void startMenu() {
		
	}
}
