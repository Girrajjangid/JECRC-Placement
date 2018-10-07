package placementproject.studentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SingleItemView extends AppCompatActivity {
    SharedPreferences prefs;

    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> body = new ArrayList<>();
    ArrayList<String> column = new ArrayList<>();
    int position;
    TextView bodyTV, titleTV;
    public static final String preference = "UserData";
    private  SweetAlertDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title_singleviewactivity);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.password_g4));

        bodyTV = (TextView) findViewById(R.id.title);
        titleTV = (TextView) findViewById(R.id.tvtitle);
        Intent intent = getIntent();
        title = intent.getStringArrayListExtra("title");
        body = intent.getStringArrayListExtra("body");

        column = intent.getStringArrayListExtra("column");

        position = intent.getExtras().getInt("position");
        bodyTV.setText(body.get(position));

        getSupportActionBar().setTitle(title.get(position));
        prefs = getSharedPreferences(preference, MODE_PRIVATE);


    }

    public void interested(View view) {
        String moa = prefs.getString(Configuration.TAG_MOA, null);
        if (moa.equalsIgnoreCase("null")) {
            new SweetAlertDialog(SingleItemView.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry !")
                    .setContentText("You need to Update your Profile first")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();}
        else {
            new AddUser().execute();
            pDialog = new SweetAlertDialog(SingleItemView.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.forget_password_background);
            pDialog.setTitleText("Processing...");
            pDialog.setContentText("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
    }

    class AddUser extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            prefs = getSharedPreferences(preference, Context.MODE_PRIVATE);
            String valid_rollno = prefs.getString(Configuration.TAG_ROLLNO, null);
            HashMap<String, String> params = new HashMap<>();
            params.put(Configuration.KEY_USER_ROLLNO, valid_rollno);
            params.put(Configuration.KEY_COLUMN_NAME, column.get(position));
            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Configuration.URL_INTERESTED_STUDENTS, params);

        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (!s.isEmpty()) {
                    pDialog.setTitleText("Successfully Registered")
                            .setContentText("Thank you for your Interest.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                } else {
                    new AddUser().execute();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


