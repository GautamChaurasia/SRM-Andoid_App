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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PreviosQuestPaperActivity extends AppCompatActivity {
    ImageView qpShowPdf, qpCancel, qpBrowse;
    EditText qsTitle;
    Spinner qpSemSpinner, qpDeptSpinner;
    Button qsSubmit;
    Uri filepath;
    String title,dept,sem;
    DatabaseReference databaseReference,dbRef;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previos_quest_paper);

        qpBrowse = findViewById(R.id.qpBrowse);
        qpCancel = findViewById(R.id.qpCancel);
        qpShowPdf = findViewById(R.id.qpShowPdf);

        qsTitle = findViewById(R.id.qsTitle);

        qpSemSpinner = findViewById(R.id.qpSemSpinner);
        qpDeptSpinner = findViewById(R.id.qpDeptSpinner);

        qsSubmit = findViewById(R.id.qsSubmit);

        qpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qpShowPdf.setVisibility(View.GONE);
                qpCancel.setVisibility(View.GONE);
                qpBrowse.setVisibility(View.VISIBLE);
            }

        });

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("questionPapers");

        qpBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(PreviosQuestPaperActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select pdf files"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(PreviosQuestPaperActivity.this, R.array.dept, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qpDeptSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(PreviosQuestPaperActivity.this, R.array.sem, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qpSemSpinner.setAdapter(semAdapter);

        qsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField();
            }
        });



    }

    private void checkField() {
        title = qsTitle.getText().toString();
        dept = qpDeptSpinner.getSelectedItem().toString();
        sem = qpSemSpinner.getSelectedItem().toString();

        if(title.isEmpty()){
            qsTitle.setError("Empty");
            qsTitle.requestFocus();
        }else if(dept.equals("Select Department")){
            Toast.makeText(PreviosQuestPaperActivity.this, "Please select a department", Toast.LENGTH_SHORT).show();
        }else if(sem.equals("Select Semester")){
            Toast.makeText(PreviosQuestPaperActivity.this, "Please select a Semester", Toast.LENGTH_SHORT).show();
        }else {
            uploadPdf();
        }

    }

    private void uploadPdf() {
        ProgressDialog pd = new ProgressDialog(PreviosQuestPaperActivity.this);
        pd.setTitle("Please wait");
        pd.setMessage("uploading..");
        pd.show();

        StorageReference reference = storageReference.child("questionPapers/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        SyllabusPdfModel obj = new SyllabusPdfModel(title,uri.toString(),dept,sem);

                        databaseReference.child(dept).child(sem).child(title).child(databaseReference.push().getKey()).setValue(obj);
                        qsTitle.setText("");
                        qpBrowse.setVisibility(View.VISIBLE);
                        qpCancel.setVisibility(View.GONE);
                        qpShowPdf.setVisibility(View.GONE);

                        pd.dismiss();

                        Toast.makeText(PreviosQuestPaperActivity.this, title +" Uploaded", Toast.LENGTH_SHORT).show();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PreviosQuestPaperActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                filepath = data.getData();
                qpShowPdf.setVisibility(View.VISIBLE);
                qpCancel.setVisibility(View.VISIBLE);
                qpBrowse.setVisibility(View.GONE);
            }

        }

    }

}