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
//https://coding-factory.tistory.com/203 액티비티 이동에 관한 자료

public class MainActivity extends AppCompatActivity{
    public Socket client;
    public DataInputStream in;
    public DataOutputStream out;
    //public PrintWriter out; //서버에 출력하기 위한 스트림
    //public BufferedReader in; //입력 스트림

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lodingManager operator = new lodingManager();
        operator.execute();

    }

    public class lodingManager extends AsyncTask<Void, String, Void>{
        @Override
        protected void onPreExecute() { //시작 전 UI 세팅
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) { //동작 중에 작업 처리 //종료 시 리턴
            try {
                client = SocketManager.getSocket();
                System.out.println("서버 연결 동작"); // 확인
            }catch(IOException e) {
                System.out.println("연결 실패1");
            }

            if(client != null){
                publishProgress("PageOpen");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) { //동작 중에 UI 업데이트
            super.onProgressUpdate(values);
            System.out.println(values);
            if(values[0].equals("PageOpen")){ //어플을 시작했을 경우
                System.out.println("Start 페이지 이동");
                Intent intent = new Intent(getApplicationContext(), StartMenu.class);
                startActivity(intent);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) { //동작 마무리 UI 업데이트
            super.onPostExecute(aVoid);
            System.out.println("접속 스레드 종료1");
            finish();
        }

        @Override
        protected void onCancelled() { // 동작 종료 시 발생
            super.onCancelled();
            System.out.println("접속 스레드 종료2");
        }

    }

}