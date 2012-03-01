package com.alphabet7.ittinfocenter;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BemIttTweets extends ListActivity {

	private static final String TAG = "BemIttTweets";

	List<twitter4j.Status> statuses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.officialtweets);

		TextView header = new TextView(this);
		header.setText("@bem_itt");
		header.setBackgroundColor(Color.BLACK);
		header.setTextColor(Color.WHITE);
		header.setTextSize(30);
		header.setGravity(Gravity.CENTER_HORIZONTAL);
		getListView().addHeaderView(header);

		registerForContextMenu(getListView());

		new TweetsTask().execute();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, 1, 0, "Copy");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case 1:
			// Gets a handle to the clipboard service.
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText("@bem_itt: "
					+ statuses.get((info.position) - 1).getText());
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private class TweetsTask extends AsyncTask<Void, Void, Void> {

		ArrayAdapter<String> adapter;
		ProgressDialog progressDialog;

		@Override
		protected Void doInBackground(Void... params) {
			// Load twitter's timeline
			Twitter twitter = new TwitterFactory().getInstance();
			AccessToken a = new AccessToken(
					"40597450-ptlVmglI9xjCbscqzZUSX1dsddHRE5SRqucGsAz0",
					"jOjCzXVnNcGuR18GsgpuhmmrxczLOfdsezEf3xI4k");
			twitter.setOAuthConsumer("vLiTn5RIzPbUbPOhnLdOw",
					"iOCpzjS3X5hMeZoZgLlbMFJcPWff9TLZyK6hchV8");
			twitter.setOAuthAccessToken(a);

			adapter = new ArrayAdapter<String>(BemIttTweets.this,
					R.layout.tweetsrow, R.id.text);

			try {
				// Gets the ittelkom's timeline
				statuses = twitter.getUserTimeline("bem_itt");
				Log.i(TAG, "Start loading tweets");
				for (twitter4j.Status status : statuses) {
					adapter.add(status.getText());
					// Log.i(TAG, status.getText());
				}
			} catch (TwitterException e) {
				Log.e(TAG, "Twitter:" + e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setListAdapter(adapter);
			progressDialog.dismiss();
			Log.i(TAG, "Done loading tweets");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(BemIttTweets.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

	}

	// ---------------- Options Menu ----------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.setQwertyMode(true);
		MenuItem menuAbout = menu.add(0, 1, 0, "About");
		{
			menuAbout.setIcon(android.R.drawable.ic_menu_info_details);
		}
		MenuItem menuHelp = menu.add(0, 2, 0, "Help");
		{
			menuHelp.setIcon(android.R.drawable.ic_menu_help);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
			aboutDialog
					.setMessage(
							"Version " + getString(R.string.app_version)
									+ "\nby Gentra Aditya.\n\n"
									+ getString(R.string.about))
					.setCancelable(true)
					.setTitle("ITT InfoCenter (Unofficial)")
					.setIcon(R.drawable.icon);
			AlertDialog alert = aboutDialog.create();
			alert.show();
			return true;
		case 2:
			AlertDialog.Builder helpDialog = new AlertDialog.Builder(this);
			helpDialog.setMessage(R.string.helpdialog).setCancelable(true)
					.setTitle("Help").setIcon(R.drawable.icon);
			AlertDialog help = helpDialog.create();
			help.show();
			return true;
		}
		return false;
	}

}