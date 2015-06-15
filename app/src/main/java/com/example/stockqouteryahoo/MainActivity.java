package com.example.stockqouteryahoo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	String TAG = "stocks";

	// The JSON REST Service I will pull from
	static String yahooStockInfo = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	
	// Will hold the values I pull from the JSON 
	static String stockSymbol = "";
	static String stockDaysLow = "";
	static String stockDaysHigh = "";
	static String stockChange = "";
	static String stockYearHigh = "";
	static String stockYearLow = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Get any saved data
		super.onCreate(savedInstanceState);
		
		Log.e(TAG, "here is oncreate");

		// Point to the name for the layout xml file used
		setContentView(R.layout.main);

		// Call for doInBackground() in MyAsyncTask to be executed
		new MyAsyncTask().execute();

	}
	
	// Use AsyncTask if you need to perform background tasks, but also need
	// to change components on the GUI. Put the background operations in
	// doInBackground. Put the GUI manipulation code in onPostExecute

	private class MyAsyncTask extends AsyncTask<String, String, String> {

		private static final String TAG = "json parser";

		protected String doInBackground(String... arg0) {
			
			Log.e(TAG, "here is doInBackground");

			// HTTP Client that supports streaming uploads and downloads
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			// Define that I want to use the POST method to grab data from
			// the provided URL
			HttpPost httppost = new HttpPost(yahooStockInfo);
			
			// Web service used is defined
			httppost.setHeader("Content-type", "application/json");

			// Used to read data from the URL
			InputStream inputStream = null;
			
			// Will hold the whole all the data gathered from the URL
			String result = null;

			try {
				
				// Get a response if any from the web service
				HttpResponse response = httpclient.execute(httppost);        
				
				// The content from the requested URL along with headers, etc.
				HttpEntity entity = response.getEntity();

				// Get the main content from the URL
				inputStream = entity.getContent();
				
				// JSON is UTF-8 by default
				// BufferedReader reads data from the InputStream until the Buffer is full
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				
				// Will store the data
				StringBuilder theStringBuilder = new StringBuilder();

				String line = null;
				
				// Read in the data from the Buffer untilnothing is left
				while ((line = reader.readLine()) != null)
				{
					
					// Add data from the buffer to the StringBuilder
					theStringBuilder.append(line + "\n");
				}
				
				// Store the complete data in result
				result = theStringBuilder.toString();
				
				Log.e(TAG, "here is result" + result);


			} catch (Exception e) { 
				e.printStackTrace();
			}
			finally {
				
				// Close the InputStream when you're done with it
				try{if(inputStream != null)inputStream.close();}
				catch(Exception e){}
			}

			// Holds Key Value pairs from a JSON source
			JSONObject jsonObject;
			try {

				Log.e(TAG, "here is try" );

				jsonObject = new JSONObject(result);
				
				JSONObject reader = new JSONObject(result);
				
				JSONObject sys  = reader.getJSONObject("query");

				String count = sys.getString("count");
				
				Log.e(TAG, "0 here is count " + count);
				
				// Get the JSON object named query
				JSONObject queryJSONObject = jsonObject.getJSONObject("query");
				Log.e(TAG, "1 here is queryJSONObject" + queryJSONObject);

				// Get the JSON object named results inside of the query object
				JSONObject resultsJSONObject = queryJSONObject.getJSONObject("results");
				Log.e(TAG, "2 here is resultsJSONObject" + resultsJSONObject);

				// Get the JSON object named quote inside of the results object
				//1JSONObject quoteJSONObject = resultsJSONObject.getJSONObject("quote");
				////////////////////////////////////////////////////////////
				//JSONArray jar = new JSONArray("quote");
				
				JSONArray jar = resultsJSONObject.getJSONArray("quote");
				
				Log.e(TAG, "3 here is jar " + jar);

				String stock1 = jar.getString(0);
				String stock2 = jar.getString(1);
				
				JSONObject symbolJSONObject = jar.getJSONObject(1);
				
				Log.e(TAG, "3 here is stock1 " + stock1);
				Log.e(TAG, "3 here is stock2 " + stock2);
			
				//works but gives all info stockYearHigh = jar.optString(0, "YearHigh");//new
				stockSymbol  = symbolJSONObject.getString("Symbol");//new
				stockYearHigh = symbolJSONObject.getString("YearHigh");//new
				stockYearLow = symbolJSONObject.getString("YearLow");//new
				stockDaysLow = symbolJSONObject.getString("DaysLow");
				stockDaysHigh = symbolJSONObject.getString("DaysHigh");
				stockChange = symbolJSONObject.getString("Change");
				
				Log.e(TAG, "here is stockSymbol 4" + stockSymbol);
	
				// EXTRA STUFF THAT HAS NOTHING TO DO WITH THE PROGRAM
				
				Log.e("SYMBOL ", stockSymbol);
				Log.e("Days Low ", stockDaysLow);
				Log.e("Days High ", stockDaysHigh);
				Log.e("Change ", stockChange);
				
				// GET ARRAY DATA

				// END OF GET ARRAY DATA

				// Gets the first item in the JSONObject
				JSONArray objectArray = resultsJSONObject.names();

				// Prints out that first item in the JSONObject
				Log.v("JSON NEXT NODE ", objectArray.getString(0));


			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}

		protected void onPostExecute(String result){

			// Gain access so I can change the TextViews
			TextView line1 = (TextView)findViewById(R.id.line1); 
			TextView line2 = (TextView)findViewById(R.id.line2); 
			TextView line3 = (TextView)findViewById(R.id.line3);
			TextView line4 = (TextView)findViewById(R.id.line4);//new 
			TextView line5 = (TextView)findViewById(R.id.line5);//new 
			TextView line6 = (TextView)findViewById(R.id.line6);//new 

			// Change the values for all the TextViews
			line1.setText("Stock: " + stockSymbol); 

			line2.setText("Days Low: " + stockDaysLow); 

			line3.setText("Days High: " + stockDaysHigh);
			
			line4.setText("Year High: " + stockYearHigh);//new........
			
			line5.setText("Year Low: " + stockYearLow);//new........

			line6.setText("Stock Change: " +  stockChange);//new........

		}

	}

}

