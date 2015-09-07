import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
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




public class PeerChatClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("PeerChat");
    ImageIcon ico = new ImageIcon("pk.png");
    JLabel t = new JLabel(ico);
    JTextField textField = new JTextField(40);
    EmptyBorder eb = new EmptyBorder(new Insets(0, 0, 0, 0));
    JTextPane txt = new MyTextPane();
    JScrollPane tPane = new JScrollPane(txt);    
    public static String KEY = "KEYKEYXX";

    public static Color hex2Rgb(String colorStr) {
            return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

    private static class MyTextPane extends JTextPane {
        

        public MyTextPane() {
            super();
            setOpaque(false);
            setBackground(new Color(0,0,0,0));
        }

        @Override
        protected void paintComponent(Graphics g) {
              Color col = hex2Rgb("#F1F0C7");

            g.setColor(col);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

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
            

            String temp = new String(textDecrypted, "ISO-8859-1");
            return temp;
        }

        catch(Exception e) {
            return null;
        }
    }
 
    
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
   
    public PeerChatClient() {

        textField.setEditable(false);
        frame.getContentPane().add(textField, "North");
        tPane.setBorder(eb);
        txt.setMargin(new Insets(10, 10, 500, 0));
        frame.getContentPane().add(tPane);
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(encrypto(textField.getText()));
                textField.setText("");
            }
        });
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter The Server IP Address:",
            "PeerChat Connect",
            JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        
       JTextField xField = new JTextField(10);
       JTextField yField = new JTextField(10);

      JPanel myPanel = new JPanel();
      myPanel.add(new JLabel("Username:"));
      myPanel.add(xField);
      myPanel.add(new JLabel("Password:"));
      myPanel.add(yField);

      int result = JOptionPane.showConfirmDialog(null, myPanel, 
               "Login In", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
         String temp = xField.getText() + yField.getText();
         return temp;
      }

      String temp = xField.getText() + yField.getText();
      return temp;


     
    }

   
    private void run() throws IOException {

        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 8888);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        int count = 0;

        while (true) {
            String line = decrypto(in.readLine());
            if(line == null) {
                continue;
            }
            if (line.startsWith("LOGIN")) {
                out.println(encrypto(getName()));
            } else if (line.startsWith("HANDSHAKE")) {
                textField.setEditable(true);
            } else if (line.startsWith("STREAM")) {
               // messageArea.append(line.substring(8) + "\n");
                if(count % 2 == 0) {
                    Color blue = hex2Rgb("#124E78");
                    appendToPane(txt, line.substring(7) + "\n", blue);
                    count++;
                }

                else {
                    Color orang = hex2Rgb("#D74E09");

                    appendToPane(txt, line.substring(7) + "\n", orang);
                    count++;


                }

            }
        }
    }

   
    public static void main(String[] args) throws Exception {
        PeerChatClient client = new PeerChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
