package projectphoenix;

public class PortDetails {

    private int chatServer = 35450;
    private int chatReceiver = 35451;
    private int fileReceiver = 35452;
    private int serverPermission = 35460;
    private int receiverPermission = 35461;
    private int receiverMessage = 35465;
    private int serverMessage = 35466;

    public int getChatServer() {
        return chatServer;
    }

    public void setChatServer(int chatServer) {
        this.chatServer = chatServer;
    }

    public int getChatReceiver() {
        return chatReceiver;
    }

    public void setChatReceiver(int chatReceiver) {
        this.chatReceiver = chatReceiver;
    }

    public int getFileReceiver() {
        return fileReceiver;
    }

    public void setFileReceiver(int fileReceiver) {
        this.fileReceiver = fileReceiver;
    }

    public int getServerPermission() {
        return serverPermission;
    }

    public void setServerPermission(int serverPermission) {
        this.serverPermission = serverPermission;
    }

    public int getReceiverPermission() {
        return receiverPermission;
    }

    public void setReceiverPermission(int receiverPermission) {
        this.receiverPermission = receiverPermission;
    }

    public int getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(int receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public int getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(int serverMessage) {
        this.serverMessage = serverMessage;
    }

}
