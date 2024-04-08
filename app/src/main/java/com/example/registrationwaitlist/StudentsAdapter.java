package com.example.registrationwaitlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {

    private Context context;
    private List<Student> studentList;
    private final OnItemClickListener itemClickListener;
    private final OnItemDeleteListener itemDeleteListener;
    public StudentsAdapter(Context context, List<Student> studentList, OnItemClickListener itemClickListener, OnItemDeleteListener itemDeleteListener) {
        this.context = context;
        this.studentList = studentList;
        this.itemClickListener = itemClickListener;
        this.itemDeleteListener = itemDeleteListener;
    }
    @NonNull
    @Override //recycler view
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position); //load data and buttons
        holder.textViewName.setText(student.getName());
        holder.textViewCourseId.setText(student.getCourseId());
        holder.textViewPriority.setText(String.valueOf(student.getPriority()));

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(student));
        holder.deleteButton.setOnClickListener(v -> itemDeleteListener.onItemDelete(student));
    }
    @Override
    public int getItemCount() {
        return studentList.size();
    }
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCourseId, textViewPriority;
        Button deleteButton;
        public StudentViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCourseId = itemView.findViewById(R.id.textViewCourseId);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Student student);
    }
    public interface OnItemDeleteListener {
        void onItemDelete(Student student);
    }
}