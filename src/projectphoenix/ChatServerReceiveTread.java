/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 *
 * @author KASUN
 */
public class ChatServerReceiveTread extends Thread {

    int index = 0;
    static String message;

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
//            server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9108));
            server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getChatServer()));
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

                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer ab = ByteBuffer.allocate(256);
                        channel.read(ab);
                        message = new String(ab.array()).trim();
//                        System.out.println(message);
                        index++;
                        SendToPcController.chatterstates.setMsgCount(index);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e + " receiver");

        }
    }

    public static String passMessage() {
        return message;
    }
}
