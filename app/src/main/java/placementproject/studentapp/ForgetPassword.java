package placementproject.studentapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgetPassword extends AppCompatActivity {
    EditText contactET, rollnoET;
    String valid_contact, valid_rollno;
    Random random;
    SharedPreferences prefs;
    String preference="UserData";
    final private int REQUEST_SEND_SMS=123;
    private SweetAlertDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        contactET = (EditText) findViewById(R.id.forget_password_contact_no);
        rollnoET  = (EditText) findViewById(R.id.forget_password_roll_no);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},REQUEST_SEND_SMS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_SEND_SMS:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ForgetPassword.this,"Permission Granted",Toast.LENGTH_SHORT).show();}
                else{
                    Toast.makeText(ForgetPassword.this,"Permission Denied",Toast.LENGTH_SHORT).show();}break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void confirm(View view) {
        valid_contact = contactET.getText().toString().trim();
        valid_rollno  = rollnoET.getText().toString().trim();
        if (!isConnected2()) {
            Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_LONG).setAction("Action",null).show();
        }else if (valid_contact.isEmpty() || valid_contact.length() != 10) {
            alertDialog("Invalid Mobile Number");
        } else if (valid_rollno.isEmpty() || valid_rollno.length() != 9) {
             alertDialog("Invalid Registration Number");
         }
         else {
            prefs = getSharedPreferences(preference, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("rollno2", valid_rollno);
            editor.apply();

            class AddUser extends AsyncTask<Void, Void, String> {
                //            private ProgressDialog loading;

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Configuration.KEY_USER_CONTACT, valid_contact);
                    params.put(Configuration.KEY_USER_ROLLNO, valid_rollno);
                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Configuration.URL_FORGET_PASSWORD, params);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
//                    loading = ProgressDialog.show(ForgetPassword.this, "Processing...", "Please Wait..", false, false);
                }

                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //loading.dismiss();
                    if (!s.isEmpty()) {
                        try {
                            pDialog.dismissWithAnimation();
                            JSONObject obj = new JSONObject(s);
                            JSONArray result = obj.getJSONArray(Configuration.TAG_JSON_ARRAY);
                            JSONObject c = result.getJSONObject(0);
                            String password = c.getString(Configuration.TAG_PASSWORD);
                            //  if (password != "null") {
                            if (!password.equalsIgnoreCase("null")) {
                                Intent i = new Intent(ForgetPassword.this, ForgetPasswordVerfication.class);
                                final int min = 2000;
                                final int max = 9000;
                                random = new Random();
                                int random_no = random.nextInt((max - min) + 1) + min;
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(valid_contact, null, "Your One Time Password (OTP) is " + String.valueOf(random_no)
                                        + " for password updation.\nThank you.\nJECRC Placement", null, null);
                                i.putExtra("random", random_no);
                                i.putExtra("contact", valid_contact);
                                startActivity(i);
                            } else {
                                alertDialog("Wrong Registration Number or Mobile Number");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ForgetPassword.this, "slow Internet connection", Toast.LENGTH_SHORT).show();
                        new AddUser().execute();
                    }
                }
            }
            AddUser adduser = new AddUser();
            adduser.execute();
            pDialog = new SweetAlertDialog(ForgetPassword.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.forget_password_background);
            pDialog.setTitleText("Processing...");
            pDialog.setContentText("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }
    private void alertDialog(String mess) {
        /*final AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setTitle("Try again ");
        aBuilder.setMessage(mess);
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();*/
        SweetAlertDialog dialog = new SweetAlertDialog(this , SweetAlertDialog.ERROR_TYPE);
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
