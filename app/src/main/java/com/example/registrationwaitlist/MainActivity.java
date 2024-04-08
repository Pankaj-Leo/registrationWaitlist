package com.example.registrationwaitlist;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentsAdapter adapter;
    private List<Student> studentList = new ArrayList<>();
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView); //views stacked using recycler
        FloatingActionButton fabAddStudent = findViewById(R.id.fab_add_student); //Floating Button

        studentList = db.getAllStudents(); // Fetch the list of students from the database

        adapter = new StudentsAdapter(this, studentList, student -> {
            // Intent to start EditStudentActivity with the student's ID
            Intent editIntent = new Intent(MainActivity.this, EditStudentActivity.class);
            editIntent.putExtra("STUDENT_ID", student.getId());
            startActivity(editIntent);
        }, student -> {
            // Delete action
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        db.deleteStudent(student.getId()); // Delete the student
                        refreshStudentList(); // Refresh the list
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddStudent.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStudentList(); // Refresh the student list every time MainActivity resumes
    }

    private void refreshStudentList() {
        studentList.clear();
        studentList.addAll(db.getAllStudents());
        adapter.notifyDataSetChanged();
    }
}
