package com.com.boha.monitor.library.util;

import android.content.Context;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;

public class ErrorUtil {

    public static boolean checkServerError(Context ctx, ResponseDTO response) {
        if (response.getStatusCode() > 0) {
            Util.showErrorToast(ctx, response.getMessage());
            return false;
        }
        return true;
    }
    public static void showServerCommsError(Context ctx) {
        Util.showErrorToast(ctx, ctx.getResources().getString(R.string.error_server_comms));
    }
	public static void handleErrors(Context ctx, int errCode) {
		switch (errCode) {
		case Constants.ERROR_DATABASE:
			ToastUtil
					.errorToast(ctx,
							"Database error. Contact mgGolf support on the web site");
			break;
		case Constants.ERROR_NETWORK_UNAVAILABLE:
			Util.showErrorToast(ctx,
					"Network unavailable. Check settings and signal strength");
			break;
		case Constants.ERROR_ENCODING:
			Util.showErrorToast(ctx,
					"Error encoding request. Contact mgGolf support");
			break;
		case Constants.ERROR_SERVER_COMMS:
			ToastUtil
					.errorToast(ctx,
							"Problem communicating with the server. Please contact mgGolf support");
			break;
		case Constants.ERROR_DUPLICATE:
			Util.showErrorToast(ctx,
					"Attempting to put duplicate data in database, ignored");
			break;
		default:
			break;
		}
	}
}
