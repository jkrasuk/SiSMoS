package com.jk.sismos.main.data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jk.sismos.R;
import com.jk.sismos.main.data.model.Earthquake;

import java.util.List;

public class EarthquakeListAdapter extends ArrayAdapter<Earthquake> {
    Context context;

    private class ViewHolder {
        TextView resume;
        TextView datetime;
        TextView placeReference;
        TextView depth;
        TextView distance;

        private ViewHolder() {
        }
    }

    public EarthquakeListAdapter(Context context, List<Earthquake> items) {
        super(context, 0, items);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Earthquake rowItem = (Earthquake) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.earthquake_item, null);
            holder = new ViewHolder();
            holder.resume = (TextView) convertView.findViewById(R.id.resume);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
            holder.placeReference = (TextView) convertView.findViewById(R.id.placeReference);
            holder.depth = (TextView) convertView.findViewById(R.id.depth);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.resume.setText(rowItem.getTitle());
        holder.depth.setText(rowItem.getEstado());
        return convertView;
    }
}
