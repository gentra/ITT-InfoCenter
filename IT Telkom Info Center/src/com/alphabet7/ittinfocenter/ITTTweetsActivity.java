package com.alphabet7.ittinfocenter;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class ITTTweetsActivity extends TabActivity {

	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources res = getResources();
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// Official Tweets Tab
		intent = new Intent().setClass(this, OfficialTweets.class);
		spec = tabHost.newTabSpec("official")
				.setIndicator(null, res.getDrawable(R.drawable.ittlogo))
				.setContent(intent);
		tabHost.addTab(spec);

		// @infoitt Tweets Tab
		intent = new Intent().setClass(this, InfoIttTweets.class);
		spec = tabHost.newTabSpec("infoitt")
				.setIndicator(null, res.getDrawable(R.drawable.infoitt))
				.setContent(intent);
		tabHost.addTab(spec);

		// @ittelkomlibrary Tweets Tab
		intent = new Intent().setClass(this, IttLibraryTweets.class);
		spec = tabHost.newTabSpec("library")
				.setIndicator(null, res.getDrawable(R.drawable.ittlibrary))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTabByTag("official");
	}

}
