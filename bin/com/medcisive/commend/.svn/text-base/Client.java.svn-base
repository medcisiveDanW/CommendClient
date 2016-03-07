package com.medcisive.commend;

import com.medcisive.utility.*;
import java.awt.event.*;
import org.jdesktop.jdic.browser.*;

/**
 *
 * @author vhapalchambj
 */
public abstract class Client extends CPRSListener implements WebBrowserListener, ComponentListener {
    protected com.medcisive.utility.PropertiesUtility _properties;
    protected String _clientComputerName;
    protected String _url;
    protected String _startupURL;
    protected String _shutdownURL; 
    protected String _wsURL;
    protected String _initFile;
    protected String _key;
    protected String _title;
    protected WebBrowserController _wbc;

    public Client(String[] args, String title) {
        _title = title;
        _properties = UtilityFramework.init2(args[0]);
        _url = _properties.getProperty("URL");
        _startupURL = _url + "/" + _properties.getProperty("HOME_PAGE");
        _shutdownURL = _url + "/" + _properties.getProperty("SHUTDOWN_PAGE");
        _wsURL = _url + "/" + _properties.getProperty("WS_PAGE");
        try {
            _clientComputerName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            System.out.println("Exception (clientComputerName): " + e.getMessage());
        }
        _wbc = new WebBrowserController(this);
    }

    public abstract void updateBrowser();

    public abstract void windowCloseEvent();

    public abstract void destroyBrowser();

    public void initializationCompleted(WebBrowserEvent event) { /*System.out.println("Initialization Completed");*/ }

    public void downloadStarted(WebBrowserEvent event) { /*System.out.println("Download Started");*/ }

    public void downloadProgress(WebBrowserEvent event) { /*System.out.println("Download Progress");*/ }

    public void downloadError(WebBrowserEvent event) { /*System.out.println("Download Error:" + event);*/ }

    public void documentCompleted(WebBrowserEvent event) { /*System.out.println("Document Completed");*/ }

    public void downloadCompleted(WebBrowserEvent event) { /*System.out.println("Download Completed");*/ }

    public void titleChange(WebBrowserEvent event) { /*System.out.println("Title Change");*/ }

    public void statusTextChange(WebBrowserEvent event) { /*System.out.println("Status Text Change");*/ }

    public void windowClose(WebBrowserEvent event) { /*System.out.println("Window Close");*/ }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}
}
