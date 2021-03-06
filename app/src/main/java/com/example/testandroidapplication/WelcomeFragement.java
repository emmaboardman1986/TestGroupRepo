package com.example.testandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeFragement extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_sucessful, container, false);

        Button getStarted = v.findViewById(R.id.get_started);

        final String userEmail = getArguments().getString("USER_EMAIL");

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity().getApplicationContext(),
                        ContentActivity.class);
                i.putExtra("userEmail", userEmail);
                startActivity(i);

                getActivity().finish();

            }
        });

        return v;
    }
}
