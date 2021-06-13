lazy val akkaVersion = "2.6.15"
lazy val akkaHttpVersion = "10.2.4"
lazy val logbackVersion = "1.2.3"
lazy val scalatestVersion = "3.2.9"

lazy val route = (project in file(".")).settings(
	inThisBuild(List(
		organization := "net.imadz",
		scalaVersion := "2.12.13"
		)),
        name := "quick-start-akka-http",
	libraryDependencies ++= Seq(
		"com.typesafe.akka"    %%  "akka-http"                        % akkaHttpVersion,
		"com.typesafe.akka"    %%  "akka-http-spray-json"             % akkaHttpVersion,
		"com.typesafe.akka"    %%  "akka-actor-typed"                 % akkaVersion,
		"com.typesafe.akka"    %%  "akka-stream"                      % akkaVersion,
		"ch.qos.logback"        %  "logback-classic"                  % logbackVersion,


		"com.typesafe.akka"   %%  "akka-http-testkit"                 % akkaHttpVersion    % Test,
		"com.typesafe.akka"   %%  "akka-actor-testkit-typed"          % akkaVersion        % Test,
		"org.scalatest"       %%  "scalatest"                         % scalatestVersion

		)
	)
