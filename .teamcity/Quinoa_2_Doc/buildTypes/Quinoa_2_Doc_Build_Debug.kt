package Quinoa_2_Doc.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*

object Quinoa_2_Doc_Build_Debug : BuildType({
    template(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Matrix)
    uuid = "1308360c-2059-48e8-8188-c0341f15ecfb_Debug"
    extId = "Quinoa_2_Doc_Build_Debug"
    name = "Debug"
    description = "Doc matrix build instance"

    params {
        param("buildtype", "Debug")
    }
})
