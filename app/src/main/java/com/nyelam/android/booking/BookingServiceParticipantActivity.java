package com.nyelam.android.booking;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Participant;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceParticipantActivity extends AppCompatActivity {

    private TextView savetextView;
    private TextInputEditText nameInputEditText;
    private ImageView closeImageView;

    private int position;
    private List<Participant> participants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service_participant);
        initExtra();
        initView();
        initControl();
    }

    private void initExtra() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt(NYHelper.POSITION);

            try {
                JSONArray array = new JSONArray(bundle.getString(NYHelper.PARTICIPANT));
                if (array != null && array.length() > 0) {
                    participants = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Participant a = new Participant();
                        a.parse(o);
                        participants.add(a);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initControl() {
        savetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServiceParticipantActivity.this, BookingServiceActivity.class);
                intent.putExtra(NYHelper.SERVICE, "");
                startActivity(intent);
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        savetextView = (TextView) findViewById(R.id.save_textView);
        nameInputEditText  = (TextInputEditText) findViewById(R.id.name_editText);
        closeImageView = (ImageView) findViewById(R.id.close_imageView) ;
    }


}
