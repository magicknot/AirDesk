package pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;

public class ReceiveCommTask extends AsyncTask<SimWifiP2pSocket, String, Void> {
    public static final String TAG = ReceiveCommTask.class.getSimpleName();
    SimWifiP2pSocket s;

    @Override
    protected Void doInBackground(SimWifiP2pSocket... params) {
        BufferedReader sockIn;
        String st;

        s = params[0];
        try {
            sockIn = new BufferedReader(new InputStreamReader(s.getInputStream()));

            while ((st = sockIn.readLine()) != null) {
                publishProgress(st);
            }
        } catch (IOException e) {
            Log.d("Error reading socket:", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.i(TAG, values[0]+"\n");
    }

    @Override
    protected void onPostExecute(Void result) {
        if (!s.isClosed()) {
            try {
                s.close();
            }
            catch (Exception e) {
                Log.d("Error closing socket:", e.getMessage());
            }
        }
    }
}