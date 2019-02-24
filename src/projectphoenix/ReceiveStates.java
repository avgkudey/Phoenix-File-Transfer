package projectphoenix;

import java.io.File;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ReceiveStates {

    private IntegerProperty fileCount;
    private IntegerProperty state;
    private IntegerProperty fileRes;
    private String fileName;
    private long fileSize;
    private String message;
    private List<String> flist;
    private List<String> fsList;
    private String outPath;

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public final int getFileRes() {
        if (fileRes != null) {
            return fileRes.get();
        }
        return 0;
    }

    public final void setFileRes(int fileRes) {
        this.fileResProperty().set(fileRes);
    }

    public final IntegerProperty fileResProperty() {
        if (fileRes == null) {
            fileRes = new SimpleIntegerProperty(0);
        }
        return fileRes;
    }

    ///////////////////////////////////
    public List<String> getFsList() {
        return fsList;
    }

    public void setFsList(List<String> fsList) {
        this.fsList = fsList;
    }

    public List<String> getFlist() {
        return flist;
    }

    public void setFlist(List<String> flist) {
        this.flist = flist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //file Name................
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //file Size...................
    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    //file count.....................
    public final int getFileCount() {
        if (fileCount != null) {
            return fileCount.get();
        }
        return 0;
    }

    public final void setFileCount(int fileCount) {
        this.fileCountProperty().set(fileCount);
    }

    public final IntegerProperty fileCountProperty() {
        if (fileCount == null) {
            fileCount = new SimpleIntegerProperty(0);
        }
        return fileCount;
    }
//state......................

    public final int getState() {
        if (state != null) {
            return state.get();
        }
        return 0;
    }

    public final void setState(int state) {
        this.stateProperty().set(state);
    }

    public final IntegerProperty stateProperty() {
        if (state == null) {
            state = new SimpleIntegerProperty(0);
        }
        return state;
    }
}
