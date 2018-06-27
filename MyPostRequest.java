package de.dfb.fanclub.fragments.quiz;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyPostRequest extends AsyncTask<String, String, String>
{
    private Map<String, String> post   = new LinkedHashMap<>();
    private Map<String, String> header = new LinkedHashMap<>();
    HttpURLConnection urlConnectionFromUrl;

    @Override
    protected String doInBackground(String... params)
    {
        String urlString = params[0]; // URL to call

        try
        {
            urlConnectionFromUrl = getUrlConnectionFromUrl(urlString, post, header);
            if (urlConnectionFromUrl != null)
            {

                urlConnectionFromUrl.connect();
                int responseCode = urlConnectionFromUrl.getResponseCode();
                if (responseCode == 200)
                {
                    return "success";
                }
            }
            return "error!";
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            Log.v("Error QuizPost: ", ioe.getMessage());
        }
        finally
        {
            urlConnectionFromUrl.disconnect();
        }
        return "success";
    }

    public HttpURLConnection getUrlConnectionFromUrl(String url, Map<String, String> post, Map<String, String> header)
    {
        HttpURLConnection urlConnection = null;
        try
        {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setConnectTimeout(10000);

            urlConnection.setRequestMethod("POST");

            header.put("Content-Type", "application/json");
            post.put("user_name", "Zach");
            post.put("user_id", "599288");
            post.put("quiz_id", "103");
            post.put("score", "135");
            post.put("hash", "8e2dd9022917e78061eefe33a070178f");

            OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(post));
            writer.flush();
            writer.close();
            os.close();

            return urlConnection;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            urlConnection.disconnect();
        }
        return null;
    }

    private String getQuery(Map<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext())
        {
            String key = iterator.next();

            if (first)
            {
                first = false;
            }
            else
            {
                result.append("&");
            }

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(key), "UTF-8"));
        }
        return result.toString();
    }

}
