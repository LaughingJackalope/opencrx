/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: RunExport workflow
 * Owner:       CRIXP AG, Switzerland, http://www.crixp.com
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2017, CRIXP Corp., Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * * Neither the name of CRIXP Corp. nor the names of the contributors
 * to openCRX may be used to endorse or promote products derived
 * from this software without specific prior written permission
 * 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * ------------------
 * 
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 * 
 * This product includes software developed by contributors to
 * openMDX (http://www.openmdx.org/)
 */
package org.opencrx.kernel.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

import org.opencrx.kernel.backend.Workflows;
import org.opencrx.kernel.home1.jmi1.WfProcessInstance;
import org.opencrx.kernel.utils.WorkflowHelper;
import org.opencrx.kernel.workflow1.jmi1.ExporterTask;
import org.opencrx.kernel.workflow1.jmi1.RunExportResult;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;

/**
 * RunExport workflow.
 *
 */
public class RunExport extends Workflows.AsynchronousWorkflow {

	/* (non-Javadoc)
	 * @see org.opencrx.kernel.backend.Workflows.AsynchronousWorkflow#execute(org.opencrx.kernel.home1.jmi1.WfProcessInstance)
	 */
	@Override
	public void execute(
        WfProcessInstance wfProcessInstance
    ) throws ServiceException {
		PersistenceManager pm = JDOHelper.getPersistenceManager(wfProcessInstance);
		Object targetObject = pm.getObjectById(new Path(wfProcessInstance.getTargetObject()));
		if(targetObject instanceof ExporterTask) {
			ExporterTask exporterTask = (ExporterTask)targetObject;
			Map<String,Object> wfParams = new TreeMap<String,Object>(WorkflowHelper.getWorkflowParameters(wfProcessInstance));
			List<String> params = new ArrayList<String>();			
			for(Object value: wfParams.values()) {
				params.add(value.toString());
			}
			RunExportResult result = Workflows.getInstance().runExport(
				exporterTask, 
				params
			);
			WorkflowHelper.createLogEntry(
				wfProcessInstance, 
				"status", 
				Short.toString(result.getStatus())
			);
			WorkflowHelper.createLogEntry(
				wfProcessInstance, 
				"statusMessage", 
				result.getStatusMessage()
			);
			WorkflowHelper.createLogEntry(
				wfProcessInstance, 
				"fileMimeType", 
				result.getFileMimeType()
			);
			WorkflowHelper.createLogEntry(
				wfProcessInstance, 
				"fileName", 
				result.getFileName()
			);
			try {
				java.nio.file.Path filePath = Files.createTempFile(null, "-" + result.getFileName());
				WorkflowHelper.createLogEntry(
					wfProcessInstance,
					"file",
					filePath.toString()
				);
				FileOutputStream out = new FileOutputStream(filePath.toFile());
				out.write(result.getFile());
				out.close();
			} catch(Exception e) {
				new ServiceException(e).log();
			}
		}
    }

}
