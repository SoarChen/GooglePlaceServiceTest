package com.soar.googleplayservicetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by user on 2016/1/4.
 */
public class PlaceListActivity extends Activity {


    public static final String KEY_PLACE_LIST = "key_place_list";

    private ListView mListView = null;
    private List<PlaceWrapper> mPlaceList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        mListView = (ListView)findViewById(R.id.listView);
        Intent intent = getIntent();
        mPlaceList = intent.getParcelableArrayListExtra(KEY_PLACE_LIST);
        PlaceListAdapter placeListAdapter = new PlaceListAdapter();
        mListView.setAdapter(placeListAdapter);
        //placeListAdapter.notifyDataSetChanged();

    }


    class ViewHolder {
        TextView mTextName;
        TextView mTextAddress;
        TextView mTextPhoneNumber;
    }

    class PlaceListAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        PlaceListAdapter() {
            mInflater = LayoutInflater.from(PlaceListActivity.this);
        }


        @Override
        public int getCount() {
            if (mPlaceList != null) {
                return mPlaceList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (mPlaceList != null && position >= 0 && position < mPlaceList.size()) {
                return mPlaceList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.placelist_item, null);
                vh = new ViewHolder();
                vh.mTextAddress = (TextView)convertView.findViewById(R.id.text_address);
                vh.mTextName = (TextView)convertView.findViewById(R.id.text_name);
                vh.mTextPhoneNumber = (TextView)convertView.findViewById(R.id.text_phone_umber);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder)convertView.getTag();
            }
            PlaceWrapper currentPlace = (PlaceWrapper)getItem(position);
            if (vh.mTextAddress != null) {
                vh.mTextAddress.setText(currentPlace.getAddress());
            }

            if (vh.mTextPhoneNumber != null) {
                vh.mTextPhoneNumber.setText(currentPlace.getPhoneNumber());
            }

            if (vh.mTextName != null) {
                vh.mTextName.setText(currentPlace.getName());
            }

            return convertView;
        }
    }

}
