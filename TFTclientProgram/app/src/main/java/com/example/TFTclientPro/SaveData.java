package com.example.TFTclientPro;
//저장한 전적 가져오기 페이지
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

public class SaveData extends AppCompatActivity {

    ArrayList<String[]> Matchlist; //경기 정보
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedata);


        //뒤로 가기 시 task 강제 종료 필요
    }

    @Override
    public void onBackPressed() { //뒤로 가기 버튼
        super.onBackPressed(); // 이전 액티비티로 돌아가기

    }

}
