package Quinoa_2_Doc

import Quinoa_2_Doc.buildTypes.*
import Quinoa_2_Doc.buildParams.*
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.Project

object Project : Project({
    uuid = "19ac1646-b1ed-4d1f-bed9-a16af3458062"
    extId = "Quinoa_2_Doc"
    parentId = "Quinoa_2"
    name = "Doc"
    description = "Documentation and code coverage builds"

    template(Quinoa_2_Doc_Matrix)

     val builds = mutableListOf< BuildParams >()

    // Generate matrix with all possible combinations of build parameters
    // defined in package buildParams.
    CmakeBuildType.values().forEach{ b ->
      builds.add( BuildParams(b) )
    }

    // Generate TeamCity builds
    builds.forEach{ buildType( Quinoa_2_Doc_Build(it) ) }
})
