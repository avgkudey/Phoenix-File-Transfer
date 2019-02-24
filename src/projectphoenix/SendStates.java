package projectphoenix;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SendStates {

    private IntegerProperty fileCount;
    private IntegerProperty state;
    private IntegerProperty fileinfo;
    private String fileName;
    private String fileSize;
    private String filePath;
//file path

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
//file Name..................

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }
// File Size........................

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public final int getFileCount() {
        if (fileCount != null) {
            return fileCount.get();
        }
        return 0;
    }
//File Count........................

    public final void setFileCount(int fileCount) {
        this.fileCountProperty().set(fileCount);
    }

    public final IntegerProperty fileCountProperty() {
        if (fileCount == null) {
            fileCount = new SimpleIntegerProperty(0);
        }
        return fileCount;
    }

    public final int getState() {
        if (state != null) {
            return state.get();
        }
        return 0;
    }
//State..................................

    public final void setState(int state) {
        this.stateProperty().set(state);
    }

    public final IntegerProperty stateProperty() {
        if (state == null) {
            state = new SimpleIntegerProperty(0);
        }
        return state;
    }

    /////////////////////////////////////////////////
    //File Info..................................
    public final int getfileinfo() {
        if (fileinfo != null) {
            return fileinfo.get();
        }
        return 0;
    }

    public final void setfileinfo(int fileinfo) {
        this.fileinfoproperty().set(fileinfo);
    }

    public final IntegerProperty fileinfoproperty() {
        if (fileinfo == null) {
            fileinfo = new SimpleIntegerProperty(0);
        }
        return fileinfo;
    }
}
