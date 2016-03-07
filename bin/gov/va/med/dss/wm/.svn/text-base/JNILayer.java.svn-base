/**
 * Implements the communication with the Windows Messaging DLL
 * The client should register itself as a message listener, and should
 * only invoke listenForMessages once (since it is a blocking call, that
 * sets up the listener).
 *
 */
package gov.va.med.dss.wm;

import java.util.Iterator;
import java.util.Vector;

public class JNILayer {
    // The blocking message listening JNI call.  The callback function
    // must take a string with the message as its sole argument

    native public void listenForMessages(String callbackFunc, String logDir);

    // Use this function to get the name of the currently logged in
    // Windows user
    native public String getUserName();

    static {
        try {
            //System.out.println("Loading library");
            System.loadLibrary("AthenaWM");
            System.out.println("Loaded AthenaWM library");
        } catch (Throwable e) {
            System.out.println("ERROR LOADING WINDOWS MESSAGING LIBRARY");
            e.printStackTrace();
            System.exit(0);
        }
    }
    static JNILayer _instance = null;
    Vector vector = new Vector();

    private JNILayer() {
        super();
    }

    /**
     * Static instance retrieval
     * @return The singleton instance of the JNILayer
     */
    public static JNILayer getInstance() {
        if (_instance == null) {
            _instance = new JNILayer();
        }
        return _instance;
    }

    /**
     * Adds the given listener to be notified when messages are received
     * @param listener
     */
    public void addListener(IMessageListener listener) {
        vector.add(listener);
    }

    /**
     * Removes the given listener to be notified when messages are received
     * @param listener
     */
    public void removeListener(IMessageListener listener) {
        vector.remove(listener);
    }

    /**
     * Handler for receiving messages from the library
     * @param message
     */
    public void receiveMessageJNI(String message) {
        //System.out.println(message);
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            IMessageListener listener = (IMessageListener) it.next();
            listener.sendMessage(message);
        }
    }
}
