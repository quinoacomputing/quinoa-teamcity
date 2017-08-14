package Quinoa_2_Doc.buildParams

enum class CmakeBuildType { Debug, Release }

data class BuildParams( val buildtype: CmakeBuildType )
