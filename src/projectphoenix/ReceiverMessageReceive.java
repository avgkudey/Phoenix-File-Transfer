package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;

public class ReceiverMessageReceive extends Thread {

    private final int initialState = 0; // initial State
    private final int permissionWatingState = 1; // waiting for permission
    private final int permissionAcceptedState = 2; // permission Accepted
    private final int permissionDeniedState = 3; //permission Denied
    private final int fileCountWaitState = 4; // file cout waiting
    private final int fileNameWaitState = 5; // file name waiting
    private final int fileSizeWaitState = 6; // file size waiting
    private final int fileWaitingState = 7; // waiting for incoming
    private final int fileReceivingState = 8; // file incomming
    private final int fileReceivedState = 9; // file received
    private final int operationCompletedState = 10; // file tranfer completed

    public void run() {
        while (true) {
            try {
                System.err.println("Receiver Message Receiver");
                Selector selector = Selector.open();
                ServerSocketChannel server = ServerSocketChannel.open();
                server.configureBlocking(false);
//                server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9300));
                 server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getReceiverMessage()));
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
//                            System.out.println(result + " pre");
                            if (result.trim().matches("fsend")&&ReceiveFromPcController.receiveStates.getState()==fileWaitingState) {
                                ReceiveFromPcController.receiveStates.setState(operationCompletedState);
                            }
                            if (result.trim().matches("permission")) {
                                ReceiveFromPcController.receiveStates.setState(permissionWatingState);
                                System.out.println("receiving permission request");
                            }
                            if (ReceiveFromPcController.receiveStates.getState() == fileCountWaitState) {
                                ReceiveFromPcController.receiveStates.setFileCount(Integer.valueOf(result.trim()));
                                ReceiveFromPcController.receiveStates.setState(fileNameWaitState);
                            }
                            if (ReceiveFromPcController.receiveStates.getState() == fileNameWaitState) {
                                if (result.trim().matches("fnend")) {
                                    System.out.println("End of file names");
                                    ReceiveFromPcController.receiveStates.setState(fileSizeWaitState);
                                } else {
                                    String[] items = result.split(",");
                                    ReceiveFromPcController.receiveStates.setFlist(Arrays.asList(items));
                                    System.out.println(ReceiveFromPcController.receiveStates.getFlist().get(0));
                                }
                            }
                            if (ReceiveFromPcController.receiveStates.getState() == fileSizeWaitState) {
                                if (result.trim().matches("fsend")) {
                                    System.out.println("End of file sizes");
                                    ReceiveFromPcController.receiveStates.setState(fileWaitingState);
                                } else {
                                    String[] itemsSize = result.split(",");
                                    ReceiveFromPcController.receiveStates.setFsList(Arrays.asList(itemsSize));
                                    System.out.println(ReceiveFromPcController.receiveStates.getFsList().get(0));
                                }
                            }
                        }
                        if (key.isWritable()) {
                        }
                    }
                }
            } catch (Exception ex) {
                ErrorWriter.writer(ex+" : receiver message receive -> port 35445");
                System.err.println(ex);
            }
        }
    }
}
