package com.olo.jambajuice.Fragments.GiftCardFragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard.AddExistingCardActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.PurchaseNewCardActivityGiftCard;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;


public class BottomsheetFragment extends BottomSheetDialogFragment {

    String mString;

    public static BottomsheetFragment newInstance(String string) {
        BottomsheetFragment f = new BottomsheetFragment();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_bottomsheet_fragment, container, false);
        TextView purchasenewCard = (TextView) v.findViewById(R.id.purchasenewCard);
        TextView addExistingCard= (TextView) v.findViewById(R.id.addExistingCard);
        TextView cancelView= (TextView) v.findViewById(R.id.cancelView);

        purchasenewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.slideUp(getActivity(), PurchaseNewCardActivityGiftCard.class);
                dismiss();
            }
        });
        addExistingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.slideUp(getActivity(), AddExistingCardActivityGiftCard.class);
                dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }

}
