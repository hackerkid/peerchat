import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class PeerChatServer {

   
    public static String KEY = "KEYKEYXX";
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    private static final int PORT = 8888;


    
    public static String encrypto(String inp) {
        if(inp == "" || inp == null)
            return inp;
        
        try{
            byte[] textEncrypted;   

            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");

            byte[] desKeyData = KEY.getBytes("ISO-8859-1");
            DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey myDesKey = keyFactory.generateSecret(desKeySpec);
            
            Cipher desCipher;

            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

            byte[] text = inp.getBytes("ISO-8859-1");
            textEncrypted = desCipher.doFinal(text);


            return new String(textEncrypted, "ISO-8859-1") ;
            
        }catch(Exception e) {

            return  null;
        }
        
    }

    public static String decrypto(String response)
    {   
        if(response == "" || response == null)
            return response;

        try {
            
            byte[] desKeyData2 = KEY.getBytes("ISO-8859-1");
            byte[] textEncrypted = response.getBytes("ISO-8859-1");
            Cipher desCipher;
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");


            DESKeySpec desKeySpec2 = new DESKeySpec(desKeyData2);
            SecretKeyFactory keyFactory2 = SecretKeyFactory.getInstance("DES");
            SecretKey myDesKey2 = keyFactory2.generateSecret(desKeySpec2);
            
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey2);
            
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);
            

            String temp = new String(textDecrypted,"ISO-8859-1");
            return temp;
        }

        catch(Exception e) {
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Peer Chat server is now running");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

      
        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void print(boolean x)
        {
            System.out.println(x);
        }
        
        public void print(String x)
        {
            System.out.println(x);
        }
        
        public void run() {
            try {

                String enc;

                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

              
                while (true) {
                    out.println(encrypto("LOGIN"));
                    name = decrypto(in.readLine());
                    
                   
                    synchronized (names) {
                        if (name.equals("batmandarkknight")) {
                            name = "batman";
                            names.add(name);
                            print(name + " joined");
                            break;
                        }

                        if (name.equals("ironmantonystark")) {
                            name = "ironman";
                            names.add(name);
                            print(name + " joined");

                            break;
                        }

                        if (name.equals("hulkiiita")) {
                            name = "hulk";
                            names.add(name);
                            print(name + " joined");

                            break;
                        }

                        if (name.equals("jonsnowiiita")) {
                            name = "jonsnow";
                            names.add(name);
                            print(name + " joined");

                            break;
                        }

                        if (name.equals("pottermalfoy") && !names.contains(name)) {
                            name = "potter";
                            names.add(name);
                            print(name + " joined");

                            break;
                        }

                        if (name.equals("snapeiiita") && !names.contains(name)) {
                            name = "snape";
                            names.add(name);
                            print(name + " joined");

                            break;
                        }
                    }
                }

             
                out.println(encrypto("HANDSHAKE"));
                writers.add(out);

             
                while (true) {
                    String input = decrypto(in.readLine());
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println(encrypto("STREAM " + name + ": " + input));
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
            
                if (name != null) {
                    names.remove(name);

                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}