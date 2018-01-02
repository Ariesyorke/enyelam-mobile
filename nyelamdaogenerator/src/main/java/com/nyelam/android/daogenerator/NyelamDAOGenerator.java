package com.nyelam.android.daogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Schema;

public class NyelamDAOGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.nyelam.android.data.dao");

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
//            new DaoGenerator().generateAll(schema, "D:\\Android\\Project\\Android\\oxone-android\\app\\src\\main\\java");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
