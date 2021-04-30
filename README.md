# Corebot: A Slack bot for Rundeck and Jenkins [![CircleCI](https://circleci.com/gh/outofcoffee/corebot.svg?style=svg)](https://circleci.com/gh/outofcoffee/corebot)

Trigger your [Rundeck](http://rundeck.org) or [Jenkins](https://jenkins.io) jobs from [Slack](https://slack.com).

_Example:_

    @corebot deploy user-service 1.0 to staging
    > corebot:
    > OK, I'm deploying user-service version 1.0 to staging.
    > Status of job is: running
    > Details: http://rundeck/jobs/abc/123

> Why would you want this? Check out [ChatOps](http://blogs.atlassian.com/2016/01/what-is-chatops-adoption-guide/).

## What can it do?

##### Trigger your deployment jobs

<img alt="Deploy" src="https://github.com/outofcoffee/corebot/raw/master/docs/images/deploy.png" width="467">

##### Trigger your other custom jobs

<img alt="Restart" src="https://github.com/outofcoffee/corebot/raw/master/docs/images/restart.png" width="472">

##### Lock things to prevent accidental deployment

<img alt="Lock deployment failure" src="https://github.com/outofcoffee/corebot/raw/master/docs/images/lock_deploy_fail.png" width="371">

##### Unlock things you've locked

<img alt="Unlock job" src="https://github.com/outofcoffee/corebot/raw/master/docs/images/unlock.png" width="336">

##### Get help

<img alt="Help" src="https://github.com/outofcoffee/corebot/raw/master/docs/images/unknown.png" width="389">

## Instructions

* As a Slack admin, create a Slack bot user and obtain its access token - [instructions](https://my.slack.com/services/new/bot)
* As a Rundeck admin, generate a Rundeck API token - [instructions](http://rundeck.org/2.6.9/api/index.html#token-authentication)
* Set environment variables
* Run!

## Getting started

The quickest way to get up and running is to use our free cloud-hosted version at [https://www.remotebot.io/bot](https://www.remotebot.io/bot)

## Docker

If you'd like to run Corebot yourself as a Docker container, you can do the following:

    docker run -d \
            --env SLACK_AUTH_TOKEN="CHANGEME" \
            --env SLACK_CHANNELS="corebot" \
            --env RUNDECK_API_TOKEN="CHANGEME" \
            --env RUNDECK_BASE_URL="http://rundeck:4440" \
            -v /path/to/actions.yml:/opt/corebot/actions.yml \
            outofcoffee/corebot

> Note: the container does not require any inbound ports to be exposed.

> Note: See the _Environment variables_ section for the available configuration settings.

## Creating a Slack app

As a Slack admin, create a 'Classic App' in Slack: https://api.slack.com/apps?new_classic_app=1

Add a bot user by expanding 'Add features and functionality', and clicking on Bots:

    https://api.slack.com/apps/<your app ID>/app-home

Add the required scopes in the 'OAuth & Permissions' section:

    https://api.slack.com/apps/<your app ID>/oauth

The scopes are:

    chat:write:bot

> Don't forget to save changes after adding scopes.

Install your app to your workspace. This will generate the token you need. You'll want to copy the 'Bot User OAuth Access Token'. It should look like this:

    xoxp-123456789012-123456789012-abcdef1234567890abcdef1234567890

## Drivers

Corebot's capabilities are determined by its driver. For example, a driver might allow you to interact with a CI/CD system.

### Jenkins driver

A modern version (1.7+) of Jenkins is required - version 2.x or higher is preferred.

Here is the official Jenkins Docker image:

    docker run -it \
        -p 8080:8080 \
        jenkins

Example usage:

    @corebot deploy services v2 to staging
    > corebot:
    > OK, I'm deploying user-service version 1.0 to staging.
    > Status of job is: running
    > Details: http://rundeck/jobs/abc/123

### Rundeck driver

Any Rundeck instance can be used as long as it supports API v14 or higher.

As an example, here is an unofficial Rundeck Docker image: https://hub.docker.com/r/jordan/rundeck/

    docker run -it \
        -p 4440:4440 \
        -e SERVER_URL=http://localhost:4440 \
        jordan/rundeck

Example usage:

    @corebot deploy services v2 to staging
    > corebot:
    > OK, I'm deploying user-service version 1.0 to staging.
    > Status of job is: running
    > Details: http://rundeck/jobs/abc/123

### Items driver

The items driver acts like a lending library. It allows users to borrow, return, or evict users from items in the library.

An example use case is a group of people reserving a server/environment.

Example usage:

    @corebot list
    > corebot:
    > dev1 - no one is borrowing
    > dev2 - in use by @alice, @bob
    > staging - no one is borrowing
    
    @corebot borrow dev1 for bugfixing
    > corebot:
    > OK, you've borrowed dev1
    
    @corebot return dev1
    > corebot:
    > OK, you've returned dev1

## Configuration

### Environment variables

Configure the bot using the following environment variables.

#### Common variables

    SLACK_AUTH_TOKEN="CHANGEME"
    SLACK_CHANNELS="corebot"
    ACTION_CONFIG_FILE="/path/to/actions.yaml"
    
> Note: `SLACK_CHANNELS` is a comma-separated list of channel names, such as `"channelA,channelB"`. You can also use regular expressions to match channel names, such as `"channel.*"`.

> Note: the default path for `ACTION_CONFIG_FILE` is `/opt/corebot/actions.yml`. When using corebot within a Docker container, it is typical to add your configuration file at this location, or bind-mount a file to this path.

#### Variables for Rundeck

    RUNDECK_API_TOKEN="CHANGEME"
    RUNDECK_BASE_URL="http://rundeck:4440"

> Note: ensure that the API token you generate in Rundeck has the necessary permissions to trigger builds. For more information, consult the Rundeck ACL documentation.

#### Variables for Jenkins

    JENKINS_BASE_URL="http://localhost:8080"
    JENKINS_USERNAME="CHANGEME"
    JENKINS_PASSWORD="CHANGEME"
    JENKINS_API_TOKEN="CHANGEME"
    
> Note: typically you will specify the username and password for accessing a Jenkins instance. The token approach is rarely used and can be omitted.

#### Advanced variables

Advanced variables to tune behaviour and performance:

    CACHE_EXPIRY="60"

The cache expiry controls the period of time, in seconds, corebot holds the action configuration in memory after reading it from file.

    EXECUTION_STATUS_TIMEOUT="120"
    EXECUTION_STATUS_POLL="10"

The execution status timeout controls the period of time, in seconds, corebot will poll a running job for status updates, after which it gives up. You can also control the polling interval, also in seconds.

    SLACK_REPLY_IN_THREAD="true"
    
Posts replies from the bot to a thread starting from the trigger message. Default: `false`.
    
    SLACK_ALLOW_THREADED_TRIGGERS="true"
    
Allows child thread messages to be trigger messages. Implies `SLACK_REPLY_IN_THREAD="true"`. Default: `false`.

    CHAT_GENERATOR_FILE="/path/to/file.yml"
    
The path to an external YAML file containing custom chat lines. See the default file, `default-chat.yml`, for examples.

    SYSTEM_CONFIG_FILE="/path/to/file.yml"

The path to an external YAML file containing system configuration. See the sample file, `system.yml`, for examples.

### Operations and actions

Corebot has both built-in operations and external actions. Examples of built in operations are the lock/unlock actions. External actions are triggers for your Rundeck/Jenkins jobs, configured using a configuration file, typically called `actions.yml`.

#### Action configuration file

> Note: the configuration file path is specified with the `ACTION_CONFIG_FILE` environment variable.

_Example file:_
```
version: '1'
actions:
  services:
    jobId: 9374f1c8-7b3f-4145-8556-6b55551fb60f
    template: deploy services {version} to {environment}
```

File structure:

* All files must specify version ‘1’
* All actions must sit under a top level `actions` block
* Each action must have a name (it’s `services` in this example)
* Each action must have either:
  * a Rundeck job ID (obtain this from Rundeck), or
  * a Jenkins job name
* Each action must have a template - more details below
* Each action may optionally specify a list of tags
* Each action may optionally specify option configuration, such as:
  * static values
  * value transformers
  * whether the option can be locked
  
> Tip: Check out the `examples/config` directory for sample configuration files.

#### Action driver

Actions should specify a driver. The available drivers are:

* jenkins - use this to allow users to trigger Jenkins jobs
* rundeck - use this to allow users to trigger Rundeck jobs
* items - use this to allow users to borrow/return a set of items (e.g. books, servers etc.)

> Note: if none is specified, the driver is assumed to be `rundeck`.

_Example:_
```
version: '1'
actions:
  services:
    driver: jenkins
    jobId: my-jenkins-job
    template: deploy web {version} to {environment}
```

**Important:** Ensure that you set the environment variables corresponding to the driver(s) you use, such as the base URL, API key/username etc.

#### Action template

An action template provides the syntax for invoking the command. 

_Example:_

	deploy services

A template also allows you to specify job options as placeholders, such as:

	deploy services {version} to {environment}

In this example both _version_ and _environment_ are captured from the command, such as:

	@corebot deploy services 1.0 to UAT

This will result in the action being fired, passing the following options:

- version=1.0
- environment=UAT

#### Static option values

You might want to pass an option value to a job every time, and not require the user to provide it. You can accomplish this using the `value` property of an option within the `options` action block:

```
version: '1'
actions:
  services:
    jobId: 9374f1c8-7b3f-4145-8556-6b55551fb60f
    template: deploy services {version} to {environment}
    options:
      myOption:
        value: someValue
      myOtherOption:
        value: someOtherValue
```

This will result in the action being fired, passing the following options:

- version=1.0
- environment=UAT
- _myOption=someValue_
- _myOtherOption=someOtherValue_

#### Transforming options

You might want to transform an option value provided by a user before it is passed to a job. You can accomplish this using the `transformers` section of an option within the `options` action block:

```
version: '1'
actions:
  services:
    jobId: 9374f1c8-7b3f-4145-8556-6b55551fb60f
    template: deploy services {version} to {environment}
    options:
      version:
        transformers:
          - LOWERCASE
      environment:
        transformers:
          - UPPERCASE
```

If the user typed this command:

    @corebot deploy services V1.0 to uat

This will result in the action being fired, passing the following options:

- version=v1.0 (note: lowercased)
- environment=UAT (note: uppercased)

#### Locking options

You might want to lock an option value, so that it cannot be passed to an action.

> Example: If you have an option to specify the environment for a deployment, you might wish to lock deployments to the environment named 'production'.

You can accomplish this using the `lockable` property of an option within the `options` action block:

```
version: '1'
actions:
  services:
    jobId: 9374f1c8-7b3f-4145-8556-6b55551fb60f
    template: deploy services {version} to {environment}
    options:
      environment:
        lockable: true
```

If the user typed this command:

    @corebot lock environment prod

This will result in a lock being placed on the 'environment' option, with the value 'prod'.

With the lock applied, this will fail:

    @corebot deploy services 1.0 to prod
    
...but this will still succeed:

    @corebot deploy services 1.0 to uat
    
You can of course unlock the option with:

    @corebot unlock environment prod
    
> Note: It's strongly advisable to apply a _transformer_ to lockable options, to ensure the value 'prod' is considered equivalent to 'PROD'.

#### Tags and multiple job actions

Sometimes actions can be run on multiple jobs. To do this, set the `tags` block:

```
version: '1'
actions:
  deploy-services:
    jobId: 9374f1c8-7b3f-4145-8556-6b55551fb60f
    template: deploy services {version} to {environment}
    tags:
	  - services
	    
  restart-services:
    jobId: e9d12eec-abff-4780-89cd-56a48b8c67be
    template: restart services in {environment}
    tags:
	  - services
```

Here, two actions are defined: `deploy-services` and `restart-services`, both tagged with `services`. This means you can do things like:

	@corebot lock services

…and both actions will be locked.

> Tip: There is a special tag set on all actions, named 'all'. This means you can do things like `@corebot lock all`.

#### Customised output

Sometimes the bots reaction is enough to see the status. To do this, set the `showJobOutcome` option to `false`. Default is `true`.
 
Sometimes the output of the job is needed to be given back by the bot. To do this, set the `showJobOutput` option to `true`. Default is `false`.

```
version: '1'
actions:
  deploy-services:
    jobId: 9374f1c8-7b3f-4145-8556-6b55551fb60f
    template: deploy services {version} to {environment}
    showJobOutput: true
    showJobOutcome: false    
```

#### Security

You can choose which users are authorised to perform actions, using the `security` block:

```
security:
  users:
    # alice uses the built-in 'admin' role
    alice:
      roles:
        - admin
```
> *Important:* if you do not specify a security configuration explicitly, the default will be used. The default settings permit all users to perform all actions.

There is a built in role, named `admin`, which you can assign to users. You can also define your own roles, listing the permissions granted to users with that role.

You can assign roles on a per-username basis or, if you wish to assign certain roles to all users, use the special `"*"` key, as shown in the example below:

```
security:
  roles:
    # a role that can only trigger jobs
    deployer:
      permissions:
        - trigger

  users:
    # alice uses the built-in 'admin' role
    alice:
      roles:
        - admin

    # all users can trigger jobs
    "*":
      roles:
        - deployer
```

By default, role definitions apply to all actions. If you wish to restrict the permissions granted by a role to certain actions only, add a tag to the action and also to the corresponding `tags` array in the role:
 
```
security:
  roles:
    # a role that can only trigger jobs
    deployer:
      permissions:
        - trigger
    
    # this role only permits triggering of actions tagged with 'services'
      tags:
        - services
```

### Built-in actions

There are a number of built in actions, such as:

* `@corebot help` - show usage information.
* `@corebot lock {action or tag name}` - lock action(s) to prevent them being triggered accidentally.
* `@corebot unlock {action or tag name}` - unlock locked action(s).
* `@corebot status {action or tag name}` - show status of action(s).
* `@corebot lock {option name} {option value}` - lock an option with a given value.
* `@corebot unlock {option name} {option value}` - unlock an option with a given value.
* `@corebot status {option name} {option value}` - show status of an option with a given value.
* `@corebot enable {action or tag name}` - set the Rundeck execution status for a job - *Note:* this requires the Rundeck ACL to permit the API user to set the execution status of a job.
* `@corebot disable {action or tag name}` - set the Rundeck execution status for a job - *Note:* this requires the Rundeck ACL to permit the API user to set the execution status of a job.

### System and shared/default configuration

You can set some default operations for all actions using the system configuration file.

The path to this file is specified using the `SYSTEM_CONFIG_FILE` environment variable.

An example configuration follows:

    # System configuration
    ---
    version: '1'
    
    system:
      defaults:
        driver: jenkins
        showJobOutput: false
        showJobOutcome: true
        runAsTriggerUser: false
        
        options:
          myVar:
            value: someDefaultValue
    
      requestHeaders:
        Cookie: "foo=bar"

The example above sets a number of default values, which can be overridden by your individual action configurations.
 
This example also specifies a map of HTTP headers to set on each API request to the external build/deployment systems. These headers will be sent to Jenkins and Rundeck when making any requests.

## Plugins

Corebot supports plugins, loaded dynamically at startup.

### Plugin categories

The following plugin categories are supported:

* front-ends (e.g. Slack, WebSocket)
* back-ends (e.g. Rundeck, Jenkins, Items)
* stores (e.g. MySQL, Redis)

### Plugin distribution

Plugins are packaged and distributed as WAR files and include all required dependencies.

### Loading plugins

You can load plugins by using the `generic-bot` distribution.

> See `bots/generic/README.md` for information.
> See the `examples/plugins` directory for plugin configuration examples.

## Building

If you wish to build and run locally, you can run:

    ./gradlew installDist
    docker-compose build

Once built, set the environment variables in `docker-compose.yml`. See the _Environment variables_ section.

Then run with:

    docker-compose up

If you change anything, don't forget to rebuild before running again.

#### Publishing

Dependencies can be published to the project Maven repository.

> Note: Publishing to the repository requires appropriate AWS keys to be set in `gradle.properties`.

## Embedding Corebot in your applications

The Corebot libraries are published to our Maven repository. You can include them as dependencies in your project.

### Maven dependencies

To use the dependencies in a project, add the repository:

```
repositories {
    maven {
        url 'https://s3-eu-west-1.amazonaws.com/gatehillsoftware-maven/snapshots/'
    }
    
    // jitpack required for Slack dependency
    maven { url "https://jitpack.io" }
}
```

...then add a dependency:

```
compile "com.gatehill.corebot:core:0.7.0"
```

> Note: update the version with the latest stable [release](https://github.com/outofcoffee/corebot/tags).

# Recent changes and Roadmap
  
For recent changes see the [Changelog](CHANGELOG.md), or view the [Roadmap](docs/roadmap.md).

## Contributing

* Pull requests are welcome.
* PRs should target the `develop` branch.
* Please run `ktlint` against the code first ;-)

## Author

Pete Cornish (outofcoffee@gmail.com)
