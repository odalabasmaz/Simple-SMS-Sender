package com.antibyteapps.simplesmssender;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
	private Button btnSendSMS;
	private EditText txtPhoneNumber;
	private TextView txtMessage;
	private EditText txtCount;
	private String phoneNo;
	private String message;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
		txtMessage = (TextView) findViewById(R.id.txtMessage);
		txtCount = (EditText) findViewById(R.id.txtCount);

		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkPermission();
				verifyAndSendSMS();
			}
		});
	}

	private void checkPermission() {
		phoneNo = txtPhoneNumber.getText().toString();
		message = txtMessage.getText().toString();
		count = Integer.valueOf(txtCount.getText().toString());

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
				&&
				!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
		}
	}

	private void verifyAndSendSMS() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						sendSMS();
						toast("SMS sent.");
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						toast("SMS sending cancelled!");
						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure to send message for " + count + " times" + "?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	private void sendSMS() {
		SmsManager smsManager = SmsManager.getDefault();
		for (int i = 0; i < count; ++i) {
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			sleep(100);
		}
	}

	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
