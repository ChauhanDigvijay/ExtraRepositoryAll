package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.C2170e;
import com.bumptech.glide.load.p112b.C2285b;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.MenuItem;
import com.donatos.phoenix.network.common.MenuRecipe;
import com.donatos.phoenix.network.common.MenuSize;
import com.donatos.phoenix.p134b.C2510n;
import java.text.NumberFormat;

public class YouMayAlsoLikeItemView extends FrameLayout {
    private ImageView f8511a;
    private TextView f8512b;
    private TextView f8513c;
    private TextView f8514d;
    private FrameLayout f8515e;

    public YouMayAlsoLikeItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_youmayalsolike, this);
        this.f8511a = (ImageView) inflate.findViewById(R.id.youmayalsolike_image);
        this.f8512b = (TextView) inflate.findViewById(R.id.youmayalsolike_title);
        this.f8513c = (TextView) inflate.findViewById(R.id.youmayalsolike_subtitle);
        this.f8514d = (TextView) inflate.findViewById(R.id.youmayalsolike_price);
        this.f8515e = (FrameLayout) inflate.findViewById(R.id.youmayalsolike_layout);
    }

    public void setData(MenuItem menuItem) {
        this.f8512b.setText(menuItem.getName());
        this.f8513c.setText(menuItem.getDescription());
        this.f8514d.setText(NumberFormat.getCurrencyInstance().format(((MenuSize) ((MenuRecipe) menuItem.getRecipes().get(0)).getSizes().get(0)).getDefaultPrice()));
        C2170e.m6597b(this.f8511a.getContext()).m6725a(menuItem.getImage() != null ? "https://files.donatos.com/" + menuItem.getImage().getFilepath() : C2510n.m7365a(this.f8514d.getContext(), R.drawable.placeholder_horiz).toString()).m6484a((int) R.drawable.placeholder_horiz).m6485a(C2285b.ALL).m6495b().mo1356a(this.f8511a);
    }

    public void setItemClickListener(OnClickListener onClickListener) {
        this.f8515e.setOnClickListener(onClickListener);
    }
}
