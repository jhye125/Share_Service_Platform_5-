package com.example.share.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.OrientationHelper;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.example.share.R;

import java.util.Calendar;
import java.util.List;


public class SelectDateActivity extends AppCompatActivity {


    private CalendarView calendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initViews();
    }

    private void initViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.setSelectionType(SelectionType.RANGE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.clear_selections:
                clearSelectionsMenuClick();
                return true;

            case R.id.send_selections:
                List<Calendar> days = calendarView.getSelectedDates();
                Calendar start_date = days.get(0);
                Calendar end_date = days.get(days.size()-1);
                int sday = start_date.get(Calendar.DAY_OF_MONTH);
                int smonth = start_date.get(Calendar.MONTH) + 1;
                int syear = start_date.get(Calendar.YEAR);
                int eday = end_date.get(Calendar.DAY_OF_MONTH);
                int emonth = end_date.get(Calendar.MONTH) + 1;
                int eyear = end_date.get(Calendar.YEAR);

                String send_s = syear+"-"+smonth+"-"+sday;
                String send_e = eyear+"-"+emonth+"-"+eday;
                String send = send_s+"~"+send_e;
                Intent intent = new Intent();
                intent.putExtra("date", send);
                intent.putExtra("startdate",send_s);
                intent.putExtra("enddate",send_e);
                setResult(RESULT_OK, intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void clearSelectionsMenuClick() {
        calendarView.clearSelections();

    }




}
