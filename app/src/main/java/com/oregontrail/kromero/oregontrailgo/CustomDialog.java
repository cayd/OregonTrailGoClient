package com.oregontrail.kromero.oregontrailgo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("ValidFragment")
public class CustomDialog extends AppCompatDialogFragment {
    private String positiveResponseAddress = "http://10.0.2.2:8080/respond/";
    private String negativeResponseAddress = "http://10.0.2.2:8080/respond/";

    private Game game;
    private Player client;
    private TextView messageText;

    @SuppressLint("ValidFragment")
    public CustomDialog(Game game, Player player) {
        super();
        this.game = game;
        this.client = player;
        positiveResponseAddress = positiveResponseAddress + client.getId() + "/true";
        negativeResponseAddress = negativeResponseAddress + client.getId() + "/false";
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        messageText = (TextView) view.findViewById(R.id.message);
        messageText.setText(game.getPromptMessage());

        if (client.eventHasChoice()) {
            builder.setView(view)
                    .setTitle(null)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                URL url = new URL(negativeResponseAddress);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");

                                String data = conn.getResponseMessage();
                                Log.i("DATA", data);

                                Log.i("RESPONSE", "Successfully said no to event");
                                client.clearEvent();

                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(e.getMessage());
                            }
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                URL url = new URL(positiveResponseAddress);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");

                                String data = conn.getResponseMessage();
                                Log.i("DATA", data);

                                Log.i("RESPONSE", "Successfully said yes to event");
                                client.clearEvent();

                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(e.getMessage());
                            }
                        }
                    });
        } else {
            builder.setView(view)
                    .setTitle(null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        return builder.create();
    }

    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
