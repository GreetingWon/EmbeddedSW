package com.example.termproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class TermProject2Activity extends AppCompatActivity {

    public static Activity _Main_Activity;


    public TextView mPushSwitchValue;
    public TextView mPushStateValue;
    public TextView Test_num;

    String Rand_Num_value ="";
    int Rand_value;
    int score= 0;
    int Total= 0;
    int Random=0;

    static {
        System.loadLibrary("term-project1-jni"); // 나중에 수정해야될수도있음.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_project2);

        mPushSwitchValue = (EditText) findViewById(R.id.my_prt_value);
        mPushStateValue = (TextView) findViewById(R.id.my_prt_string);
        Test_num = (EditText)findViewById(R.id.Test_Num); //난수

        Rand_value = SendRandNum2(); //최초의 난수 생성
        Rand_Num_value = Integer.toString(Rand_value);
        Test_num.setText(Rand_Num_value); //최초의 난수 띄우기

        ReceiveLedValue2(7);

        switch(Rand_Num_value){ //최초의 난수에 따라 led 점등
            case "1":
                ReceiveLedValue2(0);
                break;
            case "2":
                ReceiveLedValue2(1);
                break;
            case "3":
                ReceiveLedValue2(2);
                break;
            case "4":
                ReceiveLedValue2(3);
                break;
            case "5":
                ReceiveLedValue2(4);
                break;
            case "6":
                ReceiveLedValue2(5);
                break;
            case "7":
                ReceiveLedValue2(6);
                break;
            case "8":
                ReceiveLedValue2(7);
                break;
            case "9":
                ReceiveLedValue2(8);
                break;

        }

        Random = Integer.parseInt(Rand_Num_value);
        Total = 0;

        TimerTask task = new TimerTask() {

            Handler mHandler = new Handler();

            public void run(){
                mHandler.postDelayed(()->{
                    int value;
                    int tmp;
                    int game_value;
                    String Total_game_value = "";

                    value = DeviceOpen2();

                    if(value != -1){
                        value = ReceivePushSwitchValue2();
                    }

                    game_value = SendValue2(); //스위치 입력값

                    if(game_value != 0) {
                        String gameValue_str = Integer.toString(game_value);
                        Total_game_value = Total_game_value.concat(gameValue_str);
                        Total = Integer.parseInt(Total_game_value);
                    }


                    if(value == 9) { //ok 버튼 클릭 시 동작 구현해야함(led 점등, 스위치 입력값 초기화, 난수 뒤에 붙여서 출력)
                        if(Total==Random){ //맞았을 때
                            Toast.makeText(TermProject2Activity.this, "Correct!",Toast.LENGTH_LONG).show();
                            score += 1; //점수 +1
                            Rand_value = SendRandNum2(); //난수 생성
                            //if 난수에 따른 led 점등 코드
                            Rand_Num_value = Rand_Num_value + Integer.toString(Rand_value); //기존의 난수에 새로 생성된 난수 붙임
                            Random = Integer.parseInt(Rand_Num_value); //비교를 위해 int 형으로 변환
                            Test_num.setText(Rand_Num_value); //난수 디스플레이 표기
                            char[] index = Rand_Num_value.toCharArray(); //원소로 접근하기 위해 char array로 변환

                            for (int i = 0; i < Rand_Num_value.length(); i++) { //난수에 따라 LED 점등
                                tmp = Character.getNumericValue(index[i]);
                                ReceiveLedValue2(tmp - 1);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    System.out.println("error");
                                } //1초 대기
                                ReceiveLedValue2(10);
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    System.out.println("error");
                                }
                            }
                        }
                        else if(Total!=Random){ // 틀렸을 때
                            Toast.makeText(TermProject2Activity.this, "Failed..... Score : " +score +
                                    "\n정답 : " + Rand_Num_value,Toast.LENGTH_LONG).show();
                            score = 0;
                            Rand_Num_value="";
                            Rand_value = SendRandNum2(); //최초의 난수 생성
                            Rand_Num_value = Integer.toString(Rand_value);
                            Test_num.setText(Rand_Num_value); //최초의 난수 띄우기

                            switch(Rand_Num_value){ //최초의 난수에 따라 led 점등
                                case "1":
                                    ReceiveLedValue2(0);
                                    break;
                                case "2":
                                    ReceiveLedValue2(1);
                                    break;
                                case "3":
                                    ReceiveLedValue2(2);
                                    break;
                                case "4":
                                    ReceiveLedValue2(3);
                                    break;
                                case "5":
                                    ReceiveLedValue2(4);
                                    break;
                                case "6":
                                    ReceiveLedValue2(5);
                                    break;
                                case "7":
                                    ReceiveLedValue2(6);
                                    break;
                                case "8":
                                    ReceiveLedValue2(7);
                                    break;
                                case "9":
                                    ReceiveLedValue2(8);
                                    break;

                            }
                            Random = Integer.parseInt(Rand_Num_value);
                        }
                    }

                    mPushSwitchValue.setText(Total_game_value);

                    Show2(Total);

                    if(value != -1){
                        DeviceClose2();
                    }

                },100);
            }


        };
        Timer t = new Timer();
        t.schedule(task,100,100);

    }

    public native int ReceivePushSwitchValue2();
    public native int DeviceOpen2();
    public native int DeviceClose2();
    public native int SendValue2();
    public native int SendRandNum2();
    public native String ReceiveLedValue2(int x);
    public native String Show2(int k);
}