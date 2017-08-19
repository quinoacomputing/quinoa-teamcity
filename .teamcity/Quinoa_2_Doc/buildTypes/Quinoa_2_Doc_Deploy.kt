package Quinoa_2_Doc.buildTypes

import Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Release
import Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Debug
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.FinishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.FinishBuildTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.finishBuildTrigger

object Quinoa_2_Doc_Deploy : BuildType({
    uuid = "7674b556-609f-4375-be3d-45ea9b1f380c"
    extId = "Quinoa_2_Doc_Deploy"
    name = "Deploy"
    description = "Deploy documentation, code coverage, and static analysis reports"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster)
    }

    steps {
        script {
            name = "Combine documentation, code coverage and static analysis reports"
            id = "RUNNER_31"
            scriptContent = """
                rm -rf Release Debug
                mv newRelease Release
                mv newDebug Debug
            """.trimIndent()
        }
        script {
            name = "Push documentation, code coverage, and static analysis reports"
            id = "RUNNER_32"
            scriptContent = """
                git rm .
                mv Release/html/* Release/html/.nojekyll .
                rm -rf Release/html Debug/html
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
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
        finishBuildTrigger {
            buildTypeExtId = Quinoa_2_Doc_Build_Debug.extId
            successfulOnly = true
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
    }

    dependencies {
        dependency(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Release) {
            snapshot {
                runOnSameAgent = true
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = "Release => newRelease"
            }
        }
        dependency(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Debug) {
            snapshot {
                runOnSameAgent = true
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = "Debug => newDebug"
            }
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux")
        contains("teamcity.agent.name", "ccscs3-14")
    }
})
