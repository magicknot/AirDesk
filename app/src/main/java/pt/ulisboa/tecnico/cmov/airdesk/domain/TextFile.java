package pt.ulisboa.tecnico.cmov.airdesk.domain;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TextFile implements Externalizable {
    public static final String TAG = "TextFile";

    private String name;
    private File file;

    //TODO ACL - ACL will be a a Map[User, Permissions] and this will be used to know when a File
    //           should not be sent to the user. When externalizing this class, the ACL externalized
    //           should be the one matching the remote user

    public TextFile(String path, String name) {
        this.name = name;
        this.file = new File(path, name);
    }

    public TextFile(String path, String name, String content) {
        this(path, name);
        write(content);
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }

    public void write(String content) {
        if (content.length() <= 0) {
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "write() - could not find file " + this.name);
        } catch (IOException e) {
            Log.e(TAG, "write() - could not write to file " + this.name);
        }
    }

    public String read() {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;

            while ((line = in.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "read() - could not find file " + this.name);
        } catch (IOException e) {
            Log.e(TAG, "write() - could not read file " + this.name);
        }
        return text.toString();
    }

    public boolean delete() {
        return file.delete();
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.name = objectInput.readUTF();
        this.file = (File) objectInput.readObject();
        //TODO ACL
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeUTF(this.name);
        objectOutput.writeObject(file);
        //TODO ACL
    }
}
