package com.example.TFTclientPro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.AsyncTask;
import java.io.*;
import java.io.InputStreamReader;
import java.net.*;
import java.util.StringTokenizer;

public class SearchMenu extends AppCompatActivity {
    public Socket client;
    public DataInputStream in;
    public DataOutputStream out;
    //public PrintWriter out; //서버에 출력하기 위한 스트림
    //public BufferedReader in; //입력 스트림
    StringTokenizer line; //문자 메시지 구분자
    String allMsg = null; //전체 메시지
    String sign = null; //클라이언트 신호
    String next = null; //다음 메시지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchManager searchPage = new SearchManager();
        searchPage.execute();
    }

    public class SearchManager extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() { //시작 전 UI 세팅
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) { //동작 중에 작업 처리 //종료 시 리턴
            try {
                System.out.println("동작5"); // 확인
                client = SocketManager.getSocket();
            }catch(IOException e) {
                System.out.println("연결 실패5");
            }

            if(client != null){
                try{
                    /*out = new PrintWriter(client.getOutputStream(), true);
                    InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    in = new BufferedReader(inputStreamReader);*/
                    out = new DataOutputStream(client.getOutputStream());
                    in = new DataInputStream(client.getInputStream());
                    publishProgress("0");
                }catch (IOException e){}
            }

            while (true){
                try{
                    allMsg = in.readUTF(); //메시지 올 때까지 대기
                }catch (IOException e){
                    return null;
                }

                line = new StringTokenizer(allMsg, "$");
                sign = line.nextToken();
                next = line.nextToken();

                //US : 등록 유저 명령어 구분   DE : 비등록 유저 명령어 구분
                if(sign.equals("USLOGINMOVE")) { //로그인 페이지로 이동
                    return "USLOGINMOVE";

                }else if(sign.equals("USJOINMOVE")) { //회원가입 페이지로 이동
                    return "USJOINMOVE";

                }else if(sign.equals("DESTARTMOVE")) { //로그인 없이 시작
                    return "DESTARTMOVE";

                }else {
                    //질못된 전송이 있을 경우 - return 하면 안 됨
                }

            }
        }

        @Override
        protected void onProgressUpdate(String... values) { //동작 중에 UI 업데이트
            super.onProgressUpdate(values);
            //Intent intent = new Intent(getApplicationContext(), SubActibit.class);
            //startActivity(intent);
        }

        @Override
        protected void onPostExecute(String aVoid) { //동작 마무리 UI 업데이트
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() { // 동작 종료 시 발생
            super.onCancelled();
        }

    }
}
