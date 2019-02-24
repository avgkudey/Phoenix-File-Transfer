package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerPermissionSend {

    static Selector selector;
    static boolean flag = true;

    public static void messenger(String messages) {
        try {
            System.out.println("Server Permission Send");
            selector = Selector.open();
            SocketChannel connectionClient = SocketChannel.open();
            connectionClient.configureBlocking(false);
//            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), 9100));
connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), PcUIController.portDetails.getReceiverPermission()));
            connectionClient.register(selector, SelectionKey.OP_CONNECT);
            flag = true;
            while (flag) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    SocketChannel client = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        if (client.isConnectionPending()) {
//                            System.out.println("Receiver send message");
                            client.finishConnect();

                        }
                        client.register(selector, SelectionKey.OP_WRITE);
                        continue;
                    }
                    if (key.isWritable()) {
//                        System.out.println(messages);
//                        Scanner sc = new Scanner(System.in);
                        SocketChannel channel = (SocketChannel) key.channel();
                        byte[] message = new String(messages).getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        channel.write(buffer);
//                        System.out.println(message);
                        flag = false;
                    }

                    if (key.isReadable()) {

                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
            ErrorWriter.writer(ex + " : Server permission send -> port 35461");
        }
    }
}
