package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jzkl.Bean.First;
import com.jzkl.R;
import java.util.List;


public class ListViewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	private int selectedPosition = 0;
	List<First> foods;
	private Resources resources;
	ViewHolder holder;

	public ListViewAdapter(Context context, List<First> foods){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.foods = foods;
	}
	@Override
	public int getCount() {
		return foods.size();
	}

	@Override
	public Object getItem(int position) {
		return foods.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		First first = (First) getItem(position);
		if (convertView==null) {
			holder = new ViewHolder();
			convertView  = inflater.inflate(R.layout.item_category_listview, null);
			holder.list_ll = (LinearLayout) convertView.findViewById(R.id.item_list_ll);
			holder.text = (TextView) convertView.findViewById(R.id.list_txt);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(first.getmFirstName());
		secletBg(position);
		return convertView;
	}

	private void secletBg(int position) {
		if(position == selectedPosition){
			resources = context.getResources();
			holder.text.setTextColor(Color.parseColor("#6db0ff"));
			holder.list_ll.setBackgroundResource(R.drawable.shape_orde);
		}else {
			holder.text.setTextColor(Color.parseColor("#484848"));
			holder.list_ll.setBackgroundResource(R.drawable.shape_orde_no);
		}
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

    public final class ViewHolder{
        public TextView text;
        public LinearLayout list_ll;
    }
}
