/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.admingui.devtests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdminServiceTest extends BaseSeleniumTestClass {
    private static final String TRIGGER_EDIT_JMX_CONNECTOR = "Edit JMX Connector";
    private static final String TRIGGER_SSL = "Requires the client to authenticate itself to the server";

    @Test
    public void testEditJmxConntector() {
        String address = generateRandomNumber(255)+"."+generateRandomNumber(255)+"."+generateRandomNumber(255)+"."+generateRandomNumber(255);
        clickAndWait("treeForm:tree:configurations:server-config:adminService:adminService_link", TRIGGER_EDIT_JMX_CONNECTOR);
        selenium.check("form1:propertySheet:propertySheetSection:SecurityProp:Security");
        selenium.type("form1:propertySheet:propertySheetSection:AddressProp:Address", address);
        int count = addTableRow("form1:basicTable","form1:basicTable:topActionsGroup1:addSharedTableButton");
        selenium.type("form1:basicTable:rowGroup1:0:col2:col1St", "property"+generateRandomString());
        selenium.type("form1:basicTable:rowGroup1:0:col3:col1St", "value");
        selenium.type("form1:basicTable:rowGroup1:0:col4:col1St", "description");
        clickAndWait("form1:propertyContentPage:topButtons:saveButton", MSG_NEW_VALUES_SAVED);
        clickAndWait("form1:jmxConnectorTab:jmxSSLEdit", TRIGGER_SSL);
        clickAndWait("treeForm:tree:configurations:server-config:adminService:adminService_link", TRIGGER_EDIT_JMX_CONNECTOR);
        assertEquals(address, selenium.getValue("form1:propertySheet:propertySheetSection:AddressProp:Address"));
        assertTableRowCount("form1:basicTable", count);
    }
    
    @Test
    public void testSsl() {
        final String nickname = "nickname"+generateRandomString();
        final String keystore = "keystore"+generateRandomString()+".jks";
        final String maxCertLength = Integer.toString(generateRandomNumber(10));

        clickAndWait("treeForm:tree:configurations:server-config:adminService:adminService_link", TRIGGER_EDIT_JMX_CONNECTOR);
        clickAndWait("form1:jmxConnectorTab:jmxSSLEdit", TRIGGER_SSL);

        selenium.uncheck("propertyForm:propertySheet:propertySheetSection:SSL3Prop:SSL3");
        selenium.uncheck("propertyForm:propertySheet:propertySheetSection:TLSProp:TLS");
        selenium.check("propertyForm:propertySheet:propertySheetSection:ClientAuthProp:ClientAuth");
        selenium.type("propertyForm:propertySheet:propertySheetSection:CertNicknameProp:CertNickname", nickname);
        selenium.type("propertyForm:propertySheet:propertySheetSection:keystore:keystore", keystore);
        selenium.type("propertyForm:propertySheet:propertySheetSection:maxCertLength:maxCertLength", maxCertLength);
//        selenium.click("propertyForm:propertySheet:sun_propertySheetSection433:CommonCiphersProp:commonAddRemove:commonAddRemove_addAllButton");
//        selenium.click("propertyForm:propertySheet:sun_propertySheetSection433:EphemeralCiphersProp:ephemeralAddRemove:ephemeralAddRemove_addAllButton");
//        selenium.click("propertyForm:propertySheet:sun_propertySheetSection433:OtherCiphersProp:otherAddRemove:otherAddRemove_addAllButton");
        if (selenium.isElementPresent("propertyForm:propertyContentPage:topButtons:newButton")) {
            clickAndWait("propertyForm:propertyContentPage:topButtons:newButton", MSG_NEW_VALUES_SAVED);
        } else {
            clickAndWait("propertyForm:propertyContentPage:topButtons:saveButton", MSG_NEW_VALUES_SAVED);
        }
        clickAndWait("treeForm:tree:configurations:server-config:adminService:adminService_link", TRIGGER_EDIT_JMX_CONNECTOR);
        clickAndWait("form1:jmxConnectorTab:jmxSSLEdit", TRIGGER_SSL);

        assertEquals("off", selenium.getValue("propertyForm:propertySheet:propertySheetSection:SSL3Prop:SSL3"));
        assertEquals("off", selenium.getValue("propertyForm:propertySheet:propertySheetSection:TLSProp:TLS"));
        assertEquals("on", selenium.getValue("propertyForm:propertySheet:propertySheetSection:ClientAuthProp:ClientAuth"));
        assertEquals(nickname, selenium.getValue("propertyForm:propertySheet:propertySheetSection:CertNicknameProp:CertNickname"));
        assertEquals(keystore, selenium.getValue("propertyForm:propertySheet:propertySheetSection:keystore:keystore"));
        assertEquals(maxCertLength, selenium.getValue("propertyForm:propertySheet:propertySheetSection:maxCertLength:maxCertLength"));
//        assertTrue(selenium.isTextPresent("SSL_RSA_WITH_RC4_128_MD5 SSL_RSA_WITH_RC4_128_SHA TLS_RSA_WITH_AES_128_CBC_SHA TLS_RSA_WITH_AES_256_CBC_SHA SSL_RSA_WITH_3DES_EDE_CBC_SHA __________________________________"));
//        assertTrue(selenium.isTextPresent("TLS_DHE_RSA_WITH_AES_128_CBC_SHA TLS_DHE_RSA_WITH_AES_256_CBC_SHA SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA TLS_DHE_DSS_WITH_AES_128_CBC_SHA TLS_DHE_DSS_WITH_AES_256_CBC_SHA SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA ______________________________________"));
//        assertTrue(selenium.isTextPresent("SSL_RSA_WITH_DES_CBC_SHA SSL_DHE_RSA_WITH_DES_CBC_SHA SSL_DHE_DSS_WITH_DES_CBC_SHA SSL_RSA_EXPORT_WITH_RC4_40_MD5 SSL_RSA_EXPORT_WITH_DES40_CBC_SHA SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA __________________________________________"));
        
    }
}
