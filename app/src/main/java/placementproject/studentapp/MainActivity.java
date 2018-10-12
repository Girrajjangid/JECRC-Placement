package placementproject.studentapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//If by default app crash remove addFlags from intents which added in UserProfile,SingleItemView,Forgetpassword,Signup
public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Toolbar toolbar;
    RatingBar ratingBar;
    DBAdapter db = new DBAdapter(this);
    Fragment1 fragment;
    TextView titleactionbar;
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
            fragmentTransaction.commit(); }


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

        return super.onOptionsItemSelected(item);
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


}
