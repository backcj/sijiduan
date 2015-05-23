package com.rd.callcar.Util;

import com.rd.callcarDriver.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class DialogManager {
	public static void showExitHit(final Activity act, int title, int msg) {
		final AlertDialog alertDialog = new AlertDialog.Builder(act)
				.setTitle(title)
				.setMessage(msg)
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								act.finish();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						}).create();
		alertDialog.show();
	}

	public void showExitHit(final Activity act) {
		final AlertDialog alertDialog = new AlertDialog.Builder(act)
				.setTitle(R.string.exitHint)
				.setMessage(R.string.exitChoose)
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ExitApplication.getInstance().exit();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						}).create();
		alertDialog.show();
	}

	public void toggleGPS(Activity act) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(act, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

}
