package gov.va.med.util;

import java.util.*;
import java.text.SimpleDateFormat;

public class MsgStack extends Object {

    private boolean m_msgAvailable; // flag to indicate when a message is available
    // to be read.
    // flag is set to true after a provider or patient
    // message is added; set to false after the message
    // is read
    private MsgGroup m_msgGroup;  //At any given time, this is the single message
    //group stored on this one-element stack
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd hhmmss.SSS");

    public MsgStack() {
        m_msgGroup = new MsgGroup();
        m_msgAvailable = false;
    }

    public synchronized void putMsg(String aMsg) throws InterruptedException {

        //while (m_msgAvailable == true) {
        //  // wait until last message has been read
        //  wait();
        //}
        // parse and save message
        String msgStart = aMsg.substring(0, 3);
        //System.out.println(" putMsg- msgStart= " + msgStart);
        if (msgStart.equals(MsgGroup.PVLOGIN_START)
                || msgStart.equals(MsgGroup.PVLOGOUT_START)) {
            m_msgGroup.setProviderMsg(aMsg);
            // When a new provider logs in, clear the patient
            // stored
            m_msgGroup.setPatientSelectMsg(null);
            m_msgAvailable = true;
            notifyAll();
        } else if (msgStart.equals(MsgGroup.PTSELECT_START)) {
            m_msgGroup.setPatientSelectMsg(aMsg);
            m_msgAvailable = true;
            notifyAll();
        } else {
            // Here to ignore other kinds of messages
            ;
        }

    }

    public synchronized MsgGroup getMsg() throws InterruptedException {

        while (m_msgAvailable == false) {
            // wait until a message is availabe (i.e. not read)
            wait();
        }
        m_msgAvailable = false;
        notifyAll();
        return m_msgGroup;
    }

    public static void main(String[] args) {

        MsgStack msgStack = new MsgStack();
        try {
            // When creating the writer thread, the run method used
            // is specified by the second argument
            WriterThread writer = new WriterThread(msgStack, 5);
            writer.start();

            Thread.sleep(2000);

            ReaderThread reader = new ReaderThread(msgStack);
            reader.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class WriterThread extends Thread {

    MsgStack m_msgStack;
    int m_runMethod;

    public WriterThread(MsgStack aStack, int runMethod) {
        m_msgStack = aStack;
        m_runMethod = runMethod;
    }

    public void run() {
        if (m_runMethod == 1) {
            run1();
        } else if (m_runMethod == 2) {
            run2();
        } else if (m_runMethod == 3) {
            run3();
        } else if (m_runMethod == 4) {
            run4();
        } else if (m_runMethod == 5) {
            run5();
        } else {
            ;
        }
    }

    public void run1() {
        /*
         * Case: provider log in and patient selection
         *   both occur before the reader thread is
         *   ready to listen.
         *
         */
        try {
            // provider login
            String msg = MsgGroup.PVLOGIN_START + "^436";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            msg = MsgGroup.PTSELECT_START + "^williams";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^smith";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^johnson";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // end
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGOUT_START;
            logMsgTime(msg);
            m_msgStack.putMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run5() {
        /*
         * Case: provider log in and patient selection
         *   both occur before the reader thread is
         *   ready to listen.
         *   After CPRS closes, a second provider logs in,
         *   and then selects a patient.
         *
         */
        try {
            // provider login
            String msg = MsgGroup.PVLOGIN_START + "^436";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            msg = MsgGroup.PTSELECT_START + "^williams";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // end
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGOUT_START;
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // 2nd provider login
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGIN_START + "^691";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^fiedler";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // end
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGOUT_START;
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run2() {
        /*
         * Case: only provider log in occurs
         *   before the reader thread is
         *   ready to listen.
         *
         */
        try {
            // provider login
            String msg = MsgGroup.PVLOGIN_START + "^436";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^smith";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^johnson";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // end
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGOUT_START;
            logMsgTime(msg);
            m_msgStack.putMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run3() {
        /*
         * Case: provider log in and 2 patient selections occur
         *   before the reader thread is
         *   ready to listen.
         *
         */
        try {
            // provider login
            String msg = MsgGroup.PVLOGIN_START + "^436";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient1 select
            msg = MsgGroup.PTSELECT_START + "^patient1";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient2 select
            msg = MsgGroup.PTSELECT_START + "^patient2";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^smith";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^johnson";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // end
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGOUT_START;
            logMsgTime(msg);
            m_msgStack.putMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run4() {
        /*
         * Case: provider log in patient selections occur
         *   only after the reader thread is
         *   ready to listen.
         *
         */
        try {
            // provider login
            Thread.sleep(8000);
            String msg = MsgGroup.PVLOGIN_START + "^436";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^smith";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // patient select
            Thread.sleep(8000);
            msg = MsgGroup.PTSELECT_START + "^johnson";
            logMsgTime(msg);
            m_msgStack.putMsg(msg);

            // end
            Thread.sleep(8000);
            msg = MsgGroup.PVLOGOUT_START;
            logMsgTime(msg);
            m_msgStack.putMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logMsgTime(String msg) {
        Date now = new Date();
        String tNow = MsgStack.timeFormat.format(now);
        System.out.println(tNow + " Msg Sent: " + msg);
    }
}

class ReaderThread extends Thread {

    private MsgStack m_msgStack;

    public ReaderThread(MsgStack msgStack) {
        m_msgStack = msgStack;
    }

    public void run() {
        while (true) {
            try {
                MsgGroup msg = m_msgStack.getMsg();
                logMsgTime(msg.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logMsgTime(String msg) {
        Date now = new Date();
        String tNow = MsgStack.timeFormat.format(now);
        System.out.println(tNow + " Msg Rec.: " + msg);
    }
}
/*
 * There are 3 kinds of messages of interest
 * provider log in message: BEG^CPRS^^295
 * patient select message:  XPT^CPRS^70^111-22-3333^2600303^Name
 * cprs close message: END
 */

/*
Results when using run1: provider login and 1 patient selection
both occur before the system is ready.

E:\vaWMDSS\build\opioidclient\MsgStack>ant -f MsgStackBuild.xml MsgStack
Buildfile: MsgStackBuild.xml
MsgStack:
[java] 09/04 110042.879 Msg Sent: BEG^436
[java] 09/04 110042.894 Msg Sent: XPT^williams
[java] 09/04 110044.879 Msg Rec.: BEG^436, XPT^williams
[java] 09/04 110050.894 Msg Sent: XPT^smith
[java] 09/04 110050.894 Msg Rec.: BEG^436, XPT^smith
[java] 09/04 110058.895 Msg Sent: XPT^johnson
[java] 09/04 110058.895 Msg Rec.: BEG^436, XPT^johnson
[java] 09/04 110106.895 Msg Sent: END
[java] 09/04 110106.895 Msg Rec.: END

Results of run2: only provider log in occurs before
1st patient selection.

MsgStack:
[java] 09/04 092716.095 Msg Sent: BEG^436
[java] 09/04 092718.095 Msg Rec.: BEG^436
[java] 09/04 092724.095 Msg Sent: XPT^smith
[java] 09/04 092724.095 Msg Rec.: BEG^436, XPT^smith
[java] 09/04 092732.095 Msg Sent: XPT^johnson
[java] 09/04 092732.095 Msg Rec.: BEG^436, XPT^johnson
[java] 09/04 092740.095 Msg Sent: END
[java] 09/04 092740.095 Msg Rec.: END

Results of run3:provider log in and 2 patient selections
occur before reader is ready.
The first "complete" message received corresponds to
patient2.  Patient1 is completely missed, but that is
OK.

MsgStack:
[java] 09/04 094533.795 Msg Sent: BEG^436
[java] 09/04 094533.795 Msg Sent: XPT^patient1
[java] 09/04 094533.795 Msg Sent: XPT^patient2
[java] 09/04 094535.795 Msg Rec.: BEG^436, XPT^patient2
[java] 09/04 094541.795 Msg Sent: XPT^smith
[java] 09/04 094541.795 Msg Rec.: BEG^436, XPT^smith
[java] 09/04 094549.795 Msg Sent: XPT^johnson
[java] 09/04 094549.795 Msg Rec.: BEG^436, XPT^johnson
[java] 09/04 094557.795 Msg Sent: END
[java] 09/04 094557.795 Msg Rec.: END

Results of run4
Provider log in patient selections occur
only after the reader thread is
ready to listen.  No problem at all.

MsgStack:
[java] 09/04 094827.987 Msg Sent: BEG^436
[java] 09/04 094827.987 Msg Rec.: BEG^436
[java] 09/04 094835.987 Msg Sent: XPT^smith
[java] 09/04 094835.987 Msg Rec.: BEG^436, XPT^smith
[java] 09/04 094843.987 Msg Sent: XPT^johnson
[java] 09/04 094843.987 Msg Rec.: BEG^436, XPT^johnson
[java] 09/04 094851.987 Msg Sent: END
[java] 09/04 094851.987 Msg Rec.: END

Results of run method 5:
Initially, 1 provider login and patient selection
before the reader is initialized. Then CPRS is closed,
and a new provider logs in and another patient
selection is made.


MsgStack:
[java] 09/04 095856.659 Msg Sent: BEG^436
[java] 09/04 095856.659 Msg Sent: XPT^williams
[java] 09/04 095858.659 Msg Rec.: BEG^436, XPT^williams
[java] 09/04 095904.659 Msg Sent: END
[java] 09/04 095904.659 Msg Rec.: END
[java] 09/04 095912.659 Msg Sent: BEG^691
[java] 09/04 095912.659 Msg Rec.: BEG^691
[java] 09/04 095920.660 Msg Sent: XPT^fiedler
[java] 09/04 095920.660 Msg Rec.: BEG^691, XPT^fiedler
[java] 09/04 095928.660 Msg Sent: END
[java] 09/04 095928.660 Msg Rec.: END
 */
