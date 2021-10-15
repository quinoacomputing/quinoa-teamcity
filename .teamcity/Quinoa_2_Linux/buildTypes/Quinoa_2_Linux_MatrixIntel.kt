package Quinoa_2_Linux.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Quinoa_2_Linux_MatrixIntel : Template({
    uuid = "07323274-88d1-40b6-a743-d19c9f74f295"
    id = "Quinoa_2_Linux_Matrix_Intel"
    name = "Matrix-Intel"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
    }

    val stepPrefix = """
      source /opt/intel/oneapi/setvars.sh intel64
      export FI_PROVIDER=tcp
    """.trimIndent()

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_17"
            scriptContent = """git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
        script {
            name = "Build code"
            id = "RUNNER_18"
            scriptContent = """
                ${stepPrefix}
                rm -rf build && mkdir build && cd build
                if [[ %rndq% = true ]]; then cmake -DTPL_DIR=/scratch3/jbakosi/quinoa-tpl/intel-mkl-rndq -DCMAKE_C_COMPILER=mpiicc -DCMAKE_CXX_COMPILER=mpiicpc -DENABLE_RNGTEST=false -DCMAKE_BUILD_TYPE=%buildtype% -DCMAKE_CXX_FLAGS=-Werror -GNinja ../src; fi
                if [[ %rndq% = false ]]; then cmake -DTPL_DIR=/scratch3/jbakosi/quinoa-tpl/intel-mkl -DCMAKE_C_COMPILER=mpiicc -DCMAKE_CXX_COMPILER=mpiicpc -DENABLE_RNGTEST=false -DCMAKE_BUILD_TYPE=%buildtype% -DCMAKE_CXX_FLAGS=-Werror -GNinja ../src; fi
                ninja -j8
            """.trimIndent()
        }
        script {
            name = "Run tests"
            id = "RUNNER_19"
            workingDir = "build"
            scriptContent = """
                ${stepPrefix}
                if [ %smp% = true ]; then ./charmrun +p6 "--bind-to none" Main/unittest -v -q +ppn 3; else ./charmrun +p8 Main/unittest -v -q; fi && ctest -j8 --output-on-failure -LE extreme
            """.trimIndent()
        }
        stepsOrder = arrayListOf("RUNNER_17", "RUNNER_18", "RUNNER_19")
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux", "RQ_19")
        contains("teamcity.agent.name", "lagrange", "RQ_20")
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
