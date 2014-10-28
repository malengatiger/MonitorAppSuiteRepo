package com.com.boha.monitor.library.util.bean;

public class CommsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int type;
	public CommsException(int type) {
		this.type = type;
		
	}
	
	public static final int CONNECTION_ERROR = 1;
}
