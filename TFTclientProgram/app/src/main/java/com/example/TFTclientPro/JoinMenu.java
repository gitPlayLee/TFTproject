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

public class JoinMenu extends AppCompatActivity {
    public Socket client;
    public DataInputStream in;
    public DataOutputStream out;
    JoinManager joinPage;
    //public PrintWriter out; //서버에 출력하기 위한 스트림
    //public BufferedReader in; //입력 스트림
    StringTokenizer line; //문자 메시지 구분자
    String allMsg = null; //전체 메시지
    String sign = null; //클라이언트 신호
    String next = null; //다음 메시지
    boolean IDcontrol = false; // ID 확인
    boolean Nickcontrol = false; // TFT 닉네임 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinPage = new JoinManager();
        joinPage.execute();
    }

    public void onBackPressed() { //뒤로 가기 버튼
        try{
            out.writeUTF("ENDPAGE$"); // 종료 메시지
        }catch (IOException e){}
        joinPage.cancel(true); // 이전 액티비티로
    }

    public class JoinManager extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() { //시작 전 UI 세팅
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) { //동작 중에 작업 처리 //종료 시 리턴
            try {
                System.out.println("동작4"); // 확인
                client = SocketManager.getSocket();
            }catch(IOException e) {
                System.out.println("연결 실패4");
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

                if(sign.equals("USEPLAY")) { //사용 가능
                    IDcontrol = true;

               }else if(sign.equals("USEDID")) { //중복 아이디
                    IDcontrol = false;

                }else if(sign.equals("CLEARNICK")) { //TFT 닉네임 성공
                    Nickcontrol = true;

                }else if(sign.equals("FAILNICK")) { //TFT 닉네임 실패
                    Nickcontrol = false;

                }else if(sign.equals("JOBCLEAR")) { //확인 작업
                    if(IDcontrol == false){ //ID가 잘못된 경우
                        publishProgress("IDFALSE");

                    }else if(Nickcontrol == false){ //닉네임이 잘못된 경우
                        publishProgress("NICKFALSE");

                    }else if(IDcontrol == true && Nickcontrol == true){ //가입 조건을 만족한 경우
                        try{
                            out.writeUTF("TERMOK$"); // 가입 절차 진행 요구
                        }catch (IOException e){}

                    }

                }else if(sign.equals("JOINSUCCESS")) { //가입 성공
                    return "JOINSUCCESS";

                }else if(sign.equals("JOINFALE")) { //가입 실패
                    publishProgress("JOINFALE");

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
            if(values[0].equals("IDFALSE")){ //
                AlertDialog.Builder SubMsg = new AlertDialog.Builder(JoinMenu.this);
                SubMsg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                SubMsg.setMessage("이미 있는 '아이디' 입니다.");
                SubMsg.show();

            }else if(values[0].equals("NICKFALSE")){ //중복된 아이디
                AlertDialog.Builder failMsg = new AlertDialog.Builder(JoinMenu.this);
                failMsg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                failMsg.setMessage("잘못된 'TFT 닉네임' 입니다.");
                failMsg.show();
            }

            if(values[0].equals("JOINFALE")){ //가입 실패하는 경우
                AlertDialog.Builder SubMsg = new AlertDialog.Builder(JoinMenu.this);
                SubMsg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                SubMsg.setMessage("알 수 없는 이유로 가입이 실패했습니다.");
                SubMsg.show();

            }

        }

        @Override
        protected void onPostExecute(String Result) { //동작 마무리 UI 업데이트
            super.onPostExecute(Result);

            if(Result.equals("JOINSUCCESS")){ //가입 성공 및 시작 페이지로 나가기
                Intent intent = new Intent(getApplicationContext(), StartMenu.class);
                startActivity(intent); //페이지 이동
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
        protected void onCancelled() { // 동작 종료 시 발생 -- 뒤로가기
            super.onCancelled();
            Intent intent = new Intent(getApplicationContext(), StartMenu.class);
            startActivity(intent); //페이지 이동
            finish(); //액티비티 삭제
        }

    }

}
