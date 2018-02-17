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
        . ${'$'}SPACK_ROOT/share/spack/setup-env.sh
        module load openmpi-3.0.0-gcc-4.8.5-fcuaicj hdf5-1.10.1-gcc-4.8.5-rosmsxt netcdf-4.4.1.1-gcc-4.8.5-4rodz7d hypre-2.12.1-gcc-4.8.5-f34qmcg root/gnu mkl/2018 rngsse2 testu01 charm/gnu-libstdc++ h5part/gnu trilinos/gnu-libstdc++/mkl pugixml pegtl pstreams boost-1.65.1-gcc-4.8.5-s7d4zmv gmsh-3.0.1-gcc-4.8.5-xqrg5fi random123 tut cartesian_product numdiff
        module list
        rm -rf build && mkdir build && cd build
        """.trimIndent()

    val cmakeCmd = "cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DENABLE_ROOT=on -DCMAKE_CXX_FLAGS=-Werror -DCOVERAGE=on -DRUNNER_ARGS=\"--bind-to none --map-by node -oversubscribe\" ../src"

    val makeCmd = "make -j16"

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_17"
            scriptContent = """/ccs/opt/git/bin/git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
        script {
            name = "Generate unit test coverage"
            id = "RUNNER_18"
            scriptContent = """
                ${stepPrefix}
                ${cmakeCmd}
                ${makeCmd} unittest_coverage
                mkdir -p ../%buildtype%
                rm -rf ../%buildtype%/unittest_coverage
                mv doc/html/unittest_coverage ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate regression test coverage"
            id = "RUNNER_19"
            scriptContent = """
                ${stepPrefix}
                ${cmakeCmd}
                ${makeCmd} regression_coverage
                rm -rf ../%buildtype%/regression_coverage
                mv doc/html/regression_coverage ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate full test coverage"
            id = "RUNNER_20"
            scriptContent = """
                ${stepPrefix}
                ${cmakeCmd}
                ${makeCmd} test_coverage
                rm -rf ../%buildtype%/test_coverage
                mv doc/html/test_coverage ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate cppcheck static analysis report"
            id = "RUNNER_21"
            scriptContent = """
                ${stepPrefix}
                ${cmakeCmd}
                ${makeCmd} cppcheck
                rm -rf ../%buildtype%/cppcheck
                mv doc/cppcheck ../%buildtype%
            """.trimIndent()
        }
        script {
            name = "Generate documentation"
            id = "RUNNER_22"
            scriptContent = """
                ${stepPrefix}
                ${cmakeCmd}
                ${makeCmd} doc
                rm -rf ../%buildtype%/html
                cd doc/html && touch .nojekyll && echo %build.vcs.number.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster% > sha1 && cp ../../../doc/images/* . && mv ../../../README.md . && cd -
                mv doc/html ../%buildtype%
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
