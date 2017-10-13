/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: DoPropfind
 * Owner:       CRIXP AG, Switzerland, http://www.crixp.com
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2017, CRIXP Corp., Switzerland
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
package org.opencrx.application.carddav;

import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencrx.application.uses.net.sf.webdav.RequestContext;
import org.opencrx.application.uses.net.sf.webdav.Resource;
import org.opencrx.application.uses.net.sf.webdav.WebDavStore;
import org.opencrx.application.uses.net.sf.webdav.fromcatalina.XMLWriter;
import org.opencrx.kernel.home1.jmi1.SyncProfile;
import org.opencrx.kernel.home1.jmi1.UserHome;

/**
 * DoPropfind
 *
 */
public class DoPropfind extends org.opencrx.application.uses.net.sf.webdav.methods.DoPropfind {

	/**
	 * Constructor.
	 * 
	 * @param store
	 */
	public DoPropfind(
		WebDavStore store 
	) {
	    super(store);
    }
	
	/* (non-Javadoc)
	 * @see org.opencrx.application.uses.net.sf.webdav.methods.DoPropfind#getNamespaces()
	 */
	@Override
    protected Map<String, String> getNamespaces(
    ) {
		Map<String,String> namespaces = super.getNamespaces();
		namespaces.put("urn:ietf:params:xml:ns:carddav", "C");
		return namespaces;
    }

	
	/* (non-Javadoc)
	 * @see org.opencrx.application.uses.net.sf.webdav.methods.DoPropfind#writeCollectionType(org.opencrx.application.uses.net.sf.webdav.RequestContext, org.opencrx.application.uses.net.sf.webdav.fromcatalina.XMLWriter, org.opencrx.application.uses.net.sf.webdav.Resource)
	 */
	@Override
    protected void writeCollectionType(
    	RequestContext requestContext, 
    	XMLWriter writer, 
    	Resource res
    ) {
		if(res instanceof AccountCollectionResource) {
			writer.writeElement("urn:ietf:params:xml:ns:carddav:addressbook", XMLWriter.NO_CONTENT);
		}
    }

	/* (non-Javadoc)
	 * @see org.opencrx.application.uses.net.sf.webdav.methods.WebDavMethod#getVersion()
	 */
	@Override
    protected String getVersion(
    ) {
    	return "1, 2, addressbook";
    }
	
	/* (non-Javadoc)
	 * @see org.opencrx.application.uses.net.sf.webdav.methods.DoPropfind#handleExtension(org.opencrx.application.uses.net.sf.webdav.RequestContext, org.opencrx.application.uses.net.sf.webdav.fromcatalina.XMLWriter, java.lang.String, org.opencrx.application.uses.net.sf.webdav.Resource, java.lang.String)
	 */
	@Override
    protected boolean handleExtension(
    	RequestContext requestContext,
    	XMLWriter writer,
    	String contextPath,
    	Resource res,
    	String property
    ) {
		HttpServletRequest req = requestContext.getHttpServletRequest();
		HttpServletResponse resp = requestContext.getHttpServletResponse();
		if(res instanceof CardProfileResource) {
			SyncProfile syncProfile = ((CardProfileResource)res).getObject();
			PersistenceManager pm = JDOHelper.getPersistenceManager(syncProfile);
    		UserHome userHome = (UserHome)pm.getObjectById(
    			syncProfile.refGetPath().getParent().getParent()
    		);
    		String providerName = userHome.refGetPath().getSegment(2).toClassicRepresentation();
    		String segmentName = userHome.refGetPath().getSegment(4).toClassicRepresentation();
			if(property.indexOf("current-user-principal") > 0) {
                writer.writeElement("DAV::principal-URL", XMLWriter.OPENING);
                writer.writeElement("DAV::href", XMLWriter.OPENING);
                writer.writeText(this.encodeURL(resp, this.getHRef(req, "/" + providerName + "/" + segmentName + "/user/" + userHome.refGetPath().getLastSegment() + "/profile/" + syncProfile.getName(), true)));
                writer.writeElement("DAV::href", XMLWriter.CLOSING);
                writer.writeElement("DAV::principal-URL", XMLWriter.CLOSING);
                return true;
			} else if(property.indexOf("principal-address") > 0) {
	            writer.writeElement("urn:ietf:params:xml:ns:carddav:principal-address", XMLWriter.OPENING);
	            writer.writeElement("DAV::href", XMLWriter.OPENING);
	            writer.writeText(resp.encodeRedirectURL(this.getHRef(req, req.getServletPath(), true)));
	            writer.writeElement("DAV::href", XMLWriter.CLOSING);
	            writer.writeElement("urn:ietf:params:xml:ns:carddav:principal-address", XMLWriter.CLOSING);
	            return true;
			} else if(property.indexOf("addressbook-home-set") > 0) {
	            writer.writeElement("urn:ietf:params:xml:ns:carddav:addressbook-home-set", XMLWriter.OPENING);
	            writer.writeElement("DAV::href", XMLWriter.OPENING);
	            writer.writeText(this.encodeURL(resp, this.getHRef(req, "/" + providerName + "/" + segmentName + "/" + userHome.refGetPath().getLastSegment() + "/" + res.getName(), true)));
	            writer.writeElement("DAV::href", XMLWriter.CLOSING);
	            writer.writeElement("urn:ietf:params:xml:ns:carddav:addressbook-home-set", XMLWriter.CLOSING);
	            return true;
			} else if(property.indexOf("principal-URL") > 0) {
	            writer.writeElement("DAV::principal-URL", XMLWriter.OPENING);
	            writer.writeElement("DAV::href", XMLWriter.OPENING);
	            writer.writeText(this.encodeURL(resp, this.getHRef(req, "/" + providerName + "/" + segmentName + "/user/" + userHome.refGetPath().getLastSegment() + "/profile/" + syncProfile.getName(), true)));
	            writer.writeElement("DAV::href", XMLWriter.CLOSING);
	            writer.writeElement("DAV::principal-URL", XMLWriter.CLOSING);
	            return true;				
			}
			else {
				return false;
			}
		} else if(res instanceof AccountCollectionResource) {
			AccountCollectionResource accountCollectionResource = (AccountCollectionResource)res;
			if(property.indexOf("addressbook-description") > 0) {
				writer.writeElement("urn:ietf:params:xml:ns:carddav:addressbook-description", XMLWriter.OPENING);
	            writer.writeData(res.getDisplayName());				
				writer.writeElement("urn:ietf:params:xml:ns:carddav:addressbook-description", XMLWriter.CLOSING);
				return true;
			} else if(property.indexOf("supported-address-data") > 0) {
				writer.writeElement("urn:ietf:params:xml:ns:carddav:supported-address-data", XMLWriter.OPENING);
				writer.writeText("<C:address-data-type content-type=\"text/vcard\" version=\"3.0\"/>");				
				writer.writeElement("urn:ietf:params:xml:ns:carddav:supported-address-data", XMLWriter.CLOSING);	
				return true;
			} else if(property.indexOf("getctag") > 0) {
				writer.writeElement("http://calendarserver.org/ns/:getctag", XMLWriter.OPENING);
	            writer.writeText(Long.toString(System.currentTimeMillis()));				
				writer.writeElement("http://calendarserver.org/ns/:getctag", XMLWriter.CLOSING);				
				return true;
			} else if(property.indexOf("current-user-privilege-set") > 0) {
				writer.writeElement("DAV::current-user-privilege-set", XMLWriter.OPENING);
				writer.writeElement("DAV::privilege", XMLWriter.OPENING);
				if(
					Boolean.TRUE.equals(accountCollectionResource.getObject().isAllowAddDelete()) &&
					Boolean.TRUE.equals(accountCollectionResource.getObject().isAllowChange())
				) {
					writer.writeElement("DAV::all", XMLWriter.NO_CONTENT);
				} else {
					writer.writeElement("DAV::read", XMLWriter.NO_CONTENT);
				}
				writer.writeElement("DAV::privilege", XMLWriter.CLOSING);
				writer.writeElement("DAV::current-user-privilege-set", XMLWriter.CLOSING);
				return true;
			} else {
				return false;
			}
		} else if(res instanceof AccountResource) {
			if(property.indexOf("getctag") > 0) {
				writer.writeElement("http://calendarserver.org/ns/:getctag", XMLWriter.OPENING);
	            writer.writeText(this.getETag(res));				
				writer.writeElement("http://calendarserver.org/ns/:getctag", XMLWriter.CLOSING);				
				return true;
			} else if(property.indexOf("owner") > 0) {
				return true;
			}
		}
		return false;
    }

}
