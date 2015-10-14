package com.linsen.h5.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.linsen.h5.R;
import com.linsen.h5.domain.Meizhi;
import com.squareup.picasso.Picasso;

public class MeizhiListAdapter extends RecyclerView.Adapter<MeizhiListAdapter.ViewHolder> {

    private List<Meizhi> mList;
    private Context mContext;

    public MeizhiListAdapter(Context context, List<Meizhi> meizhiList) {
        mList = meizhiList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meizhi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Meizhi meizhi = mList.get(position);
        viewHolder.meizhi = meizhi;
        viewHolder.titleView.setText(meizhi.getMid());
        viewHolder.card.setTag(meizhi.getMid());
        ViewTreeObserver vto = viewHolder.meizhiView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int thumbWidth = viewHolder.meizhi.getThumbWidth();
                        int thumbHeight = viewHolder.meizhi.getThumbHeight();
                        if (thumbWidth > 0 && thumbHeight > 0) {
                            int width = viewHolder.meizhiView.getMeasuredWidth();
                            int height = Math.round(width * ((float) thumbHeight / thumbWidth));
                            viewHolder.meizhiView.getLayoutParams().height = height;
                            viewHolder.meizhiView.setMinimumHeight(height);
                        }
                        viewHolder.meizhiView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
        );

        Picasso.with(mContext).load(meizhi.getUrl()).into(viewHolder.meizhiView);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.meizhiView != null) holder.meizhiView.setImageBitmap(null);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Meizhi meizhi;
        ImageView meizhiView;
        TextView titleView;
        View card;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            meizhiView = (ImageView) itemView.findViewById(R.id.iv_meizhi);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            meizhiView.setOnClickListener(this);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (meizhi == null)
                return;
            if (v == meizhiView) {
//                Intent i = new Intent(mContext, PictureActivity.class);
//                i.putExtra(PictureActivity.EXTRA_IMAGE_URL, meizhi.getUrl());
//                i.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, meizhi.getMid());
//
//                if (mContext instanceof Activity) {
//                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            (Activity) mContext, meizhiView, PictureActivity.TRANSIT_PIC
//                    );
//                    ActivityCompat.startActivity((Activity) mContext, i, optionsCompat.toBundle());
//                } else {
//                    mContext.startActivity(i);
//                }
            } else if (v == card) {
//                Intent intent = new Intent(mContext, WebViewActivity.class);
//                intent.putExtra("meizhi", meizhi);
//                mContext.startActivity(intent);
            }
        }
    }
}
