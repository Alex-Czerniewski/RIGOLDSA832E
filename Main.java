/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

/**
 *
 * @author Alex Czerniewski
 * https://magna-power.com/learn/kb/instrumentation-programming-with-python
 * 
 * Used Netbeans
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        // make socket server with DSA832E
        int port = 5555;
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(10000);
        } catch (SocketException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.connect(new InetSocketAddress("192.168.0.146", port), 10000);

            try ( // send IDN?
                OutputStream outputStream = socket.getOutputStream()) {
                outputStream.write("*IDN?\r\n".getBytes());
                
                try (InputStream inputStream = socket.getInputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead = inputStream.read(buffer);
                    String idn = new String(buffer, 0, bytesRead);
                    System.out.println(idn);
                    
                    // set center frequency and span
                    String Centercmd = "SENS:FREQ:CENT 1000000\n";
                    String Spancmd = "SENS:FREQ:SPAN 10000000\n";
                    
                    outputStream.write(Centercmd.getBytes());
                    outputStream.write(Spancmd.getBytes());
                    
                    // sweep
                    int F = 1000000;
                    
                    for (int i = 0; i <= 11; i++) {
                        F += 100000000;
                        String f = String.valueOf(F);
                        String cmd = "SENS:FREQ:CENT " + f + "\n";
                        System.out.println(cmd);
                        outputStream.write(cmd.getBytes());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    socket.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
* https://rfmw.em.keysight.com/wireless/helpfiles/e5055a/Programming/GP-IB_Command_Finder/Sense/Frequency.htm
*
* List of commands for center frequency
*
* SENS:FREQ:CENT 1000000
*
* sense2:frequency:center 1mhz
*
* sense2:frequency:center 1e6
*
* ---------------------------
*
* List of commands for span
*
* SENS:FREQ:SPAN 1000000
* sense2:frequency:span max
*
* --------------------------
*
* List of commands for sweep
*
* SENS:SWE:DWEL .1
* sense2:sweep:dwell min
*/
