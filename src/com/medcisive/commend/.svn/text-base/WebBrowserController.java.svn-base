package com.medcisive.commend;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import org.jdesktop.jdic.browser.*;

/**
 *
 * @author vhapalchambj
 */
public class WebBrowserController {

    public static JFrame frame;
    public WebBrowser webBrowser;
    protected String initFile;
    public URL startupURL;
    public URL shutdownURL;
    public int windowWidth = 10;
    public int windowHeight = 10;
    public int windowLeftSide = 10;
    public int windowTopSide = 10;;
    public GraphicsDevice graphicsDevice;
    public DisplayMode displayMode;
    private Client _client;

    public WebBrowserController(Client client) {
        _client = client;
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        displayMode = graphicsDevice.getDisplayMode();
        frame = new JFrame(client._title);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        WebBrowser.setDebug(false);
        webBrowser = new WebBrowser(true);
        webBrowser.setDebug(false);
        webBrowser.setSize(windowWidth, windowHeight);
        frame.setLocation(windowLeftSide, windowTopSide);
        try {
            startupURL = new URL(client._startupURL);
            shutdownURL = new URL(client._shutdownURL);
            webBrowser.setURL(new URL(shutdownURL.toString()));
        } catch (MalformedURLException e) { System.out.println(e); }
        frame.addComponentListener(client);
        webBrowser.addWebBrowserListener(client);
        contentPane.add(webBrowser, BorderLayout.CENTER);
        frame.addWindowListener(returnWindowAdapter());
        frame.pack();
        frame.setVisible(false);
    }

    public void updateGraphicsDevice() {
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        displayMode = graphicsDevice.getDisplayMode();
    }

    public boolean isOpen() {
        return frame.isVisible();
    }

    public void newBrowser() {
        hideMe();
        try { webBrowser.setURL(new URL(startupURL.toString() + "?key=" + _client._key));
        } catch (MalformedURLException e) { System.out.println(e); }
    }

    public void openBrowser() {
        hideMe();
        try { webBrowser.setURL(new URL(startupURL.toString()));
        } catch (MalformedURLException e) { System.out.println(e); }
    }

    public void closeBrowser() {
        hideMe();
    }

    public boolean compareURLs(URL first, URL second) {
        if ((first == null) || (second == null)) {
        } else if (first.toString().contains(second.toString()) || second.toString().contains(first.toString())) {
            return true;
        }
        return false;
    }

    private WindowAdapter returnWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                _client.windowCloseEvent();
            }
        };
    }

    public void showMe() {
        frame.setVisible(true);
        frame.toFront();
        frame.repaint();
    }

    public void hideMe() {
        frame.setVisible(false);
    }

    public static int getPositionOffset(int max, int min) {
        return max / 2 - min / 2;
    }
}
