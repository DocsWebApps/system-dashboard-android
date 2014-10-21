package org.docsapps.mobapps.systemdashboard;

/**
 * @class SystemRecord
 * @brief Handles the records for the SystemListActivity
 */
public class SystemListRecord {
	public final static String TABLE_NAME="systems";
	public final static String ID = "id";
    public final static String NAME = "name";
	public final static String STATUS="status";
    public final static String COLOR = "color";
    public final static String MESSAGE = "message";

    private final String id;
	private final String name;
	private final String status;
    private final String color;
    private final String message;
	
	public SystemListRecord(String id, String name, String status, String color, String message) {
        this.id=id;
		this.name=name;
		this.status=status;
        this.color=color;
        this.message=message;
	}
	
	public String getId() {
        return this.id;
	}

    public String getName() {
        return this.name;
	}
	
	public String getStatus() {
		return this.status;
	}

	public String getColor() {
		return this.color;
	}

	public String getMessage() {
        return this.message;
	}
}