package zhoushi.ist.controls.horizontalscrollview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import zhoushi.ist.R;
import zhoushi.ist.network.NetProtocol;

public class HorizontalScrollViewAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;

	public ArrayList<String> getmDatas() {
		return mDatas;
	}

	public void setmDatas(ArrayList<String> mDatas) {
		this.mDatas = mDatas;
	}

	private ArrayList<String> mDatas = new ArrayList<String>();

	public HorizontalScrollViewAdapter(Context context)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return mDatas.size();
	}

	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_index_gallery_item, parent, false);
			viewHolder.mImg = (ImageView) convertView
					.findViewById(R.id.id_index_gallery_item_image);
			viewHolder.mText = (TextView) convertView
					.findViewById(R.id.id_index_gallery_item_text);

			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		x.view().inject(convertView);
		x.image().bind(viewHolder.mImg, mDatas.get(position));
		//viewHolder.mImg.setImageResource(mDatas.get(position));
		viewHolder.mText.setText("");

		return convertView;
	}

	private class ViewHolder
	{
		ImageView mImg;
		TextView mText;
	}

}
