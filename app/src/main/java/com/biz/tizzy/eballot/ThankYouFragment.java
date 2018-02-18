package com.biz.tizzy.eballot;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by tizzy on 2/18/18.
 */

public class ThankYouFragment extends DialogFragment {

    private Button mCloseButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_thankyou, null);

        final android.app.AlertDialog alert = new android.app.AlertDialog.Builder(getActivity())
                .setView(v)
                .create();

        mCloseButton = (Button) v.findViewById(R.id.close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close app
                System.exit(0);
            }
        });

        return alert;
    }
}
