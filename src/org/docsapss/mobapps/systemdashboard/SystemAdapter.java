package org.docsapss.mobapps.systemdashboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.ContentValues;

/**
 * @class SystemAdapter
 * @brief Handles parsing returned JSON responses, writing to the DB and reading from the DB and building the view.
 */
public class SystemAdapter extends BaseAdapter{

	private final Context mContext;
	private SQLiteDatabase database;
	final private static String DB_NAME = "dashboard_db";
	final private static Integer DB_VERSION = 1;
	List<SystemRecord> recordList=new ArrayList<SystemRecord>();

	final static String _ID = "_id";
	final private String[] mColumns={SystemRecord.ID, SystemRecord.NAME, SystemRecord.STATUS, SystemRecord.LAST_INCIDENT_DATE};
	final private static String CREATE_TABLE = "CREATE TABLE " + SystemRecord.TABLE_NAME +  " (" 
					+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ SystemRecord.ID + " TEXT NOT NULL,"
					+ SystemRecord.NAME + " TEXT NOT NULL,"
					+ SystemRecord.STATUS + " TEXT NOT NULL,"
					+ SystemRecord.LAST_INCIDENT_DATE + " TEXT NULL);";
	
	public SystemAdapter(Context context) {
		mContext = context;
		//DatabaseHelper dbHelper=new DatabaseHelper();
		//database = dbHelper.getWritableDatabase();
	}
	
	public void parseJsonString(String jsonMessage) throws JSONException {
		JSONObject jObject = new JSONObject(jsonMessage);
		JSONArray jArray = jObject.getJSONArray(SystemRecord.TABLE_NAME);
		recordList.clear();
		
		for (int i=0; i < jArray.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArray.getJSONObject(i);
		        //String id = oneObject.getString(SystemRecord.ID);
		        String name = oneObject.getString("name");
		        String status = oneObject.getString("status");
		        //String last_incident_date = oneObject.getString(SystemRecord.LAST_INCIDENT_DATE);
		        recordList.add(new SystemRecord(null, name, status, null));
		    } catch (JSONException e) {}
		}
	}
	
	private void getRecords() {
		recordList.clear();
		SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
		qb.setTables(SystemRecord.TABLE_NAME);
		Cursor mCursor=qb.query(database, mColumns, null, null, null, null, SystemRecord.NAME);
		if (mCursor.moveToFirst()) {
			do {	
				recordList.add(new SystemRecord(mCursor.getString(mCursor.getColumnIndex(SystemRecord.ID))
												,mCursor.getString(mCursor.getColumnIndex(SystemRecord.NAME))
												,mCursor.getString(mCursor.getColumnIndex(SystemRecord.STATUS))
												,mCursor.getString(mCursor.getColumnIndex(SystemRecord.LAST_INCIDENT_DATE))));	
			} while (mCursor.moveToNext());
		}
	}
	
	public void addRecord(ContentValues values) {
		long ROWID=database.insert(SystemRecord.TABLE_NAME,null,values);
		getRecords();
		notifyDataSetChanged();
		if (!(ROWID>0)) {
			throw new SQLException("Failed to add record into " + SystemRecord.NAME);
		}
	}

	@Override
	public int getCount() {
		if (recordList.isEmpty()) {
			return 0;
		} else { 
			return recordList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return recordList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SystemRecord record=recordList.get(position);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listView=(LinearLayout) inflater.inflate(R.layout.system_view, null);
		TextView systemName=(TextView) listView.findViewById(R.id.system_name);
		ImageView systemImage=(ImageView) listView.findViewById(R.id.system_image);
		systemName.setText(record.getName());
		
		if ("green".equals(record.getStatus())) {
			systemImage.setImageResource(R.drawable.green);
		} else if ("amber".equals(record.getStatus())) {
			systemImage.setImageResource(R.drawable.amber);
		} else {
			systemImage.setImageResource(R.drawable.red);
		}
		return listView;
	}
	
	/**
	 * @class DatabaseHelper
	 * @brief Creates a new database or returns an existing database.
	 */
	public class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper() {
			super(mContext, DB_NAME, null, DB_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SystemRecord.TABLE_NAME);
		}	
	}
}