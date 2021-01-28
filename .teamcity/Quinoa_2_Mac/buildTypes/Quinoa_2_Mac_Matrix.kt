package Quinoa_2_Mac.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Quinoa_2_Mac_Matrix : Template({
    uuid = "75d8a7e0-9c7c-4bc4-9cbb-3d1d8e5ffebd"
    id = "Quinoa_2_Mac_Matrix"
    name = "Matrix"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)

    }

    val stepPrefix = """
      . ${'$'}SPACK_ROOT/share/spack/setup-env.sh
      module load gnupg-2.2.25-apple-clang-12.0.0-7qxfsez ninja-1.10.2-apple-clang-12.0.0-cmaq3l3 ccache-3.7.11-apple-clang-12.0.0-gun2fae cmake-3.19.2-apple-clang-12.0.0-fnkxqra libtool-2.4.6-apple-clang-12.0.0-55jkyhe autoconf-2.69-apple-clang-12.0.0-kurpqjy automake-1.16.3-apple-clang-12.0.0-wpk76mp environment-modules-4.6.1-apple-clang-12.0.0-hxinmyj
      [ %compiler% == clang ] && openmpi-3.1.4-clang-10.0.0-apple-gktnzf5 hdf5-1.10.5-clang-10.0.0-apple-ebdcvu2
      [ %compiler% == gnu ] && module load gcc-10.2.0-apple-clang-12.0.0-yhi67dp openmpi-4.0.5-gcc-10.2.0-4w7bqvj hdf5-1.10.7-gcc-10.2.0-3ebiti7
      module list
    """.trimIndent()

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_20"
            scriptContent = """
                ${stepPrefix}
                git verify-commit %build.vcs.number% 2>&1 | grep "Good signature
            """"
        }
        script {
            name = "Build code"
            id = "RUNNER_21"
            scriptContent = """
                ${stepPrefix}
                rm -rf build && mkdir build && cd build
                cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DCMAKE_CXX_FLAGS=-Werror -DTPL_DIR=/Users/jbakosi/code/quinoa-tpl/install/%compiler%-x86_64%tpl% -DCMAKE_C_COMPILER_LAUNCHER=ccache -DCMAKE_CXX_COMPILER_LAUNCHER=ccache -GNinja ../src && ccache -z && ninja && ccache -s
            """.trimIndent()
        }
        script {
            name = "Run tests"
            id = "RUNNER_22"
            workingDir = "build"
            scriptContent = """
                ${stepPrefix}
                if [ %smp% = true ]; then ./charmrun +p 22 --bind-to none Main/unittest -v -q +ppn 11; else ./charmrun +p 24 -oversubscribe Main/unittest -v -q; fi && ctest -j24 --output-on-failure -LE extreme
            """.trimIndent()
        }
    }

    requirements {
        equals("teamcity.agent.jvm.os.name", "Mac OS X", "RQ_21")
        contains("teamcity.agent.name", "fermat", "RQ_22")
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
