package com.acme;


import javax.ejb.*;
import javax.annotation.*;

import javax.naming.InitialContext;

import javax.management.j2ee.ManagementHome;
import javax.management.j2ee.Management;
import javax.rmi.PortableRemoteObject;

import com.sun.ejte.ccl.reporter.SimpleReporterAdapter;

public class Client {

    private static SimpleReporterAdapter stat = 
        new SimpleReporterAdapter("appserv-tests");

    private static String appName;

    public static void main(String args[]) {

	appName = args[0]; 
	stat.addDescription(appName);
	Client client = new Client(args);       
        client.doTest();	
        stat.printSummary(appName + "ID");
    }

    public Client(String[] args) {}

    public void doTest() {

	try {

	    SFSB bean = (SFSB) new InitialContext().lookup("java:global/" + appName + "/SFSB");
	    System.out.println("SFSB says : " + bean.hello());

	     stat.addStatus("local main", stat.PASS);

	} catch(Exception e) {
	    stat.addStatus("local main", stat.FAIL);
	    e.printStackTrace();
	}
    }


}
