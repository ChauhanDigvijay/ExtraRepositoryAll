package com.identity.arx.httpasynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.identity.arx.httpconnection.HttpUrlConnection;
import com.identity.arx.progressbar.MyCustomProgressDialog;

import org.springframework.http.ResponseEntity;

public class HttpAsyncTask extends AsyncTask<ResponseEntity<?>, Void, ResponseEntity<?>> {
    AsyncResponse asyncResponse;
    Context mContext;
    Object obj;
    MyCustomProgressDialog progressDialog;
    String url;

    public HttpAsyncTask(Context mContext, String url, Object obj, AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
        this.mContext = mContext;
        this.url = url;
        this.obj = obj;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.mContext != null) {
            this.progressDialog = new MyCustomProgressDialog(this.mContext);
            this.progressDialog.setMessage("Please Wait...Process is  ");
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setProgress(0);
            this.progressDialog.show();
        }
    }

    protected ResponseEntity<?> doInBackground(ResponseEntity<?>... responseEntityArr) {
        return HttpUrlConnection.setHttpUrlConnection(this.obj, this.url);
    }

    protected void onPostExecute(ResponseEntity<?> response) {
        super.onPostExecute(response);
        if (response != null) {
            this.asyncResponse.asyncResponse(response);
        }
        if (this.mContext != null) {
            this.progressDialog.dismiss();
        }
    }
}
