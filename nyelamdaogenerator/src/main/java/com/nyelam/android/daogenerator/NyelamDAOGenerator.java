package com.nyelam.android.daogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class NyelamDAOGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "com.nyelam.android.data.dao");

        //Country Code
        Entity countryCode = schema.addEntity("NYCountryCode");
        countryCode.addStringProperty("id").primaryKey();
        countryCode.addStringProperty("countryCode");
        countryCode.addStringProperty("countryName");
        countryCode.addStringProperty("countryNumber");
        countryCode.addStringProperty("countryImage");


        //Category
        Entity category = schema.addEntity("NYCategory");
        category.addStringProperty("id").primaryKey();
        category.addStringProperty("name");
        category.addStringProperty("iconUrl");
        category.addByteArrayProperty("iconImage");
        //category.addBooleanProperty("status");

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
//            new DaoGenerator().generateAll(schema, "D:\\Android\\Project\\Android\\oxone-android\\app\\src\\main\\java");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
