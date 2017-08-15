package Quinoa_2_Doc.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.vcs

object Quinoa_2_Doc_Matrix : Template({
    uuid = "dfd6c8ef-72e2-4e44-be66-fea7a62d423e"
    extId = "Quinoa_2_Doc_Matrix"
    name = "Matrix"

    artifactRules = "build/doc/html => html"

    vcs {
        root(Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
    }

    val stepPrefix = """
      . ${'$'}SPACK_ROOT/share/spack/setup-env.sh
      module load openmpi-2.0.1-gcc-4.8.5-jv7w2de hdf5-1.10.0-patch1-gcc-4.8.5-mmtlfty netcdf-4.4.1-gcc-4.8.5-5xen4a5 hypre-2.10.1-gcc-4.8.5-beuxbxv root/gnu mkl/2018 rngsse2 testu01 charm/gnu-libstdc++ h5part/gnu trilinos/gnu-libstdc++/mkl pugixml pegtl pstreams boost-1.61.0-gcc-4.8.5-q2hywin gmsh-2.12.0-gcc-4.8.5-p3vpjfb random123 tut cartesian_product numdiff
      module list""".trimIndent()
    val cmakeCmd = "cmake -DCMAKE_CXX_COMPILER=mpicxx -DCMAKE_C_COMPILER=mpicc -DCMAKE_BUILD_TYPE=%buildtype% -DENABLE_ROOT=on -DCMAKE_CXX_FLAGS=-Werror -DCOVERAGE=on ../src"
    val makeCmd = "make -j`grep -c processor /proc/cpuinfo`"

    steps {
        script {
            name = "Verify commit"
            id = "RUNNER_17"
            scriptContent = """/ccs/opt/git/bin/git verify-commit %build.vcs.number% 2>&1 | grep "Good signature""""
        }
//                 ${cmakeCmd}
//                 ${makeCmd} regression_coverage
//                 mv doc/html/regression_coverage .. && rm * -rf
//                 ${cmakeCmd}
//                 ${makeCmd} test_coverage
//                 mv doc/html/test_coverage .. && rm * -rf
//                 ${cmakeCmd}
//                 ${makeCmd} cppcheck
//                 mv doc/cppcheck .. && rm * -rf
        script {
            name = "Generate documentation"
            id = "RUNNER_18"
            scriptContent = """
                ${stepPrefix}
                rm -rf unittest_coverage regression_coverage test_coverage cppcheck
                rm -rf build && mkdir build && cd build
                ${cmakeCmd}
                ${makeCmd} unittest_coverage
                mv doc/html/unittest_coverage .. && rm * -rf
                ${cmakeCmd}
                ${makeCmd} doc
                cd doc/html
                mkdir %buildtype% && mv ../../../unittest_coverage %buildtype%/.
                touch .nojekyll
                cp ../../../doc/images/* .
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
            """.trimIndent()
            branchFilter = """
                +:<default>
                +:develop
            """.trimIndent()
        }
    }
})
