package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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

import java.net.URLEncoder;

public class TtActivity extends AppCompatActivity {

    DatabaseReference reference,dbRef;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    WebView spdfWebView;
    Loading load;
    String year="",dept="",div="",res="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();
        spdfWebView = findViewById(R.id.spdfWebView);
        load = new Loading(TtActivity.this);

        load.startLoading();

//        spdfWebView.setBackgroundColor(1);



        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    dept = documentSnapshot.getString("Dept");
                    div = documentSnapshot.getString("Div");
                    year = documentSnapshot.getString("Year");



//                    Log.i("ayushhh","ayushhh");
                    Log.i("Check", dept);
                    Log.i("Check", div);


//                    pd.show();

//                    pd.cancel();


                    Log.i("Check", year);
                    res = year+"_"+dept+"_"+div;

                    showPdf(res);

                }
                else
                    Toast.makeText(TtActivity.this, "User not found", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPdf(String res) {



        Log.i("Andar",res+"");
        dbRef = reference.child("timetable").child(res+"");
        Log.i("Andar",dbRef+"");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    TtModel data = snapshot.getValue(TtModel.class);
                    Log.i("Bhetla",data.getFileurl()+"");
                    ProgressDialog pd = new ProgressDialog(TtActivity.this);

                    pd.setMessage("Opening...!!");
//                    pd.show();

//                    pd.cancel();
                    load.dismissDialog();
                    spdfWebView.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            pd.show();
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            pd.dismiss();
                        }
                    });

                    spdfWebView.getSettings().setJavaScriptEnabled(true);
                    String url="";

                    try{
                        Log.i("Bhetla","In try");
                        url = URLEncoder.encode(data.getFileurl()+"","UTF-8");
                    }catch (Exception e){

                    }


                    spdfWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+ url);




                }else{
                    Toast.makeText(TtActivity.this, "No File Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}