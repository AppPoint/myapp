package com.example.myapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by patrick on 24/05/14.
 */
public class ReservationActivity extends Activity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private EditText name;
    private EditText email;
    private EditText date;
    private EditText time;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

        final int id = getIntent().getIntExtra("idRestaurant", 0);
        name = (EditText) findViewById(R.id.editTextName);
        email = (EditText) findViewById(R.id.editTextEmail);
        date = (EditText) findViewById(R.id.editTextDate);
        time = (EditText) findViewById(R.id.editTextTime);
        send = (Button) findViewById(R.id.sendButton);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    // Create a new instance of DatePickerDialog and return it
                    DatePickerDialog datePicker = new DatePickerDialog(ReservationActivity.this, ReservationActivity.this, year, month, day);
                    datePicker.show();
                }
            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    // Create a new instance of TimePickerDialog and return it
                    TimePickerDialog timePicker = new TimePickerDialog(ReservationActivity.this, ReservationActivity.this, hour, minute,
                            true);
                    timePicker.show();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                String emailStr =email.getText().toString();
                String dateStr = date.getText().toString();
                String timeStr = time.getText().toString();
                String urlStr = "http://192.168.56.1:8080/axis2/services/controler/createReservation?idRestaurant=" + id + "&" +
                   "name=" + nameStr + "&email=" + emailStr + "&date=" + dateStr+ "&time=" + timeStr + "&response=application/json";
                try {
                    String response = new Controler().execute(urlStr).get();
                    JSONObject json = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), json.getString("return"), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (minute < 10) {
            time.setText(hourOfDay + ":0" + minute);
        } else{
            time.setText(hourOfDay + ":" + minute);
        }
    }
}
