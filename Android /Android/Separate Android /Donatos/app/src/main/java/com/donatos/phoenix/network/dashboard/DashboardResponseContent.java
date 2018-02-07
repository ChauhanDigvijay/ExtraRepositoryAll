package com.donatos.phoenix.network.dashboard;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.network.common.MenuItem;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.common.User;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class DashboardResponseContent {
    @JsonField(name = {"location"})
    private Location location = null;
    @JsonField(name = {"ltos"})
    private List<MenuItem> ltos = new ArrayList();
    @JsonField(name = {"orders"})
    private List<Order> orders = new ArrayList();
    @JsonField(name = {"quickpicks"})
    private List<MenuItem> quickpicks = new ArrayList();
    @JsonField(name = {"user"})
    private User user = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public DashboardResponseContent addLtosItem(MenuItem menuItem) {
        this.ltos.add(menuItem);
        return this;
    }

    public DashboardResponseContent addOrdersItem(Order order) {
        this.orders.add(order);
        return this;
    }

    public DashboardResponseContent addQuickpicksItem(MenuItem menuItem) {
        this.quickpicks.add(menuItem);
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DashboardResponseContent dashboardResponseContent = (DashboardResponseContent) obj;
        return C2507k.m7346a(this.location, dashboardResponseContent.location) && C2507k.m7346a(this.orders, dashboardResponseContent.orders) && C2507k.m7346a(this.quickpicks, dashboardResponseContent.quickpicks) && C2507k.m7346a(this.ltos, dashboardResponseContent.ltos) && C2507k.m7346a(this.user, dashboardResponseContent.user);
    }

    public Location getLocation() {
        return this.location;
    }

    public List<MenuItem> getLtos() {
        return this.ltos;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public List<MenuItem> getQuickpicks() {
        return this.quickpicks;
    }

    public User getUser() {
        return this.user;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.location, this.orders, this.quickpicks, this.ltos, this.user});
    }

    public DashboardResponseContent location(Location location) {
        this.location = location;
        return this;
    }

    public DashboardResponseContent ltos(List<MenuItem> list) {
        this.ltos = list;
        return this;
    }

    public DashboardResponseContent orders(List<Order> list) {
        this.orders = list;
        return this;
    }

    public DashboardResponseContent quickpicks(List<MenuItem> list) {
        this.quickpicks = list;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLtos(List<MenuItem> list) {
        this.ltos = list;
    }

    public void setOrders(List<Order> list) {
        this.orders = list;
    }

    public void setQuickpicks(List<MenuItem> list) {
        this.quickpicks = list;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class DashboardResponseContent {\n");
        stringBuilder.append("    location: ").append(toIndentedString(this.location)).append("\n");
        stringBuilder.append("    orders: ").append(toIndentedString(this.orders)).append("\n");
        stringBuilder.append("    quickpicks: ").append(toIndentedString(this.quickpicks)).append("\n");
        stringBuilder.append("    ltos: ").append(toIndentedString(this.ltos)).append("\n");
        stringBuilder.append("    user: ").append(toIndentedString(this.user)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public DashboardResponseContent user(User user) {
        this.user = user;
        return this;
    }
}
