package com.example.nsaxena.musicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;


/**
 * Created by root on 19/12/17.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private List<Song> mSongList;

    private Context mContext;

    OnItemClickListener mOnItemClickListener;

    public RecyclerViewAdapter(ArrayList<Song> songList, MainActivity context) {
        mSongList = songList;
        mContext = context;
    }

    public interface OnItemClickListener
    {
        void onItemClick(Button button,View view,Song songObject,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener=onItemClickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        // Called by the layout manager when it want new data in an exisiting row
        final Song songItem=mSongList.get(position);
        Log.d(TAG, "onBindViewHolder: "+songItem.getSongImage()+"-->"+position);
        Glide.with(mContext)
                .load(songItem.getSongImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
        holder.title.setText(songItem.getSongTitle());
        holder.artists.setText(songItem.getSongArtists());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(holder.play,view,songItem,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mSongList!=null)&&(mSongList.size()!=0)?mSongList.size():0);
    }

    void loadNewData(List<Song> newSongs)
    {
        mSongList=newSongs;
        notifyDataSetChanged();
    }

    public Song getSong(int position)
    {
        return((mSongList!=null)&&(mSongList.size()!=0)?mSongList.get(position):null);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "ImageViewHolder";

        ImageView thumbnail=null;
        TextView title=null;
        TextView artists=null;
        Button play=null;

        public ImageViewHolder(View itemView)
        {
            super(itemView);
            this.thumbnail=(ImageView)itemView.findViewById(R.id.coverPic);
            this.title=(TextView) itemView.findViewById(R.id.songTitle);
            this.artists=(TextView)itemView.findViewById(R.id.songArtist);
            this.play=(Button)itemView.findViewById(R.id.play);
        }
    }
}
