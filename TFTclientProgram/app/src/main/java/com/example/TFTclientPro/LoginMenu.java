package com.example.TFTclientPro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.AsyncTask;
import java.io.*;
import java.io.InputStreamReader;
import java.net.*;
import java.util.StringTokenizer;

public class LoginMenu extends AppCompatActivity {
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
        setContentView(R.layout.activity_login);

        loginManager loginPage = new loginManager();
        loginPage.execute();
    }

    public class loginManager extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() { //시작 전 UI 세팅
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) { //동작 중에 작업 처리 //종료 시 리턴
            try {
                System.out.println("동작3"); // 확인
                client = SocketManager.getSocket();
            }catch(IOException e) {
                System.out.println("연결 실패3");
            }

            if(client != null){
                try{
                    /*out = new PrintWriter(client.getOutputStream(), true);
                    InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    in = new BufferedReader(inputStreamReader);*/
                    out = new DataOutputStream(client.getOutputStream());
                    in = new DataInputStream(client.getInputStream());
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
                if(sign.equals("LOGINSUCCESS")) { //로그인 성공
                    /*
                    로그인 작업 후 필요한 작업 처리
                    ex) 로그인 아이디나 TFT 아이디 정보 저장(보내기용)
                    */
                    return "LOGINSUCCESS";

                }else if(sign.equals("LOGINFAIL")) { //로그인 실패
                    publishProgress("LOGINFAIL");

                }else if(sign.equals("ENDPAGE")) { //페이지 나가기(뒤로 가기 버튼)
                    return "DESTARTMOVE"; //미완성 부분 보완 필요

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
            if(values[0].equals("LOGINFAIL")){ //로그인 실패 처리, 메시지창 띄우기
                AlertDialog.Builder failMsg = new AlertDialog.Builder(LoginMenu.this);
                failMsg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                failMsg.setMessage("ID/PW가 틀렸습니다.");
                failMsg.show();
            }

        }

        @Override
        protected void onPostExecute(String commend) { //동작 마무리 UI 업데이트
            super.onPostExecute(commend);
            if(commend.equals("LOGINSUCCESS")){ // 로그인 성공 처리
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
                MyData.Login = true;
                startActivity(intent); //페이지 이동
                //intent.putExtra("login", true); // 회원 시작 전달
                /*
                추가 작업 필요 시(다음 페이지로 정보 전달)
                방법 : 발신 : intent.putExtra("이름", "전달 값");
                방법 : 수신 : Intent intent = getIntent();
                             String name = intent.getExtras.getString("이름");
                */
            }

            finish();
        }

        @Override
        protected void onCancelled() { // 동작 종료 시 발생
            super.onCancelled();
        }

    }
}
