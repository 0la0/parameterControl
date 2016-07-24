package etc.a0la0.osccontroller.app.osc;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lukeanderson on 6/11/16.
 */
public class OscClient {

    private String oscServerAddress;
    private int port;
    private OSCPortOut oscPortOut;
    private Thread oscWorker;
    private final BlockingQueue<OSCMessage> oscMessageQueue = new LinkedBlockingQueue<OSCMessage>();

    public OscClient(String oscServerAddress, int port) {
        this.oscServerAddress = oscServerAddress;
        this.port = port;
    }

    public void start() {
        oscWorker = buildServerThread();
        oscWorker.start();
    }

    public void stop() {
        //try {
            oscWorker.interrupt();
//            //oscWorker.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private Thread buildServerThread () {
        return new Thread () {
            @Override
            public void run() {
                try {
                    oscPortOut = new OSCPortOut(InetAddress.getByName(oscServerAddress), port);
                } catch(UnknownHostException e) {
                    e.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (!oscMessageQueue.isEmpty()) {
                            OSCMessage message = oscMessageQueue.take();
                            //Log.i("send message", message.getAddress() + ", " + (float) message.getArguments().get(0));
                            try {
                                oscPortOut.send(message);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        };
    }


    public void send (OSCMessage message) {
        this.oscMessageQueue.add(message);
    }

    public void send (List<OSCMessage> messageList) {
        this.oscMessageQueue.addAll(messageList);
    }

}
