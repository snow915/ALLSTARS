package com.store.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.store.Filter;
import com.store.R;
import java.util.ArrayList;


public class AdapterFiltros extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Filter> filters;

    public AdapterFiltros(Context context, ArrayList<Filter> filters) {
        this.context = context;
        this.filters = filters;
    }

    @Override
    public int getGroupCount() {
        return filters.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filters.get(groupPosition).filterTypes.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.filters.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filters.get(groupPosition).filterTypes.get(childPosition);
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
        Filter f = (Filter) getGroup(groupPosition);
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
        final EditText minimum = convertView.findViewById(R.id.edtxt_minimum);
        final EditText maximum = convertView.findViewById(R.id.edtxt_maximum);
        Button btnApplyFilter = convertView.findViewById(R.id.btn_apply_filter);
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("MINIMUM VALUE", minimum.getText().toString());
                Log.w("MAXIMUM VALUE", maximum.getText().toString());
            }
        });
        String filterName = getGroup(groupPosition).toString();

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
