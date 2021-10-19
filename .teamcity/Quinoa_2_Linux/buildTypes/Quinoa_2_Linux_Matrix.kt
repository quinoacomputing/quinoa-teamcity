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
      [ %compiler% == clang ] && module load clang/9 openmpi/3.1.3/clang/9
      [ %compiler% == gnu ] && module load gcc-9.1.0-gcc-8.2.0-757ly4p openmpi-4.0.1-gcc-9.1.0-wiqayvj root/gnu-9
      [ %stdlibcpp% == libc++ ] && module load libc++-clang-9 
      [ %mathlib% == mkl ] && module load mkl/2019 || module load netlib-lapack-3.8.0-gcc-8.3.1-owoo3yp
      [ %rngsse2% == true ] && module load rngsse2
      [ %testu01% == true ] && module load testu01
      [ %rndq% == true ] && module load charm-rndq/%compiler%-9-%stdlibcpp%
      [[ %rndq% == false && %smp% == true ]] && module load charm-smp/%compiler%-9-%stdlibcpp%
      [[ %rndq% == false && %smp% == false ]] && module load charm/%compiler%-9-%stdlibcpp%
      module load hdf5/%compiler%-9 netcdf/%compiler%-9 h5part/%compiler%-9 trilinos/%compiler%-9-%stdlibcpp%/%mathlib% omega_h/%compiler%-9-%stdlibcpp%
      module load pugixml pegtl pstreams boost-1.65.1-gcc-4.8.5-s7d4zmv gmsh-4.0.0-gcc-8.2.0-qcgnz7f ninja-1.8.2-gcc-4.8.5-srfy2lo random123 tut numdiff backward-cpp highwayhash brigand sol2
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
                cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DSTDLIBCPP=%stdlibcpp% -DCMAKE_DISABLE_FIND_PACKAGE_RNGSSE2=!%rngsse2% -DCMAKE_DISABLE_FIND_PACKAGE_TestU01=!%testu01% -DCMAKE_CXX_FLAGS=-Werror -DRUNNER_ARGS="--bind-to none" -GNinja -DEXCEPTIONS_WRITE_TO_CERR=false ../src && ninja -j8
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
                +:*
            """.trimIndent()
        }
    }
})
