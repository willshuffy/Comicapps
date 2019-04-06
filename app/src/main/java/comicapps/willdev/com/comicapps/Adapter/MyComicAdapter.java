package comicapps.willdev.com.comicapps.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comicapps.willdev.com.comicapps.Model.Comic;
import comicapps.willdev.com.comicapps.R;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {

    Context context;
    List<Comic> comicList;
    LayoutInflater inflater;

    public MyComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.comic_item,viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Picasso.get().load(comicList.get(i).Image).into(myViewHolder.comic_image);
        myViewHolder.comic_name.setText(comicList.get(i).Name);

    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView comic_name;
        ImageView comic_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            comic_image = (ImageView)itemView.findViewById(R.id.image_comic);
            comic_name = (TextView)itemView.findViewById(R.id.comic_name);
        }
    }
}
