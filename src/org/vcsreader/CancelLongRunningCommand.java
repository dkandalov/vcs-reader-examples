package org.vcsreader;

import org.vcsreader.vcs.git.GitVcsRoot;

import static java.nio.file.Files.createTempDirectory;

public class CancelLongRunningCommand {
	public static void main(String[] args) throws Exception {
		String tempDirectory = createTempDirectory("").toFile().getAbsolutePath();
		VcsProject vcsProject = new VcsProject(new GitVcsRoot(tempDirectory, "https://github.com/hamcrest/JavaHamcrest"));

		new Thread(() -> {
			sleep();
			boolean notRunning = vcsProject.cancelLastCommand();
			System.out.println(notRunning ? "Cancelled" : "Failed to cancel");
		}).start();

		System.out.println("Cloning...");
		CloneResult cloneResult = vcsProject.cloneIt();

		System.out.println("Successful cloning: " + cloneResult.isSuccessful());
	}

	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignored) {
		}
	}
}
