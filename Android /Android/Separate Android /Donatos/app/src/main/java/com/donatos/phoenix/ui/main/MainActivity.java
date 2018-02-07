package com.donatos.phoenix.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.p014b.C0293o.C0292a;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bumptech.glide.C2170e;
import com.donatos.phoenix.DonatosApplication;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.Cart;
import com.donatos.phoenix.network.common.LoadShoppingCartEvent;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.network.common.MenuItemResponse;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.dashboard.DashboardInteractor;
import com.donatos.phoenix.network.locations.ConvertOrderResponse;
import com.donatos.phoenix.network.locations.LocationsInteractor;
import com.donatos.phoenix.network.pushnotification.PushNotification;
import com.donatos.phoenix.network.pushnotification.PushNotificationReceivedEvent;
import com.donatos.phoenix.network.pushnotification.PushNotificationSubscriptionService;
import com.donatos.phoenix.network.pushnotification.PushNotificationTokenUpdateEvent;
import com.donatos.phoenix.network.token.TokenInteractor;

import com.donatos.phoenix.ui.login.LoginActivity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import retrofit2.HttpException;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends C2728a implements aa {
    C2750s f8914o;
    C2747q f8915p;
    TokenInteractor f8916q;
    DashboardInteractor f8917r;
    C2509m f8918s;
    C1167n f8919t;
    LocationsInteractor f8920u;
    PushNotificationSubscriptionService f8921v;
    private AppBarLayout f8906A;
    private List<C1172b> f8907B = new ArrayList();
    private boolean f8908C = false;
    private boolean f8909D = false;
    private OnClickListener f8910E = new C28481(this);
    private OnClickListener f8911F = new C28492(this);
    private OnClickListener f8912G = C2858h.m8417a(this);
    private C0292a f8913H = new C28503(this);
    private Toolbar f8922w;
    private DrawerLayout f8923x;
    private C2754v f8924y;
    private FrameLayout f8925z;

    static /* synthetic */ void m8398a(MainActivity mainActivity, Cart cart, Location location) {
        mainActivity.f8915p.m8186b();
        C2508l.m7347a().m7349a(new C2853c());
        C2508l.m7347a().m7349a(new C2629h(cart, location));
    }

    static /* synthetic */ void m8399a(MainActivity mainActivity, Cart cart, Location location, ConvertOrderResponse convertOrderResponse) throws Exception {
        if (convertOrderResponse.getContent().getMessages().isEmpty()) {
            mainActivity.f8918s.m7353a(location);
            cart.getOrder().setItems(convertOrderResponse.getContent().getOrder().getItems());
            cart.setPrice(convertOrderResponse.getContent().getPrice());
            cart.setLocationId(location.getId());
            if (!(cart.getOrder().getDeliveryAddress() == null || cart.getOrder().getDeliveryAddress().getStoreNumber() == null || cart.getOrder().getDeliveryAddress().getStoreNumber().equals(location.getId()))) {
                cart.getOrder().setDeliveryAddress(null);
            }
            mainActivity.f8918s.m7351a(cart);
            mainActivity.f8915p.m8180a(mainActivity.f8918s.m7363e());
            C2508l.m7347a().m7349a(new C2630i(cart, location));
        } else {
            Object[] objArr = new Object[1];
            objArr[0] = convertOrderResponse.getContent().getMessages() != null ? TextUtils.join(" ", convertOrderResponse.getContent().getMessages()) : Integer.valueOf(R.string.error.app.generic);
            C2742m.m8171a((Context) mainActivity, mainActivity.getString(R.string.error.locationchange.cart.message, objArr), (int) R.string.error.locationchange.cart.title, (int) R.string.error.locationchange.cart.cancel, C2861k.m8420a(mainActivity, cart, location), (int) R.string.error.locationchange.cart.ok, C2862l.m8421a(mainActivity, location, cart, convertOrderResponse));
        }
        mainActivity.f8915p.m8181a(mainActivity.f8912G);
    }

    static /* synthetic */ void m8400a(MainActivity mainActivity, Location location, Cart cart, ConvertOrderResponse convertOrderResponse) {
        mainActivity.f8918s.m7353a(location);
        cart.getOrder().setItems(convertOrderResponse.getContent().getOrder().getItems());
        cart.setPrice(convertOrderResponse.getContent().getPrice());
        cart.setLocationId(location.getId());
        if (!(cart.getOrder().getDeliveryAddress() == null || cart.getOrder().getDeliveryAddress().getStoreNumber() == null || cart.getOrder().getDeliveryAddress().getStoreNumber().equals(location.getId()))) {
            cart.getOrder().setDeliveryAddress(null);
        }
        mainActivity.f8918s.m7351a(cart);
        mainActivity.f8915p.m8180a(mainActivity.f8918s.m7363e());
        C2508l.m7347a().m7349a(new C2630i(cart, location));
    }

    static /* synthetic */ void m8401a(MainActivity mainActivity, Location location, Cart cart, Throwable th) throws Exception {
        C2508l.m7347a().m7349a(new C2853c());
        if (th instanceof HttpException) {
            try {
                ConvertOrderResponse convertOrderResponse = (ConvertOrderResponse) C2504h.m7341a(((HttpException) th).response().errorBody().string(), ConvertOrderResponse.class);
                Object[] objArr = new Object[1];
                String valueOf = (convertOrderResponse == null || convertOrderResponse.getContent() == null || convertOrderResponse.getContent().getMessages() == null) ? Integer.valueOf(R.string.error.app.generic) : TextUtils.join(" ", convertOrderResponse.getContent().getMessages());
                objArr[0] = valueOf;
                C2742m.m8171a((Context) mainActivity, mainActivity.getString(R.string.error.locationchange.cart.message, objArr), (int) R.string.error.locationchange.cart.title, (int) R.string.error.locationchange.cart.cancel, C2859i.m8418a(mainActivity), (int) R.string.error.locationchange.cart.ok, C2860j.m8419a(mainActivity, location, convertOrderResponse, cart));
            } catch (Throwable e) {
                C2495b.m7330a(e);
            }
        } else {
            C2495b.m7330a(th);
        }
        mainActivity.f8915p.m8181a(mainActivity.f8912G);
    }

    static /* synthetic */ void m8402a(MainActivity mainActivity, Location location, ConvertOrderResponse convertOrderResponse, Cart cart) {
        int i = 1;
        mainActivity.f8918s.m7353a(location);
        if (convertOrderResponse != null) {
            int i2 = convertOrderResponse.getContent() != null ? 1 : 0;
            if (convertOrderResponse.getContent().getOrder() == null) {
                i = 0;
            }
            if (!((i2 & i) == 0 || convertOrderResponse.getContent().getOrder().getItems() == null)) {
                cart.getOrder().setItems(convertOrderResponse.getContent().getOrder().getItems());
                cart.setPrice(convertOrderResponse.getContent().getPrice());
                cart.setLocationId(location.getId());
                mainActivity.f8918s.m7351a(cart);
                mainActivity.f8915p.m8180a(mainActivity.f8918s.m7363e());
                C2508l.m7347a().m7349a(new C2630i(cart, location));
            }
        }
        mainActivity.f8918s.m7356a("cart", null);
        mainActivity.f8915p.m8180a(mainActivity.f8918s.m7363e());
        C2508l.m7347a().m7349a(new C2630i(cart, location));
    }

    static /* synthetic */ void m8403a(MainActivity mainActivity, PushNotification pushNotification) {
        if (mainActivity.f8924y.f8615j) {
            mainActivity.m8414a(pushNotification);
            return;
        }
        mainActivity.f8918s.m7354a(pushNotification);
        aa.m8117a(mainActivity.findViewById(R.id.activity_main_fragment_container), R.string.notification.dialog.offer.will.be.added);
    }

    static /* synthetic */ void m8404a(MainActivity mainActivity, PushNotification pushNotification, MenuItemResponse menuItemResponse) throws Exception {
        C2509m c2509m = mainActivity.f8918s;
        String id = pushNotification.getId();
        try {
            List<PushNotification> d = c2509m.m7362d();
            if (d != null) {
                int indexOf;
                for (PushNotification pushNotification2 : d) {
                    if (pushNotification2.getId().equals(id)) {
                        indexOf = d.indexOf(pushNotification2);
                        break;
                    }
                }
                indexOf = -1;
                if (indexOf >= 0) {
                    d.remove(indexOf);
                }
                c2509m.m7356a("push_notifications", C2504h.m7342a(d));
            }
        } catch (Throwable e) {
            C2495b.m7330a(e);
        }
        mainActivity.f8914o.m8196b(C2785a.m8273d(menuItemResponse.getContent().getId().intValue()), C2749a.ENTER_LEFT_EXIT_RIGHT, C2749a.ENTER_RIGHT_EXIT_LEFT, C2785a.f8702f);
    }

    static /* synthetic */ void m8405a(MainActivity mainActivity, C3932a c3932a) {
        IOException e;
        if (c3932a.m12954a().size() == 0) {
            C2742m.m8172a(mainActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2863m.m8422a());
            return;
        }
        PushNotification pushNotification;
        try {
            pushNotification = (PushNotification) LoganSquare.parse((String) c3932a.m12954a().get("payload"), PushNotification.class);
            try {
                pushNotification.setId(c3932a.m12955b());
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                C2742m.m8173a((Context) mainActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2864n.m8423a(), C2865o.m8424a(mainActivity, pushNotification));
            }
        } catch (IOException e3) {
            IOException iOException = e3;
            pushNotification = null;
            e = iOException;
            e.printStackTrace();
            C2742m.m8173a((Context) mainActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2864n.m8423a(), C2865o.m8424a(mainActivity, pushNotification));
        }
        C2742m.m8173a((Context) mainActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2864n.m8423a(), C2865o.m8424a(mainActivity, pushNotification));
    }

    static /* synthetic */ void m8406a(MainActivity mainActivity, Object obj) throws Exception {
        if (obj instanceof C2855e) {
            C2509m c2509m = mainActivity.f8918s;
            c2509m.m7356a("cart", null);
            c2509m.m7356a("orders", null);
            c2509m.m7356a("selected_location", null);
            c2509m.m7356a("auth_token", null);
            c2509m.m7356a("guest", null);
            c2509m.m7356a("push_notifications", null);
            C1186b c = mainActivity.f8919t.m4653c();
            Object c2871u = new C2871u(mainActivity);
            C1253b.m4784a(c2871u, "onComplete is null");
            c.mo1054a(new C1270i(c2871u));
        }
        if (obj instanceof C2856f) {
            mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
            mainActivity.finish();
        } else if (obj instanceof LoadShoppingCartEvent) {
            mainActivity.m8413g();
        } else if (obj instanceof C2854d) {
            Location location = ((C2854d) obj).f8930a;
            C2508l.m7347a().m7349a(new ab());
            Cart c2 = mainActivity.f8918s.m7361c();
            if (!(c2 == null || c2.getLocationId() != null || mainActivity.f8918s.m7359b() == null)) {
                c2.setLocationId(mainActivity.f8918s.m7359b().getId());
            }
            if (c2.getOrder().getItems().size() <= 0 || location.getId().equals(c2.getLocationId())) {
                mainActivity.f8918s.m7353a(location);
                C2508l.m7347a().m7349a(new C2630i(c2, location));
                return;
            }
            mainActivity.f8915p.m8181a(C2872v.m8429a());
            mainActivity.f8918s.m7351a(c2);
            mainActivity.f8907B.add(mainActivity.f8920u.convertOrderTo(c2.getLocationId(), location.getId(), c2.getOrder()).subscribe(new C2873w(mainActivity, c2, location), new C2874x(mainActivity, location, c2)));
        } else if (obj instanceof ab) {
            mainActivity.f8925z.setVisibility(0);
            mainActivity.f8925z.setOnClickListener(C2866p.m8425a());
        } else if (obj instanceof C2853c) {
            mainActivity.f8925z.setVisibility(8);
        } else if (obj instanceof C2852b) {
            mainActivity.f8909D = ((C2852b) obj).f8929a;
        } else if (obj instanceof PushNotificationTokenUpdateEvent) {
            if (mainActivity.f8918s.m7358a()) {
                mainActivity.f8921v.addOrUpdateActiveUserSubscription(((PushNotificationTokenUpdateEvent) obj).getToken());
            }
        } else if (obj instanceof PushNotificationReceivedEvent) {
            mainActivity.runOnUiThread(C2868r.m8426a(mainActivity, ((PushNotificationReceivedEvent) obj).getMessage()));
        }
    }

    static /* synthetic */ void m8407b(MainActivity mainActivity) throws Exception {
        mainActivity.f8917r.cleanUp();
        C2170e.m6592a((Context) mainActivity).m6600a();
        mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
        mainActivity.finish();
    }

    private void m8413g() {
        this.f8914o.m8196b(C2702p.m8053a(), C2749a.ENTER_LEFT_EXIT_RIGHT, C2749a.ENTER_RIGHT_EXIT_LEFT, C2702p.f8430i);
    }

    public final void m8414a(PushNotification pushNotification) {
        if (pushNotification.getMenuItem() != null) {
            this.f8920u.getLocationMenuItemByExternalId(this.f8918s.m7359b().getId(), pushNotification.getMenuItem()).subscribe(new C2869s(this, pushNotification), C2870t.m8427a());
        }
        if (pushNotification.getCoupon() != null) {
            Cart c = this.f8918s.m7361c();
            if (c != null && c.getOrder() != null) {
                Order order = c.getOrder();
                if (!C2500d.m7332a(order.getCoupons(), pushNotification.getCoupon())) {
                    order.addCouponsItem(pushNotification.getCoupon());
                    this.f8918s.m7351a(c);
                    Log.i("Coupons", "Size: " + order.getCoupons().size());
                    aa.m8117a(findViewById(R.id.activity_main_fragment_container), R.string.dashboard.dealadded.message);
                }
            }
        }
    }

    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    public final View mo1599d() {
        return findViewById(R.id.activity_main_fragment_container);
    }

    public final C2754v mo1614f() {
        return this.f8924y;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        C2508l.m7347a().m7349a(new C2851a(i, i2, intent));
    }

    public void onBackPressed() {
        if (this.f8923x.m2469c()) {
            this.f8923x.m2467b();
        } else if (this.f8914o.f8603a.mo258a(C2540n.f7843i) != null && this.f8914o.f8603a.mo258a(C2540n.f7843i).m1000p() && !this.f8908C) {
            this.f8908C = true;
            Toast.makeText(this, R.string.navigation.leaveapp.toast, 0).show();
        } else if (this.f8914o.f8603a.mo258a(C2892m.f9014e) != null && this.f8914o.f8603a.mo258a(C2892m.f9014e).m1000p()) {
            this.f8914o.m8194a(new C2540n(), C2749a.CROSSFADE, C2749a.CROSSFADE, C2540n.f7843i);
        } else if (!this.f8909D) {
            this.f8908C = false;
            super.onBackPressed();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        this.f8922w = (Toolbar) findViewById(R.id.app_toolbar);
        this.f8906A = (AppBarLayout) findViewById(R.id.app_bar);
        this.f8923x = (DrawerLayout) findViewById(R.id.drawer);
        this.f8925z = (FrameLayout) findViewById(R.id.overlay);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.n = C2458e.m7218a().m7217a(DonatosApplication.m7167a((Context) this).f7442a).m7216a(new C2445b(this, b_(), this.f8923x, navigationView, this.f8906A, this.f8922w)).m7215a();
        this.n.mo1507a(this);
        this.f8924y = new C2754v(this, navigationView, this.f8922w, this.f8923x);
        C2750s c2750s = this.f8914o;
        c2750s.f8603a.mo262a(this.f8913H);
        this.f8914o.f8604b = R.id.activity_main_fragment_container;
        this.f8914o.m8194a(new C2540n(), C2749a.CROSSFADE, C2749a.CROSSFADE, C2540n.f7843i);
        this.f8907B.add(C2508l.m7347a().f7674a.subscribe(new C2867q(this)));
        this.f8915p.m8181a(this.f8912G);
        C2905a.m8508a();
    }

    public void onDestroy() {
        for (C1172b c1172b : this.f8907B) {
            C2508l.m7347a();
            C2508l.m7348a(c1172b);
        }
        this.f8921v.cleanUp();
        this.f8907B = new ArrayList();
        if (this.f8915p != null) {
            this.f8915p.f8579a = null;
        }
        this.f8925z = null;
        this.f8906A = null;
        if (this.f8924y != null) {
            this.f8924y.m8207b();
        }
        if (this.f8914o != null) {
            this.f8914o.f8603a = null;
        }
        super.onDestroy();
    }

    class C28481 implements OnClickListener {
        final /* synthetic */ MainActivity f8903a;

        C28481(MainActivity mainActivity) {
            this.f8903a = mainActivity;
        }

        public final void onClick(View view) {
            C2754v d = this.f8903a.f8924y;
            d.f8613h.m2471e(d.f8611f);
        }
    }

    class C28492 implements OnClickListener {
        final /* synthetic */ MainActivity f8904a;

        C28492(MainActivity mainActivity) {
            this.f8904a = mainActivity;
        }

        public final void onClick(View view) {
            this.f8904a.f8914o.m8193a();
        }
    }

    class C28503 implements C0292a {
        final /* synthetic */ MainActivity f8905a;

        C28503(MainActivity mainActivity) {
            this.f8905a = mainActivity;
        }

        public final void mo1613a() {
            int d = this.f8905a.f8914o.f8603a.mo266d();
            this.f8905a.f8908C = false;
            if (d > 0) {
                this.f8905a.f8915p.m8183a(false, R.string.app_toolbar.action.back, this.f8905a.f8911F);
                this.f8905a.f8924y.m8206a(true);
                return;
            }
            this.f8905a.f8915p.m8183a(true, R.string.app_toolbar.action.drawer, this.f8905a.f8910E);
            this.f8905a.f8924y.m8206a(false);
        }
    }
}
