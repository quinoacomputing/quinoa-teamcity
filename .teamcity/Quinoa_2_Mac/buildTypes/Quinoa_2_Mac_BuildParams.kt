package Quinoa_2_Mac.buildParams

enum class CmakeBuildType { Debug, Release }
// enum class Compiler { clang, gnu }
enum class Compiler { gnu }

data class BuildParams( val buildtype: CmakeBuildType,
                        val compiler: Compiler,
                        val rndq: Boolean,
                        val tpl: String )
