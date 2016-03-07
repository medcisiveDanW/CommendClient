package com.medcisive.commend;

import static com.medcisive.commend.CommendClient._isServerDown;
import java.util.regex.*;
import org.apache.log4j.*;
import gov.va.med.dss.wm.*;
import gov.va.med.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author vhapalchambj
 */
public abstract class CPRSListener {
    protected boolean m_loggedIn;
    protected MsgStack m_msgStack;
    public String m_currentPtID;
    public String m_currentPtDFN;
    public String m_currentProviderID;
    public String m_currentPatientName;
    private boolean _isFristAlert = true;

    public CPRSListener() {
        m_msgStack = new MsgStack();
        initCPRSListenerThread();
        initMsgHandlingLoop();
    }

    /**
     * Create the CPRS listener thread to listen for CPRS messages and pass them on
     * to GUI creation thread.
     */
    private void initCPRSListenerThread() {
        Thread listenerThread = new Thread() {

            @Override
            public void run() {
                System.out.println("***************** Started listener thread *****************");
                try {
                    CPRSMessageListener listener = new CPRSMessageListener(m_msgStack);
                    JNILayer.getInstance().addListener(listener);
                    JNILayer.getInstance().listenForMessages("receiveMessageJNI", "C:\\Temp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        listenerThread.start();
    }

    private void initMsgHandlingLoop() {
        Thread msgHandlingLoopThread = new Thread() {

            @Override
            public void run() {
                m_loggedIn = false;
                try {
                    while (true) {
                        MsgGroup messageGroup = m_msgStack.getMsg(); // this call blocks w/ wait()
                        processCPRSMessage(messageGroup);
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        msgHandlingLoopThread.start();
    }

    private void processCPRSMessage(MsgGroup messageGroup) {
        // Create a pattern that'll match the following patient select messages:
        // (1) XPT^CPRS^70^045-48-5305^2600303^Name
        // (2) XPT^CPRS^7328705^000-00-4321^2800101^ZZTEST,ATHENAPATIENT A
        // The fourth group would match 2600303 and the first three are part of SSN
        //final Pattern newPatientPattern = Pattern.compile(".*?\\^.*?\\^.*?\\^(\\d*?)\\-(\\d*?)\\-(\\d*?)\\^(\\d*?)\\^.*");
        final Pattern newPatientPattern = Pattern.compile(".*?\\^.*?\\^.*?\\^(.*?)\\^(\\d*?)\\^.*");
        // Create a pattern that'll match the following string:
        // BEG^CPRS^^295
        final Pattern newProviderPattern = Pattern.compile(".*?\\^.*?\\^.*?\\^(\\d*?)");
        String message = null;
        if(messageGroup!=null) {
            message = messageGroup.getProviderMsg();
        }
        //message = "BEG^CPRS^^295"; // for test only
        if (message!=null && message.startsWith("BEG")) {
            Matcher m = newProviderPattern.matcher(message);
            if (m.matches()) {
                m_currentProviderID = m.group(1);
                System.out.println("processCPRSMessage -- Message: " + message);
                System.out.println("processCPRSMessage -- Matched provider Id: " + m_currentProviderID);
                m_loggedIn = true;
                String ptSelMsg = messageGroup.getPatientSelectMsg();
                if (ptSelMsg == null) {
                    // if it's only a login message, just log it and return
                    // See that in MsgStack, if a putMsg corresponds to a patient
                    // log in, the ptSelMsg in the group is always set to null
                    // Here mCurrentProviderID has been identified, regardless of providerStatus
                    return;
                }
                // now check for the patient Id
                m = newPatientPattern.matcher(ptSelMsg);
                if (m.matches()) {
                    //final String patientId = m.group(1) + m.group(2) + m.group(3);
                    m_currentPtID = m.group(1);
                    m_currentPtID = m_currentPtID.replaceAll("\\-", "");
                    m_currentPtDFN = obtainDFNFromCPRSMsg(ptSelMsg);
                    System.out.println("processCPRSMessage -- Message: " + ptSelMsg);
                    System.out.println("processCPRSMessage -- Matched patient SSN: " + m_currentPtID);
                    System.out.println("processCPRSMessage -- Matched patient DFN: " + m_currentPtDFN);
                    // Here to start the thread to create the WebBrowser window
                    //CreateBrowserThread cbth= new CreateBrowserThread(this);
                    //cbth.run();
                    int index = ptSelMsg.lastIndexOf('^');
                    if ((index > 0) && (index != ptSelMsg.length())) {
                        m_currentPatientName = ptSelMsg.substring(index + 1, ptSelMsg.length());
                        System.out.println("Patient Selected: " + m_currentPatientName);
                    }
                    updateBrowser();
                } else {
                    System.out.println("processCPRSMessage -- Receieved patient message =>" + ptSelMsg + "<=");
                    System.out.println("does not match the requested pattern!!!");
                }
            } else {
                System.out.println("processCPRSMessage -- Recieved provider message =>" + message + "<=");
                System.out.println("does not match the requested pattern!!!");
            }
        } else if (message!=null && message.startsWith("END")) {
            destroyBrowser();
            System.out.println("Setting loggedIn to FALSE");
            m_loggedIn = false;
        } else if (messageGroup==null || message==null) {
            if(_isFristAlert && !_isServerDown) {
                JOptionPane.showMessageDialog(WebBrowserController.frame, "CPRS needs to be restarted to use COMMEND."); 
                _isFristAlert = false;
            }
        } else {
            System.out.println("processCPRSMessage -- Recieved message ==>" + message + "<==");
            System.out.println("is not recognised as a valid CPRS message!!!");
        }
    }
    /*
     * Form of message
     * XPT^CPRS^7328705^000-00-4321^2800101^ZZTEST,ATHENAPATIENT A
     *
     * The Patient DFN is the unique VA patient identifier within a
     * VistA site.
     *
     */

    private String obtainDFNFromCPRSMsg(String message) {
        String caret = "^";
        String tempStr = null;
        // Find first caret
        int pos = message.indexOf(caret);
        tempStr = message.substring(pos + 1);
        // Find second caret
        pos = tempStr.indexOf(caret);
        tempStr = tempStr.substring(pos + 1);
        // Find third caret
        pos = tempStr.indexOf(caret);
        String result = tempStr.substring(0, pos);
        return result;
    }

    public abstract void updateBrowser();

    public abstract void destroyBrowser();
}