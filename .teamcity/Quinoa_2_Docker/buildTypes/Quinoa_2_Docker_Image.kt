package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Quinoa_2_Docker_Image : Template({
    uuid = "e6b8a4ab-5cd9-469e-a60a-03419b103842"
    id = "Quinoa_2_Docker_Image"
    name = "Image"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)

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
            scriptContent = "docker build --build-arg http_proxy=http://proxyout.lanl.gov:8080/ --build-arg https_proxy=http://proxyout.lanl.gov:8080/ --build-arg COMMIT=%build.vcs.number% --no-cache=true --rm=true -t %organization%/%repository%-build:%tag% --shm-size=1g -f %dockerfile% ."
        }
        script {
            name = "Squash image"
            id = "RUNNER_30"
            workingDir = "%workdir%"
            scriptContent = "/home/jbakosi/.local/bin/docker-squash --cleanup --tmp-dir /scratch3/jbakosi/docker-squash-%tag% -t %organization%/%repository%:%tag% %organization%/%repository%-build:%tag%"
        }
        script {
            name = "Push image"
            id = "RUNNER_32"
            workingDir = "%workdir%"
            scriptContent = "docker push %organization%/%repository%:%tag%"
        }
        script {
            name = "Delete image"
            id = "RUNNER_33"
            workingDir = "%workdir%"
            scriptContent = "docker rmi %organization%/%repository%:%tag%"
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux", "RQ_23")
        contains("teamcity.agent.name", "lagrange", "RQ_24")
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
})
