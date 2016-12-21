package org.vcsreader;

import org.vcsreader.lang.TimeRange;
import org.vcsreader.vcs.git.GitVcsRoot;

import java.io.File;

import static org.vcsreader.VcsChange.Type.*;

public class ReadCommitsAndChanges {
	public static void main(String[] args) {
		GitVcsRoot gitVcsRoot = new GitVcsRoot(".hamcrest-clone", "https://github.com/hamcrest/JavaHamcrest");
		VcsProject vcsProject = new VcsProject(gitVcsRoot);

		if (!new File(".hamcrest-clone").exists()) {
			System.out.println("Cloning...");
			vcsProject.cloneToLocal();
		}

		LogResult logResult = vcsProject.log(TimeRange.beforeNow());

		for (VcsCommit commit : logResult.commits()) {
			System.out.printf("%s %s\n", commit.getDateTime(), commit.getAuthor());
			for (VcsChange change : commit.getChanges()) {
				System.out.printf("%s %s\n", change.getType(), filePath(change));
			}
			System.out.println();
		}
	}

	private static String filePath(VcsChange change) {
		VcsChange.Type it = change.getType();
		if (it == Added) return change.getFilePath();
		else if (it == Modified) return change.getFilePath();
		else if (it == Moved) return change.getFilePathBefore() + " (->) " + change.getFilePath();
		else if (it == Deleted) return change.getFilePathBefore();
		else throw new IllegalStateException();
	}
}
