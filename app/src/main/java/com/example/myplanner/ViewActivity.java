package com.example.myplanner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ArrayList<Planner> plannerList = XMLOperator.parseXml(ViewActivity.this);
        printPlanner(plannerList);
    }

    private void printPlanner(ArrayList<Planner> plannerList) {
        recyclerView = findViewById(R.id.recyclerView);

        PlannerAdapter plannerAdapter = new PlannerAdapter(this, plannerList);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setAdapter(plannerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}