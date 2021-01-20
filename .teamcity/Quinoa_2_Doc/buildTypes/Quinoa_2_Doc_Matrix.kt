package Quinoa_2_Doc.buildTypes

import Quinoa_2_Doc.buildParams.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Quinoa_2_Doc_Matrix : Template({
    uuid = "dfd6c8ef-72e2-4e44-be66-fea7a62d423e"
    id = "Quinoa_2_Doc_Matrix"
    name = "Matrix"

    artifactRules = "%buildtype% => %buildtype%.tgz"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
    }

    val stepPrefix = """
        rm -rf build && mkdir build && cd build
        """.trimIndent()

    val cmakeCmd = "cmake -DCMAKE_BUILD_TYPE=%buildtype% -DCMAKE_CXX_FLAGS=-Werror -DCOVERAGE=on -DRUNNER_ARGS=\"--bind-to none\" -DTPL_DIR=/scratch3/jbakosi/quinoa-tpl/gnu -DENABLE_EXAM2M=true ../src"

    val makeCmd = "make -j30"

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_17"
            scriptContent = """git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
        script {
            name = "Generate unit test coverage"
            id = "RUNNER_18"
            scriptContent = """
                ${stepPrefix} && ${cmakeCmd} && ${makeCmd} unittest_coverage && mkdir -p ../%buildtype% && rm -rf ../%buildtype%/unittest_coverage && mv doc/html/unittest_coverage ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate regression test coverage"
            id = "RUNNER_19"
            scriptContent = """
                ${stepPrefix} && ${cmakeCmd} && ${makeCmd} regression_coverage && rm -rf ../%buildtype%/regression_coverage && mv doc/html/regression_coverage ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate full test coverage"
            id = "RUNNER_20"
            scriptContent = """
                ${stepPrefix} && ${cmakeCmd} && ${makeCmd} test_coverage && rm -rf ../%buildtype%/test_coverage && mv doc/html/test_coverage ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate cppcheck static analysis report"
            id = "RUNNER_21"
            scriptContent = """
                ${stepPrefix} && ${cmakeCmd} && ${makeCmd} cppcheck-xml && rm -rf ../%buildtype%/cppcheck && mv doc/cppcheck ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate documentation"
            id = "RUNNER_22"
            scriptContent = """
                ${stepPrefix} && ${cmakeCmd} && ${makeCmd} doc && rm -rf ../%buildtype%/html && cd doc/html && touch .nojekyll && echo "quinoacomputing.org" > CNAME && echo %build.vcs.number.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster% > sha1 && cp ../../../doc/images/* . && cd - && mv doc/html ../%buildtype%
            """.trimIndent()
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux", "RQ_19")
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
