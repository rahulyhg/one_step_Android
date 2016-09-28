package com.os.onestep.utilities.httpconnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class NetworkUtility {

    private static final int CONNECTION_TIME_OUT = 180000;

    public String postHttp(String url, String params) {
        String response = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIME_OUT);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            StringEntity entity = new StringEntity(params);
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setParams(httpParams);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == 201) {
                response = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (null != response && !"".equals(response)) {
            response = response.replace("null", "-").replace("-string", "-");
        }
        return response;
    }

    public String postHttpData(String url, String params) {
        String response = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIME_OUT);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            StringEntity entity = new StringEntity(params);
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setParams(httpParams);
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == 201) {
                response = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}
