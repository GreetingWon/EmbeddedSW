package com.example.termproject1;

import static android.os.SystemClock.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


public class TermProject1Activity extends AppCompatActivity {

    public static TermProject1Activity activity = null;
    public static Activity _Main_Activity;

    //public TextView mPushSwitchValue;
    public TextView mPushStateValue;
    public TextView mGameValue_1;
    public TextView mGameValue_2;
    public TextView mGameValue_3;
    public TextView mGameValue_4;
    public TextView mGameValue_5;
    public TextView mGameValue_6;
    public TextView mGameValue_7;
    public TextView mGameValue_8;

    public TextView mStrikeValue_1;
    public TextView mBallValue_1;
    public TextView mOutValue_1;
    public TextView mStrikeValue_2;
    public TextView mBallValue_2;
    public TextView mOutValue_2;
    public TextView mStrikeValue_3;
    public TextView mBallValue_3;
    public TextView mOutValue_3;
    public TextView mStrikeValue_4;
    public TextView mBallValue_4;
    public TextView mOutValue_4;
    public TextView mStrikeValue_5;
    public TextView mBallValue_5;
    public TextView mOutValue_5;
    public TextView mStrikeValue_6;
    public TextView mBallValue_6;
    public TextView mOutValue_6;
    public TextView mStrikeValue_7;
    public TextView mBallValue_7;
    public TextView mOutValue_7;
    public TextView mStrikeValue_8;
    public TextView mBallValue_8;
    public TextView mOutValue_8;

    private TextView Show_Difficulty;
    public int Buzzer_ON;

    public TextView Test_num;
    int Rand_Start = 0;
    int Rand_value;
    String Rand_Num_value ="";
    int aa =0;

    // Used to load the 'termproject1' library on application startup.
    static {
        System.loadLibrary("term-project1-jni");
    }

    @Override
    public void onBackPressed() {
        aa = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        _Main_Activity = TermProject1Activity.this;

        //mPushSwitchValue = (EditText) findViewById(R.id.my_prt_value);
        mPushStateValue = (TextView) findViewById(R.id.my_prt_string);
        mGameValue_1 = (EditText)findViewById(R.id.get_value1);
        mGameValue_2 = (EditText)findViewById(R.id.get_value2);
        mGameValue_3 = (EditText)findViewById(R.id.get_value3);
        mGameValue_4 = (EditText)findViewById(R.id.get_value4);
        mGameValue_5 = (EditText)findViewById(R.id.get_value5);
        mGameValue_6 = (EditText)findViewById(R.id.get_value6);
        mGameValue_7 = (EditText)findViewById(R.id.get_value7);
        mGameValue_8 = (EditText)findViewById(R.id.get_value8);

        mStrikeValue_1 = (EditText)findViewById(R.id.Strike_Zone_String_1);
        mBallValue_1 = (EditText)findViewById(R.id.Ball_Zone_String_1);
        mOutValue_1 = (EditText)findViewById(R.id.Out_Zone_String_1);
        mStrikeValue_2 = (EditText)findViewById(R.id.Strike_Zone_String_2);
        mBallValue_2 = (EditText)findViewById(R.id.Ball_Zone_String_2);
        mOutValue_2 = (EditText)findViewById(R.id.Out_Zone_String_2);
        mStrikeValue_3 = (EditText)findViewById(R.id.Strike_Zone_String_3);
        mBallValue_3 = (EditText)findViewById(R.id.Ball_Zone_String_3);
        mOutValue_3 = (EditText)findViewById(R.id.Out_Zone_String_3);
        mStrikeValue_4 = (EditText)findViewById(R.id.Strike_Zone_String_4);
        mBallValue_4 = (EditText)findViewById(R.id.Ball_Zone_String_4);
        mOutValue_4 = (EditText)findViewById(R.id.Out_Zone_String_4);
        mStrikeValue_5 = (EditText)findViewById(R.id.Strike_Zone_String_5);
        mBallValue_5 = (EditText)findViewById(R.id.Ball_Zone_String_5);
        mOutValue_5 = (EditText)findViewById(R.id.Out_Zone_String_5);
        mStrikeValue_6 = (EditText)findViewById(R.id.Strike_Zone_String_6);
        mBallValue_6 = (EditText)findViewById(R.id.Ball_Zone_String_6);
        mOutValue_6 = (EditText)findViewById(R.id.Out_Zone_String_6);
        mStrikeValue_7 = (EditText)findViewById(R.id.Strike_Zone_String_7);
        mBallValue_7 = (EditText)findViewById(R.id.Ball_Zone_String_7);
        mOutValue_7 = (EditText)findViewById(R.id.Out_Zone_String_7);
        mStrikeValue_8 = (EditText)findViewById(R.id.Strike_Zone_String_8);
        mBallValue_8 = (EditText)findViewById(R.id.Ball_Zone_String_8);
        mOutValue_8 = (EditText)findViewById(R.id.Out_Zone_String_8);

        Test_num = (EditText)findViewById(R.id.Test_Num);
        Show_Difficulty = findViewById(R.id.show_difficulty);


        //int Rand_value;
        int[] Rand_value_Save_Normal = new int[3];
        int[] Rand_value_Save_Hard = new int[4];
        String str_difficulty_1 = "일반";
        String str_difficulty_2 = "심화";
        String Reset_str="";

        Reset();
        //Total_game_value = "";

        //String Rand_Num_value ="";

        Intent intent = getIntent();
        String Save_difficulty = intent.getStringExtra("str_difficulty");
        Show_Difficulty.setText(Save_difficulty);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // 난이도 결정
        if(Save_difficulty.equals(str_difficulty_1)){
            Rand_value = SendRandNumNormal();

            Rand_value_Save_Normal[0] = Rand_value / 100;
            Rand_value_Save_Normal[1] = (Rand_value % 100) / 10;
            Rand_value_Save_Normal[2] = (Rand_value % 100) % 10;

            Rand_Num_value = Integer.toString(Rand_value);
        }else if(Save_difficulty.equals(str_difficulty_2)){
            Rand_value = SendRandNumHard();

            Rand_value_Save_Hard[0] = Rand_value / 1000;
            Rand_value_Save_Hard[1] = (Rand_value % 1000) / 100;
            Rand_value_Save_Hard[2] = ((Rand_value % 1000) % 100) / 10;
            Rand_value_Save_Hard[3] = ((Rand_value % 1000) % 100) % 10;

            Rand_Num_value = Integer.toString(Rand_value);
        }

        Test_num.setText(Rand_Num_value);
        ReceiveDotValue(0);

        Timer timer1 = new Timer();

        TimerTask task = new TimerTask() {

            Handler mHandler = new Handler();

            //String Total_game_value ="";
            int game_Flag = 0;
            int Save_User=0;
            int[]Save_User_Num_Normal = new int[3];
            int[]Save_User_Num_Hard = new int[4];
            int Strike_Num = 0;
            int Ball_Num = 0;
            int Out_Num = 0;
            int Out_Count = 0;
            int Success_Num = 0;
            int Fail_Flag = 0;
            //String Total_game_value = new String("");
            //int value;
            String aaaaa = "";


            public void run(){
                mHandler.postDelayed(()->{
                    int value;
                    int game_value;
                    //int game_Flag = 0;
                    String Total_game_value = new String("");
                    String str ="";
                    //char []Total_game_value = new char[3];
                    String Save_Strike_Num ="";
                    String Save_Ball_Num ="";
                    String Save_Out_Num ="";
                    String gameValue_str="";

                    int abcd=0;
                    //StringBuilder aaaaa = new StringBuilder();

                    value = DeviceOpen();

                    if(value != -1){
                        value = ReceivePushSwitchValue();
                    }

                    if(value == 8) {
                        //game_value = SendValue();
                        game_Flag+=1;
                        Strike_Num = 0;
                        Ball_Num = 0;
                        Out_Count = 0;
                        Out_Num = 0;
                        Success_Num = 0;

                        // 난이도에 따른 다른 계산법
                        if(Save_difficulty.equals(str_difficulty_1)) { // 난이도 일반
                            for (int k = 0; k < 3; k++) {
                                // Strike검사
                                if (Rand_value_Save_Normal[k] == Save_User_Num_Normal[k]) {
                                    Strike_Num++;
                                }
                                // Ball검사
                                if (k == 0) {
                                    if ((Rand_value_Save_Normal[k] == Save_User_Num_Normal[k + 1]) || (Rand_value_Save_Normal[k] == Save_User_Num_Normal[k + 2])) {
                                        Ball_Num++;
                                    }
                                } else if (k == 1) {
                                    if ((Rand_value_Save_Normal[k] == Save_User_Num_Normal[k - 1]) || (Rand_value_Save_Normal[k] == Save_User_Num_Normal[k + 1])) {
                                        Ball_Num++;
                                    }
                                } else if (k == 2) {
                                    if ((Rand_value_Save_Normal[k] == Save_User_Num_Normal[k - 1]) || (Rand_value_Save_Normal[k] == Save_User_Num_Normal[k - 2])) {
                                        Ball_Num++;
                                    }
                                }
                            }

                            // Out 검사
                            if ((Rand_value_Save_Normal[0] != Save_User_Num_Normal[0]) && (Rand_value_Save_Normal[0] != Save_User_Num_Normal[1]) && (Rand_value_Save_Normal[0] != Save_User_Num_Normal[2])) {
                                Out_Count++;
                            }
                            if ((Rand_value_Save_Normal[1] != Save_User_Num_Normal[0]) && (Rand_value_Save_Normal[1] != Save_User_Num_Normal[1]) && (Rand_value_Save_Normal[1] != Save_User_Num_Normal[2])) {
                                Out_Count++;
                            }
                            if ((Rand_value_Save_Normal[2] != Save_User_Num_Normal[0]) && (Rand_value_Save_Normal[2] != Save_User_Num_Normal[1]) && (Rand_value_Save_Normal[2] != Save_User_Num_Normal[2])) {
                                Out_Count++;
                            }
                            if (Out_Count == 3) {
                                Out_Num = 1;
                            }
                            // 정답검사
                            if ((Rand_value_Save_Normal[0] == Save_User_Num_Normal[0]) && (Rand_value_Save_Normal[1] == Save_User_Num_Normal[1]) && (Rand_value_Save_Normal[2] == Save_User_Num_Normal[2])) {
                                Success_Num = 1;
                            }else{
                                ReceiveDotValue(1);
                                BuzzerON(1);
                                sleep(1000);
                                BuzzerON(0);
                                ReceiveDotValue(0);
                            }
                        }
                        /////////////////////////////////////////  난이도 구분   ///////////////////////////////////////////////////////////////////////////////////////////
                        else if(Save_difficulty.equals(str_difficulty_2)){ // 난이도 심화
                            for (int k = 0; k < 4; k++) {
                                // Strike검사
                                if (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k]) {
                                    Strike_Num++;
                                }
                                // Ball검사
                                if (k == 0) {
                                    if ((Rand_value_Save_Hard[k] == Save_User_Num_Hard[k + 1]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k + 2]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k + 3])) {
                                        Ball_Num++;
                                    }
                                } else if (k == 1) {
                                    if ((Rand_value_Save_Hard[k] == Save_User_Num_Hard[k - 1]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k + 1]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k + 2])) {
                                        Ball_Num++;
                                    }
                                } else if (k == 2) {
                                    if ((Rand_value_Save_Hard[k] == Save_User_Num_Hard[k - 1]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k - 2]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k + 1])) {
                                        Ball_Num++;
                                    }
                                } else if(k == 3){
                                    if ((Rand_value_Save_Hard[k] == Save_User_Num_Hard[k - 1]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k - 2]) || (Rand_value_Save_Hard[k] == Save_User_Num_Hard[k - 3])) {
                                        Ball_Num++;
                                    }
                                }
                            }

                            // Out 검사
                            if ((Rand_value_Save_Hard[0] != Save_User_Num_Hard[0]) && (Rand_value_Save_Hard[0] != Save_User_Num_Hard[1]) && (Rand_value_Save_Hard[0] != Save_User_Num_Hard[2]) && (Rand_value_Save_Hard[0] != Save_User_Num_Hard[3])) {
                                Out_Count++;
                            }
                            if ((Rand_value_Save_Hard[1] != Save_User_Num_Hard[0]) && (Rand_value_Save_Hard[1] != Save_User_Num_Hard[1]) && (Rand_value_Save_Hard[1] != Save_User_Num_Hard[2]) && (Rand_value_Save_Hard[1] != Save_User_Num_Hard[3])) {
                                Out_Count++;
                            }
                            if ((Rand_value_Save_Hard[2] != Save_User_Num_Hard[0]) && (Rand_value_Save_Hard[2] != Save_User_Num_Hard[1]) && (Rand_value_Save_Hard[2] != Save_User_Num_Hard[2]) && (Rand_value_Save_Hard[2] != Save_User_Num_Hard[3])) {
                                Out_Count++;
                            }
                            if((Rand_value_Save_Hard[3] != Save_User_Num_Hard[0]) && (Rand_value_Save_Hard[3] != Save_User_Num_Hard[1]) && (Rand_value_Save_Hard[3] != Save_User_Num_Hard[2]) && (Rand_value_Save_Hard[3] != Save_User_Num_Hard[3])){
                                Out_Count++;
                            }
                            if (Out_Count == 4) {
                                Out_Num = 1;
                            }
                            // 정답검사
                            if ((Rand_value_Save_Hard[0] == Save_User_Num_Hard[0]) && (Rand_value_Save_Hard[1] == Save_User_Num_Hard[1]) && (Rand_value_Save_Hard[2] == Save_User_Num_Hard[2]) && (Rand_value_Save_Hard[3] == Save_User_Num_Hard[3])) {
                                Success_Num = 1;
                            }else{
                                ReceiveDotValue(1);
                                BuzzerON(1);
                                sleep(1000);
                                BuzzerON(0);
                                ReceiveDotValue(0);
                            }

//                            if((Success_Num == 0) && (game_Flag != 8)){
//                                BuzzerON(1);
//                                sleep(1000);
//                                BuzzerON(0);
//                            }
                        }


                        Total_game_value = "";
                        str = "";
                        gameValue_str = "";
                        Reset();
                    }


                    if(value != -1){
                        DeviceClose();
                    }


                    game_value = SendValue();
                    gameValue_str = Integer.toString(game_value);
                    Total_game_value = Total_game_value.concat(gameValue_str);
                    Save_User = Integer.parseInt(Total_game_value);


                    str = Integer.toString(value,16);

                    // 일단 현재는 3자리라고 가정했기에 100으로만 나눈것 (이런 부분도 나중에 수정해야해)
                    if(Save_difficulty.equals(str_difficulty_1)) {
                        Save_User_Num_Normal[0] = Save_User / 100;
                        Save_User_Num_Normal[1] = (Save_User % 100) / 10;
                        Save_User_Num_Normal[2] = (Save_User % 100) % 10;
                    }else if(Save_difficulty.equals(str_difficulty_2)){

                        Save_User_Num_Hard[0] = Save_User / 1000;
                        Save_User_Num_Hard[1] = (Save_User % 1000) / 100;
                        Save_User_Num_Hard[2] = ((Save_User % 1000) % 100) / 10;
                        Save_User_Num_Hard[3] = ((Save_User % 1000) % 100) % 10;
                    }


                    Save_Strike_Num = Integer.toString(Strike_Num);
                    Save_Ball_Num = Integer.toString(Ball_Num);
                    Save_Out_Num = Integer.toString(Out_Num);

                    ReceiveFndValue("1");

                    // 성공했을때
                    if(Success_Num == 1){
                        String answer = String.valueOf(Rand_value);
                        Toast.makeText(TermProject1Activity.this, "You Win!! The answer is  " + answer, Toast.LENGTH_LONG).show();
                        ReceiveDotValue(2);
                        SetMotorState(1, 1, 14);
                        sleep(2000);
                        SetMotorState(0, 1, 14);
                        ReceiveDotValue(0);
                        game_Flag = 0;

//
                        mGameValue_1.setText(Reset_str);
                        mGameValue_2.setText(Reset_str);
                        mStrikeValue_1.setText(Reset_str);
                        mBallValue_1.setText(Reset_str);
                        mOutValue_1.setText(Reset_str);
                        mGameValue_3.setText(Reset_str);
                        mStrikeValue_2.setText(Reset_str);
                        mBallValue_2.setText(Reset_str);
                        mOutValue_2.setText(Reset_str);
                        mGameValue_4.setText(Reset_str);
                        mStrikeValue_3.setText(Reset_str);
                        mBallValue_3.setText(Reset_str);
                        mOutValue_3.setText(Reset_str);
                        mGameValue_5.setText(Reset_str);
                        mStrikeValue_4.setText(Reset_str);
                        mBallValue_4.setText(Reset_str);
                        mOutValue_4.setText(Reset_str);
                        mGameValue_6.setText(Reset_str);
                        mStrikeValue_5.setText(Reset_str);
                        mBallValue_5.setText(Reset_str);
                        mOutValue_5.setText(Reset_str);
                        mGameValue_7.setText(Reset_str);
                        mStrikeValue_6.setText(Reset_str);
                        mBallValue_6.setText(Reset_str);
                        mOutValue_6.setText(Reset_str);
                        mGameValue_8.setText(Reset_str);
                        mStrikeValue_7.setText(Reset_str);
                        mBallValue_7.setText(Reset_str);
                        mOutValue_7.setText(Reset_str);
                        mStrikeValue_8.setText(Reset_str);
                        mBallValue_8.setText(Reset_str);
                        mOutValue_8.setText(Reset_str);

                        if(Save_difficulty.equals(str_difficulty_1)){
                            Rand_value = SendRandNumNormal();

                            Rand_value_Save_Normal[0] = Rand_value / 100;
                            Rand_value_Save_Normal[1] = (Rand_value % 100) / 10;
                            Rand_value_Save_Normal[2] = (Rand_value % 100) % 10;

                            Rand_Num_value = Integer.toString(Rand_value);
                        }else if(Save_difficulty.equals(str_difficulty_2)){
                            Rand_value = SendRandNumHard();

                            Rand_value_Save_Hard[0] = Rand_value / 1000;
                            Rand_value_Save_Hard[1] = (Rand_value % 1000) / 100;
                            Rand_value_Save_Hard[2] = ((Rand_value % 1000) % 100) / 10;
                            Rand_value_Save_Hard[3] = ((Rand_value % 1000) % 100) % 10;

                            Rand_Num_value = Integer.toString(Rand_value);
                        }
                        Test_num.setText(Rand_Num_value);
                        Success_Num = 0;
                    }

                    // 실패했을때
                    if(Fail_Flag == 1){
                        String answer = String.valueOf(Rand_value);
                        ReceiveDotValue(3);
                        for(int j=0;j<3;j++){
                            BuzzerON(1);
                            sleep(500);
                            BuzzerON(0);
                            sleep(500);
                        }
                        ReceiveDotValue(0);

                        Toast.makeText(TermProject1Activity.this, "You Failed... The answer is  " + answer, Toast.LENGTH_LONG).show();

                        game_Flag = 0;
                        mGameValue_1.setText(Reset_str);
                        mGameValue_2.setText(Reset_str);
                        mStrikeValue_1.setText(Reset_str);
                        mBallValue_1.setText(Reset_str);
                        mOutValue_1.setText(Reset_str);
                        mGameValue_3.setText(Reset_str);
                        mStrikeValue_2.setText(Reset_str);
                        mBallValue_2.setText(Reset_str);
                        mOutValue_2.setText(Reset_str);
                        mGameValue_4.setText(Reset_str);
                        mStrikeValue_3.setText(Reset_str);
                        mBallValue_3.setText(Reset_str);
                        mOutValue_3.setText(Reset_str);
                        mGameValue_5.setText(Reset_str);
                        mStrikeValue_4.setText(Reset_str);
                        mBallValue_4.setText(Reset_str);
                        mOutValue_4.setText(Reset_str);
                        mGameValue_6.setText(Reset_str);
                        mStrikeValue_5.setText(Reset_str);
                        mBallValue_5.setText(Reset_str);
                        mOutValue_5.setText(Reset_str);
                        mGameValue_7.setText(Reset_str);
                        mStrikeValue_6.setText(Reset_str);
                        mBallValue_6.setText(Reset_str);
                        mOutValue_6.setText(Reset_str);
                        mGameValue_8.setText(Reset_str);
                        mStrikeValue_7.setText(Reset_str);
                        mBallValue_7.setText(Reset_str);
                        mOutValue_7.setText(Reset_str);
                        mStrikeValue_8.setText(Reset_str);
                        mBallValue_8.setText(Reset_str);
                        mOutValue_8.setText(Reset_str);

                        if(Save_difficulty.equals(str_difficulty_1)){
                            Rand_value = SendRandNumNormal();

                            Rand_value_Save_Normal[0] = Rand_value / 100;
                            Rand_value_Save_Normal[1] = (Rand_value % 100) / 10;
                            Rand_value_Save_Normal[2] = (Rand_value % 100) % 10;

                            Rand_Num_value = Integer.toString(Rand_value);
                        }else if(Save_difficulty.equals(str_difficulty_2)){
                            Rand_value = SendRandNumHard();

                            Rand_value_Save_Hard[0] = Rand_value / 1000;
                            Rand_value_Save_Hard[1] = (Rand_value % 1000) / 100;
                            Rand_value_Save_Hard[2] = ((Rand_value % 1000) % 100) / 10;
                            Rand_value_Save_Hard[3] = ((Rand_value % 1000) % 100) % 10;

                            Rand_Num_value = Integer.toString(Rand_value);
                        }
                        Test_num.setText(Rand_Num_value);
                        Fail_Flag = 0;
                    }

                    switch(game_Flag){
                        case 0:
                            mGameValue_1.setText(Total_game_value);
                            ReceiveLedValue(0);
                            break;
                        case 1 :
                            mGameValue_2.setText(Total_game_value);
                            mStrikeValue_1.setText(Save_Strike_Num);
                            mBallValue_1.setText(Save_Ball_Num);
                            mOutValue_1.setText(Save_Out_Num);
                            ReceiveLedValue(1);
                            break;
                        case 2 :
                            mGameValue_3.setText(Total_game_value);
                            mStrikeValue_2.setText(Save_Strike_Num);
                            mBallValue_2.setText(Save_Ball_Num);
                            mOutValue_2.setText(Save_Out_Num);
                            ReceiveLedValue(2);
                            break;
                        case 3 :
                            mGameValue_4.setText(Total_game_value);
                            mStrikeValue_3.setText(Save_Strike_Num);
                            mBallValue_3.setText(Save_Ball_Num);
                            mOutValue_3.setText(Save_Out_Num);
                            ReceiveLedValue(3);
                            break;
                        case 4 :
                            mGameValue_5.setText(Total_game_value);
                            mStrikeValue_4.setText(Save_Strike_Num);
                            mBallValue_4.setText(Save_Ball_Num);
                            mOutValue_4.setText(Save_Out_Num);
                            ReceiveLedValue(4);
                            break;
                        case 5 :
                            mGameValue_6.setText(Total_game_value);
                            mStrikeValue_5.setText(Save_Strike_Num);
                            mBallValue_5.setText(Save_Ball_Num);
                            mOutValue_5.setText(Save_Out_Num);
                            ReceiveLedValue(5);
                            break;
                        case 6 :
                            mGameValue_7.setText(Total_game_value);
                            mStrikeValue_6.setText(Save_Strike_Num);
                            mBallValue_6.setText(Save_Ball_Num);
                            mOutValue_6.setText(Save_Out_Num);
                            ReceiveLedValue(6);
                            break;
                        case 7 :
                            mGameValue_8.setText(Total_game_value);
                            mStrikeValue_7.setText(Save_Strike_Num);
                            mBallValue_7.setText(Save_Ball_Num);
                            mOutValue_7.setText(Save_Out_Num);
                            ReceiveLedValue(7);
                            break;
                        case 8 :
                            mStrikeValue_8.setText(Save_Strike_Num);
                            mBallValue_8.setText(Save_Ball_Num);
                            mOutValue_8.setText(Save_Out_Num);
                            ReceiveLedValue(8);
                            Fail_Flag = 1;
                            break;
                        default:
                            break;
                    }


                    // 뒤로가기 버튼 눌렀을 시
                    if(aa == 1){
                        Total_game_value = "";
                        Intent intent = new Intent(TermProject1Activity.this, FirstActivity.class);
                        aa= 0;
                        game_Flag = 0;
                        Reset();
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ReceiveLedValue(8);
                        finish();
                        ActivityCompat.finishAffinity(_Main_Activity);
                        startActivity(intent);
                    }
                },100);

            } // 여기가 run 끝부분


        };
        Timer t = new Timer();
        t.schedule(task,100,100);



        //Rand_Num_value = Integer.toString(Rand_value);
    }

    /**
     * A native method that is implemented by the 'fpgapushswitch' native library,
     * which is packaged with this application.
     */

    public native int ReceivePushSwitchValue();
    public native int DeviceOpen();
    public native int DeviceClose();
    public native int ReceiveFndValue(String ptr);
    public native int SendValue();
    public native int SendRandNumNormal();
    public native int SendRandNumHard();
    public native String SetMotorState(int x, int y, int z);
    public native int Reset();
    public native int eeee();
    public native int BuzzerON(int x);
    public native String ReceiveLedValue(int x);
    public native String ReceiveDotValue(int x);
}