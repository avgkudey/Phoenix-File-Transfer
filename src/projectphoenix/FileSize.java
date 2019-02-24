package projectphoenix;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class FileSize {

    private IntegerProperty fileSize;

    public final int getfileSize() {
        if (fileSize != null) {
            return fileSize.get();
        }
        return 0;
    }

    public final void setfileSize(int state) {
        this.fileSizeProperty().set(state);
    }

    public final IntegerProperty fileSizeProperty() {
        if (fileSize == null) {
            fileSize = new SimpleIntegerProperty(0);
        }
        return fileSize;
    }
}
