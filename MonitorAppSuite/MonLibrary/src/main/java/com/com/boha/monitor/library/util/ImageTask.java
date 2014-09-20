package com.com.boha.monitor.library.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

public class ImageTask {

	static Bitmaps bitmaps;
	static Uri u;
	static Context ctx;
    static float rotation;
	static BitmapListener bitmapListener;
	
	public static void getResizedBitmaps(Uri uri, Context context,BitmapListener listener, float rotate) {
		ctx = context;
		bitmapListener = listener;
		u = uri;
        rotation = rotate;
		new DTask().execute();
	}
	
	static class DTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			try {
                //
                Bitmap bm = ImageUtil.getBitmapFromUri(ctx, u);
                Log.e(LOG, "Full size bitmap: width: "
                        + bm.getWidth() + " height: "
                        + bm.getHeight() + " rowBytes: "
                        + bm.getRowBytes());
                //thumb
                Matrix matrix = new Matrix();
                matrix.postScale(0.2f, 0.2f);
                matrix.postRotate(rotation);
                Bitmap thumb = Bitmap.createBitmap
                        (bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                Log.e(LOG, "thumb bitmap: width: "
                        + thumb.getWidth() + " height: "
                        + thumb.getHeight() + " rowBytes: "
                        + thumb.getRowBytes());
                //full
                matrix = new Matrix();
                matrix.postScale(0.6f, 0.6f);
                matrix.postRotate(rotation);
                Bitmap full = Bitmap.createBitmap
                        (bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                Log.e(LOG, "full bitmap: width: "
                        + full.getWidth() + " height: "
                        + full.getHeight() + " rowBytes: "
                        + full.getRowBytes());
                //
				bitmaps = new Bitmaps();
				bitmaps.setLargeBitmap(full);
				bitmaps.setThumbNail(thumb);
				//save bitmaps on disk as files
				File fReg = ImageUtil.getFileFromBitmap(full, "picM"+System.currentTimeMillis() + ".jpg");
				File fThumb = ImageUtil.getFileFromBitmap(thumb, "picT"+System.currentTimeMillis() + ".jpg");
				
				//save uri's in SharedPrefs
				//SharedUtil.saveImageUri(ctx, Uri.fromFile(fReg));
				//SharedUtil.saveThumbUri(ctx, Uri.fromFile(fThumb));
                Log.e(LOG, "Resized files, regular: " + fReg.length() + " thumb: " + fThumb.length());
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
			return 0;
		}
		@Override
		protected void onPostExecute(Integer res) {
			if (res.intValue() > 0) {
				bitmapListener.onError();
				return;
			}
			bitmapListener.onBitmapsResized(bitmaps);
		}
		
	}
    static final String LOG = "ImageTask";
}
