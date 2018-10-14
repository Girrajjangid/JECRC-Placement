package placementproject.studentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Fragment1 extends Fragment {
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> body = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> column = new ArrayList<>();
    ArrayList<Integer> server_id = new ArrayList<>();
    ArrayList<Integer> key_row_id = new ArrayList<>();
    ArrayList<Integer> deleted_id = new ArrayList<>();
    ArrayList<String> click = new ArrayList<>();

    View rootview;
    ListView listView;
    ListViewAdapter adapter;
    TextView emptyView ;
    Context context;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        rootview = inflater.inflate(R.layout.fragment_fragment1, container, false);
        listView = (ListView) rootview.findViewById(R.id.listview_fragment);
        emptyView = (TextView) rootview.findViewById(R.id.emptyElement);
        adapter = new ListViewAdapter(context, title, body, date, time, server_id, key_row_id, column, click);
        listView.setAdapter(adapter);
        saveInDatabase();
        new AddUser().execute();

        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent intent = new Intent(getActivity(), SingleItemView.class);
                    intent.putExtra("title", title);
                    intent.putExtra("body", body);
                    intent.putExtra("column", column);
                    intent.putExtra("position", position);
                    startActivity(intent);

                    long rowid = key_row_id.get(position);
                    ((MainActivity) getActivity()).db.open();
                    ((MainActivity) getActivity()).db.updateData(rowid);

                    click.set(position, "true");

                    adapter.notifyDataSetChanged();

                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    FirebaseMessaging.getInstance().subscribeToTopic("test");
                    String valid_token = FirebaseInstanceId.getInstance().getToken();
                    Log.d("Token", "Token: " + valid_token);

                    long serverid = parent.getAdapter().getItemId(position);
                    alertDialog("Are you sure you want to delete", position, (int) serverid);
                    return true;
                }
            });

        } catch (Exception ignored) {
        }
        return rootview;
    }

    public void saveInDatabase() {
        try {
            ((MainActivity) getActivity()).db.open();
            Cursor c = ((MainActivity) getActivity()).db.getAllData();
            Cursor c2 = ((MainActivity) getActivity()).db.getAllDeletedData();
            if (c.moveToFirst()) {
                do {
                    if (!(server_id.contains(c.getInt(1)))) {
                        key_row_id.add(0, c.getInt(0));
                        server_id.add(0, c.getInt(1));
                        title.add(0, c.getString(2));
                        body.add(0, c.getString(3));
                        date.add(0, c.getString(4));
                        time.add(0, c.getString(5));
                        column.add(0, c.getString(6));
                        click.add(0, c.getString(7));
                    }
                } while (c.moveToNext());
            }
            if (c2.moveToFirst()) {
                do {
                    if (!(deleted_id.contains(c2.getInt(1)))) {
                        deleted_id.add(c2.getInt(1));
                    }
                } while (c2.moveToNext());
            }
            ((MainActivity) getActivity()).db.close();

            if(title.isEmpty()) {
                listView.setEmptyView(emptyView);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    public void alertDialog(String mess, final int position, final int serverid) {
        SweetAlertDialog d = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        d.setCancelable(false);
        d.setTitleText("Are you sure?")
                .setContentText("You want to delete this message?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        key_row_id.remove(position);
                        server_id.remove(position);
                        title.remove(position);
                        body.remove(position);
                        date.remove(position);
                        time.remove(position);
                        column.remove(position);
                        ((MainActivity) getActivity()).db.open();
                        ((MainActivity) getActivity()).db.deleteData(serverid);
                        ((MainActivity) getActivity()).db.insertDataDeleted(serverid);
                        ((MainActivity) getActivity()).db.close();
                        adapter.notifyDataSetChanged();

                        if (title.isEmpty()) {
                            listView.setEmptyView(emptyView);
                        }

                        sweetAlertDialog
                                .setTitleText("Successfully Deleted!")
                                .showContentText(false)
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
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

    class AddUser extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            return new RequestHandler().sendGetRequest(Configuration.URL_DOWNLOAD_INFO);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.isEmpty()) {
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArray = obj.getJSONArray(Configuration.TAG_JSON_ARRAY);
                    Log.i("JSON", "Number of Surveys in Feed: " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer server_id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String body = jsonObject.getString("body");
                        String date = jsonObject.getString("date");
                        String time = jsonObject.getString("time");
                        String column = jsonObject.getString("column");
                        String click = "false";
                        if (!(deleted_id.contains(server_id))) {
                            ((MainActivity) getActivity()).db.open();
                            ((MainActivity) getActivity()).db.insertData(server_id, title, body, date, time,column,click);
                            ((MainActivity) getActivity()).db.close();
                        }
                    }
                    saveInDatabase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                new AddUser().execute();
            }
            adapter.notifyDataSetChanged();
        }
    }
}