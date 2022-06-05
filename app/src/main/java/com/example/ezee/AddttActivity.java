package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class AddttActivity extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    EditText Tttitle;
    ImageView Ttbrowsepdf , Ttshowpdf, Ttcancelpdf;
    Spinner addttyearpinnerr,addttdeptspinner,addttdivspinner;
    Button addttsubmit;
    Uri filepath;

    String title,year,dept,div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtt);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("timetable");


        Tttitle = findViewById(R.id.TtTitle);
        Ttbrowsepdf  = findViewById(R.id.TtBrowsePdf);
        Ttshowpdf  = findViewById(R.id.TtShowPdf);
        Ttcancelpdf = findViewById(R.id.TtCancel);
        addttyearpinnerr  = findViewById(R.id.addttyearpinnerr);
        addttdeptspinner = findViewById(R.id.addttdeptspinner);
        addttdivspinner = findViewById(R.id.addttdivspinner);
        addttsubmit = findViewById(R.id.addttsubmit);

        Ttbrowsepdf.setVisibility(View.VISIBLE);
        Ttcancelpdf.setVisibility(View.GONE);
        Ttshowpdf.setVisibility(View.GONE);

        Ttcancelpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Ttshowpdf.setVisibility(View.GONE);
                Ttcancelpdf.setVisibility(View.GONE);
                Ttbrowsepdf.setVisibility(View.VISIBLE);
            }

        });

        Ttbrowsepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AddttActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                Log.i("Kam","PDF liya");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select pdf files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) { }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addttyearpinnerr.setAdapter(adapter);

        ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.dept, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addttdeptspinner.setAdapter(semAdapter);


        ArrayAdapter<CharSequence> divAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.div, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addttdivspinner.setAdapter(divAdapter);

        addttsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = Tttitle.getText().toString();
                year = addttyearpinnerr.getSelectedItem().toString();
                dept = addttdeptspinner.getSelectedItem().toString();
                div = addttdivspinner.getSelectedItem().toString();

                if(title.isEmpty()){
                    Tttitle.setError("Empty");
                    Tttitle.requestFocus();
                }else if(year.equals("Select Year")){
                    Toast.makeText(getApplicationContext(), "Please select a Year", Toast.LENGTH_SHORT).show();
                }else if(dept.equals("Select Department")){
                    Toast.makeText(getApplicationContext(), "Please select a Department", Toast.LENGTH_SHORT).show();
                }else if(div.equals("Select Division")){
                    Toast.makeText(getApplicationContext(), "Please select a Division ", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadPdf(title,year,dept,div);
                }
            }
        });


    }

    private void uploadPdf(String title,String year,String dept,String div) {
        ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setTitle("Please wait");
        pd.setMessage("uploading..");

        StorageReference reference = storageReference.child("timetable/"+year+"_"+dept+"_"+div+"_"+title+".pdf");
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        TtModel obj = new TtModel(title,uri.toString(),year,dept,div);
                        String pass = year+"_"+dept+"_"+div;
                        databaseReference.child(pass).setValue(obj);
                        Tttitle.setText("");
                        Ttbrowsepdf.setVisibility(View.VISIBLE);
                        Ttcancelpdf.setVisibility(View.GONE);
                        Ttshowpdf.setVisibility(View.GONE);

                        pd.dismiss();

                        Toast.makeText(getApplicationContext(), title +" Uploaded", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            filepath = data.getData();
            Log.i("Kam",filepath+"");
            Ttshowpdf.setVisibility(View.VISIBLE);
            Ttcancelpdf.setVisibility(View.VISIBLE);
            Ttbrowsepdf.setVisibility(View.GONE);
//
        }
    }
}