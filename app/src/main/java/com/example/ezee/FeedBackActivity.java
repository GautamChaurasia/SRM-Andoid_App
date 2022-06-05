package com.example.ezee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class FeedBackActivity extends AppCompatActivity {

    EditText feedback_title,feedback_text;
    Button feedback_button;
    TextView textResult;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    Loading load;


    String semail = "gautamsir076@gmail.com";
    String spass = "Gautam@3011";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();


//        Log.i("Ay", emp);

        feedback_title = findViewById(R.id.feedback_title);
        feedback_button = findViewById(R.id.feedback_button);
        feedback_text = findViewById(R.id.feedback_text);
        textResult = findViewById(R.id.textResult);
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

                        load = new Loading(FeedBackActivity.this);

                        load.startLoading();

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
                            load.dismissDialog();
                            Log.i("Ay","Mail Bheja");

                            Toast.makeText(FeedBackActivity.this, "Mail Sent", Toast.LENGTH_SHORT).show();


//                    Transport.send(msg);


                        } catch (MessagingException e) {
                            Log.i("Ay",e+"");
                            e.printStackTrace();
                        }


                    }
                });


            }
        });



    }
}