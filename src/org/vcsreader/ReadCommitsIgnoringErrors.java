package org.vcsreader;

import org.vcsreader.lang.TimeRange;
import org.vcsreader.vcs.git.GitSettings;
import org.vcsreader.vcs.git.GitVcsRoot;

import java.io.File;

public class ReadCommitsIgnoringErrors {
	/**
	 * Sometimes it might be desirable to continue execution even if it has partially failed.
	 * In real world, VCS quite often can fail on some commits because of data corruption.
	 * Below one VCS roots has invalid URL to simulate failure.
	 */
	public static void main(String[] args) {
		GitSettings settings = GitSettings.defaults().withFailFast(false);
		VcsProject vcsProject = new VcsProject(
				new GitVcsRoot(".junit4-clone", "https://github.com/junit-team/junit4", settings),
				new GitVcsRoot(".invalid-project", "invalid.url", settings)
		);


		System.out.println("Cloning repositories...");
		for (VcsRoot vcsRoot : vcsProject.vcsRoots()) {
			if (new File(vcsRoot.repoFolder()).exists()) continue;
			System.out.println("Cloning " + vcsRoot.repoUrl());

			CloneResult cloneResult = vcsRoot.cloneIt();

			if (!cloneResult.isSuccessful()) {
				for (Exception e : cloneResult.exceptions()) {
					System.out.println("Clone error: \"" + e.getMessage().trim() + "\"");
				}
			}
		}


		System.out.println("Reading commits...");
		LogResult logResult = vcsProject.log(TimeRange.beforeNow());
		if (!logResult.isSuccessful()) {
			for (Exception exception : logResult.exceptions()) {
				System.out.println("Log exception: '" + exception.getMessage() + "'");
			}
		}
		System.out.println("Total commits: " + logResult.commits().size());
	}
}
