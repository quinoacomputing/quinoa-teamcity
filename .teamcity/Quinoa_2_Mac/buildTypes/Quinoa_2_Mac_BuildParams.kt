package Quinoa_2_Mac.buildParams

enum class CmakeBuildType { Debug, Release }
enum class Compiler { clang, gnu }

data class BuildParams( val buildtype: CmakeBuildType,
                        val compiler: Compiler,
                        val smp: Boolean,
                        val rndq: Boolean,
                        val tpl: String )
