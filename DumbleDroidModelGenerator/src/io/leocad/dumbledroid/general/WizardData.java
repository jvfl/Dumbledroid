package io.leocad.dumbledroid.general;

import io.leocad.dumbledroid.net.HttpLoader;
import io.leocad.dumbledroid.net.HttpMethod;
import io.leocad.dumbledroid.net.TimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WizardData {
	
	public String url;
	public JSONObject jsonObject;
	public JSONArray jsonArray;
	
	public String getJson() throws JSONException {
		String result = url;
		try {
			HttpResponse httpResponse = HttpLoader.getHttpResponse(url, HTTP.UTF_8, new ArrayList(), HttpMethod.GET);
			InputStream is = HttpLoader.getHttpContent(httpResponse);
			String content = HttpLoader.streamToString(is);
			
			try {
				jsonObject = new JSONObject(content);
				result = jsonObject.toString();
			} catch (JSONException e) {
				//Is a JSON array
				jsonArray = new JSONArray(content);
			}
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
