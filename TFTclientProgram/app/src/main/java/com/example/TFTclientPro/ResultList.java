package com.example.TFTclientPro;
// 검색 결과 페이지 - 경기 10개
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

public class ResultList extends AppCompatActivity {
    ArrayList<String[]> Matchlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultlist);
    }

    // 상세 버튼 클릭 시 이동하는 기능 넣기 = <각 버튼을 리스트로 묶을 수 있나?>
    // 방법1 : 버튼 클릭 시 해당 버튼의 숫자(0~9)가져옴, Matchlist[?]을 통해 리스트 하나 통째로 보내기?
    // 방법2 : 버튼 클릭 시 이중 리스트를 보내고, 숫자를 받아서 해당 숫자에 접근해서 값을 가져옴

    /*
    버튼 클릭 시 이동하는 것
    Intent intent = new Intent(getApplicationContext(), SaveData.class);
    startActivity(intent);

    */


    @Override
    public void onBackPressed() { //뒤로 가기 버튼
        super.onBackPressed(); // 이전 액티비티로 돌아가기

    }
}
