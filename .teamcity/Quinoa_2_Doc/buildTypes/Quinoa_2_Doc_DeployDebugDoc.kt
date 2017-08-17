package Quinoa_2_Doc.buildTypes

import Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Debug
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.FinishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.FinishBuildTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.finishBuildTrigger

object Quinoa_2_Doc_DeployDebugDoc : BuildType({
    uuid = "64caa3c2-77e4-45b4-9ab5-4cd785870789"
    extId = "Quinoa_2_Doc_DeployDebugDoc"
    name = "Deploy debug doc"
    description = "Deploy documentation, code coverage, and static analysis reports from debug build"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster)

    }

    steps {
        script {
            name = "Combine documentation, code coverage and static analysis reports"
            scriptContent = """
                cd html
                rm -rf Debug
                mkdir Debug
                mv ../unittest_coverage ../regression_coverage ../test_coverage ../cppcheck Debug/
            """.trimIndent()
        }
        script {
            name = "Push documentation, code coverage, and static analysis reports"
            scriptContent = """
                git rm -rf .
                mv html/* html/.nojekyll .
                git add .
                git commit -m "Documentation for commit %build.vcs.number.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster% updating coverage reports for Debug builds"
                git push
            """.trimIndent()
        }
    }

    triggers {
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
        dependency(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Build_Debug) {
            snapshot {
                runOnSameAgent = true
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = """
                    unittest_coverage => unittest_coverage
                    
                    regression_coverage => regression_coverage
                    
                    test_coverage => test_coverage
                    
                    cppcheck => cppcheck
                    
                    html => html
                """.trimIndent()
            }
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux")
        contains("teamcity.agent.name", "ccscs")
    }
})
