package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ChatReceiverThread extends Thread {

    int index = 1;
    static String message;

    public void run() {
        while (true) {
            System.out.println("Chat client receiver");
            try {
                Selector selector = Selector.open();
                ServerSocketChannel server = ServerSocketChannel.open();
                server.configureBlocking(false);
//                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9107));
                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getChatReceiver()));
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
                            message = new String(buff.array());
//                            System.out.println(message + " pre");
//                            kl(result);
//                            ReceiverController.ms.setmessage(result);
                            ReceiveFromPcController.chatterstates.setMsgCount(index);
                            index++;

                        }
                        if (key.isWritable()) {
                        }
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    public static String passMessage() {

        return message;
    }
}
