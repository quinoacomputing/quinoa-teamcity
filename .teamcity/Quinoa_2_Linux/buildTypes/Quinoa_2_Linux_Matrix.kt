package Quinoa_2_Linux.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Quinoa_2_Linux_Matrix : Template({
    uuid = "dfd6c8ef-72e2-4e44-be66-fea7a62d455e"
    id = "Quinoa_2_Linux_Matrix"
    name = "Matrix"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)

    }

    val stepPrefix = """
      . ${'$'}SPACK_ROOT/share/spack/setup-env.sh
      [ %compiler% == clang ] && module load clang/latest openmpi/2.0.2/clang/latest hdf5/clang netcdf/clang hypre/clang
      [ %compiler% == gnu ] && module load openmpi-3.0.0-gcc-4.8.5-fcuaicj hdf5-1.10.1-gcc-4.8.5-rosmsxt netcdf-4.4.1.1-gcc-4.8.5-4rodz7d hypre-2.12.1-gcc-4.8.5-f34qmcg root/gnu
      [ %compiler% == intel ] && module load intel/2018 openmpi/2.0.2/intel/2018 hdf5/intel netcdf/intel hypre/intel
      [ %mathlib% == mkl ] && module load mkl/2018
      [[ %mathlib% == lapack && %compiler% == intel ]] && module load lapack/intel
      [[ %mathlib% == lapack && %compiler% != intel ]] && module load netlib-lapack-3.6.1-gcc-4.8.5-ln5clys
      [ %rngsse2% == true ] && module load rngsse2
      [ %testu01% == true ] && module load testu01
      [ %rndq% == true ] && module load charm-rndq/%compiler%-%stdlibcpp%
      [ %rndq% == false ] && module load charm/%compiler%-%stdlibcpp%
      module load h5part/%compiler% trilinos/%compiler%-%stdlibcpp%/%mathlib% omega_h/%compiler%-%stdlibcpp%
      module load pugixml pegtl pstreams boost-1.65.1-gcc-4.8.5-s7d4zmv gmsh-3.0.6-gcc-4.8.5-n34xdqr random123 tut cartesian_product numdiff libc++ backward-cpp highwayhash brigand
      module list""".trimIndent()

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_17"
            scriptContent = """/ccs/opt/git/bin/git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
        script {
            name = "Build code"
            id = "RUNNER_18"
            scriptContent = """
                ${stepPrefix}
                rm -rf build && mkdir build && cd build
                cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DSTDLIBCPP=%stdlibcpp% -DCMAKE_DISABLE_FIND_PACKAGE_RNGSSE2=!%rngsse2% -DCMAKE_DISABLE_FIND_PACKAGE_TestU01=!%testu01% -DENABLE_ROOT=on -DCMAKE_CXX_FLAGS=-Werror ../src
                make -j16
            """.trimIndent()
        }
        script {
            name = "Run tests"
            id = "RUNNER_19"
            workingDir = "build"
            scriptContent = """
                ${stepPrefix}
                ../script/run_tests.sh 16 -oversubscribe
            """.trimIndent()
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Linux", "RQ_19")
        contains("teamcity.agent.name", "ccscs", "RQ_20")
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
