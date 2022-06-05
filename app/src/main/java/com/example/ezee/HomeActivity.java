package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Document;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_timetable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        LinearLayout header = (LinearLayout) navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        navigationView.setItemTextAppearance(R.style.Menu_text_style);
        navigationView.setCheckedItem(R.id.nav_timetable);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("fname") + " " + documentSnapshot.getString("lname");
                    if (name.isEmpty())
                        name = "N.A.";
                    TextView user = header.findViewById(R.id.head_user);
                    user.setText(name);
                }
                else Toast.makeText(HomeActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menu_item) {
        switch (menu_item.getItemId()) {
            case R.id.nav_timetable:
                startActivity(new Intent(HomeActivity.this, TtActivity.class));
                break;
            case R.id.nav_practice:
                startActivity(new Intent(HomeActivity.this, ViewAllSemesterActivity.class));
                Toast.makeText(this, "Practice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_syllabus:
                startActivity(new Intent(HomeActivity.this,ViewSyllabusActivity.class));
                break;
            case R.id.nav_notification:
                startActivity(new Intent(HomeActivity.this, NotificationHistory.class));
                break;
            case R.id.nav_feedback:
                startActivity(new Intent(HomeActivity.this,FeedBackActivity.class));
                break;
            case R.id.nav_logout:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("students");
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}