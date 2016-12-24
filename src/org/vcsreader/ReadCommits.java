package org.vcsreader;

import org.vcsreader.lang.TimeRange;
import org.vcsreader.vcs.git.GitVcsRoot;

import java.io.File;

public class ReadCommits {
	public static void main(String[] args) {
		VcsProject vcsProject = new VcsProject(new GitVcsRoot(".hamcrest-clone", "https://github.com/hamcrest/JavaHamcrest"));

		if (!new File(".hamcrest-clone").exists()) {
			System.out.println("Cloning...");
			vcsProject.cloneIt();
		}

		LogResult logResult = vcsProject.log(TimeRange.beforeNow());

		for (VcsCommit commit : logResult.commits()) {
			System.out.println(commit.getDateTime() + " " + commit.getAuthor());
		}
	}
}
