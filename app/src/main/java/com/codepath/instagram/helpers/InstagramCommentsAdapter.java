package com.codepath.instagram.helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bill on 12/1/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.ViewHolder>{
    private List<InstagramComment> mInstagramComments;
    //private Context mContext;

    public InstagramCommentsAdapter(List<InstagramComment> mInstagramComments) {
        this.mInstagramComments = mInstagramComments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View commentView = inflater.inflate(R.layout.layout_item_comment, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(commentView);
        //mContext = context;
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        // Get the data model based on position
        final InstagramComment comment = mInstagramComments.get(position);
        Picasso.with(context).load(comment.user.profilePictureUrl).fit().centerCrop()
                .placeholder(R.drawable.gray_rectangle)
                .error(R.drawable.gray_oval)
                .into(holder.mProfilePic);

        holder.mTimeStamp.setText(DateUtils.getRelativeTimeSpanString(
                comment.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

        Utils.setFormmatText(holder.mCommentConent, comment.user.userName, comment.text);
    }


    @Override
    public int getItemCount() {
        return mInstagramComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCommentConent;
        public TextView mTimeStamp;
        //public TextView mImgUrl;

        public ImageView mProfilePic;
        public ViewHolder(View itemView) {

            super(itemView);

            mTimeStamp = (TextView) itemView.findViewById(R.id.tvCommentTime);
            mCommentConent = (TextView) itemView.findViewById(R.id.tvCommentContent);

            mProfilePic = (ImageView) itemView.findViewById(R.id.ivCommentProfile);
        }

    }
}
