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

import com.biz.tizzy.eballot.models.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;

/**
 * Created by tizzy on 2/17/18.
 */

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";

    private EditText mEnterCode;
    private Button mButton;
    private String mEnteredVoteID;
    private boolean mIsAuthenticated;

    // firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        mEnterCode = (EditText) view.findViewById(R.id.enterid);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("election/examp/");
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
                }
            }
        });

        Query electionQuery = ref.orderByChild("elctorate").equalTo("astl4");
        electionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        mButton = (Button) view.findViewById(R.id.enterButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get entered ID
                mEnteredVoteID = mEnterCode.getText().toString();

                mDatabase.child(mEnteredVoteID);

                if (voteIDinDatabase(mEnteredVoteID)) {
                    // TEMP go to abstain
                    goToAbstain();
                }
            }
        });

        return view;
    }

    private boolean voteIDinDatabase(String voteID) {
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
