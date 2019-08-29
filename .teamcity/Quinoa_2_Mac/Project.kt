package Quinoa_2_Mac

import Quinoa_2_Mac.buildTypes.*
import Quinoa_2_Mac.buildParams.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "7b48bd08-3540-4fa1-ac86-d2381593f6f3"
    id = "Quinoa_2_Mac"
    parentId = "Quinoa_2"
    name = "Mac"
    description = "Mac builds"

    template(Quinoa_2_Mac_Matrix)
    val allBuilds = mutableListOf< BuildParams >()

    // Generate matrix
    CmakeBuildType.values().forEach{ b ->
      //                                        smp,  rndq
      allBuilds.add( BuildParams(b,Compiler.gnu,false,false,"") )
      allBuilds.add( BuildParams(b,Compiler.gnu,false,true,"-rndq") )
      allBuilds.add( BuildParams(b,Compiler.gnu,true,false,"-smp") )
    }

    val builds = mutableListOf< BuildParams >()

    // Optionally exclude some builds (no-op for now)
    allBuilds.forEach{ b ->
      builds.add( b );
    }

    // Generate TeamCity builds
    builds.forEach{ buildType( Quinoa_2_Mac_Build(it) ) }
})
