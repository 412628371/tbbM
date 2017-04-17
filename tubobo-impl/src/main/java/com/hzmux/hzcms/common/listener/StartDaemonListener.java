package com.hzmux.hzcms.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xinguang.tubobo.impl.runnable.DaemonSaveWayBill;

public class StartDaemonListener implements ServletContextListener {
    public StartDaemonListener(){}
    
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

    	//开启运单录入守护线程
    	DaemonSaveWayBill.start(true);

    }
    
}


