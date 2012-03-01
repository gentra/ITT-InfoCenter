package com.alphabet7.ittinfocenter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alphabet7.ittinfocenter.utils.BaseFeedParser;
import com.alphabet7.ittinfocenter.utils.FeedAdapter;
import com.alphabet7.ittinfocenter.utils.Message;

public class NewsFeedActivity extends Activity {

	// Debugging
	private static String TAG = "NewsFeedActivity";

	// Feeds
	private static String ID_RSS = "IT Telkom Feeds";
	private static String CAT_NAME = "ittFeeds";
	private static final String uriFeeds = "http://feeds.feedburner.com/InstitutTeknologiTelkom";
	private int LOST_CONNECTION;

	// Widgets
	TextView txtLink;
	TextView txtCenter;
	ProgressBar centerProgBar;

	// Other member fields
	private List<Message> messages;
	ListView list;
	FeedAdapter adapter;
	Dialog alert;
	boolean mBusy;
	Bundle extras;
	String idRss;
	String catName;
	String linkName;

	// ==========================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsfeed);
		initWidgets();
		LOST_CONNECTION = 0;
		txtCenter.setText("Loading...");
		getBundles();
		new FeedsTask().execute();
	}

	private void initWidgets() {
		centerProgBar = (ProgressBar) findViewById(R.id.progressBarCenter);
		txtLink = (TextView) findViewById(R.id.subtitle);

		// Sets the list
		list = (ListView) findViewById(R.id.feedList);
		txtCenter = (TextView) findViewById(R.id.progressBarText);

	}

	private void getBundles() {
		setLinkName(uriFeeds);
		setIdRss(ID_RSS);
		setCatName(CAT_NAME);

	}

	// private void setThings(int prm) {
	// String addText = "detikcom";
	// String textLink = getResources().getString(R.string.app_name);
	// textLink += " [" + addText + "]";
	// txtLink.setText(textLink);
	// }

	public void retry() {
		alert = new AlertDialog.Builder(this)
				.setIcon(R.drawable.icon)
				.setTitle("Connection Error")
				.setMessage(
						"Your internet connection might be lost lost. "
								+ "Check your internet connectivity.")
				.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								new FeedsTask().execute();
								alert.cancel();
							}
						})
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								alert.cancel();
								forceClose();

							}
						}).setCancelable(false).create();

		alert.show();
	}

	public void forceClose() {
		try {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage("com.alphabet7.ittinfocenter");
		} catch (Exception e) {
		}
		finish();
	}

	private void getListFeed() {

		String rssDefault = getLinkName();
		centerProgBar.setVisibility(View.VISIBLE);
		Log.d(TAG, getIdRss() + ", Load more feeds");
		try {
			BaseFeedParser parser = new BaseFeedParser(rssDefault);
			messages = parser.parse();
		} catch (Exception e) {
			LOST_CONNECTION = 99;
		}
	}

	private void loadFeed() {
		try {
			List<String> titles = new ArrayList<String>(messages.size());
			List<String> desc = new ArrayList<String>(messages.size());
			List<String> links = new ArrayList<String>(messages.size());
			List<String> date = new ArrayList<String>(messages.size());

			for (Message msg : messages) {
				titles.add(msg.getTitle());
				desc.add(msg.getDescription());
				date.add(msg.getDate());
				links.add(msg.getLink());
			}

			adapter = new FeedAdapter(this,
					desc.toArray(new String[desc.size()]),
					titles.toArray(new String[titles.size()]),
					links.toArray(new String[links.size()]),
					date.toArray(new String[date.size()]));

			list.setAdapter(adapter);
			txtCenter.setText("URL Not Found");
			list.setEmptyView(findViewById(R.id.progressBarLayout));

			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					String linkSelect = ((TextView) v
							.findViewById(R.id.url_link)).getText().toString();
					String titleSelect = ((TextView) v.findViewById(R.id.text))
							.getText().toString();
					String desc = ((TextView) v.findViewById(R.id.desc_link))
							.getText().toString();
					ImageView img = (ImageView) v.findViewById(R.id.image);
					startBrowsing(img, linkSelect, titleSelect, desc);

				}
			});

		} catch (Throwable t) {
			Log.e(TAG, t.getMessage(), t);
			txtCenter.setText("URL Not Found");
			Toast.makeText(this, "Check your internet connection.",
					Toast.LENGTH_LONG).show();

			return;
		}

		centerProgBar.setVisibility(View.GONE);
	}

	public void onListInit() {
		adapter = new FeedAdapter(this, new String[] {}, new String[] {},
				new String[] {}, new String[] {});
		list.setAdapter(adapter);
	}

	// ===============================================================
	private class FeedsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			getListFeed();
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			centerProgBar.setVisibility(View.GONE);
			if (LOST_CONNECTION == 0) {
				loadFeed();
				mBusy = false;
			} else if (LOST_CONNECTION == 99)
				retry();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			centerProgBar.setVisibility(View.VISIBLE);
			onListInit();
			txtCenter.setText("Loading...");
			mBusy = true;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	}

	// =============================================================
	private ProgressBar smallProgBar;
	private Button goView;

	private void startBrowsing(ImageView img, final String link,
			final String title, final String d) {

		alert = new Dialog(this);
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.setContentView(R.layout.alertweb);

		TextView tView = (TextView) alert.findViewById(R.id.txt_middle);
		WebView b = (WebView) alert.findViewById(R.id.browser);
		Button btnBack = (Button) alert.findViewById(R.id.btn_back);
		goView = (Button) alert.findViewById(R.id.btn_view);
		smallProgBar = (ProgressBar) alert
				.findViewById(R.id.topsmall_progressbar);
		// goView.setVisibility(View.GONE);
		smallProgBar.setVisibility(View.VISIBLE);
		tView.setText(title);
		b.loadData(d, "text/html", "utf-8");
		b.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				smallProgBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				smallProgBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				smallProgBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(NewsFeedActivity.this,
						description + "\nURL Not Found", Toast.LENGTH_LONG)
						.show();
				smallProgBar.setVisibility(View.GONE);
				goView.setVisibility(View.VISIBLE);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				alert.cancel();
			}
		});

		alert.show();

		// ------------------------------------------
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000 * 3);
					smallProgBar.setVisibility(View.GONE);
				} catch (Exception e) {
				}
			}
		}).start();
		goView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent newIntent = new Intent().setClass(NewsFeedActivity.this,
						WebApps.class);
				newIntent.putExtra("urlLink", link.trim());
				newIntent.putExtra("fromRss", getIdRss());
				startActivity(newIntent);
				alert.cancel();
			}
		});
	}

	// ===========================================================

	public String getIdRss() {
		return idRss;
	}

	public void setIdRss(String idRss) {
		this.idRss = idRss;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
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