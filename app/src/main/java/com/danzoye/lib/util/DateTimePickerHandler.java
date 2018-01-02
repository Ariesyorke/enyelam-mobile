package com.danzoye.lib.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Ramdhany Dwi Nugroho on Jul 2015.
 */
public class DateTimePickerHandler {
    private Context context;
    private DateTimePickerHandlerListener listener;
    private Date initialDate;
    private int year;
    private int month;
    private int day;
    private boolean onSetTimeInvoked = false; // patch untuk bug nya android, onSetTime kepanggil dua kali.
    private boolean onSetDateInvoked = false; // patch untuk bug nya android, onSetDate kepanggil dua kali.

    public DateTimePickerHandler(Context context, DateTimePickerHandlerListener listener, Date date) {
        this.context = context;
        this.listener = listener;

        if (date == null) {
            initialDate = new Date();
        } else {
            initialDate = date;
        }
    }

//    public void show() {
//        showDatePickerDialog(initialDate);
//    }

//    private void showDatePickerDialog(Date date) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//
//        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                if (!onSetDateInvoked) {
//                    onSetDateInvoked = true;
//                    DateTimePickerHandler.this.year = year;
//                    DateTimePickerHandler.this.month = monthOfYear;
//                    DateTimePickerHandler.this.day = dayOfMonth;
//                    showTimePickerDialog(initialDate);
//                }
//            }
//        },
//                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//
//        DatePicker datePicker = dialog.getDatePicker();
//        datePicker.setMinDate(GMTHelper.getMinimumBirthdate());
//
//        dialog.setCancelable(true);
//        dialog.show();
//    }

    private void showTimePickerDialog(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (!onSetTimeInvoked) {
                    onSetTimeInvoked = true;
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

                    Date date = cal.getTime();
                    listener.onDateTimeSet(date);
                }
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        dialog.setCancelable(true);
        dialog.show();
    }

    public interface DateTimePickerHandlerListener {
        void onDateTimeSet(Date date);
    }
}
