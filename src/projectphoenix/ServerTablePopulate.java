package projectphoenix;

import javafx.beans.property.SimpleStringProperty;

public class ServerTablePopulate {

    private final SimpleStringProperty name;
    private final SimpleStringProperty size;
    private final SimpleStringProperty path;

    public String getName() {
        return name.get();
    }

    public String getSize() {
        return size.get();
    }

    public String getPath() {
        return path.get();
    }

    public ServerTablePopulate(String name, String size, String path) {
        super();
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
        this.path = new SimpleStringProperty(path);
    }
}
