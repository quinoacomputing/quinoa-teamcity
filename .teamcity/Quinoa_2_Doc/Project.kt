package Quinoa_2_Doc

import Quinoa_2_Doc.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.Project

object Project : Project({
    uuid = "19ac1646-b1ed-4d1f-bed9-a16af3458062"
    extId = "Quinoa_2_Doc"
    parentId = "Quinoa_2"
    name = "Doc"
    description = "Documentation and code coverage builds"

    template(Quinoa_2_Doc_Matrix)

    // Generate doc and coverage builds
    buildType(Quinoa_2_Doc_Build_Release)
    buildType(Quinoa_2_Doc_Build_Debug)

    // Generate deploy builds
    buildType(Quinoa_2_Doc_DeployDebugDoc)
    buildType(Quinoa_2_Doc_DeployReleaseDoc)
})
