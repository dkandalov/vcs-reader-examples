package org.vcsreader;

import org.vcsreader.lang.TimeRange;
import org.vcsreader.vcs.git.GitSettings;
import org.vcsreader.vcs.git.GitVcsRoot;

import java.io.File;

public class ReadCommitsIgnoringErrors {
	public static void main(String[] args) {
		// Sometimes it might be desirable to continue executing action even if some of them fail.
		// For example, below is VCS root with invalid URL.
		// In real world, VCS might always fail on some commits because of data corruption.
		GitSettings settings = GitSettings.defaults().withFailFast(false);
		VcsProject vcsProject = new VcsProject(
				new GitVcsRoot(".junit4-clone", "https://github.com/junit-team/junit4", settings),
				new GitVcsRoot(".invalid-project", "invalid.url", settings)
		);

		for (VcsRoot vcsRoot : vcsProject.vcsRoots()) {
			if (new File(vcsRoot.repoFolder()).exists()) continue;

			System.out.println("Cloning " + vcsRoot.repoUrl());
			CloneResult cloneResult = vcsRoot.cloneToLocal();

			if (!cloneResult.isSuccessful()) {
				for (Exception e : cloneResult.exceptions()) {
					System.out.println("Clone error: '" + e.getMessage() + "'");
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
