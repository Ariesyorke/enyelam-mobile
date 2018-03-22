package com.nyelam.android.booking;

import android.content.Intent;
import android.icu.text.MessagePattern;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.data.Participant;
import com.nyelam.android.data.ParticipantList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveGetParticipantsRequest;
import com.nyelam.android.http.NYHomepageModuleRequest;
import com.nyelam.android.storage.ModulHomepageStorage;
import com.nyelam.android.storage.ParticipantsStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceParticipantActivity extends AppCompatActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private Bundle bundle;
    private TextView savetextView;
    //private TextInputEditText nameInputEditText;
    private TextInputEditText emailInputEditText;
    private ImageView closeImageView;

    private int position;
    private List<Participant> participants;

    private AutoCompleteTextView nameAutoCompleteTextView;
    private List<Participant> dataList;
    private ParticipantsSearchAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service_participant);
        initSearch();
        initView();
        initExtra();
        initControl();
    }

    private void initSearch() {
        //mList = retrievePeople();

        /*dataList = new ArrayList<>();
        dataList.add(new Participant("Adam","adam@gmali.com"));
        dataList.add(new Participant("Aprilian","aprilian@gmali.com"));
        dataList.add(new Participant("Xavier","mrx@xman.com"));*/

        ParticipantsStorage partStorage = new ParticipantsStorage(this);
        dataList = new ArrayList<>();
        if (partStorage != null && partStorage.getParticipantList() != null)dataList = partStorage.getParticipantList().getList();

        nameAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.name_autoCompleteTextView);
        nameAutoCompleteTextView.setThreshold(1);
        adapter = new ParticipantsSearchAdapter(this, R.layout.view_item_participant, dataList);
        nameAutoCompleteTextView.setAdapter(adapter);
        nameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Participant participant = adapter.getItem(position);

                if (participant != null){
                    /*if (NYHelper.isStringNotEmpty(participant.getName()))
                        nameInputEditText.setText(participant.getName());*/

                    if (NYHelper.isStringNotEmpty(participant.getEmail()))
                        emailInputEditText.setText(participant.getEmail());
                }

            }
        });
    }

    private void initExtra() {

        bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt(NYHelper.POSITION);
            participants = new ArrayList<>();

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
                //if (p != null && NYHelper.isStringNotEmpty(p.getName())) nameInputEditText.setText(p.getName());
                if (p != null && NYHelper.isStringNotEmpty(p.getName())) nameAutoCompleteTextView.setText(p.getName());
                if (p != null && NYHelper.isStringNotEmpty(p.getEmail())) emailInputEditText.setText(p.getEmail());
            }

        }
    }

    private void initControl() {
        savetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String name = nameInputEditText.getText().toString().trim();
                String name = nameAutoCompleteTextView.getText().toString().trim();
                String email = emailInputEditText.getText().toString().trim();

                if (!NYHelper.isStringNotEmpty(name)){
                    Toast.makeText(BookingServiceParticipantActivity.this, getString(R.string.warn_field_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(email)){
                    Toast.makeText(BookingServiceParticipantActivity.this, getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isValidEmaillId(email)){
                    Toast.makeText(BookingServiceParticipantActivity.this, getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else {

                    name = NYHelper.capitalizeString(name);

                    Participant p = new Participant();
                    p.setName(name);
                    p.setEmail(email);

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
        //nameInputEditText  = (TextInputEditText) findViewById(R.id.name_editText);
        emailInputEditText  = (TextInputEditText) findViewById(R.id.email_editText);
        closeImageView = (ImageView) findViewById(R.id.close_imageView) ;
    }

    private void getDataParticipants() {
        try {
            NYDoDiveGetParticipantsRequest req = null;
            req = new NYDoDiveGetParticipantsRequest(this);
            spcMgr.execute(req, onGetParticipantsRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<ParticipantList> onGetParticipantsRequest() {
        return new RequestListener<ParticipantList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(ParticipantList results) {
                if (results != null && results.getList() != null && !results.getList().isEmpty()){

                    /*adapter.clear();
                    adapter.addModules(results.getList());
                    adapter.notifyDataSetChanged();*/

                    dataList = results.getList();

                    adapter.clear();
                    adapter.addAll(dataList);
                    adapter.notifyDataSetChanged();

                    ParticipantsStorage participantsStorage = new ParticipantsStorage(BookingServiceParticipantActivity.this);
                    participantsStorage.setParticipantList(results);
                    participantsStorage.save();

                }

            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
        getDataParticipants();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

}
