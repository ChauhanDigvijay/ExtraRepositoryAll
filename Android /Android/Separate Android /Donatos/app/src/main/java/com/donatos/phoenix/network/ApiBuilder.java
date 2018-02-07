package com.donatos.phoenix.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.p134b.C2509m;
import com.p123c.p124a.p125a.C2439a;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import p078c.C1784t;
import p078c.C1784t.C1820a;
import p078c.C1900b;
import p078c.C1908c;
import p078c.C1942w.C1941a;
import p078c.ab;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiBuilder<S> {
    private static final int CACHE_SIZE_BYTES = 2097152;
    private static final int TIMEOUT_SECONDS = 30;
    private final Class<S> mApi;
    private final C1941a mOkHttpClientBuilder;
    private final Builder mRetrofitBuilder;
    private final int mTimeout;

    public static class DeviceTypeInterceptor extends AbstractOkHttpInterceptor {
        private Context mContext;

        public DeviceTypeInterceptor(Context context) {
            this.mContext = context;
        }

        private boolean isConnected() {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        public ab intercept(C1820a c1820a) throws IOException {
            if (isConnected()) {
                return c1820a.mo1189a(c1820a.mo1190a().m5973a().m5972b("X-Device-Type", "Android").m5972b("App-Version", "2.0.3").m5970a());
            }
            C2508l.m7347a().m7349a(new NotConnectedEvent());
            throw new IOException();
        }
    }

    public static class JWTInterceptor extends AbstractOkHttpInterceptor {
        private Context mContext;
        private C2509m mSharedPreferencesUtil;

        public JWTInterceptor(C2509m c2509m, Context context) {
            this.mSharedPreferencesUtil = c2509m;
            this.mContext = context;
        }

        private boolean isConnected() {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        public ab intercept(C1820a c1820a) throws IOException {
            if (isConnected()) {
                return this.mSharedPreferencesUtil.m7358a() ? c1820a.mo1189a(c1820a.mo1190a().m5973a().m5972b("X-Auth-Token", this.mSharedPreferencesUtil.m7350a("auth_token")).m5972b("X-Device-Token", "Android").m5972b("App-Version", "2.0.3").m5970a()) : c1820a.mo1189a(c1820a.mo1190a().m5973a().m5972b("X-Device-Token", "Android").m5972b("App-Version", "2.0.3").m5970a());
            } else {
                C2508l.m7347a().m7349a(new NotConnectedEvent());
                throw new IOException();
            }
        }
    }

    public ApiBuilder(Class<S> cls, int i, Context context, String str) {
        this.mApi = cls;
        this.mOkHttpClientBuilder = createOkHttpClientBuilder(context);
        this.mTimeout = i;
        this.mRetrofitBuilder = createRetrofitBuilder(str);
    }

    public ApiBuilder(Class<S> cls, Context context, String str) {
        this(cls, 30, context, str);
    }

    private static C1941a createOkHttpClientBuilder(Context context) {
        C1941a c1941a = new C1941a();
        c1941a.m5947a(new C1908c(context.getCacheDir()));
        return c1941a;
    }

    private static Builder createRetrofitBuilder(String str) {
        return new Builder().baseUrl(str).addConverterFactory(C2439a.m7164a()).addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public ApiBuilder<S> addInterceptor(C1784t c1784t) {
        this.mOkHttpClientBuilder.m5948a(c1784t);
        return this;
    }

    public S build() {
        return this.mRetrofitBuilder.client(this.mOkHttpClientBuilder.m5945a((long) this.mTimeout, TimeUnit.SECONDS).m5950b((long) this.mTimeout, TimeUnit.SECONDS).m5949a()).build().create(this.mApi);
    }

    public ApiBuilder<S> withAuthenticator(C1900b c1900b) {
        this.mOkHttpClientBuilder.m5946a(c1900b);
        return this;
    }
}
