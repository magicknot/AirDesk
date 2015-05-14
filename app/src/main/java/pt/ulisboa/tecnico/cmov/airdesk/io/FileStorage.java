package pt.ulisboa.tecnico.cmov.airdesk.io;

import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;

public final class FileStorage {

    public static final String TAG = "FileManager";

    private FileStorage() {
        // We deliberately do nothing
    }

    private static File getFile(TextFile file, Context context) {
        return new File(context.getDir(file.getPath(), Context.MODE_PRIVATE).getAbsolutePath(),
                file.getName());
    }

    public static void save(TextFile file, Context context) {
        Log.i(TAG, "save() - creating empty file");
        save(file, "", context);
    }

    public static void save(TextFile file, String content, Context context) {
        File f = getFile(file, context);

        try {
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(f));
            output.write(content.getBytes());
            output.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "save() - file not found " + f.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "save() - could not save changes on " + f.getAbsolutePath());
        }
    }

    public static String read(TextFile file, Context context) {
        File f = getFile(file, context);
        StringBuilder result  = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append('\n');
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "read() - file not found " + f.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "read() - could not read line in file " + f.getAbsolutePath());
        }
        return result.toString();
    }

    public static boolean delete(TextFile file, Context context) {
        return getFile(file, context).delete();
    }

    public static boolean deleteWorkspace(String name, Context context) {
        return context.getDir(name, Context.MODE_PRIVATE).delete();
    }

    public static long getFreeSpace(Context context) {
        return context.getFilesDir().getUsableSpace();
    }

    public static long getUsedSpace(String workspaceName, Context context) {
        long totalSize = 0;
        File workspaceDirectory = new File(context.getDir(workspaceName, Context.MODE_PRIVATE)
                .getAbsolutePath());

        if (workspaceDirectory.isDirectory()) {
            for (File f : workspaceDirectory.listFiles()) {
                totalSize += f.length();
            }
        }
        return totalSize;
    }

}
