package com.mycompany.medsconnect;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joel on 26-02-2016.
 */
public class MeowAdapter extends RecyclerView.Adapter<MeowAdapter.ViewHolder> {

    // PATCH: Because RecyclerView.Adapter in its current form doesn't natively support
    // cursors, we "wrap" a CursorAdapter that will do all teh job
    // for us
    CursorAdapter mCursorAdapter;

    Context mContext;
    ImageLoader imageLoader = new ImageLoader(mContext);
    int loader = R.drawable.noimageicon;
    Listener listener;
    String text;
    ArrayList<String> productNames = new ArrayList<String>();
    int count = 0;

    public static interface Listener {
        public void onClick(String text);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public MeowAdapter(Context context, Cursor c) {

        mContext = context;

        if(c.moveToFirst())
        {
            do{
                productNames.add(c.getString(1));
            }while(c.moveToNext());

        }

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context).inflate(R.layout.card_captioned_image,parent,false);
            }

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                // Binding operations
                TextView t = (TextView) view.findViewById(R.id.info_text);
                ImageView imageView = (ImageView) view.findViewById(R.id.info_image);
                text = cursor.getString(1);
                String image = cursor.getString(2);
                imageLoader.DisplayImage(image, loader, imageView);
                t.setText(text);
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(productNames.get(position));
                }
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        CardView v = (CardView) mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }
}