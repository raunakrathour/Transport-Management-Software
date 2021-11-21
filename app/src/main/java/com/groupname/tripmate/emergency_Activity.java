package com.groupname.tripmate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class emergency_Activity extends AppCompatActivity {

    Button btn1,btn2,btn3;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_);
        btn1 = findViewById(R.id.btn1_emergency_activity);
        btn2 = findViewById(R.id.btn2_emergency_activity);
        btn3 = findViewById(R.id.btn3_emergency_activity);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Calling Admin", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:6265502674"));
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Enter message to and send", Toast.LENGTH_SHORT).show();//send feedback, it opens mail send it to Tripcare@gmail.com
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "iit2018199@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Travel Emergency");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "emergency"));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PackageManager manager = getPackageManager();
                try {
                    Intent i = manager.getLaunchIntentForPackage("com.kinshuu.plexus");
                    if (i == null) {
                        //Toast.makeText(getApplicationContext(), "Download app first", Toast.LENGTH_SHORT).show();
                        final AlertDialog.Builder dialog2 = new AlertDialog.Builder(emergency_Activity.this);
                        dialog2.setMessage("You need to download TripMate-Protectify app");
                        dialog2.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1re_RwBBS947-WtYQNSnsLsZNusEbkR45/view?usp=sharing"));
                                startActivity(intent);
                            }
                        });
                        dialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog2.show();
                    }
                    else {
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(i);
                    }


                } catch (ActivityNotFoundException e) {
                   // Toast.makeText(getApplicationContext(), "Download app first", Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder dialog1 = new AlertDialog.Builder(emergency_Activity.this);
                    dialog1.setMessage("You need to download TripMate-Protectify app");
                    dialog1.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1re_RwBBS947-WtYQNSnsLsZNusEbkR45/view?usp=sharing"));
                            startActivity(intent);
                        }
                    });
                    dialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog1.show();




                }

            }
        });


    }
}
