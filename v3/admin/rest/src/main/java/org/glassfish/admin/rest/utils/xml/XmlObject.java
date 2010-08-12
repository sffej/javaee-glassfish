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
package org.glassfish.admin.rest.utils.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jasonlee
 */
public class XmlObject {
    private String name;
    private Object value;
    private Map<String, Object> children = new HashMap<String, Object>();

    public XmlObject(String name) {
        this(name, null);
    }

    public XmlObject(String name, Object value) {
        this.name = name.toLowerCase();
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    protected Document getDocument() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public XmlObject put(String key, Object child) {
        if (child instanceof String) {
            children.put(key, child);
        } else if (child instanceof Number) {
            children.put(key, new XmlObject("Number", (Number)child));
        } else if (child instanceof XmlObject) {
            children.put(key, (XmlObject)child);
        }
        return this;
    }

    public Object remove(String key) {
        children.remove(key);
        return this;
    }

    public int childCount() {
        return children.size();
    }

    Node createNode(Document document) {
        Node node = document.createElement(getName());
        if (value != null) {
            node.setTextContent(value.toString());
        }
        Element element = (Element)node;
        for (Map.Entry<String, Object> child : children.entrySet()) {
            String key = child.getKey();
            Object value = child.getValue();
            if (value instanceof String) {
                element.setAttribute(key, value.toString());
            } else {
                XmlObject obj = (XmlObject)value;
                Node entryNode = document.createElement("entry");
                ((Element)entryNode).setAttribute("name", obj.getName());
                entryNode.appendChild(obj.createNode(document));
                node.appendChild(entryNode);
            }
//            element.setAttribute(attribute.getKey(), attribute.getValue());
        }

        return node;
    }

    @Override
    public String toString() {
        return toString(-1);
    }

    public String toString(int indent) {
        Document document = getDocument();
        document.appendChild(createNode(document));
        try {
            Source source = new DOMSource(document);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            if (indent > -1) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
            }
            transformer.transform(source, result);

/*
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
*/

            return stringWriter.getBuffer().toString();
        } catch (Exception ex) {
            Logger.getLogger(XmlEntity.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }


    }




//    public static class XmlNumber extends XmlObject {
//        private Number value;
//
//        public XmlNumber(Number value) {
//            this("", value);
//        }
//
//        public XmlNumber(String name, Number value) {
//            super(name);
//            this.value = value;
//        }
//
//        Node createNode(Document document) {
//            Node numberNode = document.createElement("number");
//            numberNode.setTextContent(value.toString());
//            return numberNode;
//        }
//    }
}

