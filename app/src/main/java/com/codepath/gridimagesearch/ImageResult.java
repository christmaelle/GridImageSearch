package com.codepath.gridimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResult {
    public  String fullUrl;
    public String thumbUrl;
    public String title;

    //new ImageResult(..ran itemjson..)
    public ImageResult (JSONObject json){
        try{
            this.fullUrl=json.getString("url");
            this.thumbUrl=json.getString("tbUrl");
            this.title=json.getString("title");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    //Take an Array of json images and return arraylist of image results
    //ImageResult.fromJSONArray([...,...])
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
        ArrayList<ImageResult> results=new ArrayList<ImageResult>();
        for (int i=0;i<array.length();i++){
            try{
                results.add(new ImageResult(array.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
