package pro.sky.skyplagin;


import com.intellij.openapi.ui.DialogWrapper;
import io.socket.engineio.client.transports.WebSocket;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import io.socket.client.IO;
import io.socket.client.Socket;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

public class PanelContent extends DialogWrapper {
    Socket socket;
    IO.Options options = IO.Options.builder().setTransports(new String[]{WebSocket.NAME}).build();
    URI url = URI.create("wss://ws.postman-echo.com/socketio");
    private JTextField value1TextField;
    private JTextField value2TextField;
    private JButton getResultButton;
    private JButton getSocketButton;
    private JButton getTreeButton;
    private JPanel SkyPanel;
    private JFormattedTextField formattedTextField1;
    private JTextArea textArea1;

    public PanelContent(@Nullable Project project){
        super(true);
        init();
        getResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Object result = Integer.parseInt(value1TextField.getText()) + Integer.parseInt(value2TextField.getText());
                    formattedTextField1.setValue(result);
                } catch (NumberFormatException ex) {
                    formattedTextField1.setValue("value1 and value2 they should be numbers!");
                }
            }
        });

        getTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(project.getBasePath());
                StringBuilder sb = new StringBuilder();
                sb.append(file.getName()).append("\n");
                int count = 0;
                doFilePath(count, sb , file.listFiles());
                textArea1.setText(sb.toString());
            }
        });

        getSocketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                        connectToSocket();
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
            }
        });
    }

    private void doFilePath(int count , StringBuilder sb, File[] files){
        count++;
        for (File file : files) {
            sb.append("---".repeat(Math.max(0, count)));
            sb.append(file.getName() + "\n");
            if (file.isDirectory()){
                doFilePath(count,sb,file.listFiles());
            }
        }
    }

    private void connectToSocket() throws InterruptedException {
        socket = IO.socket(url, options);
        socket.on(Socket.EVENT_CONNECT, args -> formattedTextField1.setText("socket connected"));
        socket.on(Socket.EVENT_DISCONNECT, args -> formattedTextField1.setText("socket disabled"));
        socket.connect();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return SkyPanel;
    }

}
