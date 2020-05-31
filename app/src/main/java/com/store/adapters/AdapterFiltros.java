package com.store.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable.*;
import android.widget.Filterable;
import android.widget.TextView;
import com.store.FilterApp;
import com.store.R;
import com.store.vo.DatosVo;
import java.util.ArrayList;

public class AdapterFiltros extends BaseExpandableListAdapter implements Filterable {
    private Context context;
    private ArrayList<FilterApp> filterApps;
    private AdapterDatos adapter;

    public AdapterFiltros(Context context, ArrayList<FilterApp> filterApps, AdapterDatos adapter) {
        this.context = context;
        this.filterApps = filterApps;
        this.adapter = adapter;
    }

    @Override
    public int getGroupCount() {
        return filterApps.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filterApps.get(groupPosition).filterTypes.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.filterApps.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filterApps.get(groupPosition).filterTypes.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)
                    this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandible_list_group, null);
        }
        FilterApp f = (FilterApp) getGroup(groupPosition);
        TextView groupName = convertView.findViewById(R.id.group_name);
        groupName.setText(f.filterName);
        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.colorWhite));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)
                    this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandible_list_item, null);
        }
        final String filterType = (String) getChild(groupPosition, childPosition);
        final EditText minimum = convertView.findViewById(R.id.edtxt_minimum);
        final EditText maximum = convertView.findViewById(R.id.edtxt_maximum);
        Button btnApplyFilter = convertView.findViewById(R.id.btn_apply_filter);
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min = Integer.valueOf(minimum.getText().toString());
                int max = Integer.valueOf(maximum.getText().toString());
                if (min < 0) {
                    minimum.setError("Solo valores positivos");
                } else if (min >= max || min > 5) {
                    minimum.setError("Mínimo excedido");
                } else if (max < min) {
                    maximum.setError("El valor es menor al mínimo");
                }else if (max > 5) {
                    maximum.setError("Máximo excedido");
                }else {
                    if (filterType.equals("rankingRange")) {
                        getFilter().filter(minimum.getText().toString()+','+maximum.getText().toString());
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public android.widget.Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @SuppressLint("LongLogTag")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DatosVo> filteredList = new ArrayList<DatosVo>();
            int separator = constraint.toString().indexOf(',');
            int min = Integer.parseInt(constraint.toString().substring(0,separator));
            int max = Integer.parseInt(constraint.toString().substring(separator+1));
            for (DatosVo item: adapter.list_datos) {
                if (item.getStars() >= min && item.getStars() <= max) {
                    filteredList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.list_datos.clear();
            adapter.list_datos.addAll((ArrayList<DatosVo>)results.values);
            adapter.notifyDataSetChanged();
        }
    };
}
