package Quinoa_2_Doc

import Quinoa_2_Doc.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "19ac1646-b1ed-4d1f-bed9-a16af3458062"
    id = "Quinoa_2_Doc"
    parentId = "Quinoa_2"
    name = "Doc"
    description = "Documentation and code coverage builds"

    template(Quinoa_2_Doc_Matrix)

    // Generate doc and coverage builds
    buildType(Quinoa_2_Doc_Build_Release)
    buildType(Quinoa_2_Doc_Build_Debug)

    // Generate deploy build
    buildType(Quinoa_2_Doc_Deploy)

    cleanup {
      artifacts(days = 14)
    }
})
