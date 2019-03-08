import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.2"

project {

    vcsRoot(HttpsGithubComBes0nTeamcityCourseCardsGit)

    buildType(id01FastTests)
    buildType(id02Chrome)
    buildType(id03Firefox)
    buildType(id04DeployToStaging)

    template(Template_1)
}

object id01FastTests : BuildType({
    templates(Template_1)
    id("01FastTests")
    name = "01. Fast Tests"

    params {
        param("Browser", "PhantomJS")
    }
})

object id02Chrome : BuildType({
    templates(Template_1)
    id("02Chrome")
    name = "02. Chrome"

    params {
        param("Browser", "Chrome")
    }

    dependencies {
        snapshot(id01FastTests) {
        }
    }
})

object id03Firefox : BuildType({
    templates(Template_1)
    id("03Firefox")
    name = "03. Firefox"

    params {
        param("Browser", "Firefox")
    }

    dependencies {
        snapshot(id01FastTests) {
        }
    }
})

object id04DeployToStaging : BuildType({
    id("04DeployToStaging")
    name = "04. Deploy To Staging"

    vcs {
        root(HttpsGithubComBes0nTeamcityCourseCardsGit)
    }

    triggers {
        vcs {
            branchFilter = ""
        }
    }

    dependencies {
        snapshot(id02Chrome) {
        }
        snapshot(id03Firefox) {
        }
    }
})

object Template_1 : Template({
    id("Template")
    name = "Template"

    params {
        param("Browser", "")
    }

    vcs {
        root(HttpsGithubComBes0nTeamcityCourseCardsGit)
    }

    steps {
        script {
            name = "npm_install"
            id = "RUNNER_3"
            scriptContent = "npm install"
        }
        script {
            name = "npm_test"
            id = "RUNNER_4"
            scriptContent = "npm test -- --single-run --browsers %Browser% --colors false --reporters teamcity"
        }
    }
})

object HttpsGithubComBes0nTeamcityCourseCardsGit : GitVcsRoot({
    name = "https://github.com/Bes0n/teamcity-course-cards.git"
    url = "https://github.com/Bes0n/teamcity-course-cards.git"
    authMethod = password {
        userName = "Bes0n"
        password = "credentialsJSON:33ed9bea-0058-49eb-bf52-81be35e0eedb"
    }
})
