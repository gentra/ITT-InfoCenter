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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebApps extends Activity {

	public static ProgressBar mProgressBar;
	int getProgress;

	WebView browser;
	Bundle extras;
	TextView headText;

	String urlLink;
	String fromRss;
	Button backButton;
	LinearLayout linArrow;
	ImageView arrIcon;
	EditText txtUrl;
	Button goUrl;
	boolean toogleIcon;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainweb);

		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		headText = (TextView) findViewById(R.id.head_text);
		backButton = (Button) findViewById(R.id.btn_back);
		linArrow = (LinearLayout) findViewById(R.id.layout_arrow);
		arrIcon = (ImageView) findViewById(R.id.arrow_icon);
		goUrl = (Button) findViewById(R.id.go_button);
		txtUrl = (EditText) findViewById(R.id.url);
		// -----------------------------------------------------
		getBundles();

		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		browser = (WebView) findViewById(R.id.browser);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					String[] email = url.split(":");
					if (email[0].toLowerCase().equals("mailto")) {
						sendEmail(email[1].toLowerCase().trim());
						Log.e("should mailto:", email[1]);
						return true;
					}
				} catch (Exception e) {
				}

				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mProgressBar.setVisibility(View.VISIBLE);
				txtUrl.setText(getUrlLink());
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				mProgressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				DisplayToast(description
						+ "\nMaybe url address not well-formed http://...");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		browser.loadUrl(this.getUrlLink());
		;

		goTabListen();
	}

	public void sendEmail(String email) {
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { email });

		String mySubject = "notification m-jobs";
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySubject);
		String myBodyText = " Your submit description";
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, myBodyText);
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	void DisplayToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
			browser.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void getBundles() {

		this.setUrlLink("http://www.google.com/");
		extras = getIntent().getExtras();

		try {
			String urlLink = extras.getString("urlLink");
			this.setUrlLink(urlLink);
		} catch (Exception e) {
		}

		/*
		 * try { String fromRss = extras.getString("fromRss");
		 * this.setFromRss(fromRss); headText.setText(fromRss); }
		 * catch(Exception e) {}
		 */

		txtUrl.setText(getUrlLink());
		toogleIcon = false;
		linArrow.setVisibility(View.GONE);
		arrIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				toogleIcon = !toogleIcon;
				toggleArrowIcon();
			}
		});

		goUrl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HideInputVirtualKeyboard();
				setUrlLink(txtUrl.getText().toString().trim());
				browser.loadUrl(WebApps.this.getUrlLink());
			}
		});

	}

	void HideInputVirtualKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtUrl.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	void toggleArrowIcon() {
		if (toogleIcon) {
			linArrow.setVisibility(View.VISIBLE);
			arrIcon.setBackgroundResource(R.drawable.arrow_up32);
		} else {
			linArrow.setVisibility(View.GONE);
			arrIcon.setBackgroundResource(R.drawable.arrow_down32);
		}
	}

	public void goTabListen() {

		mProgressBar.setVisibility(View.VISIBLE);
		getProgress = 0;

		new Thread() {
			public void run() {
				try {
					sleep(1000 * 12);
				} catch (Exception e) {
				}

				handlerLoad.sendEmptyMessage(0);
			}
		}.start();
	}

	private Handler handlerLoad = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (getProgress >= 100) {
				mProgressBar.setVisibility(View.GONE);
			} else {
				getProgress += 18;
				handlerLoad.sendEmptyMessageDelayed(1, 25);
			}
		}
	};

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public String getFromRss() {
		return fromRss;
	}

	public void setFromRss(String fromRss) {
		this.fromRss = fromRss;
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
