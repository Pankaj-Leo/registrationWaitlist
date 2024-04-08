package com.example.registrationwaitlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WaitingList2.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STUDENTS = "Students";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COURSE_ID = "courseId";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_YEAR_OF_STUDY = "yearOfStudy"; // Assuming you've added this column

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," +
                KEY_COURSE_ID + " TEXT," +
                KEY_YEAR_OF_STUDY + " INTEGER," +
                KEY_PRIORITY + " INTEGER" +
                ")";
        db.execSQL(CREATE_STUDENTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }
    public long addStudent(String name, String courseId, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_COURSE_ID, courseId);
        values.put(KEY_PRIORITY, priority);

        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();

        Log.d("DatabaseHelper", "Added student with ID: " + id);
        return id;
    }
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int idIndex = cursor.getColumnIndex(KEY_ID);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);
        int courseIdIndex = cursor.getColumnIndex(KEY_COURSE_ID);
        int priorityIndex = cursor.getColumnIndex(KEY_PRIORITY);
        // Check if any of the column indexes are invalid
        if (idIndex == -1 || nameIndex == -1 || courseIdIndex == -1 || priorityIndex == -1) {
            Log.e("DatabaseHelper", "One or more column names are invalid. Cannot retrieve student data.");
            cursor.close();
            return studentList; // Return the empty list or handle this scenario as appropriate
        }
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                        cursor.getLong(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(courseIdIndex),
                        cursor.getInt(priorityIndex));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }
    public void updateStudent(long id, String name, String courseId, int yearOfStudy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_COURSE_ID, courseId);
        values.put(KEY_YEAR_OF_STUDY, yearOfStudy);
        // Assume you calculate priority based on yearOfStudy here
        int priority = calculatePriority(yearOfStudy);
        values.put(KEY_PRIORITY, priority);

        db.update(TABLE_STUDENTS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteStudent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_STUDENTS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
        Log.d("DatabaseHelper", "Deleted student rows affected: " + rowsDeleted);
    }

    private int calculatePriority(int yearOfStudy) {
        switch (yearOfStudy) {
            case 5: // Graduate
                return 1;
            case 4: // 4th Year
                return 2;
            case 3: // 3rd Year
                return 3;
            case 2: // 2nd Year
                return 4;
            case 1: // 1st Year
                return 5;
            default:
                return -1; // Invalid year of study
        }
    }


    public Student getStudent(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, new String[]{"id", "name", "courseId", "priority"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null, null);

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int courseIdIndex = cursor.getColumnIndex(KEY_COURSE_ID);
            int priorityIndex = cursor.getColumnIndex(KEY_PRIORITY);

            if (idIndex != -1 && nameIndex != -1 && courseIdIndex != -1 && priorityIndex != -1) {
                student = new Student(
                        cursor.getLong(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(courseIdIndex),
                        cursor.getInt(priorityIndex));
            } else {
                Log.e("DatabaseHelper", "One or more columns could not be found in the student table.");
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return student;
    }
}

