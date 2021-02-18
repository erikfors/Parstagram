package com.fors.erik.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fors.erik.parstagram.Activities.LoginActivity;
import com.fors.erik.parstagram.R;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    //widgets
    Button btnLogOut;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSettingsFragment newInstance(String param1, String param2) {
        UserSettingsFragment fragment = new UserSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        //initializing view
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        
        //finding view by id
        btnLogOut = view.findViewById(R.id.btnLogOut);
        
        //listener
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //log user out
                logOutUser();
            }
        });
        
        // Inflate the layout for this fragment
        return view;
    }

    //logs user out and send to log in activity
    private void logOutUser() {
        ParseUser.logOut();

        if(ParseUser.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Log Out Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        else {
            Toast.makeText(getContext(), "Failed to Log Out!", Toast.LENGTH_SHORT).show();
        }

        }

    }
