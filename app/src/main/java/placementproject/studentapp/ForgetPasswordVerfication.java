package placementproject.studentapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.CountDownTimer;
import android.widget.TextView;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgetPasswordVerfication extends AppCompatActivity {
    EditText otpET;
    TextView counterView;
    String valid_otp;
    int random ,otp;

    String valid_contact;
    Intent intent;
    String FORMAT = "%02d";
    Button resend;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_verification);

        otpET       =(EditText)findViewById(R.id.OTP);
        counterView =(TextView) findViewById(R.id.counter);
        resend =(Button) findViewById(R.id.resend);
        intent = getIntent();
        random = intent.getExtras().getInt("random");
        valid_contact = intent.getExtras().getString("contact");
        counter();
    }
    void counter(){
        resend.setClickable(false);
        new CountDownTimer(30000,1000) {
            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                counterView.setText("Resend OTP in " + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+" seconds");
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                resend.setClickable(true);
                resend.setVisibility(View.VISIBLE);
                counterView.setText("Click on Resend");
            }
        }.start();
    }
    public void confirm(View view) {
        valid_otp = otpET.getText().toString().trim();
        otp = Integer.valueOf(valid_otp);
        if (random == otp) {
            startActivity(new Intent(this,ForgetPasswordUpdatation.class));
        } else {
            alertDialog("Wrong One Time Password");
        }
    }
    private void alertDialog(String mess) {
        SweetAlertDialog dailog = new SweetAlertDialog(this , SweetAlertDialog.ERROR_TYPE);
                dailog.setTitleText("Oops...")
                .setContentText(mess)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void resend(View view) {
        final int min = 2000;
        final int max = 9000;
        Random r = new Random();
        random = r.nextInt((max - min) + 1) + min;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(valid_contact, null, "Your One Time Password (OTP) is " + String.valueOf(random)
                +" for password updation.\nThank you", null, null);
        counter();
    }
}
