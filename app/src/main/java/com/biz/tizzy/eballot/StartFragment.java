package com.biz.tizzy.eballot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by tizzy on 2/17/18.
 */

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";
    public static final String EXTRA_ELECID = "com.biz.tizzy.eballot.elecid";

    private EditText mEnterCode;
    private Button mButton;
    private String mElecID;
    private String mVoteType;
    private boolean mIsAuthenticated;

    // firestore
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Firebase.setAndroidContext(getActivity());
        Firebase myFirebaseRef = new Firebase("https://eballot-46bb1.firebaseio.com/");

        mEnterCode = (EditText) view.findViewById(R.id.enterid);

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

        // TESTING -----------
        Firebase result =  myFirebaseRef.child("election/examp");
        Log.d(TAG, "result " + result);

        boolean secondBool = result == null;
        Log.d(TAG, "result " + secondBool);
        // --------------------

        // need to get vote type
        mVoteType = "abstain";

        mButton = (Button) view.findViewById(R.id.enterButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get entered ID
                mElecID = mEnterCode.getText().toString();

                // read from db

                //if (mIsAuthenticated && voteIDinDatabase(mEnteredVoteID)) {
                if (true) {
                    switch (mVoteType) {
                        case "abstain":
                            goToAbstain();
                            break;
                        case "noabstain":
                            goToNoAbstain();
                            break;
                        default:
                            Toast.makeText(getContext(), "Could not get vote type", Toast.LENGTH_LONG).show();
                            break;
                    }
                    // clear previously entered code
                    mEnterCode.setText("");
                }
            }
        });

        return view;
    }

    private String getVoteIDfromDB(Firebase firebase) {
        String voteID = null;

        firebase.child("election").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Error: " + firebaseError);
            }

        });

        return voteID;
    }

    private void goToNoAbstain() {
        Intent intent = NoAbstainActivity.newIntent(getActivity(), mElecID);
        intent.putExtra(EXTRA_ELECID, mElecID);
        startActivity(intent);
    }

    private void goToAbstain() {
        Intent intent = AbstainActivity.newIntent(getActivity(), mElecID);
        intent.putExtra(EXTRA_ELECID, mElecID);
        startActivity(intent);

    }


}
