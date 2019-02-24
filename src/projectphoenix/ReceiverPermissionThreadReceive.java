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
 * @author AVG Chamara
 */
public class ReceiverPermissionThreadReceive extends Thread {

    private final int initialState = 0;
    private final int ipReceived = 1;
    private final int receiverIpsend = 2;
    private final int permissionWaiting = 3;
    private final int securityKeySend = 4;
    private final int securityKeyAccepted = 5;
    private final int securityKeyDenied = 6;
    private final int securityKeyAcceptedAfter = 7;
    private final int waitingForSenderIp = 8;

    public void run() {

        System.out.println("ReceiverPermission");
        while (true) {
            try {
                Selector selector = Selector.open();
                ServerSocketChannel server = ServerSocketChannel.open();
                server.configureBlocking(false);
//                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9100));
  server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getReceiverPermission()));
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
                            System.out.println(result + " receiver");

                            if (ReceiverConnectionPermissionController.permissionStates.getState() == initialState) {
                                PcUIController.connection.setClient(result);
                                ReceiverConnectionPermissionController.permissionStates.setState(ipReceived);
                            }
//
//                            if (result.trim().matches("check")) {
//                                ReceiverConnectionPermissionController.permissionStates.setState(searchStarted);
//                            }
                            if (result.trim().matches("permission")) {
                                ReceiverConnectionPermissionController.permissionStates.setState(permissionWaiting);
                            }
                            if (result.trim().matches("accepted")) {
                                ReceiverConnectionPermissionController.permissionStates.setState(securityKeyAccepted);
                            }
                            if (result.trim().matches("denied")) {
                                ReceiverConnectionPermissionController.permissionStates.setState(initialState);
                            }
                        }
                        if (key.isWritable()) {
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex + " server_Message_Receive ");
                ErrorWriter.writer(ex + " : receiver permission receive thread -> port 35461");
            }
        }

    }
}
