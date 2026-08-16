When contributing to this repository, please first discuss the change you wish to make with the owner. Either via the issue tracker or on the discussions page. Especially if it is regarding new features. If you just want to make a minor correction, like fix a typo or similar, feel free to just make a pull request directly.

**Guidelines:**

* Support for all KMP platforms. Graceful degradation is possible when a platform does not support a feature.
* Minimize external dependencies whenever possible. A lightweight implementation is preferred over bringing in an external dependency.
* Use platform-specific APIs whenever possible e.g. iOS, Android and web have specific APIs that are preferred over generic JVM solutions.


**The following contributions are welcome:**

* Bugfixes for new or existing bugs. Please also report new bugs in the issue tracker even if you
  also provide a fix. It makes it easier to keep track of what has been fixed and when.
* Improvements to the documentation. Particularly if the documentation is unclear. Please don't
  make any larger changes to the documentation without discussing them with the maintainer first.
* Adaptations, installation or packaging features targeting specific operating systems.
* Using AI as a coding assistant is fine as long as the code is of good quality and the scope of
  change is small, but do not generate large chunks of code or text for issues and PRs. Generated code is not only hard to maintain, but also poses licensing issues.

**Please do not:**

* Make a pull request that restructures or reformats existing code. If you think some part of the
  code could be improved, please make an issue thread or start a discussion. The same applies to
  any text document in the repository.
* Make pull requests with AI generated code. This is not a project suitable for vibe coding.
  Outright slop is not welcome.


## Building from Source

Gadulka is a Kotlin Multiplatform library project based on Gradle. You can [Setup the environment](https://kotlinlang.org/docs/multiplatform/quickstart.html#set-up-the-environment) guide on how to configure your system for building KMP projects.
