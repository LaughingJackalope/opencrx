/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: PrintConsole workflow
 * Owner:       the original authors.
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
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
 * * Neither the name of the openCRX team nor the names of the contributors
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

import java.util.Map;

import org.opencrx.kernel.backend.Workflows;
import org.opencrx.kernel.base.jmi1.WorkflowTarget;
import org.opencrx.kernel.home1.jmi1.WfProcessInstance;
import org.opencrx.kernel.utils.WorkflowHelper;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.jmi1.ContextCapable;

public class PrintConsole extends Workflows.SynchronousWorkflow {

	/* (non-Javadoc)
	 * @see org.opencrx.kernel.backend.Workflows.SynchronousWorkflow#execute(org.opencrx.kernel.base.jmi1.WorkflowTarget, org.openmdx.base.jmi1.ContextCapable, org.openmdx.base.jmi1.ContextCapable, org.opencrx.kernel.home1.jmi1.WfProcessInstance)
	 */
	@Override
	public void execute(
        WorkflowTarget wfTarget,
        ContextCapable targetObject,
        WfProcessInstance wfProcessInstance
    ) throws ServiceException {
        System.out.println("executing workflow " + wfProcessInstance.getProcess().getName());
        System.out.println("target=" + targetObject);
        System.out.println("wfProcessInstance=" + wfProcessInstance);
        Map<String,Object> params = WorkflowHelper.getWorkflowParameters(wfProcessInstance);
        System.out.println("params=" + params);
    }

}

//--- End of File -----------------------------------------------------------
