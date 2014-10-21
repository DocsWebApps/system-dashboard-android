package org.docsapps.mobapps.systemdashboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @class SystemAdapter
 * @brief Handles parsing returned JSON responses, and building the Recordset behind SystemListActivity
 */
public class SystemListAdapter extends BaseAdapter{

	private final Context mContext;
	List<SystemListRecord> recordList=new ArrayList<SystemListRecord>();
	
	public SystemListAdapter(Context context) {
		mContext = context;
	}
	
	public void parseJsonString(String jsonMessage) throws JSONException {
		JSONObject jObject = new JSONObject(jsonMessage);
		JSONArray jArray = jObject.getJSONArray(SystemListRecord.TABLE_NAME);
		recordList.clear();
		
		for (int i=0; i < jArray.length(); i++)
		{
		    try {
		        JSONObject jsonObject = jArray.getJSONObject(i);
                String id = jsonObject.getString(SystemListRecord.ID);
		        String name = jsonObject.getString(SystemListRecord.NAME);
		        String status = jsonObject.getString(SystemListRecord.STATUS);
                String color = jsonObject.getString(SystemListRecord.COLOR);
                String message = jsonObject.getString(SystemListRecord.MESSAGE);
		        recordList.add(new SystemListRecord(id, name, status, color, message));
		    } catch (JSONException e) {
                e.getStackTrace();
            }
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
		final SystemListRecord record=recordList.get(position);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listView=(LinearLayout) inflater.inflate(R.layout.system_view, null);
		TextView systemName=(TextView) listView.findViewById(R.id.system_name);
        TextView systemMessage=(TextView) listView.findViewById(R.id.system_message);
		ImageView systemImage=(ImageView) listView.findViewById(R.id.system_image);
		systemName.setText(record.getName());
        systemMessage.setText(record.getMessage());

        if ("system-red".equals(record.getColor())) {
            systemMessage.setTextColor(Color.rgb(0xFF,0x00,0x00));
        }
		
		if ("green".equals(record.getStatus())) {
			systemImage.setImageResource(R.drawable.green);
		} else if ("amber".equals(record.getStatus())) {
			systemImage.setImageResource(R.drawable.amber);
		} else {
			systemImage.setImageResource(R.drawable.red);
		}
		return listView;
	}
}