package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.common.OrderPriceResponseContent;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class ConvertOrderResponseContent {
    @JsonField(name = {"messages"})
    private List<String> messages = new ArrayList();
    @JsonField(name = {"order"})
    private Order order = null;
    @JsonField(name = {"price"})
    private OrderPriceResponseContent price = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public ConvertOrderResponseContent addMessagesItem(String str) {
        this.messages.add(str);
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConvertOrderResponseContent convertOrderResponseContent = (ConvertOrderResponseContent) obj;
        return C2507k.m7346a(this.order, convertOrderResponseContent.order) && C2507k.m7346a(this.price, convertOrderResponseContent.price) && C2507k.m7346a(this.messages, convertOrderResponseContent.messages);
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public Order getOrder() {
        return this.order;
    }

    public OrderPriceResponseContent getPrice() {
        return this.price;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.order, this.price, this.messages});
    }

    public ConvertOrderResponseContent messages(List<String> list) {
        this.messages = list;
        return this;
    }

    public ConvertOrderResponseContent order(Order order) {
        this.order = order;
        return this;
    }

    public ConvertOrderResponseContent price(OrderPriceResponseContent orderPriceResponseContent) {
        this.price = orderPriceResponseContent;
        return this;
    }

    public void setMessages(List<String> list) {
        this.messages = list;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setPrice(OrderPriceResponseContent orderPriceResponseContent) {
        this.price = orderPriceResponseContent;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class ConvertOrderResponseContent {\n");
        stringBuilder.append("    order: ").append(toIndentedString(this.order)).append("\n");
        stringBuilder.append("    price: ").append(toIndentedString(this.price)).append("\n");
        stringBuilder.append("    messages: ").append(toIndentedString(this.messages)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
