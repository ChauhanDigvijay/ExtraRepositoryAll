package com.donatos.phoenix.network.dashboard;

import com.donatos.phoenix.network.common.Address;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.network.common.MenuCategory;
import com.donatos.phoenix.network.common.MenuItem;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.locations.MenuInstruction;
import com.donatos.phoenix.p134b.C2510n;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import p027b.p040b.C1160h;
import p027b.p040b.C1167n;
import p027b.p041c.C1295k;
import p027b.p041c.C1333u;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p047e.p051b.C1253b;
import p027b.p041c.p047e.p055e.p062d.C1577a;
import p027b.p041c.p065h.C1642a;
import p027b.p041c.p066i.C1651a;

public class DashboardInteractor {
    private static final int LIFETIME_HOURS = 12;
    private ConcurrentHashMap<String, DashboardResponse> mDashBoardResponses = new ConcurrentHashMap();
    private final DashboardApi mDashboardApi;
    private final C1160h<DashboardResponse> mDashboardResponseProvider;

    public DashboardInteractor(DashboardApi dashboardApi, C1167n c1167n) {
        this.mDashboardApi = dashboardApi;
        this.mDashboardResponseProvider = c1167n.m4652b().m4640a(12, TimeUnit.HOURS).m4641a("dashboard");
    }

    static /* synthetic */ boolean lambda$getFavorites$11(Order order) throws Exception {
        return !order.getIsFavorite().equals(Boolean.valueOf(true));
    }

    static /* synthetic */ List lambda$getMenuItemsByCategory$1(int i, DashboardResponse dashboardResponse) throws Exception {
        return dashboardResponse.getContent().getLocation().getMenu().getCategories().get(i) != null ? ((MenuCategory) dashboardResponse.getContent().getLocation().getMenu().getCategories().get(i)).getItems() : null;
    }

    static /* synthetic */ boolean lambda$getQuickPicks$13(List list, MenuItem menuItem) throws Exception {
        return !list.contains(menuItem.getId());
    }

    public void cleanUp() {
        this.mDashBoardResponses = new ConcurrentHashMap();
    }

    public void clearMemoryCache() {
        this.mDashBoardResponses.clear();
    }

    public C1295k<List<Address>> getAddresses(Double d, Double d2, Integer num, boolean z) {
        return getDashboard(d, d2, num, z).map(DashboardInteractor$$Lambda$17.lambdaFactory$());
    }

    public C1295k<DashboardResponse> getDashboard(Double d, Double d2, Integer num, boolean z) {
        String a = C2510n.m7366a(d, d2, num);
        Object obj = null;
        if (z) {
            this.mDashBoardResponses.remove(a);
            this.mDashboardResponseProvider.m4646a(a).m4688b();
        } else {
            DashboardResponse dashboardResponse = (DashboardResponse) this.mDashBoardResponses.get(a);
        }
        if (obj != null) {
            return C1295k.just(obj);
        }
        C1333u a2 = this.mDashboardApi.getDashboard(d, d2, num).m4870a(this.mDashboardResponseProvider.m4647b(a));
        Object lambdaFactory$ = DashboardInteractor$$Lambda$1.lambdaFactory$(this, a);
        C1253b.m4784a(lambdaFactory$, "onSuccess is null");
        return C1642a.m5233a(new C1577a(a2, lambdaFactory$)).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<List<Order>> getFavorites(Double d, Double d2, Integer num, boolean z, List<Integer> list) {
        return getDashboard(d, d2, num, z).map(DashboardInteractor$$Lambda$7.lambdaFactory$()).flatMap(DashboardInteractor$$Lambda$8.lambdaFactory$()).filter(DashboardInteractor$$Lambda$9.lambdaFactory$()).toList().l_();
    }

    public C1295k<List<MenuInstruction>> getInstructions(Double d, Double d2, Integer num, boolean z, int i) {
        return getDashboard(d, d2, num, z).map(DashboardInteractor$$Lambda$14.lambdaFactory$(i));
    }

    public C1295k<List<MenuInstruction>> getInstructionsByMenuItem(Double d, Double d2, Integer num, boolean z, int i) {
        return getDashboard(d, d2, num, z).flatMap(DashboardInteractor$$Lambda$15.lambdaFactory$()).flatMap(DashboardInteractor$$Lambda$16.lambdaFactory$(i));
    }

    public C1295k<Location> getLocation(Double d, Double d2, Integer num, boolean z) {
        return getDashboard(d, d2, num, z).map(DashboardInteractor$$Lambda$13.lambdaFactory$());
    }

    public C1295k<MenuItem> getMenuItem(Double d, Double d2, Integer num, boolean z, int i) {
        return getDashboard(d, d2, num, z).flatMap(DashboardInteractor$$Lambda$3.lambdaFactory$()).flatMap(DashboardInteractor$$Lambda$4.lambdaFactory$(i));
    }

    public C1295k<List<MenuItem>> getMenuItems(Double d, Double d2, Integer num, boolean z, List<Integer> list) {
        return getDashboard(d, d2, num, z).flatMap(DashboardInteractor$$Lambda$5.lambdaFactory$()).flatMap(DashboardInteractor$$Lambda$6.lambdaFactory$(list)).toList().l_();
    }

    public C1295k<List<MenuItem>> getMenuItemsByCategory(Double d, Double d2, Integer num, boolean z, int i) {
        return getDashboard(d, d2, num, z).map(DashboardInteractor$$Lambda$2.lambdaFactory$(i));
    }

    public C1295k<List<MenuItem>> getQuickPicks(Double d, Double d2, Integer num, boolean z, List<Integer> list) {
        return getDashboard(d, d2, num, z).map(DashboardInteractor$$Lambda$10.lambdaFactory$()).flatMap(DashboardInteractor$$Lambda$11.lambdaFactory$()).filter(DashboardInteractor$$Lambda$12.lambdaFactory$(list)).toList().l_();
    }
}
