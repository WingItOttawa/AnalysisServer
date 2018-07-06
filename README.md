# <!-- omit in toc --> Analysis Server - Documentation

- [Project Setup](#project-setup)
- [Running in IntelliJ](#running-in-intellij)
    - [Running with Spring Boot](#running-with-spring-boot)
    - [Running with Maven](#running-with-maven)
- [Testing the Server](#testing-the-server)


# Project Setup

Note: these instructions are for macOS.
1. Navigate to your WingIt project folder
2. Clone the repository from here: https://github.com/WingItOttawa/AnalysisServer.git
3. (Optional but recommended) Set up Run Configurations in your favourite IDE (see [Running in IntelliJ](#running-in-intellij) for an example)

# Running in IntelliJ

There are two methods that can be used to run this application. We can rely on [Spring Boot](#running-with-spring-boot) to do the job and only use Maven for dependency management, or we can boot using [Maven](#running-with-maven). Either option works, but booting with Spring Boot is much faster and cleaner in the logs. Prefer this option over building and running with Maven.

In both cases, once Run Configurations are set up, you can click the green `Run` triangle in the top right of IntelliJ to run the server.

## Running with Spring Boot

1. Click on `Edit Configurations` in the top right ![Edit Configurations](./readme-images/editRunConfigurations.png)
2. Add a new Spring Boot configuration in the top left of the new window by clicking the `+` sign ![Add New Spring Boot Configuration](./readme-images/springBootAddNewConfiguration.png)
3. Fill in the `Name` and the `Main class` fields as shown ![Spring Boot Configuration](./readme-images/springBootConfiguration.png)
4. Go to the `Environment variables` field, and add a new variable called `SERVICE_ACCOUNT_KEY`. Copy the contents of `ServiceAccountKey.json` into the value field (obtain this file from another software developer on this project)

## Running with Maven

1. Click on `Edit Configurations` in the top right ![Edit Configurations](./readme-images/editRunConfigurations.png)
2. Add a new Maven configuration in the top left of the new window by clicking the `+` sign ![Add New Maven Configuration](./readme-images/mavenAddNewConfiguration.png)
3. Fill in the `Name` and the `Command line` fields as shown, then click the `+` sign under the `Before launch:` section at the bottom ![Maven Configuration for Execution](./readme-images/mavenConfiguration.png)
4. Select `Run Maven Goal` in the new window ![New Maven Goal](./readme-images/mavenAddNewConfiguration2.png)
5. Fill in the `Command line` field as shown in this image ![Maven Configuration for Packaging](./readme-images/mavenSelectMavenGoal.png)
6. On the configuration editing window, click on the `Runner` tab, uncheck `Use project settings`, then go to the `Environment variables` field and add a new variable called `SERVICE_ACCOUNT_KEY`. Copy the contents of `ServiceAccountKey.json` into the value field (obtain this file from another software developer on this project)

# Testing the Server

To test the server once it's running, navigate to http://localhost:8080/ping in your browser. You should see the response "Pong".