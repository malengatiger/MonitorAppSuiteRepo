package com.com.boha.monitor.library.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import com.android.volley.toolbox.NetworkImageView;
import com.boha.monitor.library.R;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtil {

    /**
     * Draws date and gps coordinates on the bottom of the image
     * @param mContext
     * @param path
     * @param loc
     * @return
     * @throws Exception
     */

    public static Bitmap drawTextToBitmap(Context mContext, String path, Location loc) throws Exception {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return drawTextToBitmap(mContext, bitmap, loc);


    }

    /**
     * Draws date and gps coordinates on the bottom of the image
     * @param mContext
     * @param bitmap
     * @param location
     * @return
     */
    public static Bitmap drawTextToBitmap(Context mContext, Bitmap bitmap, Location location) {
        Resources resources = mContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mContext.getResources().getColor(R.color.white));
        paint.setTextSize((int) 13);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(mContext.getResources().getColor(R.color.green));

        String text = getText(location);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.getFontMetrics(fm);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (height > width) {
            canvas.drawRect(0, height - 40, height - 40, height, paint2);
            canvas.drawText(text, 5, height - 10, paint);
        } else {
            canvas.drawRect(0, 0, width, 40, paint2);
            canvas.drawText(text, 5, 25, paint);
        }


        Log.i("Util", "Text has been drawn on bitmap? - check");
        return bitmap;
    }


    private static String getText(Location loc) {
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(new Date()));
        if (loc != null) {
            sb.append(" - lat: ").append(loc.getLatitude());
            sb.append(" lng: ").append(loc.getLongitude());
        }
        return sb.toString();
    }

    static final Locale locale = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", locale);

    public static Bitmap scaleBitMap(Context ctx, Bitmap bitmap, int size) {
        // Bitmap bitmap = ((BitmapDrawable)drawing).drawTextToBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(ctx, size);

        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the
        // ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        return scaledBitmap;
    }

    private static int dpToPx(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static Bitmap createSepiaToningEffect(Bitmap src, int depth,
                                                 double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // constant grayscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply grayscale sample
                B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepid-toning on each channel
                R += (depth * red);
                if (R > 255) {
                    R = 255;
                }

                G += (depth * green);
                if (G > 255) {
                    G = 255;
                }

                B += (depth * blue);
                if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static Bitmap getBitmapFromUri(Context ctx, Uri uri)
            throws Exception {
        Bitmap bm = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(),
                uri);
        return bm;

    }

    public static File getFileFromUri(Context ctx, Uri uri)
            throws Exception {
        String mimeType = ctx.getContentResolver().getType(uri);
        Cursor returnCursor =
                ctx.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        long size = returnCursor.getLong(sizeIndex);
        returnCursor.close();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file;

        try {
            File dir = ctx.getFilesDir();
            file = new File(dir, name);
            Log.e("Util", "new file: " + file.getAbsolutePath());
            inputStream = ctx.getContentResolver().openInputStream(uri);
            outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[2048];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }



            Log.i("Util", "File Name = " + name + " size: " + size + " mimeType: " + mimeType);
            Log.i("Util", "File from Uri: " + file.getAbsolutePath() + " length: " + file.length());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return file;

    }

    public static int getProfilePictureSize(Context ctx) {
        int screenSize = ctx.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return 320;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return 240;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return 160;
            default:
                return 200;
        }
    }

    public static int getMmeoryClass(Context ctx) {
        ActivityManager am = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();

        return memoryClass;
    }

    public static File getFileFromDrawable(Context ctx, NetworkImageView a,
                                           String fileName) throws Exception {
        Bitmap bm = ((BitmapDrawable) a.getDrawable()).getBitmap();
        File f = getFileFromBitmap(bm, fileName);

        return f;
    }

    public static File getFileFromBitmap(Bitmap bm, String filename)
            throws Exception {
        if (bm == null) throw new Exception();
        File file = null;
        try {
            File rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (rootDir == null) {
                rootDir = Environment.getRootDirectory();
            }
            File imgDir = new File(rootDir, "monitor_app");
            if (!imgDir.exists()) {
                imgDir.mkdir();
            }
            OutputStream outStream = null;
            file = new File(imgDir, filename);
            if (file.exists()) {
                file.delete();
                file = new File(imgDir, filename);
            }
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            outStream.flush();
            outStream.close();

            Log.e(LOGTAG, "File saved from bitmap: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e(LOGTAG, "Failed to get file from bitmap", e);
        }
        return file;

    }

    public static Bitmap getScaledImage(Bitmap bitmap, int maxWidth,
                                        int maxHeight, boolean filter) {
        bitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, filter);
        return bitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int maxWidth,
                                          int maxHeight, float rotate) {
        System.gc();
        Log.d(LOGTAG, "### Original Bitmap width: " + bitmap.getWidth()
                + " height: " + bitmap.getHeight());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float xRatio = (float) maxWidth / width;
        float yRatio = (float) maxHeight / height;
        float scaleRatio = xRatio < yRatio ? xRatio : yRatio;
        Bitmap resizedBitmap = null;
        try {
            Matrix matrix = new Matrix();
            matrix.postScale(scaleRatio, scaleRatio);
            matrix.postRotate(rotate);
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
            Log.d(LOGTAG,
                    "### Resized Bitmap width: " + resizedBitmap.getWidth()
                            + " height: " + resizedBitmap.getHeight()
                            + " rowBytes: " + resizedBitmap.getRowBytes()
            );

        } catch (OutOfMemoryError e) {
            Log.e(LOGTAG, "$$$$$$$$$$ OUT OF MEMORY");
            return null;
        } catch (Exception e) {
            Log.e(LOGTAG, "$$$$$$$$$$ GENERIC EXCEPTION");
            return null;
        } catch (Throwable t) {
            Log.e(LOGTAG, "$$$$ Throwable Exception getting image: ");
            return null;
        }
        return resizedBitmap;
    }

    public static Bitmaps getBitmapThumbnail(Bitmap bitmap) {
        System.gc();
        Log.d(LOGTAG, "### Original Bitmap width: " + bitmap.getWidth()
                + " height: " + bitmap.getHeight());
        Bitmaps maps = new Bitmaps();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxWidth = 200;
        int maxHeight = 160;
        int maxWidth2 = 400;
        int maxHeight2 = 300;

        float xRatio = (float) maxWidth / width;
        float yRatio = (float) maxHeight / height;
        float scaleRatio = xRatio < yRatio ? xRatio : yRatio;

        float xRatio2 = (float) maxWidth2 / width;
        float yRatio2 = (float) maxHeight2 / height;
        float scaleRatio2 = xRatio2 < yRatio2 ? xRatio2 : yRatio2;
        Bitmap resizedBitmap = null;
        try {
            Matrix matrix = new Matrix();
            matrix.postScale(scaleRatio, scaleRatio);
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
            Log.d(LOGTAG,
                    "### Thumbnail width: " + resizedBitmap.getWidth()
                            + " height: " + resizedBitmap.getHeight()
                            + " rowBytes: " + resizedBitmap.getRowBytes()
            );
            maps.setThumbNail(resizedBitmap);
            //
            matrix.postScale(scaleRatio2, scaleRatio2);
            Bitmap resizedBitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
            maps.setLargeBitmap(resizedBitmap2);
            Log.d(LOGTAG,
                    "### Resized Bitmap width: " + resizedBitmap2.getWidth()
                            + " height: " + resizedBitmap2.getHeight()
                            + " rowBytes: " + resizedBitmap2.getRowBytes()
            );


        } catch (OutOfMemoryError e) {
            Log.e(LOGTAG, "$$$$$$$$$$ OUT OF MEMORY");
            return null;
        } catch (Exception e) {
            Log.e(LOGTAG, "$$$$$$$$$$ GENERIC EXCEPTION");
            return null;
        } catch (Throwable t) {
            Log.e(LOGTAG, "$$$$ Throwable Exception getting image: ");
            return null;
        }
        return maps;
    }

    static final String LOGTAG = "ImageUtil";
}
