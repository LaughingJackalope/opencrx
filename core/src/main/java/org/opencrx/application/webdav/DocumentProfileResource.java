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
package org.opencrx.application.webdav;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

import org.opencrx.application.uses.net.sf.webdav.RequestContext;
import org.opencrx.application.uses.net.sf.webdav.Resource;
import org.opencrx.kernel.home1.cci2.DocumentFeedQuery;
import org.opencrx.kernel.home1.cci2.DocumentFilterFeedQuery;
import org.opencrx.kernel.home1.jmi1.DocumentFeed;
import org.opencrx.kernel.home1.jmi1.DocumentFilterFeed;
import org.opencrx.kernel.home1.jmi1.DocumentProfile;
import org.opencrx.kernel.home1.jmi1.SyncProfile;

/**
 * DocumentProfileResource
 *
 */
class DocumentProfileResource extends WebDavResource {
	
	/**
	 * Constructor.
	 * 
	 * @param requestContext
	 * @param syncProfile
	 */
	public DocumentProfileResource(
		RequestContext requestContext,
		DocumentProfile syncProfile
	) {
		super(requestContext, syncProfile);
	}

	/* (non-Javadoc)
	 * @see org.opencrx.application.webdav.WebDavResource#getObject()
	 */
	@Override
    public DocumentProfile getObject(
    ) {
        return (DocumentProfile)super.getObject();
    }

	/* (non-Javadoc)
	 * @see org.opencrx.application.uses.net.sf.webdav.Resource#getDisplayName()
	 */
	@Override
	public String getDisplayName(
	) {
		return this.getObject().getName();
	}
	
	/* (non-Javadoc)
	 * @see org.opencrx.application.uses.net.sf.webdav.Resource#isCollection()
	 */
	@Override
    public boolean isCollection(
    ) {
		return true;
    }
	
	/* (non-Javadoc)
	 * @see org.opencrx.application.webdav.WebDavResource#getChildren()
	 */
	@Override
	public Collection<Resource> getChildren(
		Date timeRangeStart,
		Date timeRangeEnd
	) {
		SyncProfile syncProfile = this.getObject();
		PersistenceManager pm = JDOHelper.getPersistenceManager(syncProfile);
		Collection<Resource> children = new ArrayList<Resource>();
		// DocumentFolder feeds
		DocumentFeedQuery documentFeedQuery = (DocumentFeedQuery)pm.newQuery(DocumentFeed.class);
		documentFeedQuery.thereExistsIsActive().isTrue();
		for(DocumentFeed documentFeed: syncProfile.<DocumentFeed>getFeed(documentFeedQuery)) {
			children.add(
				new DocumentCollectionResource(
					this.getRequestContext(),
					documentFeed
				)
			);
		}
		// DocumentFilter feeds
		DocumentFilterFeedQuery documentFilterFeedQuery = (DocumentFilterFeedQuery)pm.newQuery(DocumentFilterFeed.class);
		documentFilterFeedQuery.thereExistsIsActive().isTrue();
		for(DocumentFilterFeed documentFilterFeed: syncProfile.<DocumentFilterFeed>getFeed(documentFilterFeedQuery)) {
			children.add(
				new DocumentCollectionResource(
					this.getRequestContext(),
					documentFilterFeed
				)
			);
		}
		return children;
	}

}
