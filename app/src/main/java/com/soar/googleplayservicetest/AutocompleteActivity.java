package com.soar.googleplayservicetest;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2016/1/6.
 */
public class AutocompleteActivity extends Activity implements GooglePlaceWebAPI.AutocompleteListener {

    private ListView mListView = null;
    private EditText mEditAutocomplete = null;
    private Button mBtnClear = null;
    private List<GoogleAutocomplete> mAutocompleteList = null;
    private AutoCompleteAdapter mAutoCompleteAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);
        initView();
    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.listView);
        mEditAutocomplete = (EditText)findViewById(R.id.edit_autocomplete);
        mBtnClear = (Button)findViewById(R.id.btn_clear);

        if (mListView != null) {
            mAutoCompleteAdapter = new AutoCompleteAdapter();
            mListView.setAdapter(mAutoCompleteAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GoogleAutocomplete googleAutocomplete = (GoogleAutocomplete)mAutoCompleteAdapter.getItem(position);
                    String placeId =  googleAutocomplete.getPlaceId();
                    if (!TextUtils.isEmpty(placeId)) {
                        FindPlaceDetailAsyncTask findPlaceDetailAsyncTask = new FindPlaceDetailAsyncTask(placeId);
                        findPlaceDetailAsyncTask.setContext(AutocompleteActivity.this);
                        findPlaceDetailAsyncTask.execute();
                    }
                }
            });
        }

        if (mEditAutocomplete != null) {
            mEditAutocomplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    if (!TextUtils.isEmpty(text)) {
                        PlaceAutoCompleteAsyncTask placeAutoCompleteAsyncTask = new PlaceAutoCompleteAsyncTask(text);
                        placeAutoCompleteAsyncTask.setAutocompleteListener(AutocompleteActivity.this);
                        placeAutoCompleteAsyncTask.execute();
                    } else {
                        mAutocompleteList = null;
                        if (mAutoCompleteAdapter != null)
                            mAutoCompleteAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        if (mBtnClear != null) {
            mBtnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEditAutocomplete != null) {
                        mEditAutocomplete.setText("");
                    }
                }
            });
        }

    }


    @Override
    public void onResponse(List<GoogleAutocomplete> autocompleteList) {
        mAutocompleteList = autocompleteList;
        if (mAutoCompleteAdapter != null)
            mAutoCompleteAdapter.notifyDataSetChanged();
    }


    class AutoCompleteAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;
        private int lastPosition = -1;

        AutoCompleteAdapter() {
            mInflater = LayoutInflater.from(AutocompleteActivity.this);
        }

        @Override
        public int getCount() {
            if (mAutocompleteList != null)
                return mAutocompleteList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mAutocompleteList != null)
                return mAutocompleteList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.autocomplete_item, null);
                vh = new ViewHolder();
                vh.mTextDescription = (TextView)convertView.findViewById(R.id.text_desp);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder)convertView.getTag();
            }

            if (vh != null) {
                GoogleAutocomplete autocomplete = (GoogleAutocomplete)getItem(position);
                vh.mTextDescription.setText(autocomplete.getDescription(new ForegroundColorSpan(0xff00ff00)));
            }

            //Animation animation = AnimationUtils.loadAnimation(AutocompleteActivity.this, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            //convertView.startAnimation(animation);
            lastPosition = position;

            return convertView;
        }
    }


    class ViewHolder {
        TextView mTextDescription;
    }

}
