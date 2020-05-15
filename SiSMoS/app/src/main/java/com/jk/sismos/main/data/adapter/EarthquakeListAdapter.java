package com.jk.sismos.main.data.adapter;


import android.content.Context;
import android.icu.lang.UCharacter;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jk.sismos.R;
import com.jk.sismos.main.data.model.inpresList.Earthquake;

import java.util.List;
import java.util.Locale;

public class EarthquakeListAdapter extends ArrayAdapter<Earthquake> {
    Context context;
    Location lastKnownLocation;

    public EarthquakeListAdapter(Context context, List<Earthquake> items, Location lastKnownLocation) {
        super(context, 0, items);
        this.context = context;
        this.lastKnownLocation = lastKnownLocation;
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
        holder.resume.setText(rowItem.getMagnitude() + " grados - " + UCharacter.toTitleCase(Locale.US, rowItem.getProvince(), null, 0));
        holder.datetime.setText(rowItem.getDate() + " - " + rowItem.getTime());
        holder.placeReference.setText(rowItem.getPlaceReference());
        holder.depth.setText(rowItem.getDepth() + " de profundidad");

        if (!rowItem.getLongitude().isEmpty() && !rowItem.getLatitude().isEmpty() && lastKnownLocation != null) {
            Location locationA = new Location("Punto de destino");

            locationA.setLatitude(Double.parseDouble(rowItem.getLatitude()));
            locationA.setLongitude(Double.parseDouble(rowItem.getLongitude()));

            Location locationB = new Location("Punto local");

            locationB.setLatitude(lastKnownLocation.getLatitude());
            locationB.setLongitude(lastKnownLocation.getLongitude());

            float distance = locationA.distanceTo(locationB) / 1000;
            holder.distance.setText(String.format("%.2f", (distance)) + " km hasta el epicentro");
            holder.distance.setVisibility(View.VISIBLE);
        } else {
            holder.distance.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView resume;
        TextView datetime;
        TextView placeReference;
        TextView depth;
        TextView distance;

        private ViewHolder() {
        }
    }
}
