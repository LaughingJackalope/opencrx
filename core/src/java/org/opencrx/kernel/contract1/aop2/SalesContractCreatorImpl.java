/*
 * ====================================================================
 * Project:     openCRX/Core, http://www.opencrx.org/
 * Description: SalesContractCreatorImpl
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
package org.opencrx.kernel.contract1.aop2;

import org.opencrx.kernel.backend.Contracts;
import org.opencrx.kernel.contract1.jmi1.AbstractContract;
import org.openmdx.base.accessor.jmi.cci.JmiServiceException;
import org.openmdx.base.exception.ServiceException;
import org.w3c.spi2.Datatypes;
import org.w3c.spi2.Structures;


/**
 * SalesContractCreatorImpl
 *
 * @param <S>
 * @param <N>
 * @param <C>
 */
public class SalesContractCreatorImpl
	<S extends org.opencrx.kernel.contract1.jmi1.SalesContractCreator,N extends org.opencrx.kernel.contract1.cci2.SalesContractCreator,C extends Void>
	extends ContractCreatorImpl<S,N,C> {

    /**
     * Constructor.
     * 
     * @param same
     * @param next
     */
    public SalesContractCreatorImpl(
        S same,
        N next
    ) {
    	super(same, next);
    }

    /**
     * Create sales contract.
     * 
     * @param params
     * @return
     */
    public org.opencrx.kernel.contract1.jmi1.CreateContractResult createSalesContract(
        org.opencrx.kernel.contract1.jmi1.CreateSalesContractParams params
    ) {
        try {        
            AbstractContract contract = Contracts.getInstance().createSalesContract(
                this.sameObject(),
                params.getName(),
                params.getDescription(),
                params.getContractType(),
                params.getActiveOn(),
                params.getPriority(),
                params.getPricingDate(),
                params.getContractCurrency(),
                params.getPaymentTerms(),
                params.getCustomer(),
                params.getSalesRep(),
                params.getBroker(),
                params.getSupplier(),
                params.getSalesTaxTypeGroup(),
                params.getBasedOn()
            );
            return Structures.create(
            	org.opencrx.kernel.contract1.jmi1.CreateContractResult.class, 
            	Datatypes.member(org.opencrx.kernel.contract1.jmi1.CreateContractResult.Member.contract, contract)
            );            
        } catch(ServiceException e) {
            throw new JmiServiceException(e);
        }    	
    }

}
