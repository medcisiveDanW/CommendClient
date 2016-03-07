package gov.va.med.util;

import gov.va.med.dss.wm.*;
import gov.va.med.util.*;

public class CPRSMessageListener implements IMessageListener {

    MsgStack msgStack;

    public CPRSMessageListener(MsgStack msgStack) {
        this.msgStack = msgStack;
    }

    public void sendMessage(String message) {
        System.out.println("CPRSMessageListener: sendMessage() message ==>" + message);
        //OTLog.println("CPRS msg: " + message);
        //Runtime rt = Runtime.getRuntime();
        //System.out.println("Free memory: "+ rt.freeMemory() + " Total memory: "+ rt.totalMemory());
        try {
            msgStack.putMsg(message);
        } catch (InterruptedException e) {
        }
    }
}
