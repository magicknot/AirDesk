package pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk.manager.NetworkManager;

public class ReceiveCommTask extends AsyncTask<SimWifiP2pSocket, String, Void> {
    public static final String TAG = ReceiveCommTask.class.getSimpleName();
    SimWifiP2pSocket mCliSocket;

    @Override
    protected Void doInBackground(SimWifiP2pSocket... params) {
        BufferedReader sockIn;
        String st;

        mCliSocket = params[0];
        try {
            sockIn = new BufferedReader(new InputStreamReader(mCliSocket.getInputStream()));

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
        Log.i(TAG, values[0] + "\n");
        String answerString;
        Message answerMessage = NetworkManager.getInstance().receiveMessage(values[0]);


        if (answerMessage != null) {
            answerString = answerMessage.toJson().toString();
            Log.d(TAG, "onProgressUpdate() - message: " + answerString);

            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mCliSocket.getOutputStream()));

                writer.write(answerString);
                writer.newLine();

                writer.flush();

                // Close everything
                writer.close();

            } catch (UnknownHostException e) {
                Log.e(TAG, "Unknown Host:" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IO error:" + e.getMessage());
            }
        }

    }

    @Override
    protected void onPostExecute(Void result) {
        if (!mCliSocket.isClosed()) {
            try {
                mCliSocket.close();
            } catch (Exception e) {
                Log.d("Error closing socket:", e.getMessage());
            }
        }
    }
}