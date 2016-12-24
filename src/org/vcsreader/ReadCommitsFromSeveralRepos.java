package org.vcsreader;

import org.vcsreader.lang.TimeRange;
import org.vcsreader.vcs.git.GitVcsRoot;

import java.io.File;

public class ReadCommitsFromSeveralRepos {
	public static void main(String[] args) {
		VcsProject vcsProject = new VcsProject(
				new GitVcsRoot(".junit4-clone", "https://github.com/junit-team/junit4"),
				new GitVcsRoot(".junit5-clone", "https://github.com/junit-team/junit5")
		);

		for (VcsRoot vcsRoot : vcsProject.vcsRoots()) {
			if (!new File(vcsRoot.repoFolder()).exists()) {
				System.out.println("Cloning " + vcsRoot.repoUrl());
				vcsRoot.cloneIt();
			}
		}

		LogResult logResult = vcsProject.log(TimeRange.beforeNow());

		for (VcsCommit commit : logResult.commits()) {
			System.out.println(commit.getDateTime() + " " + commit.getAuthor());
		}
	}
}
