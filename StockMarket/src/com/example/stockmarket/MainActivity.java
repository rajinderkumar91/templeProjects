package com.example.stockmarket;

import java.io.IOException;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;        
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView txtCompanyName, txtStockPrice;
	EditText edtStockSymbol;
	Button btnFetch;
	HttpClient client;
	JSONObject json;
	String symbol, URL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtCompanyName = (TextView)findViewById(R.id.txtCompanyName);
		txtStockPrice = (TextView)findViewById(R.id.txtStockPrice);
		edtStockSymbol = (EditText)findViewById(R.id.edtStockSymbol);
		btnFetch = (Button)findViewById(R.id.btnFetch);

		client = new DefaultHttpClient();
		
				
		btnFetch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				symbol = edtStockSymbol.getText().toString();
				URL = "http://finance.yahoo.com/webservice/v1/symbols/" + symbol + "/quote?format=json";
				getDataFromJsonObject information = new getDataFromJsonObject();
				information.execute(URL);		
			} 
		});	
		
		//for(int i=0; i<10 ; i++)
			
		    updateStock(symbol);	
	}	
		
	private void updateStock(String symbol){		
		try {
			Thread.sleep(15000);			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		System.out.print("not good");
			e.printStackTrace();
		}
		
		URL = "http://finance.yahoo.com/webservice/v1/symbols/" + symbol + "/quote?format=json";
		getDataFromJsonObject information2 = new getDataFromJsonObject();
		information2.execute(URL);		
	}
    

	private class getDataFromJsonObject extends AsyncTask<String, Void, String>{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			setValues(result);
		}

		@Override
		protected String doInBackground(String...url) {			
			// TODO Auto-generated method stub
			String data = "";
			try {
				HttpGet httpGet = new HttpGet(url[0]);
				HttpResponse response = client.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				
				if(statusCode == 200) {
					HttpEntity e = response.getEntity();
					data = EntityUtils.toString(e);
						
				}
				else{
					Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();				
				}			
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}
	}
	
	private void setValues(String result){
		try {
			JSONObject jObject = new JSONObject(result);
			
			JSONObject list = jObject.getJSONObject("list");
			JSONArray resources = list.getJSONArray("resources");
			JSONObject resource = resources.getJSONObject(0);
				JSONObject r = resource.getJSONObject("resource");
				JSONObject fields = r.getJSONObject("fields");
				String name = fields.getString("name");
				String price = fields.getString("price");
				txtCompanyName.setText(name);
				txtStockPrice.setText(price);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
