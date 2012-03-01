package com.alphabet7.ittinfocenter.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphabet7.ittinfocenter.R;

public class FeedAdapter extends BaseAdapter {

	private Activity activity;
	private String[] data;
	private String[] title;
	private String[] link;
	private String[] date;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public FeedAdapter(Activity a, String[] d, String[] t, String[] l,
			String[] pdate) {
		activity = a;
		data = d;
		title = t;
		link = l;
		date = pdate;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text, link, desc, date;
		public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.feeditems, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.text);
			holder.link = (TextView) vi.findViewById(R.id.url_link);
			holder.desc = (TextView) vi.findViewById(R.id.desc_link);
			holder.date = (TextView) vi.findViewById(R.id.date_link);
			;
			holder.image = (ImageView) vi.findViewById(R.id.image);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		holder.link.setText(link[position]);

		try {
			holder.text.setText(title[position]);
		} catch (Exception e) {
			holder.text.setText("TextItem " + position);
		}

		holder.image.setTag(data[position]);
		imageLoader.DisplayImage(data[position], activity, holder.image);

		holder.desc.setText(data[position]);
		holder.date.setText(date[position]);

		return vi;
	}
}