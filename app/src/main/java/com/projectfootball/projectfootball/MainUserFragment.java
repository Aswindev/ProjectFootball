package com.projectfootball.projectfootball;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.projectfootball.projectfootball.R.string.SignUpDesc;
import static com.projectfootball.projectfootball.R.string.hello;


public class MainUserFragment extends Fragment {

    String name, email;
    FirebaseUser currentFirebaseUser;
    private Button logOutButton;

    GoogleApiClient mGoogleApiClient;

    public MainUserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if (currentFirebaseUser==null){
            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);

        }

        try {
            Toast.makeText(getActivity(), "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "" + currentFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "" + currentFirebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d("Userinfo", "Exception while fetching user info");
        }

    }

    private void initViews(View view){

        logOutButton = (Button) view.findViewById(R.id.log_out_button);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main_user, container, false);
        initViews(view);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                            }
            // ..
        });

        return view;
    }
}
