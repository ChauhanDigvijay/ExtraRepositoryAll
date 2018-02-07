package com.womensafety.httpimage;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.search.SearchAuth.StatusCodes;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class NetworkResourceLoader {
    public static final boolean DEBUG = true;
    public static final String TAG = "NetworkResourceLoader";
    private HttpClient mHttpClient = createHttpClient();

    public HttpResponse load(Uri uri) throws IOException {
        Log.e(TAG, "Requesting: " + uri);
        HttpGet httpGet = new HttpGet(uri.toString());
        httpGet.addHeader("Accept-Encoding", "gzip");
        return this.mHttpClient.execute(httpGet);
    }

    public final DefaultHttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, StatusCodes.AUTH_DISABLED);
        HttpConnectionParams.setSoTimeout(params, 20000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        ConnManagerParams.setTimeout(params, 5000);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(50));
        ConnManagerParams.setMaxTotalConnections(params, 200);
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, supportedSchemes), params);
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        return httpClient;
    }
}
