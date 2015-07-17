package com.codepath.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.gridimagesearch.ImageResult;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class SearchActivity extends Activity {
    private EditText etQuery;
    private GridView gvResult;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupView();
        //Creates the Data source
        imageResults=new ArrayList<ImageResult>();
        //Attaches the Data to an adapter
        aImageResults=new ImageResultsAdapter(this,imageResults);
        //Link the adapter to the adapterview(gridview)
        gvResult.setAdapter(aImageResults);
    }

    private void setupView() {
        etQuery=(EditText) findViewById(R.id.etQuery);
        gvResult=(GridView) findViewById(R.id.gvResult);
        gvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the image display activity
                //Creating on intent
                Intent i=new Intent(SearchActivity.this,ImageDisplayActivity.class);
                //Get the image result to display
                ImageResult result=imageResults.get(position);
                //Pass image result into the intent
                i.putExtra("url",result.fullUrl);
                //Launch the new activity
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public void onImageSearch(View v){
        String Query=etQuery.getText().toString();
        AsyncHttpClient client=new AsyncHttpClient();
        String SearchUrl="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+Query+"&rsz=8";
        client.get(SearchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response) {
                Log.d("DEBUG",response.toString());
                JSONArray imageResultsJson=null;
                try{
                    imageResultsJson=response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();//Clear the existing images from the array (in cases where its a new search)
                    //When you make to the adapter, it does modify the underlying data
                    imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.i("INFO",imageResults.toString());
            }
            });
        }
        //Toast.makeText(this, "Search for: "+Query, Toast.LENGTH_SHORT).show();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
