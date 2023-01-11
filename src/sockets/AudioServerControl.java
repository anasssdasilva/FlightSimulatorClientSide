package sockets;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioServerControl implements Runnable{

    private static String fileName;

    public AudioServerControl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run () {
        try {
            ServerSocket serverSocker = new ServerSocket();
            Socket client = null;
            serverSocker.bind(new InetSocketAddress(6664));
            if (serverSocker.isBound()) {
                client = serverSocker.accept();
                OutputStream out = client.getOutputStream();
                while (true) {
                    AudioInputStream ain = testPlay(fileName);
                    if (ain != null) {
                        AudioSystem.write(ain, AudioFileFormat.Type.WAVE, out);
                    }
                }
            }
            serverSocker.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static AudioInputStream testPlay(String filename) {
        AudioInputStream din = null;
        try {
            File file = new File(filename);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            System.out.println("Before :: " + in.available());

            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat =
                    new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, baseFormat.getSampleRate(),
                            8, baseFormat.getChannels(), baseFormat.getChannels(),
                            baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            System.out.println("After :: " + din.available());
            return din;
        } catch (Exception e) {
            // Handle exception.
            e.printStackTrace();
        }
        return din;
    }
}
