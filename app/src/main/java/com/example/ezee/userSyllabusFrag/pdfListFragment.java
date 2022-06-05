package com.example.ezee.userSyllabusFrag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezee.HomeActivity;
import com.example.ezee.R;
import com.example.ezee.SyllabusPdfAdapter;
import com.example.ezee.SyllabusPdfModel;
import com.example.ezee.syllabusFrag.WebFragment;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pdfListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pdfListFragment extends Fragment {

    DatabaseReference reference,dbRef;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    String usersDept;
    List<SyllabusPdfModel> list1;
    SyllabusPdfAdapter adapter;
    RecyclerView fileListRecView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public pdfListFragment() {
        // Required empty public constructor
    }

    public static pdfListFragment newInstance(String param1, String param2) {
        pdfListFragment fragment = new pdfListFragment();
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
        View view = inflater.inflate(R.layout.fragment_pdf_list, container, false);

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        fileListRecView = view.findViewById(R.id.fileListRecView);


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
                else Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                showPdf(usersDept);
            }
        }, 5000);

        return view;
    }

    private void showPdf(String usersDept) {
        Log.i("Department",usersDept);
        dbRef = reference.child("syllabus").child(usersDept);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SyllabusPdfModel data = snapshot.getValue(SyllabusPdfModel.class);
                        list1.add(data);
                    }
                    fileListRecView.setHasFixedSize(true);

                    fileListRecView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new SyllabusPdfAdapter(list1);
                    fileListRecView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No data exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}