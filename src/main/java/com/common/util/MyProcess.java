package com.common.util;

import java.io.InputStream;
import java.io.OutputStream;

public class MyProcess extends Process {
	private Process proc;
	private volatile boolean loop = true;

	public Process getProc() {
		return proc;
	}

	public boolean isLoop() {
		return loop;
	}

	/***
	 * should be invoked in end
	 */
	public int waitFor() throws InterruptedException {
		int waitFor_exitcode = proc.waitFor();
		/* must wait until ShellSwingWorker.done() is executed. */
		while (loop) {
			Thread.sleep(100);
		}
		return waitFor_exitcode;
	}

	public void stopLoop() {
		loop = false;
	}

	public OutputStream getOutputStream() {
		return proc.getOutputStream();
	}

	public InputStream getInputStream() {
		return proc.getInputStream();
	}

	public InputStream getErrorStream() {
		return proc.getErrorStream();
	}

	public int exitValue() {
		return proc.exitValue();
	}

	public MyProcess(Process proc) {
		super();
		this.proc = proc;
	}

	@Override
	public void destroy() {
		this.proc.destroy();

	}
}
