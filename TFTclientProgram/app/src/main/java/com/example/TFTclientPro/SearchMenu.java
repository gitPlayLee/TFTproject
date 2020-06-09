package com.example.TFTclientPro;
//검색 페이지
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

// 저장한 정보가 보관될 리스트 필요

// 검색한 정보가 보관될 리스트 필요

public class SearchMenu extends AppCompatActivity {
    private boolean Login = false; //사용자의 로그인 여부
    public Socket client; //클라이언트 소켓 보관
    public DataInputStream in; // 수신용
    public DataOutputStream out; // 송신용
    SearchManager searchPage;
    //public PrintWriter out; //서버에 출력하기 위한 스트림
    //public BufferedReader in; //입력 스트림
    StringTokenizer line; //문자 메시지 구분자
    String allMsg = null; //전체 메시지
    String sign = null; //클라이언트 신호
    String next = null; //다음 메시지

    ArrayList<String[]>Savelist; // 매치 정보(경기 정보) - 세이브 정보

    ArrayList<String[]>Matchlist; // 매치 정보(경기 정보) - 검색
    String Summoner = null; // 소환사 이름(검색)
    String Entry = null; // 소환사 티어(검색)
    int matchNum = 0; // 매치 횟수(가져온 경기 수)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchPage = new SearchManager();
        searchPage.execute();
    }

    @Override
    public void onBackPressed() { //뒤로 가기 버튼
        try {
            out.writeUTF("ENDPAGE"); // 시작 페이지로 이동
        }catch(IOException e) {}
    }

    public class SearchManager extends AsyncTask<Void, String, String> {
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
            //Login = intent.getBooleanExtra("login:", false); // 회원 or 비회원?
            setContentView(R.layout.activity_search);
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

                if(sign.equals("SEARCHOK")) { // 검색 성공
                    Matchlist = new ArrayList<String[]>();
                    line = new StringTokenizer(next, "&&");
                    Summoner = line.nextToken();
                    Entry = line.nextToken();
                    matchNum = Integer.parseInt(line.nextToken());

                    while (true){
                        try{
                            allMsg = in.readUTF(); //메시지 올 때까지 대기
                        }catch (IOException e){ }

                        line = new StringTokenizer(allMsg, "$");
                        sign = line.nextToken();
                        next = line.nextToken();

                        if(sign.equals("FAINALLMSG")){ // 마지막 정보
                            line = new StringTokenizer(allMsg, "&&"); //밑 : 데이터 수 많큼 넣기(한 경기)
                            //Matchlist.add(new String[]{line.nextToken(), line.nextToken(), line.nextToken() ...})
                            break;

                        }else if(sign.equals("NEWMSG")){ //정보 계속
                            line = new StringTokenizer(allMsg, "&&"); //밑 : 데이터 수 많큼 넣기(한 경기)
                            //Matchlist.add(new String[]{line.nextToken(), line.nextToken(), line.nextToken() ...})

                        }else{
                            // 잘못된 결과가 올 경우 알림
                            System.out.println("정보 절달 Miss");
                        }
                    }
                    //검색 결과를 받아오는 코드 필요
                    /*
                      String Summoner = null; // 소환사 이름(검색)
                      String Entry = null; // 소환사 티어(검색)
                      int matchNum = 0; // 매치 횟수(가져온 경기 수)

                    */

                    publishProgress("SEARCHOK");

                }else if(sign.equals("SEARCHFAIL")) { // 검색 실패
                    publishProgress("SEARCHFAIL");

                }else if(sign.equals("SAVEPAGE")) { // 저장 페이지로 가기
                    publishProgress("SAVEPAGE");

                }else if(sign.equals("ENDPAGE")) { // 뒤로가기? - 페이지 나가기
                    return "ENDPAGE";

                }else {
                    //질못된 전송이 있을 경우 - return 하면 안 됨
                }

            }
        }

        @Override
        protected void onProgressUpdate(String... values) { //동작 중에 UI 업데이트
            super.onProgressUpdate(values);
            if(values[0].equals("SEARCHOK")){ // 검색 성공
                // 검색 성공 시 UI 변경 이벤트 처리
                Intent intent = new Intent(getApplicationContext(), ResultList.class);
                intent.putExtra("Match", Matchlist);
                startActivity(intent);

            }else if(values[0].equals("SEARCHFAIL")){ //검색 실패
                // 검색 실패 시 메시지 띄위기
                Toast failToast = Toast.makeText(getApplicationContext(),
                        "닉네임을 찾을 수 없습니다.", Toast.LENGTH_SHORT);

            }else if(values[0].equals("SAVEPAGE")){ //전적 저장 페이지로 가기
                Intent intent = new Intent(getApplicationContext(), SaveData.class);
                startActivity(intent);
                //보낼 정보 작성성

               startActivity(intent);

            }else if(values[0].equals("MYPAGE")){ //나의 정보 페이지로 가기 <-아직


            }
        }

        @Override
        protected void onPostExecute(String Result) { //동작 마무리 UI 업데이트
            super.onPostExecute(Result);
            if(Result.equals("ENDPAGE")){ //뒤로 가기 - 페이지 나가기
                //Login = false;
                MyData.Login = false;
                Intent intent = new Intent(getApplicationContext(), StartMenu.class);
                startActivity(intent);

            }

        }

        @Override
        protected void onCancelled() { // 동작 종료 시 발생
            super.onCancelled();
        }

    }
}
