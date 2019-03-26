# Change Log

All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added
- ...

## [1.3.2] - 2019-03-26
### Changed
- Items backend will use the claim owner's full name if the current session can't resolve it.

### Removed
- Removes Slack backend. In future, use HTTP backend and [Slack Gateway](https://github.com/outofcoffee/slack-gateway) instead.

## [1.3.1] - 2019-03-25
### Changed
- Falls back to user ID if username cannot be found.

## [1.3.0] - 2018-10-25
### Added
- Adds more template aliases for items backend.
- Enables channel names to be specified using regular expressions.
- Allows job status check polling interval to be configured.
- Supports Jenkins job names containing slashes, such as multibranch builds.

### Changed
- Fallback to username, then user ID if Slack user information is incomplete.

### Fixed
- Bumps Slack API version to handle channel rename event correctly (fixes #54).

## [1.2.0] - 2018-02-04
### Added
- Adds HTTP bot.
- Adds Slack backend.
- Adds a RESTful API store.
- Adds a store implementation that mirrors writes to the backing store to another, separate, store implementation.
- Allows chat service implementations to indicate support for user termination. 
- Adds the ability to start with an empty action configuration.
- Allows version placeholder to be used in plugin files.

### Changed
- Moves plugins to single directory.
- Changes Maven artifact names for plugins.

## [1.1.2] - 2017-11-05
### Fixed
- Fixes action invocation with empty options map.
 
### Added
- Improves specification coverage.

## [1.1.1] - 2017-10-20
### Changed
- Publishes latest Maven artifacts.

## [1.1.0] - 2017-10-20
### Added
- Adds support for plugins loaded dynamically at startup.
- Adds new 'generic-bot' distribution, configurable using the plugins YAML format.

### Fixed
- Fixes a bug in the MySQL store where updates to existing items were discarded.

## [1.0.0] - 2017-10-07
### Added
- Added ability to trigger parameterless jobs in Jenkins (thanks DrColza).
- Adds system configuration for customisation of request and action defaults, and default action options.
- Adds option status action.

### Changed
- Upgrades Gradle, Kotlin and Jackson versions.
- Improves API path composition, including Jenkins and Rundeck endpoints other than at the root of a domain.
- Improves error status logging.
- Improves option handling.

## [0.9.2] - 2017-08-26
### Changed
- Don't post channel join message on reconnection.

## [0.9.1] - 2017-07-20
### Fixed
- Adds a work-around for a message de-duplication bug in the Slack library.

## [0.9.0] - 2017-07-08
### Added
- Adds a bot to allow simple borrow/return of items.
- Adds a new 'items' backend.
- Makes join message conditional.
- Adds MySQL store.
- Now supports multiple templates.

### Changed
- Internal structural improvements to support multiple back-end or front-end implementations.
- Improved specification coverage.
- Externalised templates to allow messages to be changed.

## [0.8.1] - 2017-06-11
### Added
- Trims message content prior to parsing.

## [0.8.0] - 2017-06-11
### Added
- Adds support for external data stores. This includes things like action locks and option locks, but can support other types in future.
- Adds in-memory data store implementation, which is the default.
- Adds Redis data store implementation.
- Adds the ability to trigger the bot from a thread, as well as reply to triggers within a thread (thanks Anojan Sivarajah).
- Externalises chat lines to a configuration file.

### Fixed
- Skip messages with null content.

## [0.7.2] - 2017-05-23
### Fixed
- Fixes non-trigger events being sunk in action drivers.

## [0.7.1] - 2017-05-07
### Changed
- Only run as trigger user if explicitly set in action configuration.

## [0.7.0] - 2017-05-06
### Added
- Improves Jenkins error handling when obtaining CSRF token.
- Adds ability to run a Rundeck job as the initiating Slack user (thanks Anojan Sivarajah).
- Adds ability to show Rundeck job output in chat (thanks Anojan Sivarajah).
- Adds ability to handle spaces in arguments using quotes; Rundeck driver only (thanks Anojan Sivarajah).
- Adds CircleCI configuration.
- Adds a number of extension points.
- Adds Maven publishing configuration.

### Changed
- Bumps Gradle version. Bumps Kotlin version.
- Internal structural improvements.

## [0.6.2] - 2016-12-03
### Added
- Adds changelog (this file!)

### Fixed
- Fixes template resolution when more than one candidate remains but one is already satisfied.
- Removes ‘roles’ from being mandatory in custom security configuration.

## [0.6.1] - 2016-11-20
### Added
- Adds grouped action start/complete messages.
- Sends join message on connection.
- Permit commands ending with colon after bot persona ID.
- Adds timeout to queued item check.

## [0.6.0] - 2016-11-16
### Added
- Adds lockable option values.
- Sorts action templates by name in help.
- Makes user-specified options override static values.

### Changed
- Stops printing usage on receipt of unknown command.

## [0.5.0] - 2016-11-14
### Added
- Adds Jenkins driver. Includes support for triggering jobs, queued items and status polling.
- Moves common driver functionality into shared modules.
- Factors out Rundeck support into separate driver module.
- Supports Jenkins instances with CSRF enabled or disabled.
- Makes trigger template matching case insensitive.

## [0.4.1] - 2016-11-06
### Added
- Adds option transformers.
- Allows roles to be restricted by tag.

## [0.4.0] - 2016-11-03
### Added
- Enables listening to multiple channels.
- Adds user and role authorisation framework.
- Adds 'help' action.
- Caches configuration for improved performance.

## [0.3.0] - 2016-10-30
### Added
- Adds support for multiple action commands, using tags.
- Polls for job status and reports back to channel.

## [0.2.0] - 2016-10-28
### Added
- Adds basic lock and unlock.
- Adds enable/disable (requires correct ACL).
- Adds status action.

## [0.1.0] - 2016-10-27
### Added
- Initial release.
- Rundeck support.
- Slack bot integration.
