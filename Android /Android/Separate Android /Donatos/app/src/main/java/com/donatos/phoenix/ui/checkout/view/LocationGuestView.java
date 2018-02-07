package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.Phone;
import com.donatos.phoenix.network.common.User;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.ui.checkout.p140b.C2647z;

public class LocationGuestView extends FrameLayout {
    private EditText f8484a;
    private EditText f8485b;
    private EditText f8486c;
    private EditText f8487d;
    private EditText f8488e;

    public LocationGuestView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_locationguest, this);
        this.f8484a = (EditText) inflate.findViewById(R.id.locationguest_firstname);
        this.f8485b = (EditText) inflate.findViewById(R.id.locationguest_lastname);
        this.f8486c = (EditText) inflate.findViewById(R.id.locationguest_email);
        this.f8487d = (EditText) inflate.findViewById(R.id.locationguest_phone);
        this.f8488e = (EditText) inflate.findViewById(R.id.locationguest_company);
        OnFocusChangeListener a = C2715g.m8104a(this);
        this.f8484a.setOnFocusChangeListener(a);
        this.f8485b.setOnFocusChangeListener(a);
        this.f8486c.setOnFocusChangeListener(a);
        this.f8487d.setOnFocusChangeListener(a);
        this.f8488e.setOnFocusChangeListener(a);
    }

    static /* synthetic */ void m8092a(LocationGuestView locationGuestView, boolean z) {
        if (!z) {
            C2508l.m7347a().m7349a(new C2647z(locationGuestView.getUser()));
        }
    }

    public User getUser() {
        String replaceAll = this.f8487d.getEditableText().toString().replaceAll("[^0-9]", "");
        return new User().firstName(this.f8484a.getEditableText().toString()).lastName(this.f8485b.getEditableText().toString()).email(this.f8486c.getEditableText().toString()).phone(replaceAll.length() > 3 ? new Phone().areaCode(replaceAll.substring(0, 3)).phoneNumber(replaceAll.substring(3, replaceAll.length())) : new Phone()).companyName(this.f8488e.getEditableText().toString());
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
    }

    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    public void setUser(User user) {
        if (user != null) {
            this.f8484a.setText(user.getFirstName());
            this.f8485b.setText(user.getLastName());
            this.f8486c.setText(user.getEmail());
            Phone phone = user.getPhone();
            if (!(phone == null || phone.getAreaCode() == null || phone.getPhoneNumber() == null)) {
                this.f8487d.setText(phone.getAreaCode() + phone.getPhoneNumber());
            }
            this.f8488e.setText(user.getCompanyName());
        }
    }
}
