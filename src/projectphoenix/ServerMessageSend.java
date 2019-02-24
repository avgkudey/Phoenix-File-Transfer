package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerMessageSend {

    static Selector selector;
    static boolean flag = true;
    static String fixedMessage;

    public static void messenger(String messages) {

        try {
            System.err.println("Server Messenger");
            fixedMessage = messages;
            selector = Selector.open();
            SocketChannel connectionClient = SocketChannel.open();
            connectionClient.configureBlocking(false);
//            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), 9300));
connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), PcUIController.portDetails.getReceiverMessage()));
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
                            client.finishConnect();
                        }
                        client.register(selector, SelectionKey.OP_WRITE);
                        continue;
                    }
                    if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        byte[] message = new String(messages).getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        channel.write(buffer);
                        flag = false;
                    }
                    if (key.isReadable()) {
                    }
                }
            }
        } catch (Exception ex) {
            messenger(fixedMessage);
            System.err.println(ex);
            ErrorWriter.writer(ex + " : Server message send -> port 35465");
        }
    }
}
