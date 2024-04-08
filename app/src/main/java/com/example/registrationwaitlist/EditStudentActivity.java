package com.example.registrationwaitlist;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditStudentActivity extends AppCompatActivity {
    private EditText editTextName, editTextCourseId;
    private Spinner spinnerYearOfStudy;
    private Button btnSave;
    private DatabaseHelper db;
    private long studentId; // Use studentId for updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        db = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextStudentName);
        editTextCourseId = findViewById(R.id.editTextCourseId);
        spinnerYearOfStudy = findViewById(R.id.spinnerYearOfStudy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years_of_study, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYearOfStudy.setAdapter(adapter);
        btnSave = findViewById(R.id.buttonSaveStudent);
        // Retrieve the student ID from the intent
        studentId = getIntent().getLongExtra("STUDENT_ID", -1);
        // Populate fields if a valid student ID was passed
        if(studentId != -1) {
            Student student = db.getStudent(studentId);
            if(student != null) {
                editTextName.setText(student.getName());
                editTextCourseId.setText(student.getCourseId());
                // Set the spinner selection based on the student's year of study
                spinnerYearOfStudy.setSelection(student.getYearOfStudy() - 1);
            } else {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
                finish(); // No valid student found
            }
        } else {
            Toast.makeText(this, "Invalid student ID", Toast.LENGTH_SHORT).show();
            finish(); // No valid ID passed
        }

        btnSave.setOnClickListener(view -> updateStudent());
    }
    private void updateStudent() {
        String name = editTextName.getText().toString().trim();
        String courseId = editTextCourseId.getText().toString().trim();
        int yearOfStudy = spinnerYearOfStudy.getSelectedItemPosition() + 1;

        // Call db to update student
        db.updateStudent(studentId, name, courseId, yearOfStudy);
        Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
