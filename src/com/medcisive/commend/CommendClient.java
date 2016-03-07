package com.medcisive.commend;

import java.io.UnsupportedEncodingException;
import java.net.*;

import org.jdesktop.jdic.browser.*;

import com.medcisive.utility.LogUtility;
import com.medcisive.utility.sql2.SQLTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

public class CommendClient extends Client {

    private boolean _lockPatient = false;
    private boolean _lockSize = true;
    private boolean _lockPosition = true;
    private static boolean _lockClient = true;
    public static boolean _isServerDown = false;
    private URL _currentURL = null;
    private SecurityCodeGenerator _scg;

    public CommendClient(String[] args, String title) {
        super(args, title);
        logEvent("Commend client Listener started.", "0");
        _scg = new SecurityCodeGenerator();
        new OpperationsManager().start();
    }

    private boolean checkProvider() {
        boolean result = checkProviderIdentity(m_currentProviderID, _clientComputerName);
        logEvent("Provider logged in: " + result, "1");
        System.out.println(" checkProviderIdentity result: " + result);
        return result;
    }

    private boolean checkPatient() {
        _key = _scg.getCode(24);
        boolean result = checkPatientIdentity(m_currentPtID, m_currentProviderID, _key);
        logEvent("Patient selected: " + result, "2");
        System.out.println(" checkProviderIdentity result: " + result);
        return result;
    }

    @Override
    public void updateBrowser() {
        if (!_lockPatient) {
            System.out.println("System is trying to launch browser.");
            System.out.println("Current information is:");
            System.out.println("ProviderID:  " + this.m_currentProviderID);
            System.out.println("Patient Name:  " + this.m_currentPatientName);
            System.out.println("Patient DFN:  " + this.m_currentPtDFN);
            System.out.println("Patient ID:  " + this.m_currentPtID);
            if (checkProvider()) {
                checkPatient();
                resetBrowser();
            }
        }
    }

    private void resetBrowser() {
        if(!_lockClient) {
            _wbc.newBrowser();
            logEvent("Commend main window displayed.", "3");
        }
    }

    @Override
    public void windowCloseEvent() {
        logEvent("Window close event.", "6");
        try {
            if (_wbc.compareURLs(_wbc.webBrowser.getURL(), _wbc.startupURL)) {
                _wbc.closeBrowser();
            } else {
                _wbc.openBrowser();
            }
        } catch (Exception e) {
            System.out.println("Overload System Shutdown - Error: " + e);
        }
    }

    @Override
    public void destroyBrowser() {
        _lockPatient = false;
        _wbc.closeBrowser();
        logEvent("Commend Browser closed.", "4");
    }

    @Override
    public void downloadError(WebBrowserEvent event) {
        logEvent(event.toString(), "5");
    }

    @Override
    public void titleChange(WebBrowserEvent event) {
        System.out.println("The web page's title has changed: " + event.getData());
    }

    @Override
    public void downloadStarted(WebBrowserEvent event) {
        //System.out.println("Download Started: " + wbc.webBrowser.getURL())
        if (!_wbc.compareURLs(_currentURL, _wbc.webBrowser.getURL())) {
            //System.out.println("There has been a change!" + currentURL + " : " + wbc.webBrowser.getURL());
            _wbc.hideMe();
        } else {
            //System.out.println("Something happend but no change in URLs!");
        }
    }

    @Override
    public void documentCompleted(WebBrowserEvent event) {
        _wbc.updateGraphicsDevice();
        if (_wbc.compareURLs(_wbc.webBrowser.getURL(), _wbc.shutdownURL)) {
            System.out.println("Goodbye!");
            destroyBrowser();
            return;
        }
        String stringLockPatient = _wbc.webBrowser.executeScript("this.clientWindowLockPatient;");
        String stringLockSize = _wbc.webBrowser.executeScript("this.clientWindowLockSize;");
        String stringlockPosition = _wbc.webBrowser.executeScript("this.clientWindowLockPosition;");
        String stringWindowWidth = _wbc.webBrowser.executeScript("this.clientWindowWidth;");
        String stringWindowHeight = _wbc.webBrowser.executeScript("this.clientWindowHeight;");

        _lockPatient = Boolean.parseBoolean(stringLockPatient);

        int width = 0;
        int height = 0;
        try {
            width = Integer.parseInt(stringWindowWidth);
        } catch (NumberFormatException e) {
            width = _wbc.displayMode.getWidth();
        }
        try {
            height = Integer.parseInt(stringWindowHeight);
        } catch (NumberFormatException e) {
            height = _wbc.displayMode.getHeight();
        }
        if (width > _wbc.displayMode.getWidth()) {
            width = _wbc.displayMode.getWidth();
        }
        if (height > _wbc.displayMode.getHeight()) {
            height = _wbc.displayMode.getHeight();
        }
        if (stringLockSize == null || stringLockSize.equalsIgnoreCase("false")) {
            _lockSize = false;
        } else {
            _lockSize = true;
        }
        if (stringlockPosition == null || stringlockPosition.equalsIgnoreCase("false")) {
            _lockPosition = false;
        } else {
            _lockPosition = true;
        }
        _wbc.frame.setSize(width, height);
        if (_lockSize) {
            _wbc.frame.setResizable(false);
        } else {
            _wbc.frame.setResizable(true);
        }
        if (_lockPosition) { // top right of screen
            int top = 0;
            int left = _wbc.displayMode.getWidth() - width;
            _wbc.frame.setLocation(left, top);
        } else { // center of screen
            int top = (_wbc.displayMode.getHeight() / 2) - (height / 2);
            int left = (_wbc.displayMode.getWidth() / 2) - (width / 2);
            _wbc.frame.setLocation(left, top);
        }
        _currentURL = _wbc.webBrowser.getURL();
        _wbc.frame.setVisible(true);
    }

    private void shutdown() {
        System.exit(0);
    }

    private void restart(String restartUrl) {
        try {
            Runtime.getRuntime().exec(restartUrl);
        } catch (java.io.IOException e) {
            LogUtility.error(e);
        }
        System.exit(0);
    }
        
    private Boolean logEvent(String event, String eventCategory) {
        Boolean result = false;
        try {
			event = java.net.URLEncoder.encode(event, "UTF-8");
		} catch (UnsupportedEncodingException e) {}
        String options = "option=logEvent&clientComputer=" + _clientComputerName + "&patientDFN=" + m_currentPtDFN + "&patientSSN=" + m_currentPtID + "&providerDUZ=" + m_currentProviderID + "&event=" + event + "&eventCategory=" + eventCategory;
        try {
            result = new com.medcisive.utility.Post<Boolean>().getResponse(result, _wsURL, options, false);
        } catch (Exception ex) {
            System.out.println("logEvent() Web Service Call Failed: " + ex);
        }
        return result;
    }
    
    private Boolean checkPatientIdentity(String patientSSN, String providerDUZ, String securityCode) {
        Boolean result = false;
        String options = "option=checkPatientIdentity&patientSSN=" + patientSSN + "&providerDUZ=" + providerDUZ + "&securityCode=" + securityCode;
        try {
            result = new com.medcisive.utility.Post<Boolean>().getResponse(result, _wsURL, options, false);
        } catch (Exception ex) {
            System.out.println("checkPatientIdentity() Web Service Call Failed: " + ex);
        }
        return result;
    }
    
    private Boolean checkProviderIdentity(String providerDUZ, String clientComputer) {
        Boolean result = false;
        String options = "option=checkProviderIdentity&providerDUZ=" + providerDUZ + "&clientComputer=" + clientComputer;
        try {
            result = new com.medcisive.utility.Post<Boolean>().getResponse(result, _wsURL, options, false);
        } catch (Exception ex) {
            System.out.println("checkProviderIdentity() Web Service Call Failed: " + ex);
        }
        return result;
    }
    
    class OpperationsManager extends Thread {

        private boolean _isStarting = true;
        private boolean _isRunning = true;
        private boolean _isRestarting = false;
        private boolean _isServerFail = false;
        private boolean _isFristAlert = true;
        private final java.sql.Timestamp _clientStartTime = new java.sql.Timestamp(System.currentTimeMillis());
        private java.sql.Timestamp _shutdownTime, _restartTime;
        private String _clientBatURL = "";

        @Override
        public void run() {
            startup();
            while (_isRunning) {
                getProperties();
                //System.out.println("_shutdownTime: " + _shutdownTime + " _restartTime: " + _restartTime);
                if( _shutdownTime!=null &&  (_shutdownTime.getTime() > _clientStartTime.getTime() && _shutdownTime.getTime() < System.currentTimeMillis()) ) {  // shutdown request
                    shutdown();
                    _isRunning = false;
                } else if( _restartTime!=null && (_restartTime.getTime() > _clientStartTime.getTime() && _restartTime.getTime() < System.currentTimeMillis()) ) { // restart request
                    restart(_clientBatURL);
                    _isRunning = false;
                } else if( !_lockClient && _isRestarting ) { // server shutdown occured has now been restarted.  Start restarting to reconnect
                    restart(_clientBatURL);
                    _isRunning = false;
                } else { // nothing new, sleep and check again later
                    try { Thread.sleep(20000L); } catch (InterruptedException ex1) {}
                }
            }
        }

        private void startup() {
            while (_isStarting) {
                try {
                    _clientBatURL = new com.medcisive.utility.Post<String>().getResponse(_clientBatURL, _wsURL, "option=getClientBat", false);
                    _isStarting = false;
                } catch (Exception ex) {
                    _isRestarting = true;
                    serverFailure();
                    try { Thread.sleep(20000L); } catch (InterruptedException ex1) {}
                    System.out.println("Get client bat URL exception: " + ex);
                }
            }
            if(_isRestarting) {
                restart(_clientBatURL);
                _isRunning = false;
            }
        }

        private void getProperties() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");
            SQLTable sqlProps = new SQLTable();
            try {
                sqlProps = new com.medcisive.utility.Post<SQLTable>().getResponse(sqlProps, _wsURL, "option=getApplicationProperties");
                _lockClient = false;
            } catch (Exception ex) {
                _lockClient = true;
                _isRestarting = true;
                serverFailure();
                System.out.println("Error pulling ApplicationProperties: " + ex);
            }
            _restartTime = null;
            _shutdownTime = null;
            for(java.util.LinkedHashMap<String, Object> map : sqlProps.values()){
                String key = (String)map.get("Name");
                String dateStr = (String)map.get("DateModified");
                try {
                    java.sql.Timestamp date = new java.sql.Timestamp(sdf.parse(dateStr).getTime());
                    if(key.equalsIgnoreCase("Restart")){
                        _restartTime = date;
                    } else if(key.equalsIgnoreCase("Shutdown")){
                        _shutdownTime = date;
                    }
                } catch (ParseException ex) {
                    System.out.println("Timestamp ParseException: " + ex);
                }
            }
        }
        
        public void serverFailure() {
            _isServerFail = true;
            destroyBrowser();
            if(_isFristAlert) {
                JOptionPane.showMessageDialog(WebBrowserController.frame, "COMMEND servers are down currently."); 
                _isFristAlert = false;
            }
        }
    }
}
