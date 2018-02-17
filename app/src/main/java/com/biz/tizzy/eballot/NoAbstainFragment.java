package com.biz.tizzy.eballot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by tizzy on 2/17/18.
 */

public class NoAbstainFragment extends Fragment {

    private RadioButton mYayButton;
    private RadioButton mNayButton;
    private Button mVoteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noabstain, container, false);

        mYayButton = (RadioButton) view.findViewById(R.id.yay);

        mNayButton = (RadioButton) view.findViewById(R.id.nay);

        mVoteButton = (Button) view.findViewById(R.id.vote);
        mVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        return view;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yay:
                if (checked)
                    // vote yes
                    break;
            case R.id.nay:
                if (checked)
                    // vote no
                    break;
        }
    }
}
