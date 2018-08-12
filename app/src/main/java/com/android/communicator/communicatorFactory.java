/*
 * @file CommunicatorFactory.java
 * @author dkalita
 *
 * Copyright (c) 2004-2015. Parallels IP Holdings GmbH. All rights reserved.
 * http://www.parallels.com
 */
package com.android.communicator;

public class communicatorFactory {
	public static BackendCommunicator createBackendCommunicator() {
		return new BackendCommunicatorStub();
	}
}
