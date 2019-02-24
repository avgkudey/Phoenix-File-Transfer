package projectphoenix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchClients extends Thread {

    ArrayList<String> addressList;
    static ArrayList<String> hostNames;

    @Override
    public void run() {
        try {
            while (true) {
                System.err.println(getIPAddressList());
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public static ObservableList<String> getIPAddressList() throws Exception {

        ObservableList<String> addresses = FXCollections.observableArrayList();
        try {

            String k = PcUIController.connection.getThisPc();
            String[] ll = k.split("\\.");
            Runtime rt = Runtime.getRuntime();
//            Process pr = rt.exec("nmap -sS -vv -n -p9500 -max-rtt-timeout 100ms 192.168.43.1-255 -T5"); 
//            Process pr = rt.exec("nmap -sS -vv -n -p9100 -max-rtt-timeout 400ms " + ll[0] + "." + ll[1] + "." + ll[2] + ".0-255 -T5");
Process pr = rt.exec("nmap -sS -vv -n -p35461 -max-rtt-timeout 400ms " + ll[0] + "." + ll[1] + "." + ll[2] + ".0-255 -T5");
            
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(pr.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
//                System.out.println(line);
//                if (line.contains("Discovered open port 9100")) {
 if (line.contains("Discovered open port 35461")) {
//                    System.err.println(line);
                    String[] elements = line.split(" ");
                    int end = elements.length - 1;
                    String ip_address = elements[end];
                    String host = elements[elements.length - 2];
                    String line2 = input.readLine();
//                    if (line2.contains("Host is up")) {
                    addresses.add(ip_address);
//                      continue;
//                    }
//                      hostNames.add(host);
                }
            }

            int exitVal = pr.waitFor();
//            System.out.println("Exited with error code " + exitVal);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorWriter.writer(e + " : Search clients ");
//            System.exit(0);
        }
//        System.err.println(hostNames);
        ServerConnectionPermissionController.permissionStates.serverIplist = addresses;
//        System.err.println(ServerConnectionPermissionController.permission.serverIplist + " kllmljl");
        return addresses;
    }
}
