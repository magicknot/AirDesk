package pt.ulisboa.tecnico.cmov.airdesk.workspace;

/**
 * Created by oliveira on 29/03/15.
 */
public class WorkspaceTag {

    private String tag;

    public WorkspaceTag() {

    }

    public WorkspaceTag(String tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "WorkspaceTag{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
