package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SubjectPdfActivity extends AppCompatActivity {

    String subjName,wsem;
    DatabaseReference reference,dbRef;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    String usersDept;
    List<SyllabusPdfModel> list1;
    SubjectsPdfAdapter adapter;
    RecyclerView qpsubpdf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_pdf);

        Intent intent = getIntent();
        subjName = intent.getStringExtra("subName");
        wsem = intent.getStringExtra("wsem");

        Toast.makeText(this, "Subj name mil gya --> " + subjName, Toast.LENGTH_SHORT).show();

        qpsubpdf = findViewById(R.id.qpsubpdf);
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    usersDept = documentSnapshot.getString("Dept");
                    Log.i("ayushhh","ayushhh");
                    Log.i("usersDept",usersDept);

                    if (usersDept == "COMP") {
                        usersDept = "Computer Engineering";
                    }
                }
                else Toast.makeText(SubjectPdfActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                showPdf(usersDept);
            }
        }, 5000);


    }

    private void showPdf(String usersDept) {
        Log.i("Department",usersDept);

        dbRef = reference.child("questionPapers").child(usersDept).child(wsem).child(subjName);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SyllabusPdfModel data = snapshot.getValue(SyllabusPdfModel.class);
                        System.out.println(data.getFileurl());
                        System.out.println(data.getFilename());
                        list1.add(data);
                    }
                    qpsubpdf.setHasFixedSize(true);

                    qpsubpdf.setLayoutManager(new LinearLayoutManager(SubjectPdfActivity.this));
                    adapter = new SubjectsPdfAdapter(list1);
                    qpsubpdf.setAdapter(adapter);
                } else {
                    Toast.makeText(SubjectPdfActivity.this, "No data exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectPdfActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}