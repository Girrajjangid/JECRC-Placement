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
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgetPasswordUpdatation extends AppCompatActivity {
    EditText newpassword;
    String valid_newpassword;
    SharedPreferences prefs;
    String preference = "UserData";
    String rollno;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_updatation);
        newpassword = (EditText) findViewById(R.id.forget_new_password);
        prefs = getSharedPreferences(preference, MODE_PRIVATE);
        rollno = prefs.getString("rollno2", "");

    }

    public void update(View view) {
        valid_newpassword = newpassword.getText().toString().trim();
        if (!isConnected2()) {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (rollno.isEmpty()) {
            alertDialog("Try Again ");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ForgetPasswordUpdatation.this, Login.class));
                }
            }, 1000);
        } else if (valid_newpassword.length() < 6 || valid_newpassword.isEmpty()) {
            alertDialog("At least 6 characters");
        } else {
            class AddUser extends AsyncTask<Void, Void, String> {
                //private ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //  loading = ProgressDialog.show(ForgetPasswordUpdatation.this, "Processing....", "Please Wait....", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //loading.dismiss();
                    if (!s.isEmpty()) {
                        // Toast.makeText(ForgetPasswordUpdatation.this, "successfully updated", Toast.LENGTH_SHORT).show();
                        pDialog.setTitleText("Successfully Updated")
                                .showContentText(false)
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        finish();
                                        Intent intent = new Intent(ForgetPasswordUpdatation.this, Login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    } else {
                        new AddUser().execute();

                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Configuration.KEY_USER_PASSWORD, valid_newpassword);
                    params.put(Configuration.KEY_USER_ROLLNO, rollno);
                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Configuration.URL_PASSWORD_UPDATE, params);

                }
            }
            AddUser adduser = new AddUser();
            adduser.execute();
            pDialog = new SweetAlertDialog(ForgetPasswordUpdatation.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.forget_password_background);
            pDialog.setTitleText("Processing...");
            pDialog.setContentText("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
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

    public boolean isConnected2() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
    }
}
