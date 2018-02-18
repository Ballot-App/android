package com.biz.tizzy.eballot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tizzy on 2/17/18.
 */

public class AbstainFragment extends Fragment {

    private static final String DIALOG_THANK_YOU = "ThankYou";

    private RadioButton mYayButton;
    private RadioButton mNayButton;
    private RadioButton mAbstainButton;
    private Button mVoteButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> votes = new HashMap<>();
    private int mNumVotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abstain, container, false);

        mNumVotes = 0;

        // need to first set up votes from db

        mYayButton = (RadioButton) view.findViewById(R.id.yay);
        mNayButton = (RadioButton) view.findViewById(R.id.nay);
        mAbstainButton = (RadioButton) view.findViewById(R.id.abstain);

        mVoteButton = (Button) view.findViewById(R.id.vote);
        mVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // write to DB
                if (mNayButton.isChecked()) {
                    voteNo();
                    // start dialog
                    FragmentManager manager = getFragmentManager();
                    ThankYouFragment dialog = new ThankYouFragment();
                    dialog.show(manager, DIALOG_THANK_YOU);

                } else {
                    if (mYayButton.isChecked()) {
                        voteYes();
                        // start dialog
                        FragmentManager manager = getFragmentManager();
                        ThankYouFragment dialog = new ThankYouFragment();
                        dialog.show(manager, DIALOG_THANK_YOU);

                    } else {
                        if (mAbstainButton.isChecked()) {
                            voteAbs();
                            // start dialog
                            FragmentManager manager = getFragmentManager();
                            ThankYouFragment dialog = new ThankYouFragment();
                            dialog.show(manager, DIALOG_THANK_YOU);

                        } else {
                            Toast.makeText(getContext(), "Please vote", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        return view;
    }

    private void voteYes() {
        votes.put("vote" + mNumVotes, 0);
        mNumVotes++;
        db.collection("election").document("examp").collection("electorate").document("hjk123sd").set(votes);
    }

    private void voteNo() {
        votes.put("vote" + mNumVotes, 1);
        mNumVotes++;
        db.collection("election").document("examp").collection("electorate").document("hjk123sd").set(votes);
    }

    private void voteAbs() {
        votes.put("vote" + mNumVotes, 2);
        mNumVotes++;
        db.collection("election").document("examp").collection("electorate").document("hjk123sd").set(votes);
    }
}
