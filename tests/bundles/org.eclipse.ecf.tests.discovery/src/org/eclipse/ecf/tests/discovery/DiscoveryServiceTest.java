/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.tests.discovery;

import org.eclipse.ecf.discovery.IDiscoveryContainerAdapter;
import org.eclipse.ecf.discovery.service.IDiscoveryService;

public abstract class DiscoveryServiceTest extends DiscoveryTest {

	/**
	 * @param name
	 * @param aDiscoveryContainerInterval 
	 */
	public DiscoveryServiceTest(String name, long aDiscoveryContainerInterval) {
		super(name, aDiscoveryContainerInterval);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.discovery.DiscoveryTest#getAdapter(java.lang.Class)
	 */
	protected IDiscoveryContainerAdapter getAdapter(Class clazz) {
		final IDiscoveryService discoveryService = Activator.getDefault().getDiscoveryService();
		assertNotNull(discoveryService);
		return discoveryService;
	}

}
