package placementproject.studentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    EditText rollno,password;
    String valid_password, valid_rollno;
    Animation anim_logo,anim_email_pass,anim_login_sign,anim_version;
    ImageView anim1;
    LinearLayout anim2;
    LinearLayout anim3;
    SharedPreferences prefs;
    TextView anim0;
    private SweetAlertDialog pDialog = null;
    public static final String preference="UserData";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        rollno      = (EditText) findViewById(R.id.login_roll_no);
        password    = (EditText) findViewById(R.id.login_password);
        anim0       = (TextView) findViewById(R.id.version);
        anim1       = (ImageView) findViewById(R.id.logo);
        anim2       = (LinearLayout) findViewById(R.id.relative_layout_1);
        anim3       = (LinearLayout) findViewById(R.id.linear_layout1);
        anim_version    = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation0);
        anim_logo       = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation1);
        anim_email_pass = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation2);
        anim_login_sign = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim2.setVisibility(View.VISIBLE);
                anim3.setVisibility(View.VISIBLE);
                anim0.startAnimation(anim_version);
                anim1.startAnimation(anim_logo);
                anim2.startAnimation(anim_email_pass);
                anim3.startAnimation(anim_login_sign);
            }
        },3000);


    }

    protected void onResume() {
        prefs = getSharedPreferences(preference, Context.MODE_PRIVATE);
                if (prefs.contains("email")) {
                    if (prefs.contains("password")) {
                        Intent i = new Intent(this, MainActivity.class);
                        finish();
                        startActivity(i);
                    }
                }
        super.onResume();
    }

    public void logIn(View view) {

        valid_rollno = rollno.getText().toString().trim();
        valid_password = password.getText().toString().trim();
        if (!isConnected2()) {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (valid_rollno.isEmpty() || valid_rollno.length() != 9) {
            rollno.setError("All characters in lowercase");
            alertDialog("Invalid Registration Number ");
        } else if (valid_password.isEmpty() || valid_password.length() < 5) {
            password.setError("At least 6 characters ");
            alertDialog("Invalid Password");
        } else {

            class AddUser extends AsyncTask<Void, Void, String> {
                //private ProgressDialog loading;
                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Configuration.KEY_USER_ROLLNO, valid_rollno);
                    params.put(Configuration.KEY_USER_PASSWORD, valid_password);
                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Configuration.URL_LOGIN, params);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //loading = ProgressDialog.show(Login.this, "Processing...", "Please Wait..", false, false);
                }

                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //pDialog.dismiss();
                    if (!s.isEmpty()) {
                            pDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            JSONArray result = obj.getJSONArray(Configuration.TAG_JSON_ARRAY);
                            JSONObject c = result.getJSONObject(0);
                            final String name = c.getString(Configuration.TAG_NAME);
                            final String email = c.getString(Configuration.TAG_EMAIL);
                            final String rollno = c.getString(Configuration.TAG_ROLLNO);
                            final String contact = c.getString(Configuration.TAG_CONTACT);
                            final String password = c.getString(Configuration.TAG_PASSWORD);
                            final String moa = c.getString(Configuration.TAG_MOA);

                            // if (name != "null") {
                            if (!name.equalsIgnoreCase("null")) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        prefs = getSharedPreferences(preference, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("name", name);
                                        editor.putString("email", email);
                                        editor.putString("rollno", rollno);
                                        editor.putString("contact", contact);
                                        editor.putString("password", password);
                                        editor.putString("moa", moa);
                                        editor.apply();
                                        finish();
                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }, 500);
                            } else {
                                alertDialog("Wrong Registration Number or Password");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!isConnected2()) {
                            Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            new AddUser().execute();
                        }
                    }
                }
            }
            AddUser adduser = new AddUser();
            adduser.execute();
            try {
                pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(R.color.red1);
                pDialog.setTitleText("Processing...");
                pDialog.setContentText("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void alertDialog(String mess) {
       /*  AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setMessage(mess);
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
       */
        SweetAlertDialog dialog = new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("Oops...");
        dialog.setContentText(mess);
        dialog.setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }
    public void signUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }

    public void forgetPassword(View view) {startActivity(new Intent(this,ForgetPassword.class));}

    public boolean isConnected2() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
    }


}