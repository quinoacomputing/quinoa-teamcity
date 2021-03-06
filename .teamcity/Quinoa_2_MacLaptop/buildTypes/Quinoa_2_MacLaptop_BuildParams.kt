package Quinoa_2_MacLaptop.buildParams

enum class CmakeBuildType { Debug, Release }
enum class Compiler { clang, gnu }

data class BuildParams( val buildtype: CmakeBuildType,
                        val compiler: Compiler,
                        val rndq: Boolean,
                        val tpl: String )
