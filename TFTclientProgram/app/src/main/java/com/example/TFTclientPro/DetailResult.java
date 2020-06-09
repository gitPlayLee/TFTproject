package com.example.TFTclientPro;
//상세 정보 페이지
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.*;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DetailResult extends AppCompatActivity {
    private boolean Login = false; //사용자의 로그인 여부
    public Socket client; //클라이언트 소켓 보관
    public DataInputStream in; // 수신용
    public DataOutputStream out; // 송신용
    DetailManager detail;
    //public PrintWriter out; //서버에 출력하기 위한 스트림
    //public BufferedReader in; //입력 스트림
    StringTokenizer line; //문자 메시지 구분자
    String allMsg = null; //전체 메시지
    String sign = null; //클라이언트 신호
    String next = null; //다음 메시지
    ArrayList<String[]> Matchlist; //경기 정보
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detail = new DetailManager();
        detail.execute();
    }

    @Override
    public void onBackPressed() { //뒤로 가기 버튼
        detail.cancel(true); // 이전 액티비티로
    }

    public class DetailManager extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() { //시작 전 UI 세팅
            super.onPreExecute();
            Intent intent = getIntent();
                /*
                추가 작업 필요 시(다음 페이지로 정보 전달)
                방법 : 발신 : intent.putExtra("이름", "전달 값");
                방법 : 수신 : Intent intent = getIntent();
                             String name = intent.getExtras.getString("이름");
                */

            //이전 액티비티로부터 로그인 여부를 전달 받아 설정
            Login = intent.getBooleanExtra("login:", false); // 회원 or 비회원?




            setContentView(R.layout.activity_detailresult); //페이지 세팅
        }

        @Override
        protected String doInBackground(Void... arg0) { //동작 중에 작업 처리 //종료 시 리턴
            try {
                System.out.println("동작7"); // 확인
                client = SocketManager.getSocket();
            } catch (IOException e) {
                System.out.println("연결 실패7");
            }

            if (client != null) {
                try {
                    /*out = new PrintWriter(client.getOutputStream(), true);
                    InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    in = new BufferedReader(inputStreamReader);*/
                    out = new DataOutputStream(client.getOutputStream());
                    in = new DataInputStream(client.getInputStream());
                } catch (IOException e) {
                }
            }

            while (true) { //뒤로 가기 시 task 강제 종료 필요
                try {
                    allMsg = in.readUTF(); //메시지 올 때까지 대기
                } catch (IOException e) {}

                line = new StringTokenizer(allMsg, "$");
                sign = line.nextToken();
                next = line.nextToken();

                if (sign.equals("DATASAVE")) { // 저장 성공
                    publishProgress("DATASAVE");

                } else if (sign.equals("SAVEFAIL")) { // 저장 실패
                    publishProgress("SAVEFAIL");

                } else {
                    //질못된 전송이 있을 경우 - return 하면 안 됨
                }
            }
        }
        @Override
        protected void onProgressUpdate(String... values) { //동작 중에 UI 업데이트
            super.onProgressUpdate(values);
            if(values[0].equals("SEARCHFAIL")){ //저장 성공
                Toast failToast = Toast.makeText(getApplicationContext(),
                        "Save Complete", Toast.LENGTH_SHORT);

            }else if(values[0].equals("SEARCHFAIL")){ //저장 실패
                Toast failToast = Toast.makeText(getApplicationContext(),
                        "Save Fail", Toast.LENGTH_SHORT);

            }
        }

        @Override
        protected void onPostExecute(String Result) { //동작 마무리 UI 업데이트
            super.onPostExecute(Result);

        }

        @Override
        protected void onCancelled() { // 동작 종료 시 발생
            super.onCancelled();
            finish();
        }

    }
}
