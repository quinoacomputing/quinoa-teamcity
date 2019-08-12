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
      . ${'$'}SPACK_ROOT/share/spack/setup-env.sh
      module load intel/2019 mpi
      [ %stdlibcpp% == libc++ ] && module load libc++-clang-9 
      [ %mathlib% == mkl ] && module load mkl/2019
      [ %rngsse2% == true ] && module load rngsse2
      [ %testu01% == true ] && module load testu01
      [ %rndq% == true ] && module load charm-rndq/intel-%stdlibcpp%
      [[ %rndq% == false && %smp% == true ]] && module load charm-smp/intel-%stdlibcpp%
      [[ %rndq% == false && %smp% == false ]] && module load charm/intel-%stdlibcpp%
      module load hdf5/intel netcdf/intel h5part/intel trilinos/intel-%stdlibcpp%/%mathlib% omega_h/intel-%stdlibcpp%
      module load random123 tut numdiff highwayhash brigand
      module list""".trimIndent()

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
                cmake -DCMAKE_CXX_COMPILER=icpc -DCMAKE_C_COMPILER=icc -DCMAKE_BUILD_TYPE=%buildtype% -DSTDLIBCPP=%stdlibcpp% -DCMAKE_DISABLE_FIND_PACKAGE_RNGSSE2=!%rngsse2% -DCMAKE_DISABLE_FIND_PACKAGE_TestU01=!%testu01% -DMATHLIB=%mathlib% -DCMAKE_CXX_FLAGS=-Werror -DRUNNER_ARGS="--bind-to none" -DCMAKE_C_COMPILER_LAUNCHER=ccache -DCMAKE_CXX_COMPILER_LAUNCHER=ccache -GNinja ../src && ccache -z && ninja -j8 && ccache -s
            """.trimIndent()
        }
        script {
            name = "Run tests"
            id = "RUNNER_19"
            workingDir = "build"
            scriptContent = """
                ${stepPrefix}
                if [ %smp% = true ]; then ./charmrun +p6 "--bind-to none" Main/unittest -v -q +ppn 3; else ./charmrun +p8 "--bind-to none" Main/unittest -v -q; fi && ctest -j8 --output-on-failure -LE extreme
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
