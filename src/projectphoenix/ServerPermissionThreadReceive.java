package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerPermissionThreadReceive extends Thread {

    private final int initialState = 0; // initial State
    private final int searchStarted = 1;
    private final int online = 2;
    private final int securityKeySend = 3;
    private final int securityKeyCheck = 4;
    private final int securityKeyAccepted = 5;
    Selector selector;
    ServerSocketChannel server;

    public void run() {
        while (true) {
            try {
                selector = Selector.open();
                server = ServerSocketChannel.open();
                server.configureBlocking(false);
//                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9105));
                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getServerPermission()));

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
                            System.out.println(result + " pre");
                            if (ServerConnectionPermissionController.permissionStates.getState() == searchStarted && result.trim().matches("online")) {
                                System.err.println("Receiver Is online");
                                ServerConnectionPermissionController.permissionStates.setState(online);
                                continue;
                            }
                            if (ServerConnectionPermissionController.permissionStates.getState() == securityKeySend) {
                                System.err.println(" Security Key Incoming");
                                ServerConnectionPermissionController.permissionStates.incomingSecurityKey = result.trim();
                                System.err.println(ServerConnectionPermissionController.permissionStates.incomingSecurityKey);
                                ServerConnectionPermissionController.permissionStates.setState(securityKeyCheck);
                            }

                        }
                        if (key.isWritable()) {
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex + " server_permision_Message_Receive ");
                ErrorWriter.writer(ex + " : Server permission receive -> port 35460");
            }
        }
    }

}
