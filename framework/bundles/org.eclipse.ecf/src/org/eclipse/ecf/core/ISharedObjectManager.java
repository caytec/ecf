/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core;

import java.util.List;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;

/**
 * Manager for creating, disposing, and accessing ISharedObjects from an ISharedObjectContainer.
 *
 * @see ISharedObjectContainer#getSharedObjectManager()
 */
public interface ISharedObjectManager {

    /**
     * Get the array of SharedObject instances currently contained by this
     * ISharedObjectContainer
     * 
     * @return ID[] the IDs of currently contained ISharedObject instances
     */
    public ID[] getSharedObjectIDs();
    /**
     * Create a new ISharedObject within this container from the given
     * SharedObjectDescription.
     * 
     * @param sd
     *            the SharedObjectDescription that describes the SharedObject to
     *            be created
     * @param trans
     *            the transaction governing the creation of the shared object.
     *            If null, creation will not be transactional
     * @return ID the sharedObjectID of the added ISharedObject
     * @throws SharedObjectCreateException
     *             if the SharedObject cannot be created
     */
    public ID createSharedObject(SharedObjectDescription sd,
            ISharedObjectContainerTransaction trans)
            throws SharedObjectCreateException;
    /**
     * Add an ISharedObject to this container.
     * 
     * @param sharedObjectID
     *            the ID of new SharedObject
     * @param sharedObject
     *            the ISharedObject instance to add
     * @param properties
     *            the Map associated with the added ISharedObject
     * @param trans
     *            the transaction governing the creation of the shared object.
     *            If null, creation will not be transactional
     * @return ID the sharedObjectID of the added ISharedObject
     * @throws SharedObjectAddException
     *             if the add cannot be accomplished for any reason
     */
    public ID addSharedObject(ID sharedObjectID, ISharedObject sharedObject,
            Map properties, ISharedObjectContainerTransaction trans)
            throws SharedObjectAddException;

    /**
     * Get the ISharedObject instance corresponding to the given sharedObjectID.
     * 
     * @param sharedObjectID
     *            of the desired ISharedObject
     * @return ISharedObject found. Return null if ISharedObject not found.
     */
    public ISharedObject getSharedObject(ID sharedObjectID);
    /**
     * Remove the given sharedObjectID from this ISharedObjectContainer.
     * 
     * @param sharedObjectID
     *            the ID of the ISharedObject to remove
     * @return ISharedObject removed. Returns null if ISharedObject not found
     */
    public ISharedObject removeSharedObject(ID sharedObjectID);
    /**
     * Create an ISharedObjectConnector instance for sending messages from a
     * single ISharedObject to one or more receiver ISharedObjects. All
     * specified ISharedObject instances must be contained by this
     * ISharedObjectContainer.
     * 
     * @param sharedObjectFrom
     *            the ID of the sender ISharedObject
     * @param sharedObjectsTo
     *            the ID[] of the receiver ISharedObjects
     * @return a valid instance of ISharedObjectConnector. Null if no connector
     *         can be made
     * @throws SharedObjectConnectException
     *             thrown if specified sender or receivers do not exist within
     *             the context of this container
     */
    public ISharedObjectConnector connectSharedObjects(ID sharedObjectFrom,
            ID[] sharedObjectsTo) throws SharedObjectConnectException;
    /**
     * Destroy an ISharedObjectConnector instance.
     * 
     * @param connector
     *            the connector previously created via connectSharedObjects
     * @throws SharedObjectConnectException
     *             thrown if specified connector does not exist in the context
     *             of this container
     */
    public void disconnectSharedObjects(ISharedObjectConnector connector)
            throws SharedObjectDisconnectException;
    
    /**
     * Get the sharedObjectConnectors associated with the given sharedObjectID
     *
     * @return List of ISharedObjectConnector instances
     */
    public List getSharedObjectConnectors(ID sharedObjectFrom);
}
