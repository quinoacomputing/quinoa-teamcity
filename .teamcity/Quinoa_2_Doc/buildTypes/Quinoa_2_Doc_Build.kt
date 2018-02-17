package Quinoa_2_Doc.buildTypes

import Quinoa_2_Doc.buildParams.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*

class Quinoa_2_Doc_Build( bp: BuildParams ) : BuildType({

    template(Quinoa_2_Doc.buildTypes.Quinoa_2_Doc_Matrix)

    val paramToId = bp.buildtype.toString().toExtId()

    uuid = "1308360c-2059-48e8-8188-c0341f15ecfb_$paramToId"
    extId = "Quinoa_2_Doc_Build_$paramToId"
    name = "${bp.buildtype.toString()}"
    description = "Doc matrix build instance"

    params {
        param("buildtype", bp.buildtype.toString())
    }
})
