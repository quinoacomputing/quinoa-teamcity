package Quinoa_2_MacLaptop.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.vcs

object Quinoa_2_MacLaptop_Matrix : Template({
    uuid = "75d8a7e0-9c7c-4bc4-9cbb-3d1d8e58febd"
    extId = "Quinoa_2_MacLaptop_Matrix"
    name = "Matrix"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)

    }

    val stepPrefix = """
      [ %compiler% == clang ] && port select clang mp-clang-3.9 && port select mpi openmpi-clang39-fortran
      [ %compiler% == gnu ] && port select gcc mp-gcc5 && port select mpi openmpi-gcc5-fortran
    """.trimIndent()

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_20"
            scriptContent = """git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
        script {
            name = "Build code"
            id = "RUNNER_21"
            scriptContent = """
                ${stepPrefix}
                rm -rf build && mkdir build && cd build
                cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DCMAKE_CXX_FLAGS=-Werror -DTPL_DIR=/Users/jbakosi/code/quinoa-tpl/install/%compiler%-x86_64%tpl% -DENABLE_ROOT=%root% ../src
                make -j%teamcity.agent.hardware.cpuCount%
            """.trimIndent()
        }
        script {
            name = "Run tests"
            id = "RUNNER_22"
            workingDir = "build"
            scriptContent = """
                ${stepPrefix}
                ../script/run_tests.sh
            """.trimIndent()
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Mac OS X", "RQ_21")
        contains("teamcity.agent.name", "neumann", "RQ_22")
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            triggerRules = """
                +:.
                -:comment=\[ci skip\]:**
                -:comment=\[skip ci\]:**
            """.trimIndent()
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
    }
})