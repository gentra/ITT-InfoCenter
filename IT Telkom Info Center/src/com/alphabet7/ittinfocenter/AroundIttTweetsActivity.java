/*
 * Copyright 2012 Gentra Aditya Putra Ruswanda

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.alphabet7.ittinfocenter;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;

public class AroundIttTweetsActivity extends ListActivity {

	private static final String TAG = "AroundIttTweetsActivity";

	QueryResult result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arounditttweets);

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
			Tweet tweet = result.getTweets().get(info.position);
			// Gets a handle to the clipboard service.
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText("@" + tweet.getFromUser() + ": "
					+ tweet.getText());
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

			adapter = new ArrayAdapter<String>(AroundIttTweetsActivity.this,
					R.layout.tweetsrow, R.id.text);

			try {
				// Gets the ittelkom's timeline
				Query query = new Query();
				// Set the location to be around IT Telkom
				GeoLocation location = new GeoLocation(-6.975779, 107.630596);
				query.setGeoCode(location, 5, Query.KILOMETERS);

				result = twitter.search(query);
				for (Tweet tweet : result.getTweets()) {
					adapter.add("@" + tweet.getFromUser() + "\n"
							+ tweet.getText());
					// Log.i(TAG, tweet.getText());
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
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(AroundIttTweetsActivity.this);
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
