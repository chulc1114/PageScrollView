package com.clc.psv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private PageScrollView mScrollView;
    private TextView mPageNumTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollView = findViewById(R.id.psv_pages);
        mPageNumTv = findViewById(R.id.tv_page_num);

        mScrollView.setPageChangedListener(pageNum -> mPageNumTv.setText("第" + pageNum + "页"));

    }
}