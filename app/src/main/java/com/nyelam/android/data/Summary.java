package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/19/2018.
 */

public class Summary implements Parseable {

    private static String KEY_ORDER = "order";
    private static String KEY_SERVICE = "service";
    private static String KEY_CONTACT = "contact";
    private static String KEY_PARTICIPANTS = "participants";

    private Order order;
    private DiveService diveService;
    private Contact contact;
    private List<Participant> participants;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DiveService getDiveService() {
        return diveService;
    }

    public void setDiveService(DiveService diveService) {
        this.diveService = diveService;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        if(!obj.isNull(KEY_ORDER)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_ORDER);
                if(o != null) {
                    order = new Order();
                    order.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        try {
            if(!obj.isNull(KEY_SERVICE)) {
                JSONObject o = obj.getJSONObject(KEY_SERVICE);
                if(o != null) {
                    diveService = new DiveService();
                    diveService.parse(o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*try {
            if(!obj.has(KEY_SERVICE) && obj.get(KEY_SERVICE) instanceof JSONObject && !obj.isNull(KEY_SERVICE)) {
                JSONObject o = obj.getJSONObject(KEY_SERVICE);
                if(o != null && o.length() > 0) {
                    diveService = new DiveService();
                    diveService.parse(o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        if(!obj.isNull(KEY_CONTACT)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CONTACT);
                if(o != null) {
                    contact = new Contact();
                    contact.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_PARTICIPANTS)) {
            try {
                if(obj.get(KEY_PARTICIPANTS) instanceof JSONArray) {
                    JSONArray array = obj.getJSONArray(KEY_PARTICIPANTS);
                    if (array != null && array.length() > 0) {
                        participants = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            Participant p = new Participant();
                            p.parse(o);
                            participants.add(p);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try{
            if(getOrder()!=null){
                JSONObject objOrder = new JSONObject(getOrder().toString());
                obj.put(KEY_ORDER, objOrder);
            } else {
                obj.put(KEY_ORDER, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getDiveService()!=null){
                JSONObject objService = new JSONObject(getDiveService().toString());
                obj.put(KEY_SERVICE, objService);
            } else {
                obj.put(KEY_SERVICE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getContact()!=null){
                JSONObject objContact = new JSONObject(getContact().toString());
                obj.put(KEY_CONTACT, objContact);
            } else {
                obj.put(KEY_CONTACT, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(participants != null && !participants.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Participant p : participants) {
                    JSONObject o = new JSONObject(p.toString());
                    array.put(o);
                }
                obj.put(KEY_PARTICIPANTS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();

    }

}
