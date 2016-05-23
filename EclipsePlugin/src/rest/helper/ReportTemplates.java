package rest.helper;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReportTemplates {
	protected static ArrayList keys = new ArrayList();

	public static ArrayList getTemplateKeys() throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(
					"http://localhost:9080/syncope/rest/reportTemplates");

			getRequest.addHeader("accept", "application/json");
			getRequest.addHeader("authorization", "Basic YWRtaW46cGFzc3dvcmQ=");

			// Send the request; It will immediately return the response inSystem.out.println
			// HttpResponse object
			HttpResponse response = httpClient.execute(getRequest);

			// verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new RuntimeException("Failed with HTTP error code : "
						+ statusCode);
			}

			// Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);

			// Lets see what we got from API
			System.out.println(apiOutput);
			JSONParser parser = new JSONParser();
			JSONArray listKeys = (JSONArray) parser.parse(apiOutput);
			for (int i = 0; i < listKeys.size(); i++) {
				JSONObject key = (JSONObject) listKeys.get(i);
				keys.add(key.get("key"));
			}
		return keys;
	}
}
