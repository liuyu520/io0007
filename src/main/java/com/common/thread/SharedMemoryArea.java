package com.common.thread;

/***
 * Shared memory area.
 * 共享内存区
 * @author huangwei
 *
 */
public class SharedMemoryArea {
	private static boolean willStop=false;

	public static boolean isWillStop() {
		return willStop;
	}

	public static void setWillStop(boolean willStop) {
		SharedMemoryArea.willStop = willStop;
	}
	
}
