package com.com.boha.monitor.library.util;

public class PMException extends Exception {
        public PMException(String message) {
           this.message = message; 
        }
        public String message;
    }