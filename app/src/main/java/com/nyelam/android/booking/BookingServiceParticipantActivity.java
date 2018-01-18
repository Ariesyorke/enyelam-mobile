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
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceParticipantActivity extends AppCompatActivity {

    private Bundle bundle;
    private TextView savetextView;
    private TextInputEditText nameInputEditText;
    private ImageView closeImageView;

    private int position;
    private List<Participant> participants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service_participant);
        initView();
        initExtra();
        initControl();
    }

    private void initExtra() {

        bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt(NYHelper.POSITION);
            participants = new ArrayList<>();

            NYLog.e("TES Participant : "+bundle.getString(NYHelper.PARTICIPANT));
            try {
                JSONArray array = new JSONArray(bundle.getString(NYHelper.PARTICIPANT));
                if (array != null && array.length() > 0) {
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


            position = bundle.getInt(NYHelper.POSITION);

            if (participants != null && participants.size() > 0){
                Participant p = participants.get(position);
                if (p != null && NYHelper.isStringNotEmpty(p.getName())) nameInputEditText.setText(p.getName());
            }

        }
    }

    private void initControl() {
        savetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameInputEditText.getText().toString();
                if (!NYHelper.isStringNotEmpty(name)){
                    nameInputEditText.setError(getString(R.string.warn_field_name_cannot_be_empty));
                } else {

                    name = NYHelper.capitalizeString(name);

                    Participant p = new Participant();
                    p.setName(name);

                    participants.set(position, p);
                    bundle.putString(NYHelper.PARTICIPANT, participants.toString());
                    bundle.putBoolean(NYHelper.IS_NOT_NEW, true);

                    Intent intent = new Intent(BookingServiceParticipantActivity.this, BookingServiceActivity.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
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
