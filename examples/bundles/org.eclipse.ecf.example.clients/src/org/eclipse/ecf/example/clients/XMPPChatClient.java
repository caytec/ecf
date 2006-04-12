/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.example.clients;

import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.ISharedObjectContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IDInstantiationException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IMessageListener;
import org.eclipse.ecf.presence.IMessageSender;
import org.eclipse.ecf.presence.IPresenceContainer;
import org.eclipse.ecf.presence.chat.IChatRoomContainer;
import org.eclipse.ecf.presence.chat.IChatRoomManager;

public class XMPPChatClient {
	
	protected static String CONTAINER_TYPE = "ecf.xmpp.smack";
	
	Namespace namespace = null;
	IContainer container = null;
	IPresenceContainer presence = null;
	IMessageSender sender = null;
	ID userID = null;
	IChatRoomManager chatmanager = null;
	IChatRoomContainer chatroom = null;
	ISharedObjectContainer socontainer = null;
	
	// Interface for receiving messages
	IMessageReceiver receiver = null;
	
	public XMPPChatClient() {
		this(null);
	}
	
	public XMPPChatClient(IMessageReceiver receiver) {
		super();
		this.receiver = receiver;
	}
	public void connect(String account, String password) throws ECFException {
		// Create container
		container = ContainerFactory.getDefault().createContainer(CONTAINER_TYPE);
		// create target id
		ID targetID = IDFactory.getDefault().createID(container.getConnectNamespace(), account);
		// Get presence adapter off of container
		presence = (IPresenceContainer) container
				.getAdapter(IPresenceContainer.class);
		// Get sender interface
		sender = presence.getMessageSender();
		// Setup message listener to handle incoming messages
		presence.addMessageListener(new IMessageListener() {
			public void handleMessage(ID fromID, ID toID, Type type, String subject, String messageBody) {
				if (receiver != null) {
					receiver.handleMessage(fromID.getName(), messageBody);
				}
			}
		});
		//
		// Now connect
		container.connect(targetID,ConnectContextFactory.createPasswordConnectContext(password));
		// Get a local ID for user account
		userID = getID(account);
	}
	
	public IChatRoomContainer connectChatRoom(String username, String hostname, String chatRoomID) throws Exception {
		// Get chat room manager
		chatmanager = presence.getChatRoomManager();
		// Create chat room container from manager
		chatroom = chatmanager.createChatRoomContainer();
		socontainer = (ISharedObjectContainer) chatroom.getAdapter(ISharedObjectContainer.class);
		// create target room id
		ID targetChatID = IDFactory.getDefault().createID(chatroom.getConnectNamespace(), new Object[] {username,hostname,null,chatRoomID,username});
		// connect to target
		chatroom.connect(targetChatID, null);
		return chatroom;
	}

	private ID getID(String name) {
		try {
			return IDFactory.getDefault().createID(namespace, name);
		} catch (IDInstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void sendMessage(String jid, String msg) {
		if (sender != null) {
			sender.sendMessage(userID, getID(jid),
					IMessageListener.Type.NORMAL, "", msg);
		}
	}
	public synchronized boolean isConnected() {
		if (container == null) return false;
		return (container.getConnectedID() != null);
	}
	public synchronized void close() {
		if (container != null) {
			container.dispose();
			container = null;
			presence = null;
			sender = null;
			receiver = null;
			userID = null;
		}
	}
}
