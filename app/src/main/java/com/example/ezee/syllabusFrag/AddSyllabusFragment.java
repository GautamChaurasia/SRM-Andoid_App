package com.example.ezee.syllabusFrag;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ezee.R;
import com.example.ezee.SyllabusPdfModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

public class AddSyllabusFragment extends Fragment {

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    EditText syllabusTitle;
    ImageView browsepdf, showpdf, cancelpdf;
    Spinner syllabusDept, syllabusSem;
    Button syllabusSubmit;
    Uri filepath;

    String title,dept,sem;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddSyllabusFragment() {
    }

    public static AddSyllabusFragment newInstance(String param1, String param2) {
        AddSyllabusFragment fragment = new AddSyllabusFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_syllabus, container, false);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("syllabus");

        browsepdf = view.findViewById(R.id.syllabusBrowsePdf);
        showpdf = view.findViewById(R.id.syllabusShowPdf);
        cancelpdf = view.findViewById(R.id.syllabusCancel);
        syllabusTitle = view.findViewById(R.id.syllabusTitle);
        syllabusDept = view.findViewById(R.id.syllabusDeptSpinner);
        syllabusSem = view.findViewById(R.id.syllabusSemSpinner);
        syllabusSubmit = view.findViewById(R.id.syllabusSubmit);

        browsepdf.setVisibility(View.VISIBLE);
        cancelpdf.setVisibility(View.GONE);
        showpdf.setVisibility(View.GONE);

        cancelpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpdf.setVisibility(View.GONE);
                cancelpdf.setVisibility(View.GONE);
                browsepdf.setVisibility(View.VISIBLE);
            }

        });


        browsepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity((Activity) getContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                Log.i("Kam","PDF liya");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select pdf files"),1);
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.dept, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        syllabusDept.setAdapter(adapter);

        ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sem, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        syllabusSem.setAdapter(semAdapter);



        syllabusSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField();
            }
        });

        return view;
    }

    private void checkField() {
        title = syllabusTitle.getText().toString();
        dept = syllabusDept.getSelectedItem().toString();
        sem = syllabusSem.getSelectedItem().toString();

        if(title.isEmpty()){
            syllabusTitle.setError("Empty");
            syllabusTitle.requestFocus();
        }else if(dept.equals("Select Department")){
            Toast.makeText(getContext(), "Please select a department", Toast.LENGTH_SHORT).show();
        }else if(sem.equals("Select Semester")){
            Toast.makeText(getContext(), "Please select a Semester", Toast.LENGTH_SHORT).show();
        }else {
            uploadPdf();
        }

    }

    private void uploadPdf() {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait");
        pd.setMessage("uploading..");


        StorageReference reference = storageReference.child("syllabus/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        SyllabusPdfModel obj = new SyllabusPdfModel(title,uri.toString(),dept,sem);

                        databaseReference.child(dept).child(databaseReference.push().getKey()).setValue(obj);
                        syllabusTitle.setText("");
                        browsepdf.setVisibility(View.VISIBLE);
                        cancelpdf.setVisibility(View.GONE);
                        showpdf.setVisibility(View.GONE);

                        pd.dismiss();

                        Toast.makeText(getContext(), title +" Uploaded", Toast.LENGTH_SHORT).show();



                    }
                })


                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK){
            filepath = data.getData();
                Log.i("Kam",filepath+"");
                showpdf.setVisibility(View.VISIBLE);
                cancelpdf.setVisibility(View.VISIBLE);
                browsepdf.setVisibility(View.GONE);
//
        }
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1) {
//            if (resultCode == getActivity().RESULT_OK) {
//                filepath = data.getData();
//                Log.i("Kam",filepath+"");
//                showpdf.setVisibility(View.VISIBLE);
//                cancelpdf.setVisibility(View.VISIBLE);
//                browsepdf.setVisibility(View.GONE);
//            }
//
//        }

//    }

}