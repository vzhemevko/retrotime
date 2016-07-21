# Retrotime - improve your efficiency

## what is Retrotime?
>It is an angular single-page application with RESTful web services. The web services on back-end/server side were developed using Spring, Spring Data, JPA/Hibernate and a few other tools. 

> An objective of the application is to give a good tool for conducting efficient, quick and easy retrospective. Which is a part of Scrum methodology. 
see [Wikipedia](https://en.wikipedia.org/wiki/Scrum_(software_development)

> A few more words about technical details. Besides above-mentioned technologies, in the project was used Bootstrap 3, Gradle, PostgreSQL, Spring Security and it leverages WebSocket technology for instant messaging (see below). I believe this project may serve as a good example what can be built using this technology stack.

<p align="center">
  <img src="https://s-media-cache-ak0.pinimg.com/564x/1a/b2/94/1ab2943bf19068cf6f708e6349551c28.jpg"/>
</p>

## usage
1. Clone or download the project.
2. Import the project into your IDE (Eclipse or Intellij) as a gradle project.
3. Install PostgreSQL and create "retrotime" database. Make sure you have a correct password in "persistence.properties"
4. If you want you can install web-container server in your IDE (tomcat/jboss) or you can specify a system variable TOMCAT_HOME and use a custom gradle task "deploy" in build.gradle to deploy the application.
5. After you deploy the application and run the server you can go to "server_ip:port/retrotime" in your browser, most probably it's going to be "localhost:8080/retrotime".  

----
## demo
>Scrum master signs in and creates a new scrum team and a new retrospective.

<p align="center">
  <img src="https://s-media-cache-ak0.pinimg.com/originals/6c/2f/e4/6c2fe45b5846e0471fb7e716bb738158.gif"/>
</p>

>Two team members sign in and exchange their thoughts about past sprint.

<p align="center">
  <img src="https://s-media-cache-ak0.pinimg.com/originals/6b/7a/45/6b7a45b534a0d621b6f2747f5602647e.gif"/>
</p>
