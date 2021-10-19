package Quinoa_2_Linux.buildTypes

import Quinoa_2_Linux.buildParams.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*

class Quinoa_2_Linux_Build( bp: BuildParams ) : BuildType({

    template(Quinoa_2_Linux.buildTypes.Quinoa_2_Linux_Matrix)

    fun Boolean.toInt() = if (this) 1 else 0

    val paramToId = bp.buildtype.toString().toExtId() +
                    bp.compiler.toString().toExtId() +
                    bp.mathlib.toString().toExtId() +
                    bp.stdlibc.toString().toExtId() +
                    bp.smp.toInt().toString().toExtId() +
                    bp.rndq.toInt().toString().toExtId()

    uuid = "1308360c-2059-48e8-8188-c06d1f15ecfb_$paramToId"
    extId = "Quinoa_2_Linux_Build_$paramToId"
    name = "${bp.buildtype.toString()}, ${bp.compiler.toString()}, ${bp.mathlib.toString()}, ${bp.stdlibc.toString()}++, smp=${bp.smp}, rndq=${bp.rndq}"
    description = "Linux matrix build instance"

    params {
        param("buildtype", bp.buildtype.toString())
        param("compiler", bp.compiler.toString())
        param("mathlib", bp.mathlib.toString())
        param("stdlibcpp", bp.stdlibc.toString()+"++")
        param("smp", bp.smp.toString())
        param("rndq", bp.rndq.toString())
    }
})

class Quinoa_2_Linux_BuildIntel( bp: BuildParams ) : BuildType({

    template(Quinoa_2_Linux.buildTypes.Quinoa_2_Linux_MatrixIntel)

    fun Boolean.toInt() = if (this) 1 else 0

    val paramToId = bp.buildtype.toString().toExtId() +
                    bp.compiler.toString().toExtId() +
                    bp.mathlib.toString().toExtId() +
                    bp.stdlibc.toString().toExtId() +
                    bp.smp.toInt().toString().toExtId() +
                    bp.rndq.toInt().toString().toExtId()

    uuid = "1308360c-2059-48e8-8188-c02d1f15ecfb_$paramToId"
    extId = "Quinoa_2_Linux_BuildIntel_$paramToId"
    name = "${bp.buildtype.toString()}, ${bp.compiler.toString()}, ${bp.mathlib.toString()}, ${bp.stdlibc.toString()}++, smp=${bp.smp}, rndq=${bp.rndq}"
    description = "Linux matrix intel build instance"

    params {
        param("buildtype", bp.buildtype.toString())
        param("compiler", bp.compiler.toString())
        param("mathlib", bp.mathlib.toString())
        param("stdlibcpp", bp.stdlibc.toString()+"++")
        param("smp", bp.smp.toString())
        param("rndq", bp.rndq.toString())
    }
})
