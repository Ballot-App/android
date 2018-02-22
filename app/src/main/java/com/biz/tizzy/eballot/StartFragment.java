package com.biz.tizzy.eballot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tizzy on 2/17/18.
 */

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";

    private EditText mEnterCode;
    private Button mButton;
    private String mElecID;
    private boolean mIsAuthenticated;
    private boolean mValid;
    private String mElectionName;
    private ArrayList<Ballot> mBallots = new ArrayList<>();

    // firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Firebase.setAndroidContext(getActivity());
        //Firebase mFirebaseRef = new Firebase("https://eballot-46bb1.firebaseio.com/");

        // Authenticate anonymously
        authenticate();

        // check elecID
        mElectionName = "EXAMP";

        // get ballots
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBallots();
            }
        }).start();

        mEnterCode = (EditText) view.findViewById(R.id.enterid);
        mButton = (Button) view.findViewById(R.id.enterButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getElectionDetails();
                validateElec();

                //if (mIsAuthenticated && voteIDinDatabase(mEnteredVoteID)) {
                if (mIsAuthenticated && ((mElecID != null) && (mValid && (mBallots != null)))) {

                    // start votePager
                    Intent intent = VotesPagerActivity.newIntent(getActivity(), mElecID, mElectionName, mBallots);
                    startActivity(intent);

                    // clear previously entered code
                    mEnterCode.setText("");
                }
            }
        });

        return view;
    }

    private void authenticate() {
        // Authenticate anonymously
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Authentication successful
                    mIsAuthenticated = true;

                } else {
                    // Authentication failure
                    mIsAuthenticated = false;
                    Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getElectionDetails() {
        // retrieves election name and elecid

        String entered = mEnterCode.getText().toString();
        // check it's formatted right
        if ((entered.length() == 14) && (entered.charAt(5) == '-')) {

            String[] parts = entered.split("-");
            mElectionName = parts[0];
            mElecID = parts[1];
        } else {
            Toast.makeText(getContext(), "Not a valid Election ID", Toast.LENGTH_LONG).show();
        }
    }

    private void getBallots() {
        db.collection("election").document(mElectionName).collection("ballots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Ballot ballot = new Ballot(
                                        document.getId(),
                                        (String) document.get("description"),
                                        (String) document.get("type")
                                );
                                if (ballot != null) {
                                    mBallots.add(ballot);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void validateElec() {

        // check election name exists
        DocumentReference docRef = db.collection("election").document(mElectionName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        mValid = true;
                    } else {
                        Log.d(TAG, "No such document");
                        mValid = false;
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    mValid = false;
                }
            }
        });

        if (mValid) {
            // check elecID exists
            DocumentReference docRef2 = db.collection("election").document(mElectionName).collection("electorate").document(mElecID);
            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                            mValid = true;
                        } else {
                            Log.d(TAG, "No such document");
                            mValid = false;
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        mValid = false;
                    }
                }
            });

            if (mValid) {
                // check election not locked
                DocumentReference user = db.collection("election").document(mElectionName).collection("electorate").document(mElecID);
                user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            mValid = !(boolean) doc.get("locked");
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                                mValid = false;
                            }
                        });
            }
        }
    }


}
