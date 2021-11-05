package com.groupname.tripmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Drivers_Fragment extends AppCompatActivity implements personAdapter.ItemClicked {
//View view;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
     EditText etDname,etDnumber;
    ArrayList<person> people;
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_drivers_);
        recyclerView = findViewById(R.id.list);
        etDname=findViewById(R.id.etDname);
        etDnumber=findViewById(R.id.etDnumber);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        // layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        //layoutManager = new GridLayoutManager(Drivers_Fragment.this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        btn = findViewById(R.id.btn);
        people = new ArrayList<person>();
        person p1 = new person();
        p1.setName("Mr Raj Kumar Rawat");
        p1.setSurname("Mr Raj Kumar Rawat");
        p1.setNumber("9415601266");

        if(FirstClass.user.getProperty("isAdmin").equals("1"))
        {
            btn.setVisibility(View.VISIBLE);
            etDname.setVisibility(View.VISIBLE);
            etDnumber.setVisibility(View.VISIBLE);
        }
        else
        {
            btn.setVisibility(View.GONE);
            etDname.setVisibility(View.GONE);
            etDnumber.setVisibility(View.GONE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDname.getText().toString().isEmpty() || etDnumber.getText().toString().isEmpty()) {
                    Toast.makeText(Drivers_Fragment.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String name = etDname.getText().toString().trim();
                    String number = etDnumber.getText().toString().trim();


                    // people = new ArrayList<person>();
                    person p = new person();
                    p.setName(name);
                    p.setSurname(name);
                    p.setNumber(number);



                    // showProgress(true);
                    // tvLoad.setText("Adding new bus");
                    Backendless.Persistence.save(p, new AsyncCallback<person>() {
                        @Override
                        public void handleResponse(person response) {
                            Toast.makeText(Drivers_Fragment.this, "Driver added successfully", Toast.LENGTH_SHORT).show();
                            etDname.setText("");
                            etDnumber.setText("");
                            startActivity(new Intent(Drivers_Fragment.this,MainActivity.class));
                            Drivers_Fragment.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Drivers_Fragment.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        // queryBuilder.addGroupBy("time");

        Backendless.Persistence.of(person.class).find(queryBuilder, new AsyncCallback<List<person>>() {
            @Override
            public void handleResponse(List<person> response) {
                people = (ArrayList<person>) response;
                if(people.size()!=0) {
                    myAdapter = new personAdapter(Drivers_Fragment.this, people);
                    recyclerView.setAdapter(myAdapter);
                }
                //myadapter = new busAdapter(getActivity(), FirstClass.busses);
               // showProgress(false);
               // recyclerView.setAdapter(myadapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(Drivers_Fragment.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
               // showProgress(false);
            }
        });

    }



    @Override
    public void onItemClicked(int index) {
        if(people.size()!=0) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + people.get(index).getNumber()));
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Drivers_Fragment.this,MainActivity.class));
    }
}
