/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: DecimalPropertyDataBinding
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
package org.opencrx.kernel.portal;

import java.math.BigDecimal;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jmi.reflect.RefObject;

import org.opencrx.kernel.base.jmi1.DecimalProperty;
import org.opencrx.kernel.base.jmi1.Property;
import org.openmdx.portal.servlet.ApplicationContext;

/**
 * DecimalPropertyDataBinding
 *
 */
public class DecimalPropertyDataBinding extends AbstractPropertyDataBinding {

    /**
     * Constructor
     */
    public DecimalPropertyDataBinding(
    ) {
        super(PropertySetHolderType.CrxObject);
    }
    
    /**
     * Constructor.
     * @param type
     */
    public DecimalPropertyDataBinding(
        PropertySetHolderType type
    ) {
        super(type);
    }
    
    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.DataBinding#getValue(javax.jmi.reflect.RefObject, java.lang.String, org.openmdx.portal.servlet.ApplicationContext)
     */
    @Override
    public Object getValue(
        RefObject object, 
        String qualifiedFeatureName,
        ApplicationContext app
    ) {
        Property p = this.findProperty(object, qualifiedFeatureName);
        if(p instanceof DecimalProperty) {
            return ((DecimalProperty)p).getDecimalValue();
        }
        else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.DataBinding#setValue(javax.jmi.reflect.RefObject, java.lang.String, java.lang.Object, org.openmdx.portal.servlet.ApplicationContext)
     */
    @Override
    public void setValue(
        RefObject object, 
        String qualifiedFeatureName, 
        Object newValue,
        ApplicationContext app
    ) {
        Property p = this.findProperty(object, qualifiedFeatureName);
        if(p == null) {
        	PersistenceManager pm = JDOHelper.getPersistenceManager(object);
        	p = pm.newInstance(DecimalProperty.class);
            this.createProperty(
                object,
                qualifiedFeatureName,
                p
            );
        }
        if(p instanceof DecimalProperty) {
            ((DecimalProperty)p).setDecimalValue((BigDecimal)newValue);
        }
    }

}
