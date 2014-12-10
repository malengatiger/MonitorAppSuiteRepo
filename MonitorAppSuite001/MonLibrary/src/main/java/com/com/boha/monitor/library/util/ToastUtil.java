package com.com.boha.monitor.library.util;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.boha.monitor.library.R;


public class ToastUtil {

	public static void toast(Context ctx, String message, int durationSeconds,
			int gravity) {
		Vibrator vb = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vb.vibrate(30);
		LayoutInflater inf = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View main = inf.inflate(R.layout.toast, null);

		TextView msg = (TextView) main.findViewById(R.id.txtTOASTMessage);
		msg.setText(message);

		CustomToast toast = new CustomToast(ctx, durationSeconds);
		toast.setGravity(gravity, 0, 0);
		toast.setView(main);
		toast.show();
	}

	public static void toast(Context ctx, String message) {
		Vibrator vb = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vb.vibrate(30);
		LayoutInflater inf = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View main = inf.inflate(R.layout.toast, null);

		TextView msg = (TextView) main.findViewById(R.id.txtTOASTMessage);
		msg.setText(message);

		CustomToast toast = new CustomToast(ctx, 3);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(main);
		toast.show();
	}
	public static void errorToast(Context ctx, String message) {
        if (message == null) {
            Log.e("ToastUtil", "Error message is NULL");
            return;
        }
		Vibrator vb = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vb.vibrate(50);
		LayoutInflater inf = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View main = inf.inflate(R.layout.toast_error, null);

		TextView msg = (TextView) main.findViewById(R.id.txtTOASTMessage);
		msg.setText(message);

		Toast toast = new Toast(ctx);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(main);
		toast.show();

		Log.e("ToastUtil", message);
	}
	public static void noNetworkToast(Context ctx) {
		Vibrator vb = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vb.vibrate(50);
		LayoutInflater inf = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout main = (LinearLayout) inf.inflate(R.layout.toast_error, null);

		TextView msg = (TextView) main.findViewById(R.id.txtTOASTMessage);
		msg.setText("Network not available. Please check your network settings and your reception signal.\n" +
				"Please try again.");

		Toast toast = new Toast(ctx);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(main);
		toast.show();

	}


	

	public static void serverUnavailable(Context ctx) {
		
		Util.showErrorToast(ctx, "Server is not available or reachable at " +
				"this time. Please try later or contact GhostPractice support.");
	}

	public static void memoryUnavailable(Context ctx) {
		
		Util.showErrorToast(ctx, "Memory required is not available");
	}
	/*
	 * public static void locationUnavailable(Context ctx,int languageCode) {
	 * Util.showToast(ctx, TX.translate(languageCode,
	 * "GPS coordinates are not available.\nPlease check Location Settings on your phone"
	 * , ctx), Toast.LENGTH_LONG, Gravity.CENTER); } public static void
	 * underConstruction(Context ctx,int languageCode) { LayoutInflater inf =
	 * (LayoutInflater)ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
	 * LinearLayout main = (LinearLayout) inf.inflate(R.layout.toast, null);
	 * ImageView img = (ImageView)main.findViewById(R.id.imgTOASTOK); Drawable d
	 * = ctx.getResources().getDrawable(R.drawable.under_construction);
	 * img.setImageDrawable(d);
	 * 
	 * String message = TX.translate(languageCode,
	 * "Sorry, function under construction. Please try later", ctx); TextView
	 * msg = (TextView)main.findViewById(R.id.txtTOASTMessage);
	 * msg.setText(message);
	 * 
	 * Toast toast = new Toast(ctx); toast.setGravity(Gravity.CENTER,0,0);
	 * toast.setDuration(Toast.LENGTH_SHORT); toast.setView(main); toast.show();
	 * } public static void storageProblem(Context ctx,int languageCode) {
	 * LayoutInflater inf = (LayoutInflater)ctx.getSystemService(
	 * Context.LAYOUT_INFLATER_SERVICE); LinearLayout main = (LinearLayout)
	 * inf.inflate(R.layout.toast, null); ImageView img =
	 * (ImageView)main.findViewById(R.id.imgTOASTOK); Drawable d =
	 * ctx.getResources().getDrawable(R.drawable.more); img.setImageDrawable(d);
	 * 
	 * String message = TX.translate(languageCode,
	 * "Sorry, You may not have an SD memory card", ctx); TextView msg =
	 * (TextView)main.findViewById(R.id.txtTOASTMessage); msg.setText(message);
	 * 
	 * Toast toast = new Toast(ctx); toast.setGravity(Gravity.CENTER,0,0);
	 * toast.setDuration(Toast.LENGTH_SHORT); toast.setView(main); toast.show();
	 * } public static void cameraProblem(Context ctx,int languageCode) {
	 * LayoutInflater inf = (LayoutInflater)ctx.getSystemService(
	 * Context.LAYOUT_INFLATER_SERVICE); LinearLayout main = (LinearLayout)
	 * inf.inflate(R.layout.toast, null); ImageView img =
	 * (ImageView)main.findViewById(R.id.imgTOASTOK); Drawable d =
	 * ctx.getResources().getDrawable(R.drawable.cam_nikon);
	 * img.setImageDrawable(d);
	 * 
	 * String message = TX.translate(languageCode,
	 * "Sorry, Camera problem. Please try later", ctx); TextView msg =
	 * (TextView)main.findViewById(R.id.txtTOASTMessage); msg.setText(message);
	 * 
	 * Toast toast = new Toast(ctx); toast.setGravity(Gravity.CENTER,0,0);
	 * toast.setDuration(Toast.LENGTH_SHORT); toast.setView(main); toast.show();
	 * } public static void imageDownloadProblem(Context ctx,int languageCode) {
	 * LayoutInflater inf = (LayoutInflater)ctx.getSystemService(
	 * Context.LAYOUT_INFLATER_SERVICE); LinearLayout main = (LinearLayout)
	 * inf.inflate(R.layout.toast, null); ImageView img =
	 * (ImageView)main.findViewById(R.id.imgTOASTOK); Drawable d =
	 * ctx.getResources().getDrawable(R.drawable.cam_nikon);
	 * img.setImageDrawable(d);
	 * 
	 * String message = TX.translate(languageCode,
	 * "Sorry, unable to download image", ctx); TextView msg =
	 * (TextView)main.findViewById(R.id.txtTOASTMessage); msg.setText(message);
	 * 
	 * Toast toast = new Toast(ctx); toast.setGravity(Gravity.CENTER,0,0);
	 * toast.setDuration(Toast.LENGTH_SHORT); toast.setView(main); toast.show();
	 * }
	 */
}
