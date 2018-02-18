package com.biz.tizzy.eballot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.*;

/**
 * Created by tizzy on 2/17/18.
 */

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";

    private EditText mEnterCode;
    private Button mButton;
    private String mEnteredVoteID;
    private boolean mIsAuthenticated;

    // firestore
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Firebase.setAndroidContext(getActivity());
        Firebase myFirebaseRef = new Firebase("https://eballot-46bb1.firebaseio.com/");

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

        mEnterCode = (EditText) view.findViewById(R.id.enterid);

        // TESTING -----------
        Firebase result =  myFirebaseRef.child("election/examp");
        Log.d(TAG, "result " + result);

        boolean secondBool = result == null;
        Log.d(TAG, "result " + secondBool);

        // --------------------


        mButton = (Button) view.findViewById(R.id.enterButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get entered ID
                mEnteredVoteID = mEnterCode.getText().toString();

                // read from db
                /*
                DocumentReference voteIDRef = mElectionRef.collection("electorate").document("hjk123sd");
                DocumentSnapshot doc = voteIDRef.get();
                DocumentSnapshot = doc.getResult();

                if (document.exists()) {
                    Toast.makeText(getContext(), "Document data: " + document.getData(), Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(getContext(), "No such document!", Toast.LENGTH_LONG);
                }
                */

                //if (mIsAuthenticated && voteIDinDatabase(mEnteredVoteID)) {
                if (true) {
                    // TEST
                    goToAbstain();
                }
            }
        });

        return view;
    }

    private boolean voteIDinDatabase(final String voteID) {
        boolean inDB = false;

        return inDB;
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
