package Quinoa_2_MacLaptop.buildTypes

import Quinoa_2_MacLaptop.buildParams.*
import jetbrains.buildServer.configs.kotlin.v10.*

class Quinoa_2_MacLaptop_Build( bp: BuildParams ) : BuildType({

    template(Quinoa_2_MacLaptop.buildTypes.Quinoa_2_MacLaptop_Matrix)

    val paramToId = bp.buildtype.toString().toExtId() +
                    bp.compiler.toString().toExtId() +
                    bp.root.toString().toExtId() +
                    bp.rndq.toString().toExtId() +
                    bp.tpl.toExtId();

    uuid = "7df011b4-4795-7b89-9d03-aba1b1cb53f7_$paramToId"
    extId = "Quinoa_2_MacLaptop_Build_$paramToId"
    name = "${bp.buildtype.toString()}, ${bp.compiler.toString()}, root=${bp.root.toString()}, rndq=${bp.rndq.toString()}"
    description = "Mac laptop matrix build instance"

    params {
        param("buildtype", bp.buildtype.toString())
        param("compiler", bp.compiler.toString())
        param("root", bp.root.toString())
        param("rndq", bp.rndq.toString())
        param("tpl", bp.tpl)
    }
})
