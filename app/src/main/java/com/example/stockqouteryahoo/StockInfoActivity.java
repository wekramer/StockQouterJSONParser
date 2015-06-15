//package com.example.stockqouteryahoo;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.net.URLConnection;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.app.Activity;
//import android.content.Intent;
//import android.util.Log;
//import android.view.Menu;
//import android.widget.TextView;
//
////moved to below
////String[][] xmlPullParserArray = {{"AverageDailyVolume", "0"}, {"Change", "0"}, {"DaysLow", "0"}, 
////{"DaysHigh", "0"}, {"YearLow", "0"}, {"YearHigh", "0"}, {"MarketCapitalization", "0"}, 
////{"LastTradePriceOnly", "0"}, {"DaysRange", "0"}, {"Name", "0"}, {"Symbol", "0"}, 
////{"Volume", "0"}, {"StockExchane", "0"}};
//
//
//
//
//public class StockInfoActivity extends Activity {
//	
//	// Used to identify the app in the LogCat, so I
//	// can output messages and debug the program
//	
//	private static final String TAG = "StockInfoActivity";
//	
//	// Define the TextViews I use in activity_stock_info.xml
//	
//	TextView companyNameTextView;
//	TextView yearLowTextView;
//	TextView yearHighTextView;
//	TextView daysLowTextView;
//	TextView daysHighTextView;
//	TextView lastTradePriceOnlyTextView;
//	TextView changeTextView;
//	TextView daysRangeTextView;
//	
//	// XML node keys
//	static final String KEY_ITEM = "quote"; // parent node
//	static final String KEY_NAME = "Name";
//	static final String KEY_YEAR_LOW = "YearLow";
//	static final String KEY_YEAR_HIGH = "YearHigh";
//	static final String KEY_DAYS_LOW = "DaysLow";
//	static final String KEY_DAYS_HIGH = "DaysHigh";
//	static final String KEY_LAST_TRADE_PRICE = "LastTradePriceOnly";
//	static final String KEY_CHANGE = "Change";
//	static final String KEY_DAYS_RANGE = "DaysRange";
//	
//	// XML Data to Retrieve
//	String name = "";
//	String yearLow = "";
//	String yearHigh = "";
//	String daysLow = "";
//	String daysHigh = "";
//	String lastTradePriceOnly = "";
//	String change = "";
//	String daysRange = "";
//	
//	// Used to make the URL to call for XML data
//	//String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
//    //String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
//	
//	//try json data ... get all 0 for array data
//	String yahooURLFirst = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
//	String yahooURLSecond = "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
//	
//	String[][] xmlPullParserArray = {{"AverageDailyVolume", "0"}, {"Change", "0"}, {"DaysLow", "0"}, 
//			{"DaysHigh", "0"}, {"YearLow", "0"}, {"YearHigh", "0"}, {"MarketCapitalization", "0"}, 
//			{"LastTradePriceOnly", "0"}, {"DaysRange", "0"}, {"Name", "0"}, {"Symbol", "0"}, 
//			{"Volume", "0"}, {"StockExchane", "0"}};
//
//	int parserArrayIncrement = 0;
//	
//	/////////////////////////////////
//	
//	String yqlURL;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		Log.e(TAG, "onCreate" );
//////////////////////////////////////////////////////////////
//		
//		// Point to the name for the layout xml file used
//				setContentView(R.layout.main);
//
//		
////////////////////////////////////////////////////////////		
//		
//		// Creates the window used for the UI
//		setContentView(R.layout.activity_stock_info);
//		
//		// Get the message from the intent that has the stock symbol
//		Intent intent = getIntent();
//		String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);
//		
//		// Initialize TextViews
//		companyNameTextView = (TextView) findViewById(R.id.companyNameTextView);
//		yearLowTextView = (TextView) findViewById(R.id.yearLowTextView);
//		yearHighTextView = (TextView) findViewById(R.id.yearHighTextView);
//		daysLowTextView = (TextView) findViewById(R.id.daysLowTextView);
//		daysHighTextView = (TextView) findViewById(R.id.daysHighTextView);
//		lastTradePriceOnlyTextView = (TextView) findViewById(R.id.lastTradePriceOnlyTextView);
//		changeTextView = (TextView) findViewById(R.id.changeTextView);
//		daysRangeTextView = (TextView) findViewById(R.id.daysRangeTextView);
//		
//		// Sends a message to the LogCat
//		Log.d(TAG, "Before URL Creation " + stockSymbol);
//		
//		// Create the YQL query
//	    yqlURL = yahooURLFirst + stockSymbol + yahooURLSecond;
//		
//		//final String yqlURL =	"https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
//		
//		//final String yqlURL =	"https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22stockSymbol%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
//
//		
//		// The Android UI toolkit is not thread safe and must always be 
//		// manipulated on the UI thread. This means if I want to perform
//		// any network operations like grabbing xml data, I have to do it
//		// in its own thread. The problem is that you can't write to the
//		// GUI from outside the main activity. AsyncTask solves those problems
//		
//		new MyAsyncTask().execute(yqlURL);
//
//	}
//	
//	// Use AsyncTask if you need to perform background tasks, but also need
//	// to change components on the GUI. Put the background operations in
//	// doInBackground. Put the GUI manipulation code in onPostExecute
//	
//	private class MyAsyncTask extends AsyncTask<String, String, String>{
//
//		// String... arg0 is the same as String[] args
//		protected String doInBackground(String... args) {
//			
//			DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
//			
//			HttpPost httpPost = new HttpPost(yqlURL);
//			
//			httpPost.setHeader("Content-type", "application/json");
//			
//			InputStream inputStream = null;
//			
//			String result = null;
//			
//			try{
//				
//				HttpResponse response = httpClient.execute(httpPost);
//				HttpEntity entity = response.getEntity();
//				inputStream = entity.getContent();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//				StringBuilder theStringBuilder = new StringBuilder();
//				
//				String line = null;
//				
//				while((line = reader.readLine()) != null){
//					theStringBuilder.append(line + "\n");
//					
//				}
//				
//				result = theStringBuilder.toString();
//			}
//			
//			catch(Exception e){
//				
//				e.printStackTrace();
//			}
//			
//			finally{
//				
//				try{
//					
//					if(inputStream != null) inputStream.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}finally{
//					
//				}
//			}
//			
//		
//			JSONObject jsonObject;
//			
//			try{
//				
//				result = result.substring(7);
//				result = result.substring(0, result.length()-2);
//				
//				jsonObject = new JSONObject(result);
//				JSONObject queryJSONObject = jsonObject.getJSONObject("query");
//				JSONObject resultsJSONObject = queryJSONObject.getJSONObject("results");
//				JSONObject quoteJSONObject = resultsJSONObject.getJSONObject("quote");
//				
//				String stockSymbol = quoteJSONObject.getString("symbol");
//				String stockDaysLow = quoteJSONObject.getString("DaysLow");
//				String stockDaysHigh = quoteJSONObject.getString("DaysHigh");
//				String stockChange = quoteJSONObject.getString("Change");
//				
//				
//			}
//			
//			catch(JSONException e){
//				
//				e.printStackTrace();
//			}
//			
//			return result;
//
////			try{
////				
////				Log.e(TAG, "1.1 try " );
////				
////				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
////				
////				factory.setNamespaceAware(true);
////				
////				XmlPullParser parser = factory.newPullParser();
////				
////				parser.setInput(new InputStreamReader(getUrlData(args[0])));
////				
////				beginDocument(parser, "query");
////				
////				int eventType = parser.getEventType();
////				
////				do{
////					
////					Log.e(TAG, "1.1 do " );
////					
////					nextElement(parser);
////					
////					
////					parser.next();
////				// CAUSES VALUES TO BE 0	Log.e(TAG, "0 nextElement " + parser.next());
////					
////					
////					eventType = parser.getEventType();
////					
////					if(eventType == XmlPullParser.TEXT){
////						
////						String valueFromXML = parser.getText();
////						
////						xmlPullParserArray[parserArrayIncrement++][1] = valueFromXML;
////						Log.e(TAG, "1 xmlPullParserArray " + valueFromXML);
////						
////						
////					}
////					
////				}while(eventType != XmlPullParser.END_DOCUMENT);
////				
////			}catch (ClientProtocolException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}catch (XmlPullParserException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (URISyntaxException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			
////			finally{}
////		
////			return null;
//		}
//		
//		
//
////		public final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException {
////			
////			int type;
////			
////			while((type=parser.next()) !=parser.START_TAG && type != parser.END_DOCUMENT)
////				
////				;
////			
////			if(type !=parser.START_TAG){
////				
////				 throw new XmlPullParserException("No Start Tag Found");
////			}
////			
////			if(!parser.getName().equals(firstElementName)){
////				
////				 throw new XmlPullParserException("UnExpected Start Tag Found" + parser.getName() + ", expected " + firstElementName);
////				
////			}
////			
////		}
//
////		public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException  {
////			
////			DefaultHttpClient client = new DefaultHttpClient();
////			
////			Log.e(TAG, "2 url " + url);
////			
////			HttpGet method = new HttpGet(new URI(url));
////			
////			HttpResponse res = client.execute(method);
////			
////		// CAUSES CRASH	Log.e(TAG, "3 res.getEntity().getContent() " + res.getEntity().getContent());
////			
////			return res.getEntity().getContent();
////		}
////		
////		public void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
////				
////				int type;
////				
////				while((type=parser.next()) !=parser.START_TAG && type != parser.END_DOCUMENT)
////					;
////					
////		}
//
//		// Changes the values for a bunch of TextViews on the GUI
//		protected void onPostExecute(String result){
//			
//			Log.e(TAG, "onPostExecute" );
//
//			
//			// Gain access so I can change the TextViews
//						TextView line1 = (TextView)findViewById(R.id.line1); 
//						TextView line2 = (TextView)findViewById(R.id.line2); 
//						TextView line3 = (TextView)findViewById(R.id.line3);
//						TextView line4 = (TextView)findViewById(R.id.line4);//new 
//						TextView line5 = (TextView)findViewById(R.id.line5);//new 
//			
////			companyNameTextView.setText(xmlPullParserArray[9][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[9][1]);
////
////			yearLowTextView.setText("Year Low: " + xmlPullParserArray[4][1]);
////
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[4][1]);
////			
////			yearHighTextView.setText("Year High: " + xmlPullParserArray[5][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[5][1]);
////			
////			daysLowTextView.setText("Days Low: " + xmlPullParserArray[2][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[2][1]);
////			
////			daysHighTextView.setText("Days High: " + xmlPullParserArray[3][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[3][1]);
////			
////			lastTradePriceOnlyTextView.setText("Last Price: " + xmlPullParserArray[7][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[7][1]);
////			
////			changeTextView.setText("Change: " + xmlPullParserArray[1][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[1][1]);
////			
////			daysRangeTextView.setText("Daily Price Range: " + xmlPullParserArray[8][1]);
////			
////			Log.e(TAG, "4 xmlPullParserArray[9][1] " + xmlPullParserArray[8][1]);
//		}
//		
//		
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.stock_info, menu);
//		return true;
//	}
//
//}
