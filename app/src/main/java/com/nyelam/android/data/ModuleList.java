package com.nyelam.android.data;

import com.nyelam.android.dev.NYLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class ModuleList extends NYAbstractList<Module> {
    @Override
    public Class<Module> getHandledClass() {
        return Module.class;
    }

    @Override
    public void parse(JSONArray array) throws JSONException {
        setList(new ArrayList<Module>());
        for(int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            String type = o.getString("module_name");
            //Module result = null;


            if (type.equals("Event")) {

                ModuleEvent result = new ModuleEvent();
                result.parse(o);
                getList().add(result);
            } else if (type.equals("Hot Offer")){


                ModuleService result = new ModuleService();
                result.parse(o);
                getList().add(result);
            } else if (type.equals("Popular Dive Spots")){

                NYLog.e("CEK MODULE : dive spot");

                ModuleDiveSpot result = new ModuleDiveSpot();
                result.parse(o);
                getList().add(result);
            }

        }

        NYLog.e("CEK MODULE WHAT : "+getList().toString());
    }
}