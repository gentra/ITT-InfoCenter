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

		// @ittelkomlibrary Tweets Tab
		intent = new Intent().setClass(this, IttLibraryTweets.class);
		spec = tabHost.newTabSpec("library")
				.setIndicator(null, res.getDrawable(R.drawable.ittlibrary))
				.setContent(intent);
		tabHost.addTab(spec);

		// @infoitt Tweets Tab
		intent = new Intent().setClass(this, InfoIttTweets.class);
		spec = tabHost.newTabSpec("infoitt")
				.setIndicator(null, res.getDrawable(R.drawable.infoitt))
				.setContent(intent);
		tabHost.addTab(spec);

		// @bem_itt Tweets Tab
		intent = new Intent().setClass(this, BemIttTweets.class);
		spec = tabHost.newTabSpec("bem")
				.setIndicator(null, res.getDrawable(R.drawable.bemitt))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTabByTag("official");
	}

}
