package com.example.TFTclientPro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.AsyncTask;
import java.io.*;
import java.io.InputStreamReader;
import java.net.*;
import java.util.StringTokenizer;

public class StartMenu extends AppCompatActivity {
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
        //setContentView(R.layout.activity_start);

        startManager startPage = new startManager();
        startPage.execute();
    }

    public class startManager extends AsyncTask<Void, String, String>{
        @Override
        protected void onPreExecute() { //시작 전 UI 세팅
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) { //동작 중에 작업 처리 //종료 시 리턴
            try {
                client = SocketManager.getSocket();
                System.out.println("시작 페이지 동작"); // 확인
            }catch(IOException e) {
                System.out.println("연결 실패2");
            }

            if(client != null){
                try{
                    /*out = new PrintWriter(client.getOutputStream(), true);
                    InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    in = new BufferedReader(inputStreamReader);*/
                    out = new DataOutputStream(client.getOutputStream());
                    in = new DataInputStream(client.getInputStream());
                    publishProgress("PageOpen");
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
            if(values[0].equals("PageOpen")){ //어플을 시작했을 경우
                setContentView(R.layout.activity_start);
                System.out.println("시작 페이지");
            }

        }

        @Override
        protected void onPostExecute(String commend) { //동작 마무리 UI 업데이트
            super.onPostExecute(commend);
            // 화면 전환
            if(commend.equals("USLOGINMOVE")){ //로그인 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), LoginMenu.class);
                startActivity(intent);

            }else if(commend.equals("USJOINMOVE")){ //회원가입 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), JoinMenu.class);
                startActivity(intent);

            }else if(commend.equals("DESTARTMOVE")){ //비회원 시작
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
                startActivity(intent);
                /*
                추가 작업 필요 시(다음 페이지로 정보 전달)
                방법 : 발신 : intent.putExtra("이름", "전달 값");
                방법 : 수신 : Intent intent = getIntent();
                             String name = intent.getExtras.getString("이름");
                */
            }
        }

        @Override
        protected void onCancelled() { // 동작 종료 시 발생
            super.onCancelled();
        }

    }
}
