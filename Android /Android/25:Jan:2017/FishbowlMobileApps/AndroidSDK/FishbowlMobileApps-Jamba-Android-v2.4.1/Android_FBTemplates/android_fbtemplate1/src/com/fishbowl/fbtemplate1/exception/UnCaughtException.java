package com.fishbowl.fbtemplate1.exception;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;

public class UnCaughtException implements Thread.UncaughtExceptionHandler {
	private final Context context;
	private static Context context1;
	private static Activity act;


	public UnCaughtException(Context ctx) {
		context = ctx;
		context1 = ctx;
		// act = (Activity)ctx;
	}

	private StatFs getStatFs() {
		File path = Environment.getDataDirectory();
		return new StatFs(path.getPath());
	}

	private long getAvailableInternalMemorySize(StatFs stat) {
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	private long getTotalInternalMemorySize(StatFs stat) {
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	private void addInformation(StringBuilder message) {
		message.append("Locale: ").append(Locale.getDefault()).append('\n');
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi;
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			message.append("Version: ").append(pi.versionName).append('\n');
			message.append("Package: ").append(pi.packageName).append('\n');
		} catch (Exception e) {
			Log.e("CustomExceptionHandler", "Error", e);
			message.append("Could not get Version information for ").append(
					context.getPackageName());
		}
		message.append("Phone Model: ").append(android.os.Build.MODEL)
		.append('\n');
		message.append("Android Version: ")
		.append(android.os.Build.VERSION.RELEASE).append('\n');
		message.append("Board: ").append(android.os.Build.BOARD).append('\n');
		message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
		message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
		message.append("Host: ").append(android.os.Build.HOST).append('\n');
		message.append("ID: ").append(android.os.Build.ID).append('\n');
		message.append("Model: ").append(android.os.Build.MODEL).append('\n');
		message.append("Product: ").append(android.os.Build.PRODUCT)
		.append('\n');
		message.append("Type: ").append(android.os.Build.TYPE).append('\n');
		StatFs stat = getStatFs();
		message.append("Total Internal memory: ")
		.append(getTotalInternalMemorySize(stat)).append('\n');
		message.append("Available Internal memory: ")
		.append(getAvailableInternalMemorySize(stat)).append('\n');
	}

	public void uncaughtException(final Thread t, final Throwable e) {

		try {
			StringBuilder report = new StringBuilder();

			Date curDate = new Date();
			report.append("\nError Report collected on : ")
			.append(curDate.toString()).append('\n').append('\n');
			report.append("Informations :").append('\n');
			addInformation(report);
			report.append('\n').append('\n');
			report.append("Stack:\n");
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			report.append(result.toString());
			printWriter.close();
			report.append('\n');
			report.append("**** End of current Report ***");
			Log.e(UnCaughtException.class.getName(),
					"Error while sendErrorMail" + report);
			sendErrorMail(report);



		} catch (Throwable ignore) {
			Log.e(UnCaughtException.class.getName(),
					"Error while sending error e-mail", ignore);
		}


	}

	/**
	 * This method for call alert dialog when <span id="IL_AD7" class="IL_AD">application</span> crashed!
	 */
	public void sendErrorMail(final StringBuilder errorContent) {
		new Thread() {
			@Override
			public void run() {
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				reportError(errorContent);//put a time out if required
				if(!(act instanceof Activity)){
					//restart service
					return;
				}
				final AlertDialog.Builder builder = new AlertDialog.Builder(context);

				Looper.prepare();
				builder.setTitle("Sorry...!");
				builder.create();
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						System.exit(0);
					}
				});
				builder.setPositiveButton("Re-launch",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						PendingIntent intent = PendingIntent.getActivity(context, 0,
								new Intent(act.getIntent()), act.getIntent().getFlags());
						AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
						mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
						System.exit(2);
					}
				});
				builder.setMessage("Fishbowl has crashed and error has been reported");
				try {
					builder.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Looper.loop();
			}
		}.start();
	}
	public void reportError(final StringBuilder errorContent){
		try{
			JSONObject userJSON = new JSONObject();
			userJSON.put("OyeError-errorContent", errorContent.toString());
			userJSON.put("OyeError-client", 100);

			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			RequestQueue queue = Volley.newRequestQueue(context);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.POST, "", requestObj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							System.out.println("success resposne of sendingf error " + response);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							if(error != null){
								System.out.println("reportError failure response: " + error.toString());
							}
						}
					});
			queue.add(jsObjRequest);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		//		return null;
	}
}