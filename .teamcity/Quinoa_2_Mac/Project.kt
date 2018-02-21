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

    // Generate matrix with all possible combinations of build parameters
    // without ROOT, defined in package buildParams.
    CmakeBuildType.values().forEach{ b ->
      Compiler.values().forEach{ c ->
        allBuilds.add( BuildParams(b,c,false,false,"") )
        allBuilds.add( BuildParams(b,c,false,true,"-rndq") )
      }
    }

    // Add ROOT builds
    allBuilds.add( BuildParams( CmakeBuildType.Debug, Compiler.clang, true, false, "" ) )
    allBuilds.add( BuildParams( CmakeBuildType.Debug, Compiler.clang, true, true, "-rndq" ) )
    allBuilds.add( BuildParams( CmakeBuildType.Release, Compiler.clang, true, false, "" ) )
    allBuilds.add( BuildParams( CmakeBuildType.Release, Compiler.clang, true, true, "-rndq" ) )

    val builds = mutableListOf< BuildParams >()

    // Optionally exclude some builds (no-op for now)
    allBuilds.forEach{ b ->
      builds.add( b );
    }

    // Generate TeamCity builds
    builds.forEach{ buildType( Quinoa_2_Mac_Build(it) ) }
})
