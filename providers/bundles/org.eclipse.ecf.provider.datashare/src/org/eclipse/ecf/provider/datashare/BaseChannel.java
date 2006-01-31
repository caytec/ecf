/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc., Peter Nehrer, Boris Bokowski. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.datashare;

import java.io.Serializable;
import java.util.Arrays;
import org.eclipse.ecf.core.ISharedObjectTransactionConfig;
import org.eclipse.ecf.core.ReplicaSharedObjectDescription;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.TransactionSharedObject;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.core.util.IEventProcessor;
import org.eclipse.ecf.datashare.IChannel;
import org.eclipse.ecf.datashare.IChannelListener;
import org.eclipse.ecf.datashare.events.IChannelEvent;
import org.eclipse.ecf.datashare.events.IChannelGroupDepartEvent;
import org.eclipse.ecf.datashare.events.IChannelGroupJoinEvent;
import org.eclipse.ecf.datashare.events.IChannelInitializeEvent;
import org.eclipse.ecf.datashare.events.IChannelMessageEvent;

public class BaseChannel extends TransactionSharedObject implements IChannel {
	static class ChannelMsg implements Serializable {
		private static final long serialVersionUID = 9065358269778864152L;
		byte[] channelData = null;
		ChannelMsg() {
		}
		ChannelMsg(byte[] data) {
			this.channelData = data;
		}
		byte[] getData() {
			return channelData;
		}
		public String toString() {
			StringBuffer buf = new StringBuffer("BaseChannel.ChannelMsg[");
			buf.append("data=").append(getData()).append("]");
			return buf.toString();
		}
	}
	protected IChannelListener listener;
	protected void setChannelListener(IChannelListener l) {
		this.listener = l;
	}
	/**
	 * Host implementation of channel class constructor
	 * 
	 * @param config
	 *            the ISharedObjectTransactionConfig associated with this new
	 *            host instance
	 * @param listener
	 *            the listener associated with this channel instance
	 */
	public BaseChannel(ISharedObjectTransactionConfig config,
			IChannelListener listener) {
		super(config);
		setChannelListener(listener);
	}
	/**
	 * Replica implementation of channel class constructor
	 * 
	 */
	public BaseChannel() {
		super();
	}
	/**
	 * Override of TransasctionSharedObject.initialize(). This method is called
	 * on both the host and the replicas during initialization. <b>Subclasses
	 * that override this method should be certain to call super.initialize() as
	 * the first thing in their own initialization so they get the
	 * initialization defined by TransactionSharedObject and
	 * AbstractSharedObject.</b>
	 * 
	 * @throws SharedObjectInitException
	 *             if initialization should fail
	 */
	protected void initialize() throws SharedObjectInitException {
		super.initialize();
		// For the replicas, setup a channel listener that calls
		// handleReplicaChannelEvent
		if (!isPrimary()) {
			setChannelListener(new IChannelListener() {
				public void handleChannelEvent(IChannelEvent event) {
					receiveChannelEvent(event);
				}
			});
		}
		addEventProcessor(new IEventProcessor() {
			public boolean acceptEvent(Event event) {
				if (event instanceof IContainerConnectedEvent)
					return true;
				else if (event instanceof IContainerDisconnectedEvent)
					return true;
				else if (event instanceof ISharedObjectMessageEvent)
					return true;
				return false;
			}
			public Event processEvent(Event event) {
				if (event instanceof IContainerConnectedEvent)
					BaseChannel.this.listener
							.handleChannelEvent(createChannelGroupJoinEvent(
									true, ((IContainerConnectedEvent) event)
											.getTargetID()));
				else if (event instanceof IContainerDisconnectedEvent)
					BaseChannel.this.listener
							.handleChannelEvent(createChannelGroupDepartEvent(
									true, ((IContainerDisconnectedEvent) event)
											.getTargetID()));
				else if (event instanceof ISharedObjectMessageEvent)
					BaseChannel.this
							.handleMessageEvent((ISharedObjectMessageEvent) event);
				return event;
			}
		});
		listener.handleChannelEvent(new IChannelInitializeEvent() {
			public ID[] getGroupMembers() {
				return getContext().getGroupMemberIDs();
			}
			public ID getChannelID() {
				return getID();
			}
			public String toString() {
				StringBuffer buf = new StringBuffer("ChannelInitializeEvent[");
				buf.append("chid=").append(getChannelID()).append(
						";groupMembers=").append(Arrays.asList(getGroupMembers())).append("]");
				return buf.toString();
			}
		});
	}
	/**
	 * Override of TransactionSharedObject.getAdapter()
	 */
	public Object getAdapter(Class clazz) {
		if (clazz.equals(IChannel.class)) {
			return this;
		} else
			return super.getAdapter(clazz);
	}
	private IChannelGroupJoinEvent createChannelGroupJoinEvent(
			final boolean hasJoined, final ID targetID) {
		return new IChannelGroupJoinEvent() {
			private static final long serialVersionUID = -1085237280463725283L;
			public ID getTargetID() {
				return targetID;
			}
			public ID getChannelID() {
				return getID();
			}
			public String toString() {
				StringBuffer buf = new StringBuffer("ChannelGroupJoinEvent[");
				buf.append("chid=").append(getChannelID()).append(";targetid=")
						.append(getTargetID()).append("]");
				return buf.toString();
			}
		};
	}
	private IChannelGroupDepartEvent createChannelGroupDepartEvent(
			final boolean hasJoined, final ID targetID) {
		return new IChannelGroupDepartEvent() {
			private static final long serialVersionUID = -1085237280463725283L;
			public ID getTargetID() {
				return targetID;
			}
			public ID getChannelID() {
				return getID();
			}
			public String toString() {
				StringBuffer buf = new StringBuffer(
						"ChannelGroupDepartedEvent[");
				buf.append("chid=").append(getChannelID()).append(";targetid=")
						.append(getTargetID()).append("]");
				return buf.toString();
			}
		};
	}
	private Event handleMessageEvent(final ISharedObjectMessageEvent event) {
		Object eventData = event.getData();
		ChannelMsg channelMsg = null;
		if (eventData instanceof ChannelMsg) {
			channelMsg = (ChannelMsg) eventData;
			final byte[] channelData = channelMsg.getData();
			if (channelData != null) {
				listener.handleChannelEvent(new IChannelMessageEvent() {
					private static final long serialVersionUID = -2270885918818160970L;
					public ID getFromContainerID() {
						return event.getRemoteContainerID();
					}
					public byte[] getData() {
						return (byte[]) channelData;
					}
					public ID getChannelID() {
						return getID();
					}
					public String toString() {
						StringBuffer buf = new StringBuffer(
								"ChannelMessageEvent[");
						buf.append("chid=").append(getChannelID()).append(
								";fromid=").append(getFromContainerID())
								.append(";data=").append(getData()).append("]");
						return buf.toString();
					}
				});
				// Discontinue processing of this event...we are it
				return null;
			}
		}
		return event;
	}
	
	// Implementation of org.eclipse.ecf.datashare.IChannel
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.datashare.IChannel#sendMessage(byte[])
	 */
	public void sendMessage(byte[] message) throws ECFException {
		sendMessage(null, message);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.datashare.IChannel#sendMessage(org.eclipse.ecf.core.identity.ID,
	 *      byte[])
	 */
	public void sendMessage(ID receiver, byte[] message) throws ECFException {
		try {
			getContext().sendMessage(receiver, new ChannelMsg(message));
		} catch (Exception e) {
			throw new ECFException("send message exception", e);
		}
	}
	
	// Subclass API
	/**
	 * Receive and process channel events. This method can be overridden by
	 * subclasses to process channel events in a sub-class specific manner.
	 * 
	 * @param channelEvent
	 *            the IChannelEvent to receive and process
	 */
	protected void receiveChannelEvent(IChannelEvent channelEvent) {
		if (isPrimary())
			System.out.println("host.receiveChannelEvent(" + channelEvent + ")");
		else
			System.out.println("replica.receiveChannelEvent(" + channelEvent + ")");
	}
	/**
	 * Override of AbstractSharedObject.getReplicaDescription.  Note this method
	 * should be overridden by subclasses that wish to specify the type of the 
	 * replica created.
	 * 
	 * @param receiver the ID of the target container for replica creation.  If
	 * null, the target is <b>all</b> current group members
	 * @return ReplicaSharedObjectDescripton to be used for creating remote replica
	 * of this host shared object.  If null, no create message will be sent
	 * to the target container.
	 */
	protected ReplicaSharedObjectDescription getReplicaDescription(ID targetContainer) {
		return new ReplicaSharedObjectDescription(getClass(), getID(),
				getConfig().getHomeContainerID(), getConfig().getProperties());
	}

}
