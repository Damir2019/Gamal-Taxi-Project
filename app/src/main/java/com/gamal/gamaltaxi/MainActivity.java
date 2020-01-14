package com.gamal.gamaltaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText clientNumberET, departureET;
    private DatabaseReference mDatabase;
    private Calendar calendar = Calendar.getInstance();
    private TextView notWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendOrder;

        sendOrder = findViewById(R.id.make_order);
        clientNumberET = findViewById(R.id.client_number);
        departureET = findViewById(R.id.departure);
        mDatabase = FirebaseDatabase.getInstance().getReference("Gamal-Taxi-Orders");
        notWorking = findViewById(R.id.notWorking);

        activeLabelCheck();

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderClick();
            }
        });

    }

    private void onOrderClick() {
        if (dateAndTimeCheck()) {

            String clientNumber, departure, currentDate, currentTime;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            currentDate = simpleDateFormat.format(calendar.getTime());
            currentTime = simpleTimeFormat.format(calendar.getTime());


            clientNumber = clientNumberET.getText().toString();
            departure = departureET.getText().toString();

            if (!clientNumber.trim().isEmpty() && !departure.trim().isEmpty()) {

                Order order = new Order(clientNumber, departure, currentDate, currentTime);
                sendCall(order);

                Intent intent = new Intent(getApplicationContext(), OrderAccepted.class);
                startActivity(intent);

                clientNumberET.setText("");
                departureET.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "חייב למלא את כל השדות הרלוונטיים", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "אנחנו לא עובדים ברגע זה, נא לבדוק את שעות הפעילות", Toast.LENGTH_LONG).show();
        }
    }

    private void sendCall(Order order) {
        mDatabase.child("History").push().setValue(order);
        mDatabase.child("Order").push().setValue(order);
    }

    private void activeLabelCheck() {
        // will show a message "not working now" only by the condition of the work day.
        if (dateAndTimeCheck()) {
            notWorking.setVisibility(View.INVISIBLE);
        } else {
            notWorking.setVisibility(View.VISIBLE);
        }
    }

    private boolean dateAndTimeCheck() {
        int calendarDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hourInDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        // work days 1 - 5 between 6 to 22 hours
        // in day 6 between 7 to 14 hours
        // day 7 is not working
        // will return true only if these conditions apply

        System.out.println(calendarDay + " " + hourInDay);
        if (calendarDay >= 1 && calendarDay < 6){
            return (hourInDay >= 6 && hourInDay < 22);
        } else if (calendarDay == 6) {
            return (hourInDay >= 7 && hourInDay < 14);
        } else return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // will update the message every time when getting back to main activity
        if (dateAndTimeCheck()) {
            notWorking.setVisibility(View.INVISIBLE);
        } else {
            notWorking.setVisibility(View.VISIBLE);
        }
    }
}
