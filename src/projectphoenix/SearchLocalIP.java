package projectphoenix;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SearchLocalIP extends Thread {

    public void run() {
        searchwifi();
        searchlan();
    }

    public void searchwifi() {

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("ipconfig");
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(pr.getInputStream()));
            String line = null;
            String line1 = null;
            while ((line1 = input.readLine()) != null) {
                if (line1.contains("Wireless LAN adapter WiFi")) {
                    while ((line = input.readLine()) != null) {
                        if (line.contains("IPv4 Address")) {
                            String[] elements = line.split(" ");
                            int end = elements.length - 1;
                            String ip_address = elements[end];
                            String host = elements[elements.length - 2];
                            String line2 = input.readLine();
                            PcUIController.ipwifi = ip_address;
                            PcUIController.cmbiplist.add(ip_address);
                            System.err.println(ip_address);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);

        }
    }

    public void searchlan() {

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("ipconfig");
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(pr.getInputStream()));
            String line = null;
            String line1 = null;
            while ((line1 = input.readLine()) != null) {
                if (line1.contains("Ethernet adapter Ethernet")) {
                    while ((line = input.readLine()) != null) {

                        if (line.contains("IPv4 Address. . . . .")) {
                            String[] elements = line.split(" ");
                            int end = elements.length - 1;
                            String ip_address = elements[end];
                            String host = elements[elements.length - 2];
                            String line2 = input.readLine();
                            PcUIController.iplan = ip_address;
                            PcUIController.cmbiplist.add(ip_address);
                            System.err.println(ip_address);
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }
}
