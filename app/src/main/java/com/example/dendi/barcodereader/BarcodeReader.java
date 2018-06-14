package com.example.dendi.barcodereader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;

public class BarcodeReader extends AppCompatActivity {

    private Button scanBtn;

    private Button viewElementsBtn;

    private Button removeTheLastElementButton;

    private Button addAllElementsToDBBtn;

    private TextView viewElementsTextView;

    private ArrayList<String> currentElements;

    private HashMap<String, Integer> repeats;

    private DatabaseHelperSQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_reader);

        final Activity activity = this;

        scanBtn = (Button) findViewById(R.id.scanBtn);

        viewElementsBtn = (Button) findViewById(R.id.viewElementsBtn);

        removeTheLastElementButton = (Button) findViewById(R.id.removeLastButton);

        addAllElementsToDBBtn = (Button) findViewById(R.id.addAllElementsToDBBtn);

        viewElementsTextView = (TextView) findViewById(R.id.viewElementsTextView);

        currentElements = new ArrayList<String>();

        db = new DatabaseHelperSQLite(this);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        viewElementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentElements.isEmpty()){

                    Toast.makeText(activity, "Currently there are no scanned items", Toast.LENGTH_LONG).show();

                }else {

                    String result = findOccurrences().toString().replaceAll("\\{|\\}", "");

                    viewElementsTextView.setText(result + "\n");

                }
            }
        });

        removeTheLastElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!currentElements.isEmpty()) {

                    currentElements.remove(currentElements.size() - 1);

                    String result = findOccurrences().toString().replaceAll("\\{|\\}", "");

                    viewElementsTextView.setText(result + "\n");

                }else {

                    Toast.makeText(activity, "There are no scanned elements", Toast.LENGTH_LONG).show();

                }
            }
        });

        addAllElementsToDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String str : findOccurrences().keySet()){

                    Barcode barcode = new Barcode(str, findOccurrences().get(str));

                    db.addBarcode(barcode);

                    Toast.makeText(activity, "All elements added to the database", Toast.LENGTH_LONG).show();

                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result != null){

            if(result.getContents() == null){

                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();

            }else{

                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                currentElements.add(result.getContents());

            }


        }else{

            super.onActivityResult(requestCode, resultCode, data);

        }
    }


    private HashMap<String, Integer> findOccurrences() {

        repeats = new HashMap<String, Integer>();

        for (String str : currentElements) {

            if (repeats.containsKey(str)) {

                repeats.put(str, repeats.get(str) + 1);

            } else {

                repeats.put(str, 1);

            }

        }

        return repeats;

    }

    public HashMap<String, Integer> getRepeats() {

        return repeats;

    }
}
