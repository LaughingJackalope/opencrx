/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: openCRX application plugin
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
package org.opencrx.application.caldav;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.opencrx.application.uses.net.sf.webdav.RequestContext;
import org.opencrx.application.uses.net.sf.webdav.Resource;
import org.opencrx.application.uses.net.sf.webdav.WebDavStore;
import org.openmdx.base.jmi1.ContextCapable;
import org.openmdx.base.jmi1.Creatable;
import org.openmdx.base.jmi1.Modifiable;
import org.w3c.cci2.BinaryLargeObject;
import org.w3c.cci2.BinaryLargeObjects;

abstract class CalDavResource implements Resource {

	public CalDavResource(
		RequestContext requestContext,
		ContextCapable object
	) {
		this.requestContext = requestContext;
		this.object = object;
	}

	@Override
    public Date getCreationDate(
    ) {
		return this.object instanceof Creatable
			? ((Creatable)this.object).getCreatedAt()
			: new Date();
    }

	@Override
    public Date getLastModified(
    ) {
		return this.object instanceof Modifiable
			? ((Modifiable)this.object).getModifiedAt()
			: new Date();
    }

	public ContextCapable getObject(
	) {
		return this.object;
	}

	public <T extends Resource> Collection<T> getChildren(
		Date timeRangeStart,
		Date timeRangeEnd
	) {
		return Collections.emptyList();
	}

	public CalDavRequestContext getRequestContext(
	) {
		return (CalDavRequestContext)this.requestContext;
	}
	
	@Override
    public String getName(
    ) {
		return this.object.refGetPath().getLastSegment().toString();
    }

	public String getMimeType(
	) {
		return "application/xml";
	}

	public WebDavStore.ResourceContent getContent(
	) {
		return new WebDavStore.ResourceContent(){
			@Override
			public BinaryLargeObject getContent() {
				return BinaryLargeObjects.valueOf(new byte[]{});
			}
			@Override
			public Long getLength() {
				return 0L;
			}
		};
	}

	private final RequestContext requestContext;
	private final ContextCapable object;
}