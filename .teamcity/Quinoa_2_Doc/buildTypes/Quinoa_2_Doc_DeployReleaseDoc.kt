package Quinoa_2_Doc.buildTypes

import Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Release
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.FinishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.FinishBuildTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.finishBuildTrigger

object Quinoa_2_Doc_DeployReleaseDoc : BuildType({
    uuid = "7674b556-609f-4375-be3d-45ea9b1f380c"
    extId = "Quinoa_2_Doc_DeployReleaseDoc"
    name = "Deploy release doc"
    description = "Deploy documentation, code coverage, and static analysis reports from release build"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster)
    }

    steps {
        script {
            name = "Push documentation, code coverage, and static analysis reports"
            scriptContent = """
                git rm -rf .
                mv html/* html/.nojekyll .
                git add .
                git commit -m "Documentation for commit %build.vcs.number.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster%"
                git push
            """.trimIndent()
        }
    }

    triggers {
        finishBuildTrigger {
            buildTypeExtId = Quinoa_2_Doc_Build_Release.extId
            successfulOnly = true
        }
    }

    dependencies {
        dependency(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Release) {
            snapshot {
                runOnSameAgent = true
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = "html => html"
            }
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux")
        contains("teamcity.agent.name", "ccscs")
    }
})
