package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerMessageReceive extends Thread {

    private final int initialState = 0; // initial State
    private final int permissionReqState = 1; // requesting Permission
    private final int permissionAcceptedState = 2; // connection Live
    private final int fileCountSendState = 3; //file count send
    private final int fileNameSendState = 4; //file name send
    private final int fileSizeSendState = 5; //file size send
    private final int filePendingState = 6; // file waiting to send
    private final int fileSendingState = 7; // sending file
    private final int fileSentState = 8; // file sending complete
    private final int operationCompletedState = 9; // transfer complete
    private final int permissionDeniedState = 10; // denied state;

    public void run() {
        System.out.println("Server Message Receive Thread Started");
        while (true) {
            try {
                Selector selector = Selector.open();
                ServerSocketChannel server = ServerSocketChannel.open();
                server.configureBlocking(false);
//                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9200));
                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getServerMessage()));
                server.register(selector, SelectionKey.OP_ACCEPT);
                while (true) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            continue;
                        }
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buff = ByteBuffer.allocate(256);
                            channel.read(buff);
                            String result = new String(buff.array());
//                            System.out.println(result);

                            if (result.trim().matches("accepted")) {
                                SendToPcController.sendStates.setState(permissionAcceptedState);
                            }
                            if (result.trim().matches("Denied")) {
                                SendToPcController.sendStates.setState(permissionDeniedState);
                            }
                            if (SendToPcController.sendStates.getState() == fileCountSendState && result.trim().matches("received")) {
                                SendToPcController.sendStates.setState(fileNameSendState);
                            }
                        }
                        if (key.isWritable()) {
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex + " server_Message_Receive ");
                ErrorWriter.writer(ex + " : Server message receive -> port 35466");
            }
        }
    }
}
