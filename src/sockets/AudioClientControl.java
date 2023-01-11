package sockets;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class AudioClientControl implements Runnable{
    private static Socket socket;
    private static BufferedInputStream inputStream;
    public static String name="";

    public void run() {
        try {
            socket = new Socket("192.168.43.55", 6665);
            if (socket.isConnected()) {
                inputStream = new BufferedInputStream(socket.getInputStream());
                AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);

                try {
                    Random rand = new Random(); //instance of random class
                    int upperbound = 25;
                    int int_random = rand.nextInt(upperbound);
                    name="fromServer.wav"+int_random;
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(name));

                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            socket.close();
        } catch (IOException | UnsupportedAudioFileException e) {
            System.err.println(e);
        }
    }


}
