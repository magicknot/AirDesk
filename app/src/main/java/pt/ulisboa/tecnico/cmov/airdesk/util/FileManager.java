package pt.ulisboa.tecnico.cmov.airdesk.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class FileManager {

    private FileManager () {}

    public static void saveFile(String workspaceName, String filename, String content, Context context) throws IOException {

        File workspaceDirectory = new File(context.getDir(workspaceName, Context.MODE_PRIVATE).getAbsolutePath());
        Log.i("Storage", workspaceDirectory.getAbsolutePath());
        File outputFile = new File(workspaceDirectory, filename);
        Log.i("Storage(outputFile)", outputFile.getAbsolutePath());
        FileOutputStream fos;

        fos = new FileOutputStream(outputFile);
        fos.write(content.getBytes());
        fos.close();

    }

    public static String[] listFiles(String workspaceName, Context context) {

        File workspaceDirectory = new File(context.getDir(workspaceName, Context.MODE_PRIVATE).getAbsolutePath());
        return workspaceDirectory.list();

    }

    public static String readFile(String workspaceName, String filename, Context context) throws IOException {

        String filePath = context.getDir(workspaceName, Context.MODE_PRIVATE).getAbsolutePath() + "/" + filename;

        //FileInputStream fis = context.openFileInput(filePath);

        FileInputStream fis = new FileInputStream (new File(filePath));
        InputStreamReader isr = new InputStreamReader(fis);


        //Read text from file
        StringBuilder text = new StringBuilder();

        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();

        return text.toString();
    }

    public static void deleteFile(String workspaceName, String filename, Context context) {
        File file = new File (context.getDir(workspaceName, Context.MODE_PRIVATE).getAbsolutePath() + "/" + filename);
        Log.i("deleteFile", "fileName:"+context.getDir(workspaceName, Context.MODE_PRIVATE).getAbsolutePath() + "/" + filename);
        file.delete();
    }

    public static void deleteWorkspace(String workspaceName, Context context) {
        File file = new File (context.getFilesDir().getAbsolutePath() + "/" + workspaceName);
        file.delete();
    }

    public static long getFreeSpace(Context context) {
        File homeDir = new File (context.getFilesDir().getAbsolutePath());
        return homeDir.getUsableSpace();
    }

    public static long getWorkspaceUsedSpace(String workspaceName, Context context) {
        long totalSize = 0;
        File workspaceDirectory = new File (context.getFilesDir().getAbsolutePath() + "/" + workspaceName);

        for(File f: workspaceDirectory.listFiles()) {
            totalSize += f.length();
        }
        return totalSize;
    }

}
