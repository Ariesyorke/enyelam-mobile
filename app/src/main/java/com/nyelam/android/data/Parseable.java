package com.nyelam.android.data;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public interface Parseable {
    void parse(JSONObject obj);
}
