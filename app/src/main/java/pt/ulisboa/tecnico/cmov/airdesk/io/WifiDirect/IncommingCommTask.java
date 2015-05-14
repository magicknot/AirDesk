package pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

public class IncommingCommTask extends AsyncTask<Void, SimWifiP2pSocket, Void> {
    public static final String TAG = IncommingCommTask.class.getSimpleName();

    private SimWifiP2pSocketServer mServerSocket;
    private int mPort;

    public IncommingCommTask(int port) {
        this.mPort = port;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "IncommingCommTask started (" + this.hashCode() + ").");
        try {
            mServerSocket = new SimWifiP2pSocketServer(this.mPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i(TAG, "IncommingCommTask doInBackground (" + this.hashCode() + ").");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                SimWifiP2pSocket sock = mServerSocket.accept();
                if(sock != null)
                    publishProgress(sock);
            } catch (IOException e) {
                Log.d("Error accepting socket:", e.getMessage());
                break;
                //e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(SimWifiP2pSocket... values) {
        SimWifiP2pSocket mCliSocket = values[0];
        ReceiveCommTask mComm = new ReceiveCommTask();

        mComm.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCliSocket);
    }

}
