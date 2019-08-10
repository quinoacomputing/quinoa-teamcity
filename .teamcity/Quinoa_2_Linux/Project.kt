package Quinoa_2_Linux

import Quinoa_2_Linux.buildTypes.*
import Quinoa_2_Linux.buildParams.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "19ac1646-b1ed-4d1f-bed9-a16af31a8062"
    id = "Quinoa_2_Linux"
    parentId = "Quinoa_2"
    name = "Linux"
    description = "Linux builds"

    template(Quinoa_2_Linux_Matrix)

    val allBuilds = mutableListOf< BuildParams >()

    // Generate matrix with all possible combinations of build parameters,
    // defined in package buildParams, not using Charm++'s randomized message
    // queues, in Charm++'s non-SMP mode.
    Compiler.values().forEach{ c ->
      StdLibC.values().forEach{ l ->
        MathLib.values().forEach{ m ->
          CmakeBuildType.values().forEach{ b ->
            allBuilds.add( BuildParams(b,c,m,l,true,true,false,false) )
          }
        }
      }
    }

    // Add builds with some optional libraries unavailable
    allBuilds.add( BuildParams(CmakeBuildType.Release,Compiler.gnu,MathLib.mkl,StdLibC.libstdc,false,false,false,false) )
    allBuilds.add( BuildParams(CmakeBuildType.Release,Compiler.gnu,MathLib.mkl,StdLibC.libstdc,false,true,false,false) )
    allBuilds.add( BuildParams(CmakeBuildType.Release,Compiler.gnu,MathLib.mkl,StdLibC.libstdc,true,false,false,false) )

    // Add some builds using Charm++'s randomized message queues and non-SMP mode
    Compiler.values().forEach{ c ->
      StdLibC.values().forEach{ l ->
        CmakeBuildType.values().forEach{ b ->
          allBuilds.add( BuildParams(b,c,MathLib.mkl,l,true,true,false,true) )  // non-SMP, rndq
          allBuilds.add( BuildParams(b,c,MathLib.mkl,l,true,true,true,false) )  // SMP, non-rndq
        }
      }
    }

    val builds = mutableListOf< BuildParams >()

    // Exclude gnu/libc++ builds
    allBuilds.forEach{ b ->
      if ( !(b.compiler == Compiler.gnu && b.stdlibc == StdLibC.libc)) {
        builds.add( b );
      }
    }

    // Generate TeamCity builds
    builds.forEach{ buildType( Quinoa_2_Linux_Build(it) ) }
})
