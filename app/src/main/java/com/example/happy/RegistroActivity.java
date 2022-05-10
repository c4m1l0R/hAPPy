package com.example.happy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

public class RegistroActivity extends AppCompatActivity {

    //botónes
    Button cancelar;


    //editTexts
    EditText editTextBirthDate;


    //código
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        cancelar = (Button) findViewById(R.id.cancel);

        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paginaLogging = new Intent(RegistroActivity.this, LogActivity.class);
                startActivity(paginaLogging);
            }
        });

        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();
            }
        });
    }

    private  void showDatePickerDialog(){

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                editTextBirthDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");

    }
}