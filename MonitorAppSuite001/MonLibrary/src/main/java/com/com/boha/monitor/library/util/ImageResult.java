package com.com.boha.monitor.library.util;
import android.graphics.Bitmap;

public class ImageResult {

	Bitmap bitmap;
	int statusCode;
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
