package com.fortyk.studentapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fortyk.studentapp.R;

import androidx.appcompat.app.AlertDialog;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) for (int i = 0; i < info.length; i++)
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
		}
		return false;
	}

	public void noInternetPopup() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
		alertDialogBuilder.setMessage(R.string.internet_message).setTitle("Oops!").setCancelable(false).setPositiveButton(" Enable ", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
				dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				_context.startActivity(dialogIntent);
			}
		});

		alertDialogBuilder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
