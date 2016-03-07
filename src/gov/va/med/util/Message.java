package gov.va.med.util;

public class Message {

    String m_message;  // The String message
    String m_msgSrcId; // The Id of the message's publisher

    public Message(String srcId, String aMsg) {
        m_message = aMsg;
        m_msgSrcId = srcId;
    }

    public String getMessage() {
        return m_message;
    }

    public String getMsgSrcId() {
        return m_msgSrcId;
    }
}