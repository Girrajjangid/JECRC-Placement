package placementproject.studentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserProfile extends AppCompatActivity {
    String[] mode_of_admission;
    String[] BRANCH;
    String[] GENDER;
    String name, father, mother, rollno, dob, address, city, state, email, contact1, contact2, tenschoolname, twelveschoolname;
    String tenpercent, twelvepercent, gpa_1, gpa_2, gpa_3, gpa_4, gpa_5, gpa_6, gpa_7, gpa_8, cgpa_overall, diploma_1, diploma_2;
    String diploma_3,project_1, project_2, moa;
    String gender,branch;
    String fathercontact,tenpassoutyear,twelvepassoutyear,totalback,yearback;
    EditText  father_ET, mother_ET, dob_ET, address_ET, city_ET, state_ET, contact2_ET,tenschoolname_ET, twelveschoolname_ET;
    EditText  tenpercent_ET, twelvepercent_ET, gpa_1_ET, gpa_2_ET, gpa_3_ET, gpa_4_ET, gpa_5_ET,gpa_6_ET, gpa_7_ET, gpa_8_ET;
    EditText  cgpa_overall_ET, diploma_1_ET, diploma_2_ET, diploma_3_ET, project_1_ET, project_2_ET;
    EditText fathercontactET,tenpassoutyearET,twelvepassoutyearET,totalbackET,yearbackTV;
    Spinner MOA,branchspinner,genderspinner;
    SharedPreferences prefs;
    TextView rollnoTV,titleactionbar,nameTV,emailTV,contactTV;
    private  SweetAlertDialog pDialog;
    public static final String preference = "UserData";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_center_align_profile);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.password_g3));

        titleactionbar = (TextView) findViewById(R.id.tvtitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font3.ttf");
        titleactionbar.setTypeface(custom_font);


        nameTV          = (TextView) findViewById(R.id.profile_name);
        rollnoTV        = (TextView) findViewById(R.id.profile_roll_no);
        emailTV         = (TextView) findViewById(R.id.profile_email);
        contactTV       = (TextView) findViewById(R.id.profile_contact);
        contact2_ET     = (EditText) findViewById(R.id.profile_alter_mobile_no);
        father_ET       = (EditText) findViewById(R.id.profile_father_name);
        mother_ET       = (EditText) findViewById(R.id.profile_mother_name);
        dob_ET          = (EditText) findViewById(R.id.profile_DOB);
        address_ET      = (EditText) findViewById(R.id.profile_address);
        city_ET         = (EditText) findViewById(R.id.profile_city);
        state_ET        = (EditText) findViewById(R.id.profile_state);
        tenschoolname_ET= (EditText) findViewById(R.id.profile_school_ten);
        tenpercent_ET   = (EditText) findViewById(R.id.profile_percentage_ten);
        twelveschoolname_ET= (EditText) findViewById(R.id.profile_twelve_school);
        twelvepercent_ET= (EditText) findViewById(R.id.profile_percentage_twelve);
        gpa_1_ET        = (EditText) findViewById(R.id.profile_percentage_1sem);
        gpa_2_ET        = (EditText) findViewById(R.id.profile_percentage_2sem);
        gpa_3_ET        = (EditText) findViewById(R.id.profile_percentage_3sem);
        gpa_4_ET        = (EditText) findViewById(R.id.profile_percentage_4sem);
        gpa_5_ET        = (EditText) findViewById(R.id.profile_percentage_5sem);
        gpa_6_ET        = (EditText) findViewById(R.id.profile_percentage_6sem);
        gpa_7_ET        = (EditText) findViewById(R.id.profile_percentage_7sem);
        gpa_8_ET        = (EditText) findViewById(R.id.profile_percentage_8sem);
        cgpa_overall_ET = (EditText) findViewById(R.id.profile_percentage_overall);
        diploma_1_ET    = (EditText) findViewById(R.id.profile_diploma_1);
        diploma_2_ET    = (EditText) findViewById(R.id.profile_diploma_2);
        diploma_3_ET    = (EditText) findViewById(R.id.profile_diploma_3);
        project_1_ET    = (EditText) findViewById(R.id.profile_project_name_1);
        project_2_ET    = (EditText) findViewById(R.id.profile_project_name_2);
        fathercontactET = (EditText) findViewById(R.id.profile_father_contact);
        tenpassoutyearET= (EditText) findViewById(R.id.profile_ten_passout_year);
        twelvepassoutyearET = (EditText) findViewById(R.id.profile_twelve_passout_year);
        totalbackET     = (EditText) findViewById(R.id.profile_overallback);
        yearbackTV      = (EditText) findViewById(R.id.profile_year_back);
        MOA             = (Spinner) findViewById(R.id.Profile_mode_of_admission);
        branchspinner   = (Spinner) findViewById(R.id.Profile_branch);
        genderspinner   = (Spinner) findViewById(R.id.Profile_gender);
        mode_of_admission   = getResources().getStringArray(R.array.mode_of_addmision);
        BRANCH              = getResources().getStringArray(R.array.Branch);
        GENDER              = getResources().getStringArray(R.array.gender);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, mode_of_admission) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                    ((TextView) v.findViewById(android.R.id.text1)).setHintTextColor(getResources().getColor(R.color.profile_hint_color)); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MOA.setAdapter(adapter);
        MOA.setSelection(adapter.getCount());
        MOA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.White));
                moa = mode_of_admission[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UserProfile.this, "Please Select one Mode of Addmission", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, GENDER) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                    ((TextView) v.findViewById(android.R.id.text1)).setHintTextColor(getResources().getColor(R.color.profile_hint_color)); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter1);
        genderspinner.setSelection(adapter1.getCount());
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.White));
                gender = GENDER[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UserProfile.this, "Please Select your gender", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, BRANCH) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                    ((TextView) v.findViewById(android.R.id.text1)).setHintTextColor(getResources().getColor(R.color.profile_hint_color)); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchspinner.setAdapter(adapter2);
        branchspinner.setSelection(adapter2.getCount());
        branchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.White));
                branch = BRANCH[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UserProfile.this, "Please Select your branch", Toast.LENGTH_SHORT).show();
            }
        });

        prefs = getSharedPreferences(preference, MODE_PRIVATE);
        name        = prefs.getString(Configuration.TAG_NAME, null);
        contact1    = prefs.getString(Configuration.TAG_CONTACT, null);
        rollno      = prefs.getString(Configuration.TAG_ROLLNO, null);
        email       = prefs.getString(Configuration.TAG_EMAIL, null);
        nameTV.setText(name);
        contactTV.setText(contact1);
        rollnoTV.setText(rollno);
        emailTV.setText(email);
    }
    public void update(View view) {
        try {
            father = father_ET.getText().toString().trim();
            mother = mother_ET.getText().toString().trim();
            dob = dob_ET.getText().toString().trim();
            address = address_ET.getText().toString().trim();
            city = city_ET.getText().toString().trim();
            state = state_ET.getText().toString().trim();
            contact2 = contact2_ET.getText().toString().trim();
            tenschoolname = tenschoolname_ET.getText().toString().trim();
            tenpercent = tenpercent_ET.getText().toString().trim();
            twelveschoolname = twelveschoolname_ET.getText().toString().trim();
            twelvepercent = twelvepercent_ET.getText().toString().trim();
            gpa_1 = gpa_1_ET.getText().toString().trim();
            gpa_2 = gpa_2_ET.getText().toString().trim();
            gpa_3 = gpa_3_ET.getText().toString().trim();
            gpa_4 = gpa_4_ET.getText().toString().trim();
            gpa_5 = gpa_5_ET.getText().toString().trim();
            gpa_6 = gpa_6_ET.getText().toString().trim();
            gpa_7 = gpa_7_ET.getText().toString().trim();
            gpa_8 = gpa_8_ET.getText().toString().trim();
            cgpa_overall = cgpa_overall_ET.getText().toString().trim();
            diploma_1 = diploma_1_ET.getText().toString().trim();
            diploma_2 = diploma_2_ET.getText().toString().trim();
            diploma_3 = diploma_3_ET.getText().toString().trim();
            project_1 = project_1_ET.getText().toString().trim();
            project_2 = project_2_ET.getText().toString().trim();

            fathercontact = fathercontactET.getText().toString().trim();
            tenpassoutyear = tenpassoutyearET.getText().toString().trim();
            twelvepassoutyear = twelvepassoutyearET.getText().toString().trim();
            totalback = totalbackET.getText().toString().trim();
            yearback = yearbackTV.getText().toString().trim();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("Exception", "raised");
        }
        try {
            String moa2 = prefs.getString(Configuration.TAG_MOA, null);
            if (!isConnected2()) {
                Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            else if (moa2.equalsIgnoreCase("JEE Main") || moa2.equalsIgnoreCase("12th")) {
                new SweetAlertDialog(UserProfile.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry !")
                        .setContentText("Your Profile Updated!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
            }
             else if (father.isEmpty()) {
                alertDialog("Invalid Father's Name");
            } else if (mother.isEmpty()) {
                alertDialog("Invalid Mother's Name");
            } else if (dob.isEmpty()) {
                alertDialog("Invalid DOB");
            } else if (gender.equalsIgnoreCase("Gender")) {
                alertDialog("Select your gender");
            } else if (address.isEmpty()) {
                alertDialog("Invalid Address");
            } else if (city.isEmpty()) {
                alertDialog("Invalid City");
            } else if (state.isEmpty()) {
                alertDialog("Invalid State");
            } else if (branch.equalsIgnoreCase("Branch")) {
                alertDialog("Select your branch");
            } else if (contact2.isEmpty() || contact2.length() != 10) {
                alertDialog("Invalid Alternate Contact");
            } else if (fathercontact.isEmpty() || fathercontact.length() != 10) {
                alertDialog("Invalid father's contact");
            } else if (tenschoolname.isEmpty()) {
                alertDialog("Invalid 10th School Name");
            } else if (tenpercent.isEmpty() || Float.valueOf(tenpercent) >= 101) {
                alertDialog("Invalid 10th School Percentage");
            } else if (tenpassoutyear.isEmpty() || tenpassoutyear.length() != 4 || Integer.valueOf(tenpassoutyear) >= 2018) {
                alertDialog("Invalid 10th pass out year");
            } else if (twelveschoolname.isEmpty()) {
                alertDialog("Invalid 12th School Name");
            } else if (twelvepercent.isEmpty() || Float.valueOf(twelvepercent) >= 101) {
                alertDialog("Invalid 12th School Percentage");
            } else if (twelvepassoutyear.isEmpty() || twelvepassoutyear.length() != 4 || Integer.valueOf(twelvepassoutyear) >= 2018) {
                alertDialog("Invalid 12th pass out year");
            } else if (gpa_1.isEmpty() || Float.valueOf(gpa_1) >= 11) {
                alertDialog("Invalid 1st semester GPA");
            } else if (gpa_2.isEmpty() || Float.valueOf(gpa_2) >= 11) {
                alertDialog("Invalid 2nd semester GPA");
            } else if (gpa_3.isEmpty() || Float.valueOf(gpa_3) >= 11) {
                alertDialog("Invalid 3rd semester GPA");
            } else if (gpa_4.isEmpty() || Float.valueOf(gpa_4) >= 11) {
                alertDialog("Invalid 4th semester GPA");
            } else if (gpa_5.isEmpty() || Float.valueOf(gpa_5) >= 11) {
                alertDialog("Invalid 5th semester GPA");
            } else if (gpa_6.isEmpty() || Float.valueOf(gpa_6) >= 11) {
                alertDialog("Invalid 6th semester GPA");
            } /*else if (Float.valueOf(gpa_7) >= 11) {
                alertDialog("Invalid 7th semester GPA");
            } else if (Float.valueOf(gpa_8) >= 11) {
                alertDialog("Invalid 8th semester GPA");
            } else if (Float.valueOf(cgpa_overall) >= 11) {
                alertDialog("Invalid CGPA");
            } else if (Float.valueOf(diploma_1) >= 101) {
                alertDialog("Invalid 1st year Diploma Percentage");
            } else if (Float.valueOf(diploma_2) >= 101) {
                alertDialog("Invalid 2nd year Diploma Percentage");
            } else if (Float.valueOf(diploma_3) >= 101) {
                alertDialog("Invalid 3rd year Diploma Percentage");
            } else if (project_1.isEmpty()) {
                alertDialog("Invalid Project Name");
            } else if (project_2.isEmpty()) {
                alertDialog("Invalid Project Name");
            }else if (totalback.isEmpty()) {
                alertDialog("Invalid over all back");
            } else if (yearback.isEmpty()) {
                alertDialog("Invalid year back");
            }*/ else if (moa.equalsIgnoreCase("Mode of Admission")) {
                alertDialog("Select Mode of Admission");
            }   else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("moa", moa);
                editor.apply();

                SweetAlertDialog dialog = new SweetAlertDialog(UserProfile.this, SweetAlertDialog.WARNING_TYPE);
                        dialog.setTitleText("Are you sure?")
                        .setContentText("You won't be able to change this information later!")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                AddUser adduser = new AddUser();
                                adduser.execute();
                                sDialog.dismissWithAnimation();
                                pDialog = new SweetAlertDialog(UserProfile.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(R.color.forget_password_background);
                                pDialog.setTitleText("Processing...");
                                pDialog.setContentText("Please wait...");
                                pDialog.setCancelable(false);
                                pDialog.show();

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    class AddUser extends AsyncTask<Void, Void, String> {

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
         try{
            if (!s.isEmpty()) {
                pDialog.setTitleText("Successfully Updated")
                        .showContentText(false)
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);    }
                        })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            } else {
                new AddUser().execute();
            }
         }catch (Exception ignored){}
        }
        protected String doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Configuration.KEY_USER_NAME, name);
            params.put(Configuration.KEY_USER_FATHER, father);
            params.put(Configuration.KEY_USER_MOTHER, mother);
            params.put(Configuration.KEY_USER_ROLLNO, rollno);
            params.put(Configuration.KEY_USER_DOB, dob);
            params.put(Configuration.KEY_USER_ADDRESS, address);
            params.put(Configuration.KEY_USER_CITY, city);
            params.put(Configuration.KEY_USER_STATE, state);
            params.put(Configuration.KEY_USER_EMAIL, email);
            params.put(Configuration.KEY_USER_CONTACT, contact1);
            params.put(Configuration.KEY_USER_ALTERCONTACT, contact2);
            params.put(Configuration.KEY_USER_TENSN, tenschoolname);
            params.put(Configuration.KEY_USER_TENPER, tenpercent);
            params.put(Configuration.KEY_USER_TWELVESN, twelveschoolname);
            params.put(Configuration.KEY_USER_TWELVEPER, twelvepercent);
            params.put(Configuration.KEY_USER_GPA_1, gpa_1);
            params.put(Configuration.KEY_USER_GPA_2, gpa_2);
            params.put(Configuration.KEY_USER_GPA_3, gpa_3);
            params.put(Configuration.KEY_USER_GPA_4, gpa_4);
            params.put(Configuration.KEY_USER_GPA_5, gpa_5);
            params.put(Configuration.KEY_USER_GPA_6, gpa_6);
            params.put(Configuration.KEY_USER_GPA_7, gpa_7);
            params.put(Configuration.KEY_USER_GPA_8, gpa_8);
            params.put(Configuration.KEY_USER_CGPA, cgpa_overall);
            params.put(Configuration.KEY_USER_DIPLOMA_1, diploma_1);
            params.put(Configuration.KEY_USER_DIPLOMA_2, diploma_2);
            params.put(Configuration.KEY_USER_DIPLOMA_3, diploma_3);
            params.put(Configuration.KEY_USER_PROJECT_1, project_1);
            params.put(Configuration.KEY_USER_PROJECT_2, project_2);
            params.put(Configuration.KEY_USER_MOA, moa);
            params.put(Configuration.KEY_USER_FATHER_CONTACT, fathercontact);
            params.put(Configuration.KEY_USER_GENDER, gender);
            params.put(Configuration.KEY_USER_BRANCH, branch);
            params.put(Configuration.KEY_USER_TENPASS, tenpassoutyear);
            params.put(Configuration.KEY_USER_TWELVEPASS, twelvepassoutyear);
            params.put(Configuration.KEY_USER_TOTAL_BACK, totalback);
            params.put(Configuration.KEY_USER_YEAR_BACK, yearback);

            RequestHandler rh = new RequestHandler();
            return  rh.sendPostRequest(Configuration.URL_PROFILE_UPDATE, params);

        }
    }
     private void alertDialog(String message) {
         SweetAlertDialog dialog = new SweetAlertDialog(this , SweetAlertDialog.ERROR_TYPE);
                 dialog.setTitleText("Oops...")
                 .setContentText(message)
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
