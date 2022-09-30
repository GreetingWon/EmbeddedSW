#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>
#include <cstdlib>
#include <time.h>
#include <stdlib.h>

#define PUSH_SWITCH_DEVICE "/dev/fpga_push_switch"
#define FND_Device "/dev/fpga_fnd"
#define STEP_DEVICE "/dev/fpga_step_motor"
#define BUZZER_DEVICE "/dev/fpga_buzzer"
#define LED_DEVICE "/dev/fpga_led"
#define DOT_DEVICE "/dev/fpga_dot"

#define MAX_BUTTON 9
#define MAX_DIGIT 4
int gFd=-1;
int retval;
char PW[4] = {};
int String_Size=0;
int Switch_Count = 0;
int Rand_Num_Count;
int abc;
unsigned char push_sw_buff[MAX_BUTTON];
int gFd2=-1;
int retval2;
char PW2[20] = "";
int Switch_Count2 = 0;
int Wrong_Num_Count = 0;
int dif_Check = 0;


unsigned char fpga_number[4][10] = {
        {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},// 초기화
        {0x41, 0x22, 0x36, 0x14, 0x08, 0x08, 0x14, 0x36, 0x22, 0x41},// X
        {0x07, 0x07, 0x07, 0x07, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f},// 따봉
        {0x7f, 0x40, 0x40, 0x7c, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40}// Fail
};



int fpga_dot(int x){
    int i;
    int dev;
    size_t str_size;

    str_size = sizeof(fpga_number[x]);

    dev = open(DOT_DEVICE, O_RDWR);

    if(dev<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", x);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", x);

        switch(x){
            case 0:
                write(dev,fpga_number[0],str_size);
                break;
            case 1:
                write(dev,fpga_number[1],str_size);
                break;
            case 2:
                write(dev,fpga_number[2],str_size);
                break;
            case 3:
                write(dev,fpga_number[3],str_size);
                break;
        }
        close(dev);
    }
    return 0;
}

int fpga_led(int x){
    int dev;
    unsigned char data;
    unsigned char retval;

    unsigned char val[] = {0xff,0xfe,0xfc,0xf8,0xf0,0xe0,0xc0,0x80,0x00};

    dev = open(LED_DEVICE,O_RDWR);
    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error","Driver=%d",x);
    }else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success","Driver=%d",x);
        write(dev,&val[x],sizeof(unsigned char));
        close(dev);
    }
    return 0;
}


int fpga_buzzer(int x){
    int dev;
    unsigned char data;
    unsigned char retval;

    unsigned char aa[] = "cabdA";
    data = (char)x;

    dev = open(BUZZER_DEVICE, O_RDWR);
    if(dev<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    } else {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", dev);
        write(dev, &data, 1);

//        for(int i =0;i<4;i++){
//            write(dev, &aa[i], 1);
//            sleep(1);
//        }
//        write(dev, "0", 1);
        close(dev);
        return 0;
    }

}

int fpga_step_motor(int action, int direction, int speed)
{
    int i;
    int dev;
    int str_size;

    unsigned char motor_state[3];

    memset(motor_state,0,sizeof(motor_state));


    motor_state[0] = (unsigned char)action;
    motor_state[1] = (unsigned char)direction;
    motor_state[2] = (unsigned char)speed;

    dev = open(STEP_DEVICE, O_RDWR);
    if(dev<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", dev);
        write(dev, motor_state, sizeof(motor_state));

        __android_log_print(ANDROID_LOG_INFO, "debug 1", "Driver = %d", dev);
        close(dev);
    }
    return 0;
}


int fpga_fnd(const char* str){
    int dev;
    unsigned char data[4];
    unsigned char retval;
    int i;
    int str_size;

    memset(data,0,sizeof(data));

    str_size = (strlen(str));

    if(str_size > MAX_DIGIT){
        str_size = MAX_DIGIT;
    }

    for(i=0;i<str_size;i++){
        if((str[i]<0x30) || (str[i])>0x39){
            return 1;
        }
        data[i] = str[i] - 0x30;
    }
    dev = open(FND_Device,O_RDWR);

    if(dev<0){
        __android_log_print(ANDROID_LOG_INFO,"Device Open Error","Device=%s",str);
        return -1;
    }
    else{
        __android_log_print(ANDROID_LOG_INFO,"Device Open Success","aa=%s",str);
        //write(dev,&data,4);
        write(dev,&data,4);
        close(dev);
        return 0;
    }
}

int fpga_push_open(void){
    int dev;

    dev = open(PUSH_SWITCH_DEVICE, O_RDWR);

    if(dev<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    } else {
        gFd = dev;
    }
    return 0;
}



int fpga_push_close(void){

    if(gFd<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Not Open!", "Driver = %d", gFd);
        return 0;
    } else {
        close(gFd);
        __android_log_print(ANDROID_LOG_INFO, "Device Close", "Driver = %d", gFd);
        return  -1;
    }
}


int fpga_push_switch(void){

    int i;
    int u;
    int dev;
    size_t buff_size;
    //int retval;

    //unsigned char push_sw_buff[MAX_BUTTON];

    if(gFd<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error!", "Driver = %d", gFd);
        return -1;
    } else {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "bb = %d", gFd);
        buff_size = sizeof(push_sw_buff);

        __android_log_print(ANDROID_LOG_INFO, "debug 1", "Driver = %d", gFd);
        read(gFd, &push_sw_buff, buff_size);

        __android_log_print(ANDROID_LOG_INFO, "debug 2", "Driver = %d", gFd);

        retval = 0;

       // if (Switch_Count < 4){


            for (i = 0; i < MAX_BUTTON; i++) {
                if (push_sw_buff[i] != 0) {
                    //retval |= 0x1 << i;
                    switch (i) {
                        case 0 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 1;
                                    PW[Switch_Count] = '1';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 1;
                                    PW[Switch_Count] = '1';
                                }
                            }
                            break;
                        case 1 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 2;
                                    PW[Switch_Count] = '2';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 2;
                                    PW[Switch_Count] = '2';
                                }
                            }
                            break;
                        case 2 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 3;
                                    PW[Switch_Count] = '3';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 3;
                                    PW[Switch_Count] = '3';
                                }
                            }
                            break;
                        case 3 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 4;
                                    PW[Switch_Count] = '4';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 4;
                                    PW[Switch_Count] = '4';
                                }
                            }
                            break;
                        case 4 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 5;
                                    PW[Switch_Count] = '5';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 5;
                                    PW[Switch_Count] = '5';
                                }
                            }
                            break;
                        case 5 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 6;
                                    PW[Switch_Count] = '6';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 6;
                                    PW[Switch_Count] = '6';
                                }
                            }
                            break;
                        case 6 :
                            if(dif_Check == 1) {
                                if (Switch_Count < 3) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 7;
                                    PW[Switch_Count] = '7';
                                }
                            }else if(dif_Check == 2){
                                if (Switch_Count < 4) { // 이거 난이도에 따라 보통이면 3까지, 심화면 4까지 받도록 만들어야해
                                    retval = 7;
                                    PW[Switch_Count] = '7';
                                }
                            }
                            break;
                        case 7 :
                            retval = 8;

                            for (int p = 0; p < 9; p++) {
                                PW[p] = ' ';
                                //strcpy(PW,"");
                            }
                            Switch_Count = 0;
                            abc = 1;
                            break;
                        case 8 :
                            //retval = 9;
                            Switch_Count -= 2;
                            PW[Switch_Count + 1] = '0';
                            break;
                        default :
                            break;
                    }


                    if (i != 7) {
                        Switch_Count++;
                        push_sw_buff[i] = 0;
                        //Wrong_Num_Count=0;
                    }
                    for (int k = 0; k < 1000000; k++) {
                        // 여기는 딜레이 주는 부분 (채터링방지)
                    }
                }
            }

       // }
    }
    return retval;
}


int Make_RandNum_Normal(void){
    srand(time(NULL)); // 매번 다른 시드값 생성
    int random[3] = {0};
    char random_char[3] = "";
    int Send_Num = 0;

    dif_Check = 1;

    for(int i=0;i<3;i++){ // 일단 현재는 3자리로 생성하기 (이 부분에 추후 난이도에 따라 값 바꿔줘야해)
        random[i] = rand() % 7 + 1; // 난수 생성
        //strcat(random_char[i], (char) random[i]);
        random_char[i] = (char) random[i];
        if(i==1){
            if(random[i] == random[i-1]){
                i--;
            }
        }else if(i==2){
            if((random[i] == random[i-1]) || (random[i] == random[i-2])){
                i--;
            }
        }
//        else if(i==3){
//            if((random[i] == random[i-1]) || (random[i] == random[i-2])|| (random[i] == random[i-3])){
//                i--;
//            }
//        }
    }
    Send_Num = ((int)random_char[0]) * 100 + ((int)random_char[1]) * 10 + ((int)random_char[2]);

    return Send_Num;
}

int Make_RandNum_Hard(void){
    srand(time(NULL)); // 매번 다른 시드값 생성
    int random[4] = {0};
    char random_char[4] = "";
    int Send_Num = 0;

    dif_Check = 2;

    for(int i=0;i<4;i++){ // 일단 현재는 3자리로 생성하기 (이 부분에 추후 난이도에 따라 값 바꿔줘야해)
        random[i] = rand() % 7 + 1; // 난수 생성
        //strcat(random_char[i], (char) random[i]);
        random_char[i] = (char) random[i];
        if(i==1){
            if(random[i] == random[i-1]){
                i--;
            }
        }else if(i==2){
            if((random[i] == random[i-1]) || (random[i] == random[i-2])){
                i--;
            }
        }else if(i==3){
            if((random[i] == random[i-1]) || (random[i] == random[i-2])|| (random[i] == random[i-3])){
                i--;
            }
        }
    }
    Send_Num = ((int)random_char[0]) * 1000 + ((int)random_char[1]) * 100 + ((int)random_char[2]* 10) + ((int)random_char[3]);

    return Send_Num;
}

int Make_RandNum2(void){ //난수 하나 생성
    int Send_Num = 0;

    LOOP:
    srand(time(NULL)); // 매번 다른 시드값 생성
    Send_Num = rand() % 10;

    if(Send_Num == 0 || Send_Num == 9) goto LOOP;

    return Send_Num;
}

int fpga_push_switch2(void){

    int i;
    int dev;
    size_t buff_size;
    //int retval;

    unsigned char push_sw_buff[MAX_BUTTON];

    if(gFd2<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error!", "Driver = %d", gFd2);
        return -1;
    } else {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", gFd2);
        buff_size = sizeof(push_sw_buff);

        __android_log_print(ANDROID_LOG_INFO, "debug 1", "Driver = %d", gFd2);
        read(gFd2, &push_sw_buff,buff_size);

        __android_log_print(ANDROID_LOG_INFO, "debug 2", "Driver = %d", gFd2);

        retval2 = 0;

        for(i=0;i<MAX_BUTTON;i++){
            if(push_sw_buff[i] != 0){
                //retval |= 0x1 << i;
                switch(i){
                    case 0 :
                        retval2 = 1;
                        PW2[Switch_Count2] = '1';
                        break;
                    case 1 :
                        retval2 = 2;
                        PW2[Switch_Count2] = '2';
                        break;
                    case 2 :
                        retval2 = 3;
                        PW2[Switch_Count2] = '3';
                        break;
                    case 3 :
                        retval2 = 4;
                        PW2[Switch_Count2] = '4';
                        break;
                    case 4 :
                        retval2 = 5;
                        PW2[Switch_Count2] = '5';
                        break;
                    case 5 :
                        retval2 = 6;
                        PW2[Switch_Count2] = '6';
                        break;
                    case 6 :
                        retval2 = 7;
                        PW2[Switch_Count2] = '7';
                        break;
                    case 7 :
                        retval2 = 8;
                        PW2[Switch_Count2] = '8';
                        break;
                    case 8 :
                        retval2 = 9;
                        for(int i =0; i < strlen(PW2);i++){
                            PW2[i] = ' ';
                        }

                        Switch_Count2 = 0;
                        break;
                    default :
                        break;
                }
                Switch_Count2++;
                for(int k=0;k<100000;k++){
                    // 여기는 딜레이 주는 부분 (채터링방지)
                }
            }
        }
    }
    return retval2;
}

int fpga_push_open2(void){
    int dev;

    dev = open(PUSH_SWITCH_DEVICE, O_RDWR);

    if(dev<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    } else {
        gFd2 = dev;
    }
    return 0;
}


int fpga_push_close2(void){

    if(gFd2<0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Not Open!", "Driver = %d", gFd2);
        return 0;
    } else {
        close(gFd2);
        __android_log_print(ANDROID_LOG_INFO, "Device Close", "Driver = %d", gFd2);
        return  -1;
    }
}

int fpga_led2(int x){
    int dev;
    unsigned char data;
    unsigned char retval;

    unsigned char val[] = {0x80,0x40,0x20,0x10,0x08,0x04,0x02,0x01,0x00,0xFF};

    dev = open(LED_DEVICE,O_RDWR);

    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO,"Device Open Error", "Driver = %d",x);
    } else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", x);
        write(dev, &val[x], sizeof(unsigned  char));
        close(dev);
    }
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_ReceivePushSwitchValue(
        JNIEnv* env,
        jobject thiz) {
    int retval;
    retval = fpga_push_switch();
    return retval;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_DeviceOpen(
        JNIEnv* env,
        jobject thiz) {
    int Push_Open;
    Push_Open = fpga_push_open();
    return Push_Open;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_DeviceClose(
        JNIEnv* env,
        jobject thiz) {
    int Push_Close;
    Push_Close = fpga_push_close();
    return Push_Close;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_ReceiveFndValue(JNIEnv* env,jobject thiz, jstring val) {

    jint result;

    const char * str = (*env).GetStringUTFChars(val,NULL);

    //__android_log_print(ANDROID_LOG_INFO,"FpgaFndExample","value = %s",str);

    //if(strlen(PW) <= 2) {
        result = fpga_fnd(PW);
        if(abc == 1){
            fpga_fnd("0000");
            abc = 0;
        }
        //(*env).ReleaseStringUTFChars(val,str);
    //}


    return result;

}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_SendValue(JNIEnv* env,jobject thiz) {

    jint result;

    //const char * str = (*env).GetStringUTFChars(val,NULL);

    //__android_log_print(ANDROID_LOG_INFO,"FpgaFndExample","value = %s",str);

    //result += PW[Switch_Count];
    result = atoi(PW);


    //(*env).ReleaseStringUTFChars(val,str);

    return result;

}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_SendRandNumNormal(JNIEnv* env,jobject thiz) {

    jint result;

    result = Make_RandNum_Normal();

    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_SendRandNumHard(JNIEnv* env,jobject thiz) {

    jint result;

    result = Make_RandNum_Hard();

    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_termproject1_TermProject1Activity_SetMotorState(
        JNIEnv* env,jobject thiz,jint act,jint dir,jint spd)
{
    //__android_log_print(ANDROID_LOG_INFO, "FpgaStepMotorExample", "SetMotor");
    fpga_step_motor(act, dir, spd);

    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_Reset(JNIEnv* env,jobject thiz) {

    jint result;


    for (int p = 0; p < 9; p++) {
        PW[p] = ' ';
    }
    Switch_Count = 0;
    fpga_fnd("0000");

    //gFd=-1;
    retval=0;
    PW[4] = {};
    String_Size=0;
    Rand_Num_Count=0;
    abc=0;


    return result;

}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_eeee(JNIEnv* env,jobject thiz) {

    jint result = 0;
    int i=0;

    for(i=0;i<MAX_BUTTON;i++) {
        if (push_sw_buff[i] != 0) {
            result = 1;
        }
    }

    return result;

}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject1Activity_BuzzerON(JNIEnv* env,jobject thiz, jint val) {
    jint result;
    //
    // __android_log_print(ANDROID_LOG_INFO, "FpgaBuzzerExample","value=%d",val);
    result = fpga_buzzer(val);

    return result;
}

extern "C"
jstring Java_com_example_termproject1_TermProject1Activity_ReceiveLedValue(JNIEnv *env, jobject thiz, jint val) {

    __android_log_print(ANDROID_LOG_INFO,"FpgaLedJniExample","led value=%d",val);
    fpga_led(val);

    return NULL;
}



/*  Termproject2 관련내용  */
extern "C"  jint
Java_com_example_termproject1_TermProject2Activity_ReceivePushSwitchValue2(
        JNIEnv* env,
        jobject thiz) {
    //int retval;
    retval2 = fpga_push_switch2();
    return retval2;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_termproject1_TermProject2Activity_DeviceOpen2(
        JNIEnv* env,
        jobject thiz) {
    int Push_Open;
    Push_Open = fpga_push_open2();
    return Push_Open;
}

extern "C"  jint
Java_com_example_termproject1_TermProject2Activity_DeviceClose2(
        JNIEnv* env,
        jobject thiz) {
    int Push_Close;
    Push_Close = fpga_push_close2();
    return Push_Close;
}

extern "C" jint
Java_com_example_termproject1_TermProject2Activity_SendValue2(JNIEnv* env, jobject thiz) {

    jint result;

    result = atoi(PW2);

    return result;

}

extern "C" jint Java_com_example_termproject1_TermProject2Activity_SendRandNum2(JNIEnv* env, jobject thiz) {

    jint result;

    result = Make_RandNum2();

    return result;

}

extern "C" jstring
Java_com_example_termproject1_TermProject2Activity_ReceiveLedValue2(JNIEnv* env, jobject thiz, jint val) {

    __android_log_print(ANDROID_LOG_INFO,"FpgaLedJniExample", "led value = %d", val);
    fpga_led2(val);

    return NULL;

}

extern "C" jstring Java_com_example_termproject1_TermProject2Activity_Show2(JNIEnv* env, jobject thiz, jint a){

    __android_log_print(ANDROID_LOG_INFO, "Total_game_value","VALUE = %d", a );
    return NULL;
}


extern "C" jstring
Java_com_example_termproject1_FirstActivity_ReceiveLedValue(JNIEnv* env, jobject thiz, jint val) {

    __android_log_print(ANDROID_LOG_INFO,"FpgaLedJniExample", "led value = %d", val);
    fpga_led(val);

    return NULL;

}

extern "C" JNIEXPORT jstring JNICALL
        Java_com_example_termproject1_TermProject1Activity_ReceiveDotValue(
        JNIEnv* env,
        jobject thiz, jint val) {

    fpga_dot(val);
    return 0;
}

