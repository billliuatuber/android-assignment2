package com.codepath.instagram.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bill on 11/30/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.ViewHolder>{
    private List<InstagramPost> mInstagramPosts;
    //private Context mContext;

    public InstagramPostsAdapter(List<InstagramPost> mInstagramPosts) {
        this.mInstagramPosts = mInstagramPosts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.layout_item_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(postView);
        //mContext = context;
        return viewHolder;
    }

    private void setCaption(TextView tvCaption, String userName, String caption) {
        ForegroundColorSpan buleColorSpan = new ForegroundColorSpan(
                tvCaption.getContext().getResources().getColor(R.color.blue_text));

        TypefaceSpan sanSpan = new TypefaceSpan("sans-serif-medium");
        SpannableStringBuilder ssb = new SpannableStringBuilder(userName);


        ssb.setSpan(
                buleColorSpan,            // the span to add
                0,                                 // the start of the span (inclusive)
                ssb.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // behavior when text is later inserted into the SpannableStringBuilder

        ssb.setSpan(
                sanSpan,            // the span to add
                0,                                 // the start of the span (inclusive)
                ssb.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_COMPOSING);

        ssb.append(" ");

        // Create a span that will grey the text
        ForegroundColorSpan greySpan = new ForegroundColorSpan(
                tvCaption.getContext().getResources().getColor(R.color.gray_text));
        TypefaceSpan sanSerifSpan = new TypefaceSpan("sans-serif");

        // Add the secondWord and apply the grey span to only the second word
        ssb.append(caption);
        ssb.setSpan(
                greySpan,
                ssb.length() - caption.length(),
                ssb.length(),
                Spanned.SPAN_COMPOSING);
        ssb.setSpan(
                sanSerifSpan,
                ssb.length() - caption.length(),
                ssb.length(),
                Spanned.SPAN_COMPOSING);

        tvCaption.setText(ssb, TextView.BufferType.NORMAL);
    }

    public int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public float convertPixelsToDp(float px, Context context){
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        // Get the data model based on position
        final InstagramPost post = mInstagramPosts.get(position);
        Picasso.with(context).load(post.user.profilePictureUrl).fit().centerCrop()
                .placeholder(R.drawable.gray_rectangle)
                .error(R.drawable.gray_oval)
                .into(holder.mProfilePic);

        holder.mUserName.setText(post.user.userName);
        holder.mTimeStamp.setText(DateUtils.getRelativeTimeSpanString(
                post.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

        //String strLike = String.format("%d likes", post.likesCount);
        //mContext.getResources().getString(R.string.like_count, post.likesCount);
        holder.mLikeCount.setText(context.getResources().getString(R.string.like_count, post.likesCount));
        String imageUri = post.image.imageUrl;
        //holder.mImgUrl.setText(post.image.imageUrl);
        Picasso.with(context).load(imageUri).resize(getDisplayWidth(context), 0)
                .into(holder.mPic);
        if (post.caption != null) {
            setCaption(holder.mCaption, post.user.userName, post.caption);
            //holder.mCaption.setText(post.caption);
        } else {
            holder.mCaption.setVisibility(View.GONE);
        }

        holder.mComments.removeAllViews();
        int count = 0;
        if (post.commentsCount > 0) {
            for (InstagramComment comment : post.comments) {
                if (++ count > 2) {
                    break;
                }
                TextView line = (TextView) LayoutInflater.from(context).inflate(
                        R.layout.layout_item_text_comment, holder.mComments, false);
                setCaption(line, comment.user.userName, comment.text);
                holder.mComments.addView(line);

            }
        } else {
            holder.mComments.setVisibility(View.GONE);
        }
        if (post.commentsCount <=2) {
            holder.mCommentsCount.setVisibility(View.GONE);
        } else {
            holder.mCommentsCount.setText(String.format(context.getString(R.string.view_all_comments), post.commentsCount));
        }
        holder.mCommentsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("media-id", post.mediaId);
                view.getContext().startActivity(intent);
            }
        });

        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                Uri bmpUri = Uri.parse(post.image.imageUrl);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });


    }


    @Override
    public int getItemCount() {
        return mInstagramPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mUserName;
        public TextView mLikeCount;
        public TextView mCaption;
        public TextView mTimeStamp;
        //public TextView mImgUrl;
        public ImageView mPic;
        public ImageView mProfilePic;
        public TextView mCommentsCount;

        public ImageButton mShare;

        public LinearLayout mComments;

        public ViewHolder(View itemView) {

            super(itemView);
            mUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            mTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            mLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            mCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            mCommentsCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            mShare = (ImageButton) itemView.findViewById(R.id.btmShare);

            //mImgUrl = (TextView) itemView.findViewById(R.id.tvImgUrl);
            mPic = (ImageView) itemView.findViewById(R.id.ivPic);
            mProfilePic = (ImageView) itemView.findViewById(R.id.ivProfile);
            mComments = (LinearLayout) itemView.findViewById(R.id.llvComments);
        }

    }
}
