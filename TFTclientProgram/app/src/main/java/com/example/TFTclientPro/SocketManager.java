package com.example.TFTclientPro;
import java.io.*;
import java.net.*;
//single ton 방식 사용
public class SocketManager {
    public final static String ip = "10.0.2.2"; //아이피 주소
    public final static int port = 9999; // 포트 번호

    private static Socket socket; //정적 형식 소켓 저장

    public static Socket getSocket() throws IOException{ //소켓 가져오기
        if (socket == null){ //소켓 확인
            socket = new Socket();
        }

        if(!socket.isConnected()){ //소켓 연결
            socket.connect(new InetSocketAddress(ip, port));
        }
        return socket;
    }

    public static void closeSock() throws IOException{ //소켓 닫기
        if(socket != null){
            socket.close();
        }
    }
}
