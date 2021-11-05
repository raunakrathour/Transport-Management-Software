package com.groupname.tripmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccount_fragment extends Fragment {
    Button btnlogout;
    TextView tvname,tvEmail;
    View view;
    public MyAccount_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           view=     inflater.inflate(R.layout.fragment_my_account_fragment, container, false);
           return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvname = view.findViewById(R.id.tvname);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnlogout = view.findViewById(R.id.btnlogout);

        String name = FirstClass.user.getProperty("name").toString();
        String Email = FirstClass.user.getEmail();
        tvEmail.setText(Email);
        tvname.setText(name);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(getActivity(),"Hope you will be back soon",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(),login.class));
                        getActivity().finish();

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(getActivity(),"Error "+fault.getMessage(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    }
                });// logout from app

            }
        });

    }
}
