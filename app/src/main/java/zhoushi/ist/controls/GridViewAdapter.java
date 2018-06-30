package zhoushi.ist.controls;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.xutils.x;

import zhoushi.ist.R;

public class GridViewAdapter extends BaseAdapter{

	public LayoutInflater inflater;
	public String[] imageUrls;
	
	public GridViewAdapter(Context context,String[] imageUrls) {
		inflater = LayoutInflater.from(context);
		this.imageUrls = imageUrls;
	}
	@Override
	public int getCount() {
		return imageUrls.length;
	}

	@Override
	public String getItem(int position) {
		return imageUrls[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String currStr = getItem(position);
		View view;
		ViewHolder holder;
		if(convertView == null)
		{
			view = inflater.inflate(R.layout.gridview, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.photo);
			view.setTag(holder);
		}else
		{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		holder.imageView.setTag(currStr);
		holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

		holder.imageView.setImageResource(R.drawable.btn_up);
		showBitmapToImageView(currStr, holder.imageView);
		return view;
	}
	
	/**
	 * ����BitmapͼƬ��ImageView����
	 * @param url
	 * @param imageView
	 */
	public void showBitmapToImageView(String url,ImageView imageView)
	{
		x.image().bind(imageView, url);
	}
	static class ViewHolder
	{
		ImageView imageView;
	}
}






