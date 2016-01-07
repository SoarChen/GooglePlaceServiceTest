package com.soar.googleplayservicetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by user on 2016/1/6.
 */
public class PlaceDetailActivity extends Activity{


    public static final String KEY_PLACE = "key_place";

    private TextView mTextName = null;
    private TextView mTextAddress = null;
    private TextView mTextPhoneNumber = null;
    private TextView mTextLocation = null;
    private PlaceWrapper mPlace = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedetail);
        Intent intent = getIntent();
        if (intent != null)
            mPlace = intent.getParcelableExtra(KEY_PLACE);
        initView();
    }

    private void initView() {
        mTextName = (TextView)findViewById(R.id.text_name);
        mTextAddress = (TextView)findViewById(R.id.text_address);
        mTextPhoneNumber = (TextView)findViewById(R.id.text_phone_umber);
        mTextLocation = (TextView)findViewById(R.id.text_location);
        if (mPlace != null) {
            mTextName.setText(mPlace.getName());
            mTextPhoneNumber.setText(mPlace.getPhoneNumber());
            mTextAddress.setText(mPlace.getAddress());
            mTextLocation.setText(mPlace.getLocation());
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(KEY_PLACE, mPlace);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

}
