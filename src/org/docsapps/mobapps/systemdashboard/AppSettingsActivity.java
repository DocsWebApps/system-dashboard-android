package org.docsapps.mobapps.systemdashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @class AppSettingsActivity
 * @brief Collect and save application settings
 */
public class AppSettingsActivity extends Activity {
	
	public final static String WEB_SERVICE_FILE="web_service.file";
	public final static String TOKEN_FILE="token.file";
	private final Context context=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_settings_view);	
		Button saveButton=(Button) findViewById(R.id.app_set_save);
		
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText textUrl=(EditText) findViewById(R.id.new_url_entry);
				EditText textToken=(EditText) findViewById(R.id.new_token_entry);
				String url=textUrl.getText().toString().trim();
				String token=textToken.getText().toString().trim();
				if (!url.isEmpty() && !token.isEmpty()) {
					writeSettingToFile(url, WEB_SERVICE_FILE);
					writeSettingToFile(token, TOKEN_FILE);
					Intent loginIntent=new Intent(context, LoginActivity.class);
					startActivity(loginIntent);
					finish();
				}
			}
		});	
	}
	
	private void writeSettingToFile(String setting, String file) {
		StorageUtils.writeStringToFile(this, file, setting);
	}
}