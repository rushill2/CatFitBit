package com.example.petmonitoruserend;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class HandleTexts extends Activity {
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    TextView tv4 = (TextView) findViewById(R.id.textview_pro);
    TextView tv2 = (TextView) findViewById(R.id.textview_pls);
}
