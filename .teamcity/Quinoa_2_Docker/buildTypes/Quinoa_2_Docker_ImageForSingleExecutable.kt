package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Quinoa_2_Docker_ImageForSingleExecutable : Template({
    uuid = "a66895d1-4cd1-489b-9aa3-f41e562851f4"
    id = "Quinoa_2_Docker_ImageForSingleExecutable"
    name = "Image for single executable"

    vcs {
        root("Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster")

    }

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_23"
            scriptContent = """git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
        script {
            name = "Build image"
            id = "RUNNER_24"
            workingDir = "%workdir%"
            scriptContent = "docker build --build-arg http_proxy=http://proxyout.lanl.gov:8080/ --build-arg https_proxy=http://proxyout.lanl.gov:8080/ --build-arg COMMIT=%build.vcs.number% --build-arg EXECUTABLE=%executable% --no-cache=true --rm=true -t %organization%/%repository%-build:%tag% -f %dockerfile% ."
        }
        script {
            name = "Squash image"
            id = "RUNNER_30"
            enabled = false
            workingDir = "%workdir%"
            scriptContent = "/home/jbakosi/.local/bin/docker-squash --cleanup --tmp-dir /scratch3/jbakosi/docker-squash-%tag% -t %organization%/%repository%:%tag% %organization%/%repository%-build:%tag%"
        }
        script {
            name = "Push image"
            id = "RUNNER_32"
            enabled = false
            workingDir = "%workdir%"
            scriptContent = "docker push %organization%/%repository%:%tag%"
        }
        script {
            name = "Delete image"
            id = "RUNNER_33"
            enabled = false
            workingDir = "%workdir%"
            scriptContent = "docker rmi %organization%/%repository%:%tag%"
        }
        stepsOrder = arrayListOf("RUNNER_23", "RUNNER_24", "RUNNER_30", "RUNNER_32", "RUNNER_33")
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            triggerRules = """
                +:.
                -:comment=\[ci skip\]:**
                -:comment=\[skip ci\]:**
                -:comment=\[CI SKIP\]:**
                -:comment=\[SKIP CI\]:**
            """.trimIndent()
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux", "RQ_23")
        contains("teamcity.agent.name", "lagrange", "RQ_24")
    }
})
