package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Aprilian Nur Wakhid Daini on 12/5/2018.
 */
public class VariationsUtility {

    public static Map<String, List<Variation>> jsonToMap(Object json) throws JSONException {

        if(json instanceof JSONObject)
            return _jsonToMap_((JSONObject)json) ;

        else if (json instanceof String)
        {
            JSONObject jsonObject = new JSONObject((String)json) ;
            return _jsonToMap_(jsonObject) ;
        }
        return null ;
    }


    private static Map<String, List<Variation>> _jsonToMap_(JSONObject json) throws JSONException {
        Map<String, List<Variation>> retMap = new HashMap<String, List<Variation>>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }


    private static Map<String, List<Variation>> toMap(JSONObject object) throws JSONException {
        Map<String, List<Variation>> map = new HashMap<String, List<Variation>>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            List<Variation> vars = null;
            if(value instanceof JSONArray) {
                vars = toList((JSONArray) value);
                if (vars != null)map.put(key, vars);
            }
        }
        return map;
    }

    public static List<Variation> toList(JSONArray array) throws JSONException {
        List<Variation> list = new ArrayList<Variation>();
        for(int i = 0; i < array.length(); i++) {

            try {
                Variation var = new Variation();
                var.parse(array.getJSONObject(i));
                if (var != null) list.add(var);
            } catch (JSONException e){}

        }
        return list;
    }

}
