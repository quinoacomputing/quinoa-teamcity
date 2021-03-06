package Quinoa_2_MacLaptop

import Quinoa_2_MacLaptop.buildTypes.*
import Quinoa_2_MacLaptop.buildParams.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "7b48bd08-3540-4fa1-ac86-d238159396f3"
    id = "Quinoa_2_MacLaptop"
    parentId = "Quinoa_2"
    name = "MacLaptop"
    description = "Mac laptop builds"

    template(Quinoa_2_MacLaptop_Matrix)
    val allBuilds = mutableListOf< BuildParams >()

    // Generate matrix with all possible combinations of build parameters
    // defined in package buildParams.
    CmakeBuildType.values().forEach{ b ->
      Compiler.values().forEach{ c ->
        allBuilds.add( BuildParams(b,c,false,"") )
        allBuilds.add( BuildParams(b,c,true,"-rndq") )
      }
    }

    val builds = mutableListOf< BuildParams >()

    // Optionally exclude some builds (no-op for now)
    allBuilds.forEach{ b ->
      builds.add( b );
    }

    // Generate TeamCity builds
    builds.forEach{ buildType( Quinoa_2_MacLaptop_Build(it) ) }
})
