package com.womensafety.httpimage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpImageManager {
    private static final boolean DEBUG = false;
    public static final int DECODING_MAX_PIXELS_DEFAULT = 2880000;
    public static final int DEFAULT_CACHE_SIZE = 64;
    private static final String TAG = "HttpImageManager";
    public static final int UNCONSTRAINED = -1;
    private Set<LoadRequest> mActiveRequests;
    private BitmapCache mCache;
    private ThreadPoolExecutor mExecutor;
    private BitmapFilter mFilter;
    private Handler mHandler;
    private int mMaxNumOfPixelsConstraint;
    private BitmapCache mPersistence;

    public interface BitmapFilter {
        Bitmap filter(Bitmap bitmap);
    }

    private static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    if (read() < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public static class LoadRequest {
        private String mHashedUri;
        private ImageView mImageView;
        private OnLoadResponseListener mListener;
        private Uri mUri;
        private String path;

        public LoadRequest(Uri uri) {
            this(uri, null, null, null);
        }

        public LoadRequest(Uri uri, ImageView v, String path) {
            this(uri, v, null, path);
        }

        public LoadRequest(Uri uri, OnLoadResponseListener l, String path) {
            this(uri, null, l, path);
        }

        public LoadRequest(Uri uri, ImageView v, OnLoadResponseListener l, String path) {
            if (uri == null) {
                throw new NullPointerException("uri must not be null");
            }
            this.mUri = Uri.parse(String.valueOf(path.hashCode()));
            this.mHashedUri = this.mUri.toString();
            this.mImageView = v;
            this.mListener = l;
            this.path = path;
        }

        public ImageView getImageView() {
            return this.mImageView;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public String getHashedUri() {
            return this.mHashedUri;
        }

        public int hashCode() {
            return this.mUri.hashCode();
        }

        public boolean equals(Object b) {
            if (b instanceof LoadRequest) {
                return this.mUri.equals(((LoadRequest) b).getUri());
            }
            return false;
        }
    }

    public interface OnLoadResponseListener {
        void onLoadError(LoadRequest loadRequest, Throwable th);

        void onLoadProgress(LoadRequest loadRequest, long j, long j2);

        void onLoadResponse(LoadRequest loadRequest, Bitmap bitmap);
    }

    public HttpImageManager(BitmapCache cache, BitmapCache persistence) {
        this.mMaxNumOfPixelsConstraint = DECODING_MAX_PIXELS_DEFAULT;
        this.mHandler = new Handler();
     //   this.mExecutor = new ThreadPoolExecutor(7, 10, 10, TimeUnit.SECONDS, new LinkedBlockingStack());
        this.mActiveRequests = new HashSet();
        this.mCache = cache;
        this.mPersistence = persistence;
        if (this.mPersistence == null) {
            throw new IllegalArgumentException(" persistence layer should be specified");
        }
    }

    public HttpImageManager(BitmapCache persistence) {
        this(null, persistence);
    }

    public void setDecodingPixelConstraint(int max) {
        this.mMaxNumOfPixelsConstraint = max;
    }

    public int getDecodingPixelConstraint() {
        return this.mMaxNumOfPixelsConstraint;
    }

    public void setBitmapFilter(BitmapFilter filter) {
        this.mFilter = filter;
    }

    public static BitmapCache createDefaultMemoryCache() {
        return new BasicBitmapCache(64);
    }

    public Bitmap loadImage(Uri uri) {
        return loadImage(new LoadRequest(uri));
    }

    public Bitmap loadImage(LoadRequest r) {
        if (r == null || r.getUri() == null || TextUtils.isEmpty(r.getUri().toString())) {
            throw new IllegalArgumentException("null or empty request");
        }
        ImageView iv = r.getImageView();
        if (iv != null) {
            synchronized (iv) {
                iv.setTag(r.getUri());
            }
        }
        String key = r.getHashedUri();
        if (this.mCache != null && this.mCache.exists(key)) {
            return this.mCache.loadData(key);
        }
        this.mExecutor.execute(newRequestCall(r));
        return null;
    }

    private Runnable newRequestCall(final LoadRequest request) {
        return new Runnable() {
            public void run() {
                ImageView iv;
                Throwable th;
                if (request.getImageView() != null) {
                    iv = request.getImageView();
                    synchronized (iv) {
                        if (iv.getTag() != request.getUri()) {
                            return;
                        }
                    }
                }
                synchronized (HttpImageManager.this.mActiveRequests) {
                    while (HttpImageManager.this.mActiveRequests.contains(request)) {
                        try {
                            HttpImageManager.this.mActiveRequests.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    HttpImageManager.this.mActiveRequests.add(request);
                }
                Bitmap data = null;
                String key = request.getHashedUri();
                try {
                    if (HttpImageManager.this.mCache != null) {
                        data = HttpImageManager.this.mCache.loadData(key);
                    }
                    if (data == null) {
                        data = HttpImageManager.this.mPersistence.loadData(key);
                        if (data != null) {
                            if (HttpImageManager.this.mCache != null) {
                                HttpImageManager.this.mCache.storeData(key, data);
                            }
                            HttpImageManager.this.fireLoadProgress(request, 1, 1);
                        } else {
                            InputStream responseStream;
                            long millis = System.currentTimeMillis();
                            byte[] binary = null;
                            InputStream responseStream2 = null;
                            try {
                                responseStream = request.getImageView().getContext().getAssets().open(request.path);
                            } catch (Exception e2) {
                                File file = new File(request.path);
                                if (responseStream2 == null && file.exists()) {
                                    responseStream2 = new FileInputStream(file);
                                }
                                if (responseStream2 == null) {
                                    responseStream = new DefaultHttpClient().execute(new HttpGet(request.path)).getEntity().getContent();
                                } else {
                                    responseStream = responseStream2;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (responseStream2 != null) {
                                    try {
                                        responseStream2.close();
                                    } catch (IOException e3) {
                                    }
                                }
                                throw th;
                            }
                            if (responseStream != null) {
                                try {
                                    responseStream2 = new FlushedInputStream(responseStream);
                                    long contentSize = (long) responseStream2.available();
                                    binary = HttpImageManager.this.readInputStreamProgressively(responseStream2, (int) contentSize, request);
                                    data = BitmapUtil.decodeByteArray(binary, HttpImageManager.this.mMaxNumOfPixelsConstraint);
                                } catch (Throwable th3) {
                                    th = th3;
                                    responseStream2 = responseStream;
                                    if (responseStream2 != null) {
                                        responseStream2.close();
                                    }
                                    throw th;
                                }
                            } else {
                                responseStream2 = responseStream;
                            }
                            if (responseStream2 != null) {
                                try {
                                    responseStream2.close();
                                } catch (IOException e4) {
                                }
                            }
                            if (data == null) {
                                throw new RuntimeException("data from remote can't be decoded to bitmap");
                            }
                            if (HttpImageManager.this.mFilter != null) {
                                try {
                                    Bitmap newData = HttpImageManager.this.mFilter.filter(data);
                                    if (newData != null) {
                                        data = newData;
                                    }
                                } catch (Throwable th4) {
                                }
                            }
                            if (HttpImageManager.this.mCache != null) {
                                HttpImageManager.this.mCache.storeData(key, data);
                            }
                            HttpImageManager.this.mPersistence.storeData(key, binary);
                        }
                    }
                    if (!(data == null || request.getImageView() == null)) {
                        final Bitmap finalData = data;
                        iv = request.getImageView();
                        synchronized (iv) {
                            if (iv.getTag() == request.getUri()) {
                                final ImageView imageView = iv;
                                HttpImageManager.this.mHandler.post(new Runnable() {
                                    public void run() {
                                        if (imageView.getTag() == request.getUri()) {
                                            imageView.setImageBitmap(finalData);
                                        }
                                    }
                                });
                            }
                        }
                    }
                    HttpImageManager.this.fireLoadResponse(request, data);
                    synchronized (HttpImageManager.this.mActiveRequests) {
                        HttpImageManager.this.mActiveRequests.remove(request);
                        HttpImageManager.this.mActiveRequests.notifyAll();
                    }
                } catch (Throwable th5) {
                    synchronized (HttpImageManager.this.mActiveRequests) {
                        HttpImageManager.this.mActiveRequests.remove(request);
                        HttpImageManager.this.mActiveRequests.notifyAll();
                    }
                }
            }
        };
    }

    public void emptyCache() {
        if (this.mCache != null) {
            this.mCache.clear();
        }
    }

    public void emptyPersistence() {
        if (this.mPersistence != null) {
            this.mPersistence.clear();
        }
    }

    private byte[] readInputStreamProgressively(InputStream is, int totalSize, LoadRequest r) throws IOException {
        fireLoadProgress(r, 3, 1);
        int readed;
        if (totalSize <= 0 || r.mListener == null) {
            byte[] buf = new byte[1024];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long count = 0;
            while (true) {
                readed = is.read(buf);
                if (readed == -1) {
                    break;
                }
                output.write(buf, 0, readed);
                count += (long) readed;
            }
            fireLoadProgress(r, count, count);
            if (count <= 2147483647L) {
                return output.toByteArray();
            }
            throw new IOException("content too large: " + (count / 1048576) + " M");
        }
        byte[] bArr = new byte[totalSize];
        int offset = 0;
        while (offset < totalSize) {
            readed = is.read(bArr, offset, totalSize - offset);
            if (readed == -1) {
                break;
            }
            offset += readed;
            LoadRequest loadRequest = r;
            fireLoadProgress(loadRequest, (long) totalSize, (long) ((totalSize + offset) >> 1));
        }
        if (offset == totalSize) {
            return bArr;
        }
        throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + totalSize);
    }

    private void fireLoadResponse(LoadRequest r, Bitmap image) {
        if (r.mListener != null) {
            try {
                r.mListener.onLoadResponse(r, image);
            } catch (Throwable th) {
            }
        }
    }

    private void fireLoadProgress(LoadRequest r, long totalContentSize, long loadedContentSize) {
        if (r.mListener != null) {
            try {
                r.mListener.onLoadProgress(r, totalContentSize, loadedContentSize);
            } catch (Throwable th) {
            }
        }
    }

    private void fireLoadFailure(LoadRequest r, Throwable e) {
        if (r.mListener != null) {
            try {
                r.mListener.onLoadError(r, e);
            } catch (Throwable th) {
            }
        }
    }
}
