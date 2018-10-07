package placementproject.studentapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Random;

import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUp extends AppCompatActivity {
    EditText name, contact, email, password, roll_no;
    String valid_token, valid_contact, valid_name, valid_email, valid_password, valid_roll_no;
    private SweetAlertDialog pDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.name);
        contact = (EditText) findViewById(R.id.mobile_no);
        email = (EditText) findViewById(R.id.sign_up_email);
        password = (EditText) findViewById(R.id.signup_password);
        roll_no = (EditText) findViewById(R.id.roll_no);
    }

    public void submit(View view) {
        valid_name = name.getText().toString().trim();
        valid_email = email.getText().toString().trim();
        valid_password = password.getText().toString().trim();
        valid_contact = contact.getText().toString().trim();
        valid_roll_no = roll_no.getText().toString().trim();

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        valid_token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "Token: " + valid_token);

        if (valid_name.isEmpty()) {
            alertDialog("Invalid Name");
        } else if (valid_roll_no.isEmpty() || valid_roll_no.length() != 9) {
            alertDialog("Invalid Registration Number");
        } else if (valid_email.isEmpty() || !isEmailValid(valid_email)) {
            alertDialog("Invalid Email Address");
        } else if (valid_contact.isEmpty() || valid_contact.length() != 10) {
            alertDialog("Invalid Mobile number");
        } else if (valid_password.isEmpty() || valid_password.length() < 6) {
            password.setError("at least 6 character");
            alertDialog("Invalid Password");
        } else if (!isConnected2()) {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            class AddUser extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (!s.isEmpty()) {
                        pDialog.setTitleText("Successfully Registered");
                        pDialog.showContentText(false);
                        pDialog.setConfirmText("OK");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                                Intent intent = new Intent(SignUp.this, Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    } else {
                        if (!isConnected2()) {
                            Toast.makeText(SignUp.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            new AddUser().execute();
                        }
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Configuration.KEY_USER_NAME, valid_name);
                    params.put(Configuration.KEY_USER_EMAIL, valid_email);
                    params.put(Configuration.KEY_USER_TOKEN, valid_token);
                    params.put(Configuration.KEY_USER_ROLLNO, valid_roll_no);
                    params.put(Configuration.KEY_USER_CONTACT, valid_contact);
                    params.put(Configuration.KEY_USER_PASSWORD, valid_password);

                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Configuration.URL_ADD, params);

                }
            }
            AddUser adduser = new AddUser();
            adduser.execute();

            pDialog = new SweetAlertDialog(SignUp.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.red1);
            pDialog.setTitleText("Processing...");
            pDialog.setContentText("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void alertDialog(String mess) {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("Oops...")
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

    public boolean isConnected2() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();

    }
}
