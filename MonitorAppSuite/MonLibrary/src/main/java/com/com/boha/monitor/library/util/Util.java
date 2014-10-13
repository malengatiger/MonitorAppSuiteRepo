package com.com.boha.monitor.library.util;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.boha.monitor.library.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Util {

	public static final long HOUR = 60 * 60 * 1000;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;
	public static final long WEEKS = 2 * WEEK;
	public static final long MONTH = 30 * DAY;

	public static String getTruncated(double num) {
		String x = "" + num;
		int idx = x.indexOf(".");
		String xy = x.substring(idx+1);
		if (xy.length() > 2) {
			String y = x.substring(0, idx + 2);
			return y;
		} else {
			return x;
		}
	}
	public static Intent getMailIntent(Context ctx,String email, String message, String subject,
			File file) {

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		if (email == null) {
			sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "",
					"aubrey.malabie@gmail.com" });
		} else {
			sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		}

		if (subject == null) {
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,
					subject);
		} else {
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		}
		sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			sendIntent.putExtra(Intent.EXTRA_TEXT, message);
		
		sendIntent.setType("application/pdf");
		
		return sendIntent;
	}
	public static double getPercentage(int totalMarks, int attained) {
		BigDecimal total = new BigDecimal(totalMarks);
		BigDecimal totStu = new BigDecimal(attained);
		double perc = totStu.divide(total, 3, BigDecimal.ROUND_UP).doubleValue();
		perc = perc * 100;
		return perc;
	}
	public static String formatCellphone(String cellphone) {
		StringBuilder sb = new StringBuilder();
		String suff = cellphone.substring(0, 3);
		String p1 = cellphone.substring(3, 6);
		String p2 = cellphone.substring(6);
		sb.append(suff).append(" ");
		sb.append(p1).append(" ");
		sb.append(p2);
		return sb.toString();
	}


    private void animateText(View txt) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(txt, View.SCALE_X, 0);
        an.setRepeatCount(1);
        an.setDuration(200);
        an.setRepeatMode(ValueAnimator.REVERSE);
        an.start();
    }
    public static void animateScaleX(View txt, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(txt, View.SCALE_X, 0);
        an.setRepeatCount(1);
        an.setDuration(duration);
        an.setRepeatMode(ValueAnimator.REVERSE);
        an.start();
    }
    public static void animateScaleY(View txt, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(txt, View.SCALE_Y, 0);
        an.setRepeatCount(1);
        an.setDuration(duration);
        an.setRepeatMode(ValueAnimator.REVERSE);
        an.start();
    }
    public static void animateRotationY(View view, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(view, "rotation", 0.0f, 360f);
        //an.setRepeatCount(ObjectAnimator.REVERSE);
        an.setDuration(duration);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }
    public static void animateFlipFade(Context ctx, View v) {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(ctx,
                R.animator.flip_fade);
        set.setTarget(v);
        set.start();
    }
	public static ArrayList<String> getRecurStrings(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String day = getDayOfWeek(dayOfWeek);
		boolean isWeekDay = false;
		if (dayOfWeek > 1 && dayOfWeek < 7) {
			isWeekDay = true;
		}
		Log.d("Util", "#########");
		Log.d("Util", "dayOfWeek: " + dayOfWeek + " day: " + day
				+ " isWeekDay: " + isWeekDay);
		// which week?
		int week = 0;
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth < 8) {
			week = 1;
		}
		if (dayOfMonth > 7 && dayOfMonth < 15) {
			week = 2;
		}
		if (dayOfMonth > 14 && dayOfMonth < 22) {
			week = 3;
		}
		if (dayOfMonth > 21) {
			week = 4;
		}
		Log.d("Util", "dayOfMonth: " + dayOfMonth + " week in month: "
				+ getWeekOfMonth(week));

		ArrayList<String> list = new ArrayList<String>();
		list.add("One time event");
		list.add("Daily Event");

		list.add("Every Week day(Mon-Fri)");
		list.add("Weekly on " + getDayOfWeek(dayOfWeek));
		list.add("Monthly (every " + getWeekOfMonth(week) + " "
				+ getDayOfWeek(dayOfWeek));
		list.add("Monthly on day " + dayOfMonth);
		String month = getMonth(cal.get(Calendar.MONTH));
		list.add("Yearly on " + dayOfMonth + " " + month);
		return list;
	}

	public static String getMonth(int mth) {
		switch (mth) {
		case 0:
			return "January";
		case 1:
			return "February";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";
		}
		return null;
	}

	public static String getWeekOfMonth(int i) {
		switch (i) {
		case 1:
			return FIRST_WEEK;
		case 2:
			return SECOND_WEEK;
		case 4:
			return FOURTH_WEEK;
		case 3:
			return THIRD_WEEK;

		}
		return MONDAY;
	}

	public static String getDayOfWeek(int i) {
		switch (i) {
		case 2:
			return MONDAY;
		case 3:
			return TUESDAY;
		case 4:
			return WEDNESDAY;
		case 5:
			return THURSDAY;
		case 6:
			return FRIDAY;
		case 7:
			return SATURDAY;
		case 1:
			return SUNDAY;

		}
		return MONDAY;
	}



	public static final String MONDAY = "Monday";
	public static final String TUESDAY = "Tuesday";
	public static final String WEDNESDAY = "Wednesday";
	public static final String THURSDAY = "Thursday";
	public static final String FRIDAY = "Friday";
	public static final String SATURDAY = "Saturday";
	public static final String SUNDAY = "Sunday";

	public static final String FIRST_WEEK = "First";
	public static final String SECOND_WEEK = "Second";
	public static final String THIRD_WEEK = "Third";
	public static final String FOURTH_WEEK = "Fourth";

	public static int[] getDateParts(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		int[] ints = { cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH),
				cal.get(Calendar.YEAR) };
		return ints;
	}

	public static File getDirectory(String dir) {
		File sd = Environment.getExternalStorageDirectory();
		File appDir = new File(sd, dir);
		if (!appDir.exists()) {
			appDir.mkdir();
		}

		return appDir;

	}

	public static String getLongTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(date);
	}

	public static String getShortTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(date);
	}

	public static int[] getTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("HH");
		String hr = df.format(date);
		df = new SimpleDateFormat("mm");
		String min = df.format(date);
		int[] time = { Integer.parseInt(hr), Integer.parseInt(min) };
		return time;
	}

	public static long getSimpleDate(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
		cal.set(GregorianCalendar.MINUTE, 0);
		cal.set(GregorianCalendar.SECOND, 0);
		cal.set(GregorianCalendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static long getSimpleDate(int day, int month, int year) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DAY_OF_MONTH, day);
		return getSimpleDate(cal.getTime());
	}

	public static String getLongerDate(int day, int month, int year) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DAY_OF_MONTH, day);
		Date d = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM, yyyy");
		return df.format(d);
	}

	public static String getLongDate(int day, int month, int year) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DAY_OF_MONTH, day);
		Date d = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
		return df.format(d);
	}
	public static String getLongestDate(int day, int month, int year) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DAY_OF_MONTH, day);
		Date d = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
		return df.format(d);
	}

	public static String getLongDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
		return df.format(date);
	}

	public static String getLongerDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy");
		return df.format(date);
	}

	public static String getLongDateTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(
				"EEEE, dd MMMM yyyy HH:mm:ss");
		return df.format(date);
	}

	public static Calendar getLongDateTimeNoSeconds(Calendar cal) {

		int year = cal.get(Calendar.YEAR);
		int mth = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, mth);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Log.d("Util", "Reset date: " + getLongDateTimeNoSeconds(c.getTime()));
		return c;
	}

	public static String getLongDateTimeNoSeconds(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm");
		return df.format(date);
	}

	public static String getLongDateForPDF(long date) {
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy");
		Date d = new Date(date);
		return df.format(d);
	}

	public static String getShortDateForPDF(long startDate, long endDate) {
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
		Date dtStart = new Date(startDate);
		Date dtEnd = new Date(endDate);

		return df.format(dtStart) + " to " + df.format(dtEnd);
	}
	public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float)rotatedWidth) / ((float)MAX_IMAGE_DIMENSION);
            float heightRatio = ((float)rotatedHeight) / ((float)MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /* if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation. */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }
        
        String type = context.getContentResolver().getType(photoUri);
        if (type == null) {
        	type = "image/jpg";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(type.equals("image/png")) {
        	srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if(type.equals("image/jpg") || type.equals("image/jpeg")) {
        	srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }
	public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        if (cursor == null) {
        	return 1;
        }
        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
}

	
	private static int MAX_IMAGE_DIMENSION = 720;
}
