package pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;

public class OutgoingCommTask extends AsyncTask<String, Void, String>  {
    public static final String TAG = OutgoingCommTask.class.getSimpleName();

    private String mIp;
    private int mPort;
    private SimWifiP2pSocket mCliSocket;
    private ReceiveCommTask mComm;
    private String[] mArguments;

    public OutgoingCommTask(String ip, int port, String... arguments) {
        this.mIp = ip;
        this.mPort = port;
        this.mArguments = arguments;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            mCliSocket = new SimWifiP2pSocket(this.mIp, this.mPort);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mCliSocket.getOutputStream()));

            for (String argument: mArguments) {
                writer.write(argument);
                writer.newLine();
            }

            writer.flush();

            // Close everything
            writer.close();
            mCliSocket.close();

        } catch (UnknownHostException e) {
            return "Unknown Host:" + e.getMessage();
        } catch (IOException e) {
            return "IO error:" + e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.i(TAG, "onPostExecute: NULL");
        }
        else {
            //mComm = new ReceiveCommTask();
            //mComm.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCliSocket);
        }
    }


}
