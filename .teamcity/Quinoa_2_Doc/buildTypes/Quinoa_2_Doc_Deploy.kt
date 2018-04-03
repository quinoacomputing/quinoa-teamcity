package Quinoa_2_Doc.buildTypes

import Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Debug
import Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Release
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger

object Quinoa_2_Doc_Deploy : BuildType({
    uuid = "7674b556-609f-4375-be3d-45ea9b1f380c"
    id = "Quinoa_2_Doc_Deploy"
    name = "Deploy"
    description = "Deploy documentation, code coverage, and static analysis reports"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster)
    }

    steps {
        script {
            name = "Combine documentation, code coverage, and static analysis reports"
            scriptContent = """
                shopt -s extglob
                rm -rf .nojekyll !(NewCoverage)
                mkdir Debug && tar xzf NewCoverage/Debug.tgz -C Debug
                mkdir Release && tar xzf NewCoverage/Release.tgz -C Release
                rm -rf NewCoverage
                mv Release/html/* Release/html/.nojekyll .
                rm -rf Release/html Debug/html
            """.trimIndent()
        }
        script {
            name = "Push documentation, code coverage, and static analysis reports"
            scriptContent = """
                git pull
                git add --all .
                git reset HEAD sha1
                git commit -m "Documentation for commit `cat sha1`"
                git push
            """.trimIndent()
        }
    }

    triggers {
        finishBuildTrigger {
            buildTypeExtId = Quinoa_2_Doc_Build_Release.id
            successfulOnly = true
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
        finishBuildTrigger {
            buildTypeExtId = Quinoa_2_Doc_Build_Debug.id
            successfulOnly = true
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
    }

    dependencies {
        dependency(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Debug) {
            snapshot {
                runOnSameAgent = true
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = "Debug.tgz => NewCoverage"
            }
        }
        dependency(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Release) {
            snapshot {
                runOnSameAgent = true
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = "Release.tgz => NewCoverage"
            }
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux")
    }
})
