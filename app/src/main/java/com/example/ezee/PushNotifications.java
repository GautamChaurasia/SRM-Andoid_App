package com.example.ezee;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PushNotifications extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText vtitle;
    EditText vmsg;
    Spinner topics;
    Button push;

    String selectedTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notifications);

        vtitle = findViewById(R.id.atitle);
        vmsg = findViewById(R.id.amsg);
        topics = findViewById(R.id.topics);
        push = findViewById(R.id.apush);

        topics.setOnItemSelectedListener(this);

        List<String> subTopics = new ArrayList<String>();
        subTopics.add("Admins");
        subTopics.add("Students");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subTopics);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        topics.setAdapter(dataAdapter);
        topics.setSelection(dataAdapter.getPosition("Students"));

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = vtitle.getText().toString();
                String msg = vmsg.getText().toString();

                if(!title.isEmpty() && !msg.isEmpty() && !selectedTopic.isEmpty()){
                    FCMSend.pushNotification(PushNotifications.this, "/topics/"+selectedTopic.toLowerCase(), title, msg);
                    Toast.makeText(PushNotifications.this, "Notification sent", Toast.LENGTH_SHORT).show();
                    vtitle.setText("");
                    vmsg.setText("");
                }
                else{
                    Toast.makeText(PushNotifications.this, "All inputs required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        selectedTopic = parent.getItemAtPosition(position).toString();
        TextView sp = (TextView) parent.getChildAt(0);
        sp.setTextColor(Color.WHITE);
        sp.setTextSize(18);
        Toast.makeText(parent.getContext(), selectedTopic+" will be notified", Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}