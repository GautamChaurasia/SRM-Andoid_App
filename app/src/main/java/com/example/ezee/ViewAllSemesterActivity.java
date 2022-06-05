package com.example.ezee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ViewAllSemesterActivity extends AppCompatActivity implements View.OnClickListener {

    CardView sem3,sem4,sem5,sem6,sem7,sem8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_semester);

        sem3 = findViewById(R.id.sem3);
        sem4 = findViewById(R.id.sem4);
        sem5 = findViewById(R.id.sem5);
        sem6 = findViewById(R.id.sem6);
        sem7 = findViewById(R.id.sem7);
        sem8 = findViewById(R.id.sem8);

        sem3.setOnClickListener(this);
        sem4.setOnClickListener(this);
        sem5.setOnClickListener(this);
        sem6.setOnClickListener(this);
        sem7.setOnClickListener(this);
        sem8.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        Log.i("intent", String.valueOf(view.getId()));
        switch (view.getId()){
            case R.id.sem3:
                intent = new Intent(ViewAllSemesterActivity.this,AllSubjectsActivity.class);
//                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                intent.putExtra("wsem","Semester-III");
                break;
                
            case R.id.sem4:
                intent = new Intent(ViewAllSemesterActivity.this,AllSubjectsActivity.class);
//                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                intent.putExtra("wsem","Semester-IV");
                break;
            
            case R.id.sem5:
                intent = new Intent(ViewAllSemesterActivity.this,AllSubjectsActivity.class);
//                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                intent.putExtra("wsem","Semester-V");
                break;
            
            case R.id.sem6:
                intent = new Intent(ViewAllSemesterActivity.this,AllSubjectsActivity.class);
//                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                intent.putExtra("wsem","Semester-VI");
                break;
            
            case R.id.sem7:
                intent = new Intent(ViewAllSemesterActivity.this,AllSubjectsActivity.class);
//                Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                intent.putExtra("wsem","Semester-VII");
                break;
            
            case R.id.sem8:
                intent = new Intent(ViewAllSemesterActivity.this,AllSubjectsActivity.class);
//                Toast.makeText(this, "8", Toast.LENGTH_SHORT).show();
                intent.putExtra("wsem","Semester-VIII");
                break;
            default:
                break;
            
        }
        startActivity(intent);
    }
}