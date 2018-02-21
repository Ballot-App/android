package com.biz.tizzy.eballot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tizzy on 2/17/18.
 */

public class AbstainFragment extends Fragment {

    private static final String DIALOG_THANK_YOU = "ThankYou";
    private static final String ARG_ELECID = "elecID";
    private static final String TAG = "StartFragment";

    private RadioButton mYayButton;
    private RadioButton mNayButton;
    private RadioButton mAbstainButton;
    private Button mVoteButton;
    private TextView mDescView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> votes = new HashMap<>();
    private long mNumYesVotes;
    private long mNumNoVotes;
    private long mNumAbsVotes;
    private String mElecID;
    private String mDesc;

    public static AbstainFragment newInstance(String elecID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ELECID, elecID);

        AbstainFragment fragment = new AbstainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abstain, container, false);

        // get elecID
        mElecID = (String) getArguments().getSerializable(ARG_ELECID);

        // initialize votes
        votes.put("yes", 0);
        votes.put("no", 0);
        votes.put("abstain", 0);

        // get description of votes
        new Thread(new Runnable() {
            @Override
            public void run() {
                readDesc();
            }
        }).start();

        // read num yes votes
        new Thread(new Runnable() {
            public void run() {
                /*
                readNumVotes();
                votes.put("yes", mNumNoVotes);
                votes.put("no", mNumNoVotes);
                votes.put("abstain", mNumAbsVotes);
                */
            }
        }).start();

        mDescView = view.findViewById(R.id.description);
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
        votes.put("yes", mNumYesVotes+1);
        db.collection("election").document("examp").collection("electorate").document(mElecID).set(votes);
    }

    private void voteNo() {
        votes.put("no", mNumNoVotes+1);
        db.collection("election").document("examp").collection("electorate").document(mElecID).set(votes);
    }

    private void voteAbs() {
        votes.put("abstain", mNumAbsVotes+1);
        db.collection("election").document("examp").collection("electorate").document(mElecID).set(votes);
    }

    private void readNumVotes() {
        DocumentReference user = db.collection("electorate").document(mElecID);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();

                    mNumYesVotes = (long) doc.get("yes");
                    mNumNoVotes = (long) doc.get("no");
                    mNumAbsVotes = (long) doc.get("abstain");

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }
                });
    }

    private void readDesc() {
        DocumentReference user = db.collection("election").document("LukeE");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();

                    mDesc = (String) doc.get("description");
                    Log.d(TAG, mDesc);
                    mDescView.setText(mDesc);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }
                });
    }


}
