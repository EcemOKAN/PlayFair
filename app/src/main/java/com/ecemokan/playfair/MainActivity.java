package com.ecemokan.playfair;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;


@SuppressLint("DefaultLocale")
public class MainActivity extends AppCompatActivity {
    private PlayfairMain mPlayfairMain;
    private String mKey = "playfairexample".toUpperCase();
    private String mTable;
    private String mWords = "hidethegoldinthetreestump ".toUpperCase();
    private String mInfo;
    private String mCiphertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayfairMain = new PlayfairMain();

        ((TextView) findViewById(R.id.key)).setText(mKey);
        ((TextView) findViewById(R.id.table)).setText(mTable = mPlayfairMain.getTable(mKey));

        ((TextView) findViewById(R.id.words)).setText(mWords);
        ((TextView) findViewById(R.id.info)).setText(mInfo = mPlayfairMain.getInfo(mWords));

        ((TextView) findViewById(R.id.ciphertext)).setText(mCiphertext = mPlayfairMain.getCiphertext(mTable, mInfo));
        ((TextView) findViewById(R.id.plaintext)).setText(mPlayfairMain.getPlaintext(mTable, mCiphertext));
    }
}
