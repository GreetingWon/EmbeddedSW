package com.example.termproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FirstActivity extends AppCompatActivity {

    TermProject1Activity MA = (TermProject1Activity) TermProject1Activity._Main_Activity;
    public static FirstActivity abc;

    private static Button imageButton;
    private static String str_game;
    private static String str_difficulty;
    private static RadioGroup radio_g_1;
    private static RadioGroup radio_g_2;
    private static RadioButton radio_b_1;
    private static RadioButton radio_b_2;
    String str1 = "숫자야구";
    String str2 = "Memory Museum";
    String str3 = "일반";
    String str4 = "심화";
    int activity_Count = 0;
    int aa=0;

    //public static TermProject1Activity activity = null;
    public static Activity SecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        abc = this;
        ReceiveLedValue(8);

        radio_g_1 = (RadioGroup)findViewById(R.id.radioGroup1);
        radio_g_2 = (RadioGroup)findViewById(R.id.radioGroup2);
        imageButton = (Button) findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int selected_game = radio_g_1.getCheckedRadioButtonId();
                int selected_difficulty = radio_g_2.getCheckedRadioButtonId();

                radio_b_1 = (RadioButton)findViewById(selected_game);
                radio_b_2 = (RadioButton)findViewById(selected_difficulty);
                str_game = radio_b_1.getText().toString();
                str_difficulty = radio_b_2.getText().toString();

//                if(aa!=0){
//                    MA.finish();
//                }


                // 게임선택
                if(str1.equals(str_game)){ // 숫자야구선택
                    Intent intent = new Intent(FirstActivity.this, TermProject1Activity.class);
                    // 난이도 선택 (일반, 심화)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    if(str3.equals(str_difficulty)){ // 일반선택
                        intent.putExtra("str_difficulty",str_difficulty);
                    }else if(str4.equals(str_difficulty)){ // 심화선택
                        intent.putExtra("str_difficulty",str_difficulty);
                    }
//                    intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //abc.finish();
                    finish();
                }else if(str2.equals(str_game)){ // 다른게임선택
                    Intent intent = new Intent(FirstActivity.this, TermProject2Activity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }

                activity_Count++;
                //intent.putExtra("str",str);
                aa++;
            }
        });
    }

    public native String ReceiveLedValue(int x);
}