package projectphoenix;

import javafx.beans.property.SimpleStringProperty;

public class ReceiverTablePopulate {

    private final SimpleStringProperty name;
    private final SimpleStringProperty size;

    public String getName() {
        return name.get();
    }

    public String getSize() {
        return size.get();
    }

    public ReceiverTablePopulate(String name, String size) {
        super();
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
    }
}
