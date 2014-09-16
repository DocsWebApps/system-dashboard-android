package org.docsapss.mobapps.systemdashboard;

import android.content.Intent;
import android.content.ContentValues;

/**
 * @class SystemRecord
 * @brief Handles the records from the systems table in the SQLite database.
 */
public class SystemRecord {
	public final static String TABLE_NAME="systems";
	public final static String ID = "ID";
	public final static String NAME = "NAME";
	public final static String STATUS="STATUS";
	public final static String LAST_INCIDENT_DATE="LAST_INCIDENT_DATE";
	
	private String id;
	private String name;
	private String status;
	private String last_incident_date;
	
	public SystemRecord(Intent intent) {
		this.id=intent.getStringExtra("id");
		this.name=intent.getStringExtra("name");
		this.status=intent.getStringExtra("status");
		this.last_incident_date=intent.getStringExtra("last_incident_date");
	}
	
	public SystemRecord(String id, String name, String status, String last_incident_date) {
		this.id=id;
		this.name=name;
		this.status=status;
		this.last_incident_date=last_incident_date;
	}
	
	public ContentValues getContentValues() {
		ContentValues values=new ContentValues();
		values.put(SystemRecord.ID, this.id);
		values.put(SystemRecord.NAME, this.name);
		values.put(SystemRecord.STATUS, this.status);
		values.put(SystemRecord.LAST_INCIDENT_DATE, this.last_incident_date);
		return values;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getLastIncidentDate() {
		return this.last_incident_date;
	}
}