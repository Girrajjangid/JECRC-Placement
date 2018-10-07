package placementproject.studentapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

class ListViewAdapter extends BaseAdapter {

	Context context;
	private ArrayList<String> title = new ArrayList<>();
	private ArrayList<String> body = new ArrayList<>();
	private ArrayList<String> date = new ArrayList<>();
	private ArrayList<String> time = new ArrayList<>();
	private ArrayList<Integer> server_id = new ArrayList<>();
	private ArrayList<String> click = new ArrayList<>();
	private ArrayList<String> column = new ArrayList<>();
	private ArrayList<Integer> key_row_id = new ArrayList<>();
	private View itemView;
    private String  innerdate;

	ListViewAdapter(Context context, ArrayList<String> title, ArrayList<String> body,
					ArrayList<String> date, ArrayList<String> time, ArrayList<Integer> server_id, ArrayList<Integer> key_row_id
			, ArrayList<String> column,ArrayList<String> click) {

		this.context = context;
		this.title = title;
		this.body = body;
		this.date = date;
		this.time = time;
		this.column = column;
		this.server_id = server_id;
		this.key_row_id = key_row_id;
		this.click = click;
		Collections.reverse(title);
		Collections.reverse(body);
		Collections.reverse(date);
		Collections.reverse(time);
		Collections.reverse(column);
		Collections.reverse(click);
	}

    @Override
	public int getCount() {
		return title.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return server_id.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv_title;
        TextView tv_body;
        TextView tv_date;
        TextView tv_time;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            itemView = inflater.inflate(R.layout.listview_item, parent, false);
            tv_title = (TextView) itemView.findViewById(R.id.title_single);
            tv_body = (TextView) itemView.findViewById(R.id.body_single);
            tv_date = (TextView) itemView.findViewById(R.id.date_single);
            tv_time = (TextView) itemView.findViewById(R.id.time_single);
            timeDate();
            tv_title.setText(title.get(position));
            tv_body.setText(body.get(position));
            tv_time.setText(time.get(position));

            if (click.get(position).equalsIgnoreCase("false")) {
                tv_time.setTextColor(Color.RED);
                tv_date.setTextColor(Color.RED);
                tv_title.setTextColor(Color.RED);
            } else {
                tv_title.setTextColor(Color.BLACK);
                tv_time.setTextColor(Color.GRAY);
                tv_date.setTextColor(Color.GRAY);
            }

            if (innerdate.equalsIgnoreCase(date.get(position))) {
                tv_date.setText(String.valueOf("TODAY"));
            } else {
                tv_date.setText(date.get(position));
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return itemView;
    }
	private void timeDate() {
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		innerdate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year;
	}
}
