package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jzkl.Bean.Second;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;
import com.jzkl.util.Webcon;

import java.util.List;



public class GridViewAdapter extends BaseAdapter {

	private Context context;
	LayoutInflater inflater;
	List<Second> secondList;

	public GridViewAdapter(Context context, List<Second> secondList){
		this.context=context;
		this.secondList =secondList;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return secondList.size();
	}

	@Override
	public Object getItem(int position) {
		return secondList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Second second = (Second) getItem(position);
		ViewHolder  viewHolder=null;
		final int location=position;
		if (convertView==null) {
			convertView = inflater.inflate(R.layout.item_category_gridview, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image_item);
			viewHolder.title = (TextView) convertView.findViewById(R.id.text_item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		viewHolder.title.setText(second.getmSecondName());
		if(second.getmSecondImg().equals("null")){
			viewHolder.image.setImageResource(R.mipmap.logo);
		}else {
			OkHttpUtils.picassoImage(second.getmSecondImg(),context,viewHolder.image);
		}
		return convertView;
	}

	public static class ViewHolder{
		ImageView image;
		TextView title;
	}
}
