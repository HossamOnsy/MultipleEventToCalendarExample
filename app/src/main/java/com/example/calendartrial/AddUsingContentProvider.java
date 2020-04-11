package com.example.calendartrial;

import java.util.Calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class AddUsingContentProvider {
	CalendarContract contract;
	String TAG = "TAGSS";

	final static String[] CALENDAR_QUERY_COLUMNS = {
			CalendarContract.Calendars._ID,
			CalendarContract.Calendars.NAME,
			CalendarContract.Calendars.VISIBLE,
			CalendarContract.Calendars.OWNER_ACCOUNT
	};

	public void addEvent(Context ctx, String title, Calendar start, Calendar end) {
		Log.d(TAG, "AddUsingContentProvider.addEvent()");


		// Get list of Calendars (after Jim Blackler, http://jimblackler.net/blog/?p=151)
		ContentResolver contentResolver = ctx.getContentResolver();
		Log.d(TAG, "URI = " + CalendarContract.Calendars.CONTENT_URI);
		if (checkSelfPermission(ctx,Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    Activity#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
		final Cursor cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
				CALENDAR_QUERY_COLUMNS, null, null, null);
        Log.d(TAG, "cursor = " + cursor);
        while (cursor.moveToNext()) {
        	final String _id = cursor.getString(0);
        	final String displayName = cursor.getString(1);
        	final Boolean selected = !cursor.getString(2).equals("0");
        	final String accountName = cursor.getString(3);
        	Log.d(TAG, "Found calendar " + accountName);
//        	calendarList.append(
//        			"Calendar: Id: " + _id + " Display Name: " + displayName + " Selected: " + selected + " Name " + accountName);
        }
        
        ContentValues calEvent = new ContentValues();
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, title);
        calEvent.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        calEvent.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "Canada/Eastern");
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, calEvent);
        
        // The returned Uri contains the content-retriever URI for the newly-inserted event, including its id
        int id = Integer.parseInt(uri.getLastPathSegment());
        Toast.makeText(ctx, "Created Calendar Event " + id, Toast.LENGTH_SHORT).show();
    }

}