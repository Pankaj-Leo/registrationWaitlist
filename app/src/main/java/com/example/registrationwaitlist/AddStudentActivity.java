package com.example.registrationwaitlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {
    private EditText editTextName, editTextCourseId;
    private Spinner spinnerYearOfStudy;
    private Button btnSave;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        db = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextStudentName);
        editTextCourseId = findViewById(R.id.editTextCourseId);
        btnSave = findViewById(R.id.buttonSaveStudent);
        spinnerYearOfStudy = findViewById(R.id.spinnerYearOfStudy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years_of_study, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYearOfStudy.setAdapter(adapter);//drop down of year
        btnSave.setOnClickListener(view -> saveStudent());
    }
    private void saveStudent() {
        String name = editTextName.getText().toString().trim();
        String courseId = editTextCourseId.getText().toString().trim();
        int yearOfStudy = spinnerYearOfStudy.getSelectedItemPosition() + 1; // Plus one since array positions start at 0
        long studentId = db.addStudent(name, courseId, yearOfStudy); // Adjust your DatabaseHelper method to accept yearOfStudy and calculate priority inside it
        if (studentId == -1) {
            Toast.makeText(this, "Error saving student", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Student saved successfully", Toast.LENGTH_LONG).show();
            finish(); // Finish activity and return to previous screen
        }
    }
}
