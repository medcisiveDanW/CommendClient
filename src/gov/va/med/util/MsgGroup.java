package gov.va.med.util;

import java.util.*;

public class MsgGroup extends Object {

    public static String PVLOGIN_START = "BEG";
    public static String PVLOGOUT_START = "END";
    public static String PTSELECT_START = "XPT";
    public static String CARET = "^";   // token separator in Windows messages
    private String m_providerMsg;       // provider log in or log out(cprs close)
    private Date m_providerMsgTime;     // provider log in or log out message time
    private String m_patientSelectMsg;  // patient select message
    private Date m_ptSelectMsgTime;     // patient select message time

    public void setProviderMsg(String msg) {
        m_providerMsgTime = new Date(System.currentTimeMillis());
        m_providerMsg = msg;
    }

    public String getProviderMsg() {
        return m_providerMsg;
    }

    public Date getProvMsgTime() {
        return m_providerMsgTime;
    }

    public Date getPtSelectMsgTime() {
        return m_ptSelectMsgTime;
    }

    public boolean isProviderMsgLogout() {
        boolean result = false;
        if (m_providerMsg == null || m_providerMsg.length() <= 0) {
            return false;
        }
        String msgStart = m_providerMsg.substring(0, 3);
        if (msgStart.equals(PVLOGOUT_START)) {
            result = true;
        }
        return result;
    }

    // get the provider id from the message that is set
    public String getProviderIDInPVMsg() {
        String providerId = "";
        //provider message is of the format BEG^CPRS^^123
        StringTokenizer st = new StringTokenizer(m_providerMsg, CARET);
        int count = 0;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (count == 2) {
                providerId = token;
                //System.out.println("providerId = " + providerId);
            }
            count++;
        }
        return providerId;
    }

    // get the patient SSN from the message
    public String getPatientSSNInPtMsg() {
        String ptSSN = "";
        //patient message is of the format XPT^CPRS^70^111-22-3333^1234567^Name
        StringTokenizer st = new StringTokenizer(m_patientSelectMsg, CARET);
        int count = 0;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            //System.out.println(" count " + count + " token: " + token);
            if (count == 3) {
                ptSSN = token;
                //System.out.println("patientSSN = " + ptSSN);
            }
            count++;
        }
        return ptSSN;
    }

    public void setPatientSelectMsg(String msg) {
        m_ptSelectMsgTime = new Date(System.currentTimeMillis());
        m_patientSelectMsg = msg;
    }

    public String getPatientSelectMsg() {
        return m_patientSelectMsg;
    }

    public void clear() {
        m_providerMsg = null;
        m_patientSelectMsg = null;
        m_providerMsgTime = null;
        m_ptSelectMsgTime = null;
    }

    public String getContent() {

        if (m_providerMsg == null && m_patientSelectMsg == null) {
            return null;
        }

        if (m_providerMsg == null) {
            return getPatientSelectMsg();
        } else {
            String content = getProviderMsg();
            if (m_patientSelectMsg != null) {
                content += ", " + getPatientSelectMsg();
            }
            return content;
        }
    }
}

/*
 * There are 3 kinds of messages of interest
 * provider log in message: BEG^CPRS^^234
 * provider log out (cprs close) message: END^CPRS^666111^
 * patient select message:  XPT^CPRS^70^111-22-3333^1234567^Name
 *
 */
