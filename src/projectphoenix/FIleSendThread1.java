package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class FIleSendThread1 {

    private final int initialState = 0; // initial State
    private final int permissionReqState = 1; // requesting Permission
    private final int permissionAcceptedState = 2; // connection Live
    private final int fileCountSendState = 3; //file count send
    private final int fileNameSendState = 4; //file name send
    private final int fileSizeSendState = 5; //file size send
    private final int filePendingState = 6; // file waiting to send
    private final int fileSendingState = 7; // sending file
    private static final int fileSentState = 8; // file sending complete
    private final int operationCompletedState = 9; // transfer complete
    private final int permissionDeniedState = 10; // denied state;
    static String sendPath;

    public static void fileSendThreadRun() {

        try {

            Selector selector = Selector.open();
            SocketChannel connectionClient = SocketChannel.open();
            connectionClient.configureBlocking(false);
//            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), 9110));
            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), PcUIController.portDetails.getFileReceiver()));
            connectionClient.register(selector, SelectionKey.OP_CONNECT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    SocketChannel client = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        if (client.isConnectionPending()) {
                            System.out.println("Trying to finish connection");
                            client.finishConnect();
                        }
                        client.register(selector, SelectionKey.OP_WRITE);
                        continue;
                    }
                    if (key.isWritable()) {

                        sendFile(client);
                        client.close();
                        connectionClient.socket().close();
                        connectionClient.close();
                        return;
                    }
                    if (key.isReadable()) {
                    }
                }
            }
        } catch (Exception ex) {
            ErrorWriter.writer(ex + " : File Send Thread -> port 35455");
            System.out.println(ex + "  public static void main(String[] args)");
        }
    }

    private static void sendFile(SocketChannel client) {
        try {
            SendToPcController.fs.setfileSize(0);
            int BufferSize = 10240;
            Path path = Paths.get(sendPath.toString());
            FileChannel fileChannel = FileChannel.open(path);
            ByteBuffer buf = ByteBuffer.allocate(BufferSize);
            int counter = 0;
            int noOfBytesRead = 0;
            do {
                noOfBytesRead = fileChannel.read(buf);
                if (noOfBytesRead <= 0) {
                    break;
                }
                counter += noOfBytesRead;
                SendToPcController.fs.setfileSize(SendToPcController.fs.getfileSize() + noOfBytesRead);
//                SendToPcController.fs.setfileSize(counter);
                buf.flip();
                do {
                    noOfBytesRead -= client.write(buf);
                } while (noOfBytesRead > 0);
                buf.clear();
            } while (true);
            fileChannel.close();
            System.out.println("Receiver: " + counter);
            System.out.println("File Send Success");
//            SendToPcController.finfo++;
            SendToPcController.sendStates.setfileinfo(SendToPcController.sendStates.getfileinfo()+1);
//            SendToPcController.sendStates.setState(fileSentState);

        } catch (Exception ex) {
            System.out.println(ex + " private static void sendFile(SocketChannel client)");
            ErrorWriter.writer(ex + " : File Send Thread -> SendFile");
        }
    }

    public static void setpath(String path) {
        sendPath = path;
    }
}
