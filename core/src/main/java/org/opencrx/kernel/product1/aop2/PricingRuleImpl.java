/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: PricingRuleImpl
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
package org.opencrx.kernel.product1.aop2;

import org.opencrx.kernel.backend.Products;
import org.opencrx.kernel.product1.jmi1.GetPriceLevelResult;
import org.openmdx.base.accessor.jmi.cci.JmiServiceException;
import org.openmdx.base.aop2.AbstractObject;
import org.openmdx.base.exception.ServiceException;

/**
 * PricingRuleImpl
 *
 * @param <S>
 * @param <N>
 * @param <C>
 */
public class PricingRuleImpl
	<S extends org.opencrx.kernel.product1.jmi1.PricingRule,N extends org.opencrx.kernel.product1.cci2.PricingRule,C extends Void>
	extends AbstractObject<S,N,C> {
    
    /**
     * Constructor.
     * 
     * @param same
     * @param next
     */
    public PricingRuleImpl(
        S same,
        N next
    ) {
    	super(same, next);
    }

    /**
     * Get price level.
     * 
     * @param params
     * @return
     */
    public org.opencrx.kernel.product1.jmi1.GetPriceLevelResult getPriceLevel(
        org.opencrx.kernel.product1.jmi1.GetPriceLevelParams params
    ) {
        try {        
            GetPriceLevelResult result = Products.getInstance().getPriceLevel(
                this.sameObject(),
                params.getContract(),
                params.getPosition(),
                params.getProduct(),
                params.getPriceUom(),
                params.getQuantity(),
                params.getContractPositionState(),
                params.getPricingDate(),
                params.getOverridePriceLevel()
            );
            return result;
        } catch(ServiceException e) {
            throw new JmiServiceException(e);
        }
    }
    
}
