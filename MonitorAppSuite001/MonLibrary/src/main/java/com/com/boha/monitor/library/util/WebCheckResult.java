package com.com.boha.monitor.library.util;

public class WebCheckResult {

	private boolean wifiAvailable, 
	wifiConnected, mobileConnected,
	mobileAvailable, networkUnavailable;

	public boolean isWifiAvailable() {
		return wifiAvailable;
	}

	public void setWifiAvailable(boolean wifiAvailable) {
		this.wifiAvailable = wifiAvailable;
	}

	public boolean isMobileAvailable() {
		return mobileAvailable;
	}

	public void setMobileAvailable(boolean mobileAvailable) {
		this.mobileAvailable = mobileAvailable;
	}

	public boolean isNetworkUnavailable() {
		return networkUnavailable;
	}

	public void setNetworkUnavailable(boolean networkUnavailable) {
		this.networkUnavailable = networkUnavailable;
	}

	public boolean isWifiConnected() {
		return wifiConnected;
	}

	public void setWifiConnected(boolean wifiConnected) {
		this.wifiConnected = wifiConnected;
	}

	public boolean isMobileConnected() {
		return mobileConnected;
	}

	public void setMobileConnected(boolean mobileConnected) {
		this.mobileConnected = mobileConnected;
	}
}
