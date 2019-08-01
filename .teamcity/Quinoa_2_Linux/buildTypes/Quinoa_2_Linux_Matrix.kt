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
      [ %compiler% == clang ] && module load clang/latest openmpi/3.0.0/clang/latest hdf5/clang netcdf/clang hypre/clang
      [ %compiler% == gnu ] && module load openmpi-3.0.0-gcc-4.8.5-fcuaicj hdf5-1.10.1-gcc-4.8.5-rosmsxt netcdf-4.4.1.1-gcc-4.8.5-4rodz7d hypre-2.12.1-gcc-4.8.5-f34qmcg root/gnu
      [ %compiler% == intel ] && module load intel/2018 openmpi/3.0.0/intel/2018 hdf5/intel netcdf/intel hypre/intel root/intel
      [ %mathlib% == mkl ] && module load mkl/2018
      [[ %mathlib% == lapack && %compiler% == intel ]] && module load lapack/intel
      [[ %mathlib% == lapack && %compiler% != intel ]] && module load netlib-lapack-3.6.1-gcc-4.8.5-ln5clys
      [ %rndq% == true ] && module load charm-rndq/%compiler%-%stdlibcpp%
      [[ %rndq% == false && %smp% == true ]] && module load charm-smp/%compiler%-%stdlibcpp%
      [[ %rndq% == false && %smp% == false ]] && module load charm/%compiler%-%stdlibcpp%
      module load h5part/%compiler% trilinos/%compiler%-%stdlibcpp%/%mathlib% omega_h/%compiler%-%stdlibcpp%
      module load rngsse2 testu01 pugixml pegtl pstreams boost-1.65.1-gcc-4.8.5-s7d4zmv gmsh-4.0.0-gcc-8.2.0-qcgnz7f ninja-1.8.2-gcc-4.8.5-srfy2lo random123 tut numdiff libc++ backward-cpp highwayhash brigand ccache-3.3.4-gcc-4.8.5-3rwbf2t
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
                cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DSTDLIBCPP=%stdlibcpp% -DCMAKE_CXX_FLAGS=-Werror -DRUNNER_ARGS="--bind-to none -oversubscribe" -DCMAKE_C_COMPILER_LAUNCHER=ccache -DCMAKE_CXX_COMPILER_LAUNCHER=ccache -GNinja ../src && ccache -z && ninja -j8 && ccache -s
            """.trimIndent()
        }
        script {
            name = "Run tests"
            id = "RUNNER_19"
            workingDir = "build"
            scriptContent = """
                ${stepPrefix}
                if [ %smp% = true ]; then ./charmrun +p6 "--bind-to none -oversubscribe" Main/unittest -v -q +ppn 3; else ./charmrun +p8 "--bind-to none -oversubscribe" Main/unittest -v -q; fi && ctest -j8 --output-on-failure -LE extreme
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
