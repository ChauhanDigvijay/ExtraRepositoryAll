package com.olo.jambajuice.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;

import static com.olo.jambajuice.Utils.Constants.AVATAR_ICONS;

public class AvatarFragment extends Fragment {
    private static final String AVATAR_ID = "AVATAR_ID";

    public static Fragment getInstance(int avatarId) {
        Fragment fragment = new AvatarFragment();
        Bundle args = new Bundle();
        args.putInt(AVATAR_ID, avatarId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avatar, container, false);
        int avatarId = getArguments().getInt(AVATAR_ID);
        if (avatarId < AVATAR_ICONS.length) {
            ImageView avatarImage = (ImageView) view.findViewById(R.id.avatarId);
            avatarImage.setImageResource(Constants.AVATAR_ICONS[avatarId]);
        }
        return view;
    }
}
