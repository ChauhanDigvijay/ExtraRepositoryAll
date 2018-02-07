package com.raleys.libandroid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonData
{
	public String toJsonString()
	{
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
}
