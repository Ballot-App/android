package com.biz.tizzy.eballot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tizzy on 2/17/18.
 */

public class NoAbstainFragment extends Fragment {

    private TextView mTextView;
    private RadioButton mYayButton;
    private RadioButton mNayButton;
    private Button mVoteButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noabstain, container, false);

        mTextView = (TextView) view.findViewById(R.id.description);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog with more information
            }
        });

        mYayButton = (RadioButton) view.findViewById(R.id.yay);

        mNayButton = (RadioButton) view.findViewById(R.id.nay);

        mVoteButton = (Button) view.findViewById(R.id.vote);
        mVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // write to DB
                if (mNayButton.isChecked()) {
                    voteNo();
                } else {
                    if (mYayButton.isChecked()) {
                        voteYes();
                    } else {
                        Toast.makeText(getContext(), "Please vote", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    private void voteYes() {
        Map<String, Object> yes = new HashMap<>();
        yes.put("yes", true);
        db.collection("election").document("examp").collection("electorate").document("hjk123sd").set(yes);
    }

    private void voteNo() {
        Map<String, Object> no = new HashMap<>();
        no.put("no", false);
        db.collection("election").document("examp").set(no);
    }
}
