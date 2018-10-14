package placementproject.studentapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private SweetAlertDialog pDialog = null;
    SharedPreferences prefs;
    Toolbar toolbar;
    RatingBar ratingBar;
    DBAdapter db = new DBAdapter(this);
    Fragment1 fragment;
    TextView titleactionbar;
    String feedbackStr;
    EditText reviewED;
    Dialog dialog;
    public static final String preference = "UserData";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title_mainactivty);

        titleactionbar = (TextView) findViewById(R.id.tvtitle22);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font2.ttf");
        titleactionbar.setTypeface(custom_font);


        if (savedInstanceState == null) {
            fragment = new Fragment1();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            logOut(item);
        }
        if (id == R.id.profile) {
            profile(item);
        }
        if (id == R.id.ratingbar) {
            rateUs(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isConnected2() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();

    }

    public void logOut(MenuItem item) {
        prefs = getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }

    public void profile(MenuItem item) {
        startActivity(new Intent(this, UserProfile.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finish();
    }


    private void rateUs(MenuItem item) {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ratingbar);
        dialog.setCancelable(true);
        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingbars);
        reviewED = (EditText) dialog.findViewById(R.id.feedbacktext);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float i = ratingBar.getRating();
                Toast.makeText(MainActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                if (i <= 4) {
                    reviewED.setText("");
                    reviewED.setVisibility(View.VISIBLE);
                } else {
                    reviewED.setText("good");
                    reviewED.setVisibility(View.GONE);
                }
            }
        });
        dialog.show();
    }

    public void feedBack(View view) {
        feedbackStr = String.valueOf(reviewED.getText());
        if (feedbackStr.isEmpty()) {
            Toast.makeText(this, "Please give your feedback", Toast.LENGTH_SHORT).show();
        } else {
            new UpdateFeedback().execute();
            pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.red1);
            pDialog.setTitleText("Processing...");
            pDialog.setContentText("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            dialog.dismiss();
        }
    }

    class UpdateFeedback extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Configuration.KEY_FEEDBACK, feedbackStr);
            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Configuration.URL_FEEDBACK, params);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.isEmpty()) {
                pDialog.setTitleText("Successfully Submitted");
                pDialog.showContentText(false);
                pDialog.setConfirmText("OK");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    }
                })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

            } else {
                if (!isConnected2()) {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    new UpdateFeedback().execute();
                }
            }
        }
    }
}
