package org.docsapss.mobapps.systemdashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @class EnterNewPinActivity
 * @brief Facility to set a new pin code to login with
 */
public class EnterNewPinActivity extends Activity {
	private final Context context=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinentry_view);
		Button butNewPinSave=(Button) findViewById(R.id.new_pin_save);
		butNewPinSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView newPinEntry=(TextView) findViewById(R.id.new_pin_entry);
				String pinCode=newPinEntry.getText().toString().trim();
				if (PinCode.writePinToFile(context, pinCode)) {
					Intent loginIntent=new Intent(context, LoginActivity.class);
					startActivity(loginIntent);
					finish();
				} else {
					Toast.makeText(context,"Pin is invalid, please re-enter a 4 digit pin only.", Toast.LENGTH_LONG).show();
				};
			}
		});
	}
}