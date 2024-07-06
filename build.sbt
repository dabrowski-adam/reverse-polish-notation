ThisBuild / organization := "com.adamdabrowski"
ThisBuild / scalaVersion := "3.4.2"

// Scalafix has to be run twice because `OrganizeImports.expandRelative` may introduce unused imports.
// Scalafmt also has the potential to be non-idempotent.
addCommandAlias("lint", "scalafixAll;scalafixAll;scalafmtAll;scalafmtAll")

ThisBuild / semanticdbEnabled := true
ThisBuild / scalacOptions    ++= Seq(
  "-indent",
  "-rewrite",
  "-deprecation",
  "-Werror",
  "-Wvalue-discard",
  "-Wunused:all",
  "-Ykind-projector:underscores"
)


lazy val root = project.in(file("."))
  .settings(
    name                 := "interview",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client4" %% "core"            % "4.0.0-M16",
      "org.typelevel"                 %% "cats-core"       % "2.12.0",
      "org.typelevel"                 %% "alleycats-core"  % "2.12.0",
      "org.scalatest"                 %% "scalatest"       % "3.2.18"   % Test,
      "org.scalacheck"                %% "scalacheck"      % "1.17.1"   % "test",
      "org.scalatestplus"             %% "scalacheck-1-18" % "3.2.19.0" % "test",
    ),
  )
