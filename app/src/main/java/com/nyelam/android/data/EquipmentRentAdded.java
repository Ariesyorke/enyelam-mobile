package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 5/21/2018.
 */

public class EquipmentRentAdded implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_EQUIPMENT_RENT = "equipment_rent";
    private static String KEY_QUANTITY = "quantity";

    private String id;
    private EquipmentRent equipmentRent;
    private int quantity;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EquipmentRent getEquipmentRent() {
        return equipmentRent;
    }

    public void setEquipmentRent(EquipmentRent equipmentRent) {
        this.equipmentRent = equipmentRent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void parse(JSONObject obj){

        if (obj == null) return;

        if (!obj.isNull(KEY_ID)){
            try {
                setId(obj.getString(KEY_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!obj.isNull(KEY_EQUIPMENT_RENT)) {
                JSONObject objEquip = obj.getJSONObject(KEY_EQUIPMENT_RENT);
                EquipmentRent equipmentRent = new EquipmentRent();
                equipmentRent.parse(objEquip);
                setEquipmentRent(equipmentRent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!obj.isNull(KEY_QUANTITY)){
            try {
                setQuantity(obj.getInt(KEY_QUANTITY));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        if (NYHelper.isStringNotEmpty(getId())){
            try {
                obj.put(KEY_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (getEquipmentRent() != null) {
                JSONObject objEquip = new JSONObject(getEquipmentRent().toString());
                obj.put(KEY_EQUIPMENT_RENT, objEquip);
            } else {
                obj.put(KEY_EQUIPMENT_RENT, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_QUANTITY, getQuantity());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
