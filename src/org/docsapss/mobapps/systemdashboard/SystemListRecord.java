package org.docsapss.mobapps.systemdashboard;

/**
 * @class SystemRecord
 * @brief Handles the records for the SystemListActivity
 */
public class SystemListRecord {
	public final static String TABLE_NAME="systems";
	public final static String NAME = "name";
	public final static String STATUS="status";
	
	private final String name;
	private final String status;
	
	public SystemListRecord(String name, String status) {
		this.name=name;
		this.status=status;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getStatus() {
		return this.status;
	}
}