package com.com.boha.monitor.library.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.toolbox.BaseVolley;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import khandroid.ext.apache.http.HttpEntity;
import khandroid.ext.apache.http.HttpResponse;
import khandroid.ext.apache.http.client.HttpClient;
import khandroid.ext.apache.http.client.methods.HttpPost;
import khandroid.ext.apache.http.entity.mime.MultipartEntity;
import khandroid.ext.apache.http.entity.mime.content.FileBody;
import khandroid.ext.apache.http.entity.mime.content.StringBody;
import khandroid.ext.apache.http.impl.client.DefaultHttpClient;
import khandroid.ext.apache.http.util.ByteArrayBuffer;

public class ImageUpload {

	public interface ImageUploadListener {
		public void onImageUploaded(ResponseDTO response);
		public void onUploadError();
	}
	static final String LOGTAG = "ImageUpload";
	static PhotoUploadDTO photoUpload;
	static List<File> mFiles;
	static ImageUploadListener imageUploadListener;
	static ResponseDTO response;

	public static void upload(PhotoUploadDTO dto, List<File> files, Context ctx,
			ImageUploadListener listener) {
		photoUpload = dto;
		mFiles = files;
		imageUploadListener = listener;
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
		Log.i(LOGTAG, "....starting image upload");
		new ImageUploadTask().execute();
	}

	static class ImageUploadTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			InputStream is = null;
			String responseJSON = null;
			try {
				response = new ResponseDTO();
				MultipartEntity reqEntity = null;
				try {
					reqEntity = new MultipartEntity();
				} catch (Exception e) {
					Log.e(LOGTAG, "MultiPartEntity Error - some null pointer!",
							e);
					throw new Exception();
				}
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Statics.URL + "photo");
				Log.d(LOGTAG, "-------->>> sending image upload to " + Statics.URL + "photo");

				Gson gson = new Gson();
				String json = gson.toJson(photoUpload);
				Log.e(LOGTAG, "*** json to be sent: " + gson.toJson(photoUpload));
				reqEntity.addPart("JSON", new StringBody(json));

				int idx = 1;
				for (File file : mFiles) {
					FileBody fileBody = new FileBody(file);
					reqEntity.addPart("ImageFile" + idx, fileBody);
					idx++;
				}

				httppost.setEntity(reqEntity);
				HttpResponse httpResponse = httpclient.execute(httppost);
				HttpEntity resEntity = httpResponse.getEntity();

				is = resEntity.getContent();
				int size = 0;
				ByteArrayBuffer bab = new ByteArrayBuffer(8192);
				byte[] buffer = new byte[8192];
				while ((size = is.read(buffer, 0, buffer.length)) != -1) {
					bab.append(buffer, 0, size);
				}
				responseJSON = new String(bab.toByteArray());
				if (responseJSON != null) {
					Log.w(LOGTAG, "Response from upload:\n" + responseJSON);
					response = gson.fromJson(responseJSON, ResponseDTO.class);
				}

			} catch (Exception e) {
				Log.e(LOGTAG, "-------------- <<<< Upload failed", e);
				return 9997;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.i(LOGTAG, "onPostExecute...........ending image upload");
			if (result > 0) {
				imageUploadListener.onUploadError();
				return;
			}
			
			imageUploadListener.onImageUploaded(response);
		}

	}

}
