package com.biz.tizzy.eballot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by tizzy on 2/17/18.
 */

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";

    private EditText mEnterCode;
    private Button mButton;
    private String mEnteredVoteID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        mEnterCode = (EditText) view.findViewById(R.id.enterid);
        mEnteredVoteID = mEnterCode.getText().toString();

        mButton = (Button) view.findViewById(R.id.enterButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TEMP go to abstain
                goToAbstain();
            }
        });

        return view;
    }

    private void goToNoAbstain() {
        Intent intent = NoAbstainActivity.newIntent(getActivity());
        startActivity(intent);
    }

    private void goToAbstain() {
        Intent intent = AbstainActivity.newIntent(getActivity());
        startActivity(intent);
    }


}
