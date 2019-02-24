package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ReceiverMessageSend {

    static Selector selector;
    static boolean flag = true;

    public static void messenger(String messages) {
        try {
            System.err.println("Receiver Messenger");
//             System.err.println(ReceiverConnectionDetails.SendingPcIp);
            selector = Selector.open();
            SocketChannel connectionClient = SocketChannel.open();
            connectionClient.configureBlocking(false);
//            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), 9200));
connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), PcUIController.portDetails.getServerMessage()));
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
//            System.err.println(ReceiverConnectionDetails.SendingPcIp);
            System.err.println(ex + " receiver send");
            ErrorWriter.writer(ex+" : receiver messsage send -> port 35446");
        }
    }
}
