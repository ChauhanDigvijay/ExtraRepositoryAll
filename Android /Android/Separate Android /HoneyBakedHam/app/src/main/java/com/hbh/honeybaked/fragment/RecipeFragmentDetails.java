package com.hbh.honeybaked.fragment;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.module.MenuModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class RecipeFragmentDetails extends BaseFragment {
    GridAdapter adapter;
    Calendar cal = Calendar.getInstance();
    GridView grid_vw;
    int[] image = new int[]{R.drawable.recipes_chicken_tortilla, R.drawable.recipes_stuffed_peppers, R.drawable.recipes_rigatoni, R.drawable.recipes_asparagus_tart, R.drawable.recipes_caprese, R.drawable.recipes_ham_andpasta_salad, R.drawable.recipes_picnic_pie};
    String[] menu_details = new String[]{"Quick, easy and delicious using our Smoked or Oven-Roasted Turkey.", "Micro-blanch the peppers inverted in a square baking dish for 1-2 minutes. Turn them over and stuff them with the ham and rice mixture.", "Cover with foil and bake pasta in a preheated 375°F oven for 15 minutes. Uncover and stir to mix and coat pasta evenly with the melted cheeses", "Preheat oven to 450º. In a medium frying pan over medium high heat, sauté for about 1 minute:", "In a food processor blend together:", "Arrange salad on large platter. Garnish with olives, if desired. Serve, passing Parmesan cheese separately.", "In a large frying pan over medium heat sauté until tender (approximately 2 minutes)."};
    String[] menu_item_title = new String[]{"Smokey Turkey Tortilla Soup", "Stuffed Pepper Treat", "Rigatoni with Tomatoes & Feta Cheese", "Asparagus Tart", "Turkey & Mozzarella Sandwich", "Ham & Ravioli Salad with Vegetables", "Smokey Turkey Tortilla Soup"};

    private class GridAdapter extends BaseAdapter {
        AdapterListener adapter_listener = null;
        Context context;
        ArrayList<HashMap<String, String>> value;

        public GridAdapter(Context context, ArrayList<HashMap<String, String>> value) {
            this.context = context;
            this.value = value;
            this.adapter_listener = (AdapterListener) context;
        }

        public int getCount() {
            return this.value.size();
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (convertView == null) {
                view = new View(this.context);
                view = inflater.inflate(R.layout.grid_custom_layout_recipe, null);
            } else {
                view = convertView;
            }
            TextView textView1 = (TextView) view.findViewById(R.id.grid_text_sub);
            ImageView imageView = (ImageView) view.findViewById(R.id.grid_image);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.imge);
            RelativeLayout product_back_tr = (RelativeLayout) view.findViewById(R.id.product_back_tr);
            HashMap<String, String> n = (HashMap) this.value.get(position);
            ((TextView) view.findViewById(R.id.grid_text)).setText((CharSequence) n.get("titile"));
            textView1.setText((CharSequence) n.get("sub"));
            imageView.setImageResource(Integer.parseInt((String) n.get("img")));
            setColor(this.context.getResources().getColor(R.color.ham_burg_new), imageView1);
            product_back_tr.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    GridAdapter.this.adapter_listener.performAdapterAction(AppConstants.RECIPE_MAIN_PAGE, new MenuModel("RECIPES", position));
                }
            });
            return view;
        }

        private void setColor(int color, ImageView img) {
            if (img.getDrawable() != null) {
                img.getDrawable().setColorFilter(color, Mode.SRC_ATOP);
                img.invalidate();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_details, container, false);
        this.grid_vw = (GridView) v.findViewById(R.id.grid_vw);

        return v;
    }

    private void setValue(int i) {
        ArrayList<HashMap<String, String>> value = new ArrayList();
        for (int k = 0; k <= i; k++) {
            HashMap<String, String> n = new HashMap();
            n.put("img", String.valueOf(this.image[k]));
            n.put("titile", this.menu_item_title[k]);
            n.put("sub", this.menu_details[k]);
            value.add(n);
        }
        this.adapter = new GridAdapter(getActivity(), value);
        this.grid_vw.setAdapter(this.adapter);
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    public void onClick(View v) {
    }
}
