package com.example.ezee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//androidx.fragment.app.Fragment fragment

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FeedBackFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_feedback,container,false);
        EditText feedback_title,feedback_text;
        Button feedback_button;
        TextView textResult;
        FirebaseAuth mAuth;
        final Loading[] loading = new Loading[1];
        FirebaseFirestore fst;

        String semail = "gautamsir076@gmail.com";
        String spass = "Gautam@3011";

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();


//        Log.i("Ay", emp);

        feedback_title = v.findViewById(R.id.feedback_title);
        feedback_button = v.findViewById(R.id.feedback_button);
        feedback_text = v.findViewById(R.id.feedback_text);
        textResult = v.findViewById(R.id.textResult);
        feedback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String feed_title = feedback_title.getText().toString();
                String feed_text = feedback_text.getText().toString();

                DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
                data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String emp = documentSnapshot.getString("email");


                        Log.i("Ay", emp);

                        loading[0] = new Loading(getActivity());

                        loading[0].startLoading();

                        Properties p = System.getProperties();
                        p.put("mail.smtp.host", "smtp.gmail.com");
                        p.put("mail.smtp.starttls.enable", "true");
                        p.put("mail.smtp.port", "587");
                        p.put("mail.smtp.auth", "true");

                        Log.i("Ay","Mil start");

                        Session session = Session.getInstance(p, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(semail,spass);
                            }
                        });

                        Session session2 = Session.getInstance(p, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(semail,spass);
                            }
                        });

                        try {
                            MimeMessage msg = new MimeMessage(session);
                            msg.setSubject("Ezee App");
                            msg.setText("Thankyou for yr feedback , Will work on it ...");
                            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(emp));

                            Transport.send(msg);

                            MimeMessage msg2 = new MimeMessage(session2);
                            msg2.setSubject(feed_title);
                            msg2.setText(feed_text);
                            msg2.addRecipient(Message.RecipientType.TO,new InternetAddress("ayushahuja200@gmail.com"));

                            Transport.send(msg2);
                            loading[0].dismissDialog();
                            Log.i("Ay","Mail Bheja");
                            textResult.setText("Mail Sent ..");

                            Intent a =new Intent(getActivity(),HomeActivity.class);
                            startActivity(a);

//                    Transport.send(msg);


                        } catch (MessagingException e) {
                            Log.i("Ay",e+"");
                            e.printStackTrace();
                        }


                    }
                });










            }
                });



        return  v;


    }



}


