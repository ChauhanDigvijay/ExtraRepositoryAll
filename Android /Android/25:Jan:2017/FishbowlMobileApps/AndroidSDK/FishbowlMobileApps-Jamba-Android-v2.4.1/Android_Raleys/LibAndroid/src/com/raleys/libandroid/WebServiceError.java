package com.raleys.libandroid;


public class WebServiceError
{
	public int errorCode;
	public String errorMessage;

	public WebServiceError()
	{
	}


	public WebServiceError(int code, String message)
	{
		errorCode = code;
		errorMessage = message;
	}
}
