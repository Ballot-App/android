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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Firebase.setAndroidContext(getActivity());
        Firebase mFirebaseRef = new Firebase("https://eballot-46bb1.firebaseio.com/");

        mEnterCode = (EditText) view.findViewById(R.id.enterid);

        // Authenticate anonymously
        authenticate();

        // read dat shi
        //readExamp();

        // need to get vote type
        mVoteType = "abstain";

        mButton = (Button) view.findViewById(R.id.enterButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get entered ID
                mElecID = mEnterCode.getText().toString();

                // read from db

                // confirm elecID exists

                //if (mIsAuthenticated && voteIDinDatabase(mEnteredVoteID)) {
                if (mIsAuthenticated && (mElecID != null)) {
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

    private void readExamp() {
        DocumentReference user = db.collection("election").document("examp").collection("electorate").document("hjk123sd");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();

                    //StringBuilder fields = new StringBuilder("");
                    //fields.append("Name: ").append(doc.get("Name"));
                    //fields.append("\nEmail: ").append(doc.get("Email"));
                    //fields.append("\nPhone: ").append(doc.get("Phone"));
                    //textDisplay.setText(fields.toString());

                    Toast.makeText(getContext(), "Yes: " + doc.get("yes"), Toast.LENGTH_LONG);

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
