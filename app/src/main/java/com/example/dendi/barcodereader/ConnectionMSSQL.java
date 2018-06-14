package com.example.dendi.barcodereader;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMSSQL {

    String ip = "127.0.0.1";

    String classs = "net.sourceforge.jtds.jdbc.Driver";

    String db = "DB_name";

    String un = "admin";

    String password = "admin";

    @SuppressLint("NewApi")
    public ConnectionMSSQL CONN() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ConnectionMSSQL conn = null;

        String ConnURL = null;

        try {

            Class.forName(classs);

            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";" + "databaseName=" + db + ";user=" + un + ";password=" + password + ";";

            conn = (ConnectionMSSQL) DriverManager.getConnection(ConnURL);

        } catch (SQLException se) {

            Log.e("ERROR", se.getMessage());

        } catch (ClassNotFoundException e) {

            Log.e("ERROR", e.getMessage());

        } catch (Exception e) {

            Log.e("ERROR", e.getMessage());

        }

        return conn;

    }

}
