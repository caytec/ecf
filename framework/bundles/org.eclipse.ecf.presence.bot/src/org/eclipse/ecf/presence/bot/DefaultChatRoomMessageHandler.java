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

package org.eclipse.ecf.presence.bot;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.presence.chatroom.IChatRoomContainer;
import org.eclipse.ecf.presence.chatroom.IChatRoomMessage;

/**
 *
 */
public class DefaultChatRoomMessageHandler implements IChatRoomMessageHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.bot.handler.IChatRoomMessageHandler#handleRoomMessage(org.eclipse.ecf.presence.chatroom.IChatRoomMessage)
	 */
	public void handleRoomMessage(IChatRoomMessage message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.bot.handler.IChatRoomMessageHandler#preRoomConnect(org.eclipse.ecf.presence.chatroom.IChatRoomContainer, org.eclipse.ecf.core.identity.ID)
	 */
	public void preRoomConnect(IChatRoomContainer roomContainer, ID roomID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.bot.handler.IChatRoomContainerAdvisor#preChatRoomConnect(org.eclipse.ecf.presence.chatroom.IChatRoomContainer, org.eclipse.ecf.core.identity.ID)
	 */
	public void preChatRoomConnect(IChatRoomContainer roomContainer, ID roomID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.bot.handler.IContainerAdvisor#init(org.eclipse.ecf.core.IContainer)
	 */
	public void init(IContainer container) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.bot.handler.IContainerAdvisor#preContainerConnect(org.eclipse.ecf.core.identity.ID)
	 */
	public void preContainerConnect(ID targetID) {
		// TODO Auto-generated method stub
		
	}

}
