package com.com.boha.monitor.library.util;

import android.content.Context;
import android.util.Log;

import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureUtil {

    public static void uploadImage(PhotoUploadDTO dto, boolean isFullPicture,
                                   Context ctx, final PhotoUploadDTO.PhotoUploadedListener listener) {

        if (dto.getDateUploaded() != null) return;
        if (dto.getThumbFilePath() == null) return;
        File imageFile = new File(dto.getThumbFilePath());
        if (isFullPicture) {
            imageFile = new File(dto.getImageFilePath());
        }
        Log.w(LOG, "*** File to be uploaded - length: " + imageFile.length() + " - " + imageFile.getAbsolutePath());
        List<File> files = new ArrayList<File>();
        if (imageFile.exists()) {
            files.add(imageFile);
            //set ...
            ImageUpload.upload(dto, files, ctx,
                    new ImageUpload.ImageUploadListener() {
                        @Override
                        public void onUploadError() {
                            listener.onPhotoUploadFailed();
                            Log.e(LOG,
                                    "Error uploading - onUploadError");
                        }

                        @Override
                        public void onImageUploaded(ResponseDTO response) {
                            if (response.getStatusCode() == 0) {
                                listener.onPhotoUploaded();
                            } else {
                                Log.e(LOG,
                                        "Error uploading - "
                                                + response.getMessage()
                                );
                            }
                        }
                    }
            );
        }
    }

    private static final String LOG = "PictureUtil";


}
