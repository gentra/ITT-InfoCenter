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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ITTelkomInfoCenterActivity extends Activity {

	private static final String TAG = "Main";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	// ------------------ Button Action Listener ------------

	/** Called when user clicks on the News Feed button */
	public void onFeedBtnClick(View v) {
		Intent i = new Intent(this, NewsFeedActivity.class);
		startActivity(i);
	}

	/** Called when user clicks on the ITT Tweets buttn */
	public void onTweetsBtnClick(View v) {
		Intent i = new Intent(this, ITTTweetsActivity.class);
		startActivity(i);
	}

	/** Called when user clicks on the Around ITT button */
	public void onAroundIttBtnClick(View v) {
		Intent i = new Intent(this, AroundIttTweetsActivity.class);
		startActivity(i);
	}

	/** Called when user clicks on the ITT Talk button */
	public void onIttTalkBtnClick(View v) {
		Intent i = new Intent(this, IttSearchTweets.class);
		startActivity(i);
	}

	/** Called when user clicks on Contact Support button */
	public void onDialBtnClick(View v) {
		Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:62227564108"));
		startActivity(i);
	}

	/** Called when user clicks on Email Support button */
	public void onEmailBtnClick(View v) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("plain/text");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@ittelkom.ac.id" });
		try {
			startActivity(i);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "Mail intent not found");
			Toast.makeText(this, "No email application found",
					Toast.LENGTH_LONG).show();
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