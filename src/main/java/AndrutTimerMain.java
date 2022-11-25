import javax.sound.sampled.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class AndrutTimerMain implements LineListener {

    boolean isPlaybackCompleted;

    @Override
    public void update(LineEvent event) {

        if (LineEvent.Type.START == event.getType()) {
            System.out.println("Playback started.");
        } else if (LineEvent.Type.STOP == event.getType()) {
            isPlaybackCompleted = true;
            System.out.println("Playback completed.");
        }
    }

    /**
     * Play a given audio file.
     *
     * @param audioFilePath Path of the audio file.
     */
    void play(String audioFilePath) {
        try {


            //read audio data from whatever source (file/classloader/etc.)
            InputStream audioSrc = getClass().getResourceAsStream(audioFilePath);
//add buffer for mark/reset support
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);





//            InputStream inputStream = getClass().getClassLoader()
//                    .getResourceAsStream(audioFilePath);
//            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);

            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
            while (!isPlaybackCompleted) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            audioClip.close();
            audioStream.close();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
            System.out.println("Error occured during playback process:" + ex.getMessage());
        }

    }


    static String readFromUser() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        String name = reader.readLine();

        // Printing the read line
        //  System.out.println(name);
        return name;
    }

    static void consoleThing() throws IOException, InterruptedException {
        System.out.println("Andrut za ile chcesz wstać pierwszy raz?");

        int chosenTime = Integer.valueOf(readFromUser());

        System.out.println("Andt wybrałeś : " + chosenTime + " minut pracy zanim pierdolnie pierwszy alarm");


        sleep(chosenTime);
    }

    static void sleep(int sleep) throws InterruptedException {
        TimeUnit.MINUTES.sleep(sleep);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

//        final Process p = Runtime.getRuntime().exec("java -jar map.jar time.rel test.txt debug");
//
//        new Thread(new Runnable() {
//            public void run() {
//                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = null;
//
//                try {
//                    while ((line = input.readLine()) != null)
//                        System.out.println(line);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        p.waitFor();


        String audioFilePath = "AndrutAlarm.wav";
        consoleThing();


        // Clip can not play mpeg/mp3 format audio. We'll get exception if we run with below commented mp3 and mpeg format audio.
        // String audioFilePath = "AudioFileWithMpegFormat.mpeg";
        // String audioFilePath = "AudioFileWithMp3Format.mp3";

        AndrutTimerMain player = new AndrutTimerMain();
        player.play(audioFilePath);
    }
}