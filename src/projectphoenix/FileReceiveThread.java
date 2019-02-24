package projectphoenix;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;

public class FileReceiveThread extends Thread {

    private static final int operationCompletedState = 10; // file tranfer completed
    static int fileCount = 0;

    @Override
    public void run() {
        try {
            System.err.println("File Receiver Started");
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
//            server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), 9110));
            server.socket().bind(new InetSocketAddress(PcUIController.connection.getThisPc(), PcUIController.portDetails.getFileReceiver()));
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
                        System.out.println("Synjnjnjnknj");
                        SocketChannel channel = (SocketChannel) key.channel();
                        ReceiveFromPcController.fileSizeProperty.setfileSize(0);
                        receiveFile(channel);
                        channel.close();
//                        return;
                        continue;
                    }
//                    Thread.currentThread().interrupt();
                    if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        byte[] message = new String("request").getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        channel.write(buffer);
                        channel.close();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorWriter.writer(ex + " : File Receive Thread -> port 35455");
            System.out.println(ex + " public static void main(String[] args)");
        }

    }

    private static void receiveFile(SocketChannel channel) {
        try {
//            String outputFile = fileCount + ".zip";

            String outputFile = ReceiveFromPcController.receiveStates.getFlist().get(fileCount);
            System.err.println(outputFile);
            HisWritter.writter(ReceiveFromPcController.receiveStates.getFlist().get(fileCount) + "   "
                    + (Integer.valueOf(ReceiveFromPcController.receiveStates.getFsList().get(fileCount))) / (1024 * 1024) + " Mb");
            fileCount++;
            int bufferSize = 10240;
            Path path = Paths.get(ReceiveFromPcController.savePath + outputFile);
            FileChannel fileChannel = FileChannel.open(path,
                    EnumSet.of(StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.WRITE)
            );

            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            int res = 0;
            int counter = 0;
            do {
                buffer.clear();
                res = channel.read(buffer);
                ReceiveFromPcController.fileSizeProperty.setfileSize(ReceiveFromPcController.fileSizeProperty.getfileSize() + res);
//                System.out.println(res);
                buffer.flip();
                if (res > 0) {
                    fileChannel.write(buffer);
                    counter += res;
                }
            } while (res >= 0);
            channel.close();
//            System.out.println("Receiver: " + counter);
            System.out.println(fileCount + " File Receive Success");
            ReceiveFromPcController.fileSizeProperty.setfileSize(Integer.valueOf(ReceiveFromPcController.receiveStates.getFsList().get(fileCount - 1)) + 100);
            if (fileCount - 1 == ReceiveFromPcController.receiveStates.getFileCount()) {
                ReceiveFromPcController.receiveStates.setState(operationCompletedState);
            }

        } catch (Exception ex) {
            System.out.println(ex + " in FileReceive Thread FileReceive Method");
            ErrorWriter.writer(ex + " : File Send Thread -> ReceiveFile");
        }
    }
}
