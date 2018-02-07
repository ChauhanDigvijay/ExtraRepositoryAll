package com.womensafety;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.womensafety.object.ContactDO;

import java.util.ArrayList;

public class InviteFragment extends Fragment {
    private ArrayList<ContactDO> arrList = new ArrayList();
    private Context context;
    private View helpMeF;
    private InviteAdapter inviteAdapter;
    private ImageView ivInvite;
    private ListView lvFrindList;
    private LayoutInflater minflater;
    private TextView tvSkipNow;

    public InviteFragment(ArrayList<ContactDO> arrList) {
        if (arrList == null) {
            arrList = new ArrayList();
        }
        this.arrList = arrList;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.helpMeF = inflater.inflate(R.layout.helpme5, null);
        this.tvSkipNow = (TextView) this.helpMeF.findViewById(R.id.tvSkipNow);
        this.ivInvite = (ImageView) this.helpMeF.findViewById(R.id.ivInvite);
        this.lvFrindList = (ListView) this.helpMeF.findViewById(R.id.lvFrindList);
        this.ivInvite.setOnClickListener(new C06981());
        this.minflater = inflater;
        this.tvSkipNow.setText(Html.fromHtml("<u>" + this.context.getString(R.string.skip_now) + "</u>"));
        ListView listView = this.lvFrindList;
        ListAdapter inviteAdapter = new InviteAdapter(this.arrList);
        this.inviteAdapter = (InviteAdapter) inviteAdapter;
        listView.setAdapter(inviteAdapter);
        this.tvSkipNow.setOnClickListener(new C06992());
        return this.helpMeF;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    class C06981 implements OnClickListener {
        C06981() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) InviteFragment.this.context).skipNow();
        }
    }

    class C06992 implements OnClickListener {
        C06992() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) InviteFragment.this.context).skipNow();
        }
    }

    private class InviteAdapter extends BaseAdapter {
        ArrayList<ContactDO> arrayList;

        public InviteAdapter(ArrayList<ContactDO> arrayList) {
            this.arrayList = arrayList;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public int getCount() {
            return this.arrayList.size();
        }

        private void refreshList(ArrayList<ContactDO> arrayList) {
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ContactDO contactDO = (ContactDO) this.arrayList.get(position);
            String name = contactDO.name;
            String image = contactDO.imagePath;
            if (convertView == null) {
                convertView = InviteFragment.this.minflater.inflate(R.layout.invite_cell, null);
            }
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            ((TextView) convertView.findViewById(R.id.tvName)).setText(name);
            if (image != null) {
                try {
                    Bitmap bitmap = Media.getBitmap(InviteFragment.this.getActivity().getContentResolver(), Uri.parse(image));
                    if (bitmap != null) {
                        ivImage.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return convertView;
        }
    }
}
