package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;
import vanlandingham.friendimals.SearchActivity;

/**
 * Created by Owner on 12/17/2017.
 */

public class searchAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private int layoutResource;
    private List<User> mUsers = null;

    private BitmapDrawable bitmapDrawable;
    private Bitmap bitmap;
    private RoundedBitmapDrawable dr;
    private SearchActivity searchActivity;


    public searchAdapter(Context context, int resource, List<User> userList, SearchActivity searchActivity) {
        super(context,resource,userList);
        this.mContext=context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = resource;
        this.mUsers = userList;
        this.searchActivity = searchActivity;
    }

    private static class ViewHolder{
        TextView username,full_name;
        ImageView profile_image;
        ConstraintLayout constraintLayout;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            holder = new ViewHolder();

            holder.username = convertView.findViewById(R.id.SearchesUsername_textView);
            holder.full_name = convertView.findViewById(R.id.SearchesName_view);
            holder.profile_image = convertView.findViewById(R.id.searches_imageView);
            holder.constraintLayout = convertView.findViewById(R.id.searches_constraint);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.profile_image.setDrawingCacheEnabled(true);

        holder.profile_image.getLayoutParams().height = 20;

        holder.profile_image.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.profile_image.layout(0,0,holder.profile_image.getMeasuredWidth(),holder.profile_image.getMeasuredHeight());
        holder.profile_image.buildDrawingCache(true);

        //bitmap = Bitmap.createBitmap(holder.profile_image.getDrawingCache());

        bitmap = holder.profile_image.getDrawingCache();
        //Bitmap MutableBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap MutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        //Bitmap MutableBitmap = holder.profile_image.getDrawingCache();
        holder.profile_image.setDrawingCacheEnabled(false);
        Canvas canvas = new Canvas(MutableBitmap);

        BitmapShader shader;
        //shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        Rect rect = new Rect(0,0,MutableBitmap.getWidth(),MutableBitmap.getHeight());
        RectF rectF = new RectF(rect);
        //RectF rectF = new RectF(0,0,90,100);

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        //paint.setShader(shader);
        paint.setColor(Color.TRANSPARENT);

        paint.setColor(Color.TRANSPARENT);
        canvas.drawRoundRect(rectF,50,50,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(MutableBitmap,90,100,paint);


        //canvas.drawCircle(50,50,100,paint);
        //canvas.drawBitmap(MutableBitmap,90,100,paint);

        holder.profile_image.setImageBitmap(MutableBitmap);
        //holder.profile_image.setImageResource(R.drawable.selfie);
        //holder.profile_image.setImageDrawable(new BitmapDrawable(convertView.getResources(),MutableBitmap));

        holder.full_name.setText("John Doe");
        holder.username.setText(getItem(position).getUsername());


        return convertView;
    }

}
