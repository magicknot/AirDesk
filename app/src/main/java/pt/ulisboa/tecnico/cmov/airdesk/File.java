package pt.ulisboa.tecnico.cmov.airdesk;

/**
 * Created by oliveira on 27/03/15.
 */
public class File {
    private String name;
    private int size;
    private String state;
    private String path;

    public File(String name, int size, String state) {
        this.name = name;
        this.size = size;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
