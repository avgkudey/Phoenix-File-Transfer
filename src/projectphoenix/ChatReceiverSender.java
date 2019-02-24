package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatReceiverSender {

    static Selector selector;
    static boolean flag = true;
    static String msg;

    public static void msge(String messages) {

        try {
            System.err.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
            msg = messages;
            selector = Selector.open();
            SocketChannel connectionClient = SocketChannel.open();
            connectionClient.configureBlocking(false);
//            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), 9108));
            connectionClient.connect(new InetSocketAddress(PcUIController.connection.getClient(), PcUIController.portDetails.getChatServer()));
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
                        Scanner sc = new Scanner(System.in);
                        SocketChannel channel = (SocketChannel) key.channel();
                        byte[] message = new String(messages).getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        channel.write(buffer);
                        flag = false;
//                        return;
                    }

                    if (key.isReadable()) {

                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
            msge(msg);
        }
    }
}
