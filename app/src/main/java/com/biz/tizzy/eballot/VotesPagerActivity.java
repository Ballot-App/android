package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by tizzy on 2/22/18.
 */

public class VotesPagerActivity extends AppCompatActivity {
    private static final String TAG = "VotesPagerActivity";

    private static final String EXTRA_ELECID = "com.biz.tizzy.eballot.elecid";
    private static final String EXTRA_ELECNAME = "com.biz.tizzy.eballot.elecname";
    private static final String EXTRA_BALLOTS = "com.biz.tizzy.eballot.ballots";
    private static final String DIALOG_THANK_YOU = "ThankYou";

    private String mElecID;
    private String mElecName;
    private ArrayList<Ballot> mBallots;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button mDoneButton;

    public static Intent newIntent(Context packageContext, String elecID, String elecName, ArrayList<Ballot> ballots) {
        Intent intent = new Intent(packageContext, VotesPagerActivity.class);
        intent.putExtra(EXTRA_ELECID, elecID);
        intent.putExtra(EXTRA_ELECNAME, elecName);
        intent.putExtra(EXTRA_BALLOTS, ballots);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votes_pager);

        mElecID = (String) getIntent().getSerializableExtra(EXTRA_ELECID);
        mElecName = (String) getIntent().getSerializableExtra(EXTRA_ELECNAME);
        mBallots = (ArrayList<Ballot>) getIntent().getSerializableExtra(EXTRA_BALLOTS);

        mDoneButton = (Button) findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start dialog
                DialogFragment dialog = new ThankYouFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_THANK_YOU);
            }
        });

        ViewPager pager = (ViewPager) findViewById(R.id.votes_view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private void getBallots() {
        db.collection("election").document(mElecName).collection("ballots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                mBallots.add(new Ballot(
                                        document.getId(),
                                        (String) document.get("description"),
                                        (String) document.get("type")
                                ));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            /*
            switch(pos) {

                case 0: return AbstainFragment.newInstance(mElecID, mElecName);
                case 1: return NoAbstainFragment.newInstance(mElecID, mElecName, mBallotID);
                //
               default: return AbstainFragment.newInstance(mElecID, mElecName);
            }
            */

            switch (mBallots.get(pos).getVoteType()) {

                case "1": return NoAbstainFragment.newInstance(mElecID, mElecName, mBallots.get(pos).getID());
                case "2": return AbstainFragment.newInstance(mElecID, mElecName, mBallots.get(pos).getID());
                default: return AbstainFragment.newInstance(mElecID, mElecName, mBallots.get(pos).getID());
            }
        }

        @Override
        public int getCount() {
            // should be number of votes
            if (mBallots == null) {
                return 1;
            }
            return mBallots.size();
        }
    }
}


