package Quinoa_2_Doc.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*

object Quinoa_2_Doc_Build_Release : BuildType({
    template(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Matrix)
    uuid = "1308360c-2059-48e8-8188-c0341f15ecfb_Release"
    extId = "Quinoa_2_Doc_Build_Release"
    name = "Release"
    description = "Doc matrix build instance"

    params {
        param("buildtype", "Release")
    }
})
