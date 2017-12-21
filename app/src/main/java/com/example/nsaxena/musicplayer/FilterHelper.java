package com.example.nsaxena.musicplayer;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by root on 21/12/17.
 */

public class FilterHelper extends Filter{

    static ArrayList<Song> mCurrentList;
    static RecyclerViewAdapter mAdapter;

    public static FilterHelper newInstance(ArrayList<Song> currentList,RecyclerViewAdapter adapter)
    {
        FilterHelper.mAdapter=adapter;
        FilterHelper.mCurrentList=currentList;
        return new FilterHelper();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint)
    {
        FilterResults filterResults=new FilterResults();

        if(constraint!=null && constraint.length()>0)
        {
            constraint=constraint.toString().toUpperCase();

            ArrayList<Song> foundFilters=new ArrayList<>();

            String songTitle;

            for(int i=0;i<mCurrentList.size();i++)
            {
                songTitle=mCurrentList.get(i).getSongTitle();

                if(songTitle.toUpperCase().contains(constraint))
                {
                    foundFilters.add(mCurrentList.get(i));
                }
            }

            //set results to filter list

            filterResults.count=foundFilters.size();
            filterResults.values=foundFilters;
        }

        //Return Results
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        mAdapter.setSongList((ArrayList<Song>) filterResults.values);
        mAdapter.notifyDataSetChanged();
    }

}
