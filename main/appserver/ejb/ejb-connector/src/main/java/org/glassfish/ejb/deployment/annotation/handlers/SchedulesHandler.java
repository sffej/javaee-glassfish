/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package org.glassfish.ejb.deployment.annotation.handlers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Schedules;

import org.glassfish.apf.HandlerProcessingResult;
import org.glassfish.apf.AnnotationInfo;
import org.glassfish.apf.AnnotationProcessorException;
import com.sun.enterprise.deployment.annotation.context.EjbContext;
import org.jvnet.hk2.annotations.Service;

/**
 * This handler is responsible for handling the javax.ejb.Schedules attribute
 *
 */
@Service
public class SchedulesHandler extends ScheduleHandler {
    
    public SchedulesHandler() {
    }
    
    /**
     * @return the annoation type this annotation handler is handling
     */
    public Class<? extends Annotation> getAnnotationType() {
        return Schedules.class;
    }    
        

    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            EjbContext[] ejbContexts) throws AnnotationProcessorException {

        Schedules annotation = (Schedules) ainfo.getAnnotation();
        
        Schedule[] schAnnotations = annotation.value();
        List<HandlerProcessingResult> results = new ArrayList<HandlerProcessingResult>();

        for(Schedule sch : schAnnotations) {
            results.add(processSchedule(sch, ainfo, ejbContexts));
        }

        return getOverallProcessingResult(results);
    }

}
