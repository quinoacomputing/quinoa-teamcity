package Quinoa_2_Linux.buildParams

enum class CmakeBuildType { Debug, Release }
enum class Compiler { clang, gnu, intel }
enum class MathLib { mkl, lapack }
enum class StdLibC { libc, libstdc }

data class BuildParams( val buildtype: CmakeBuildType,
                        val compiler: Compiler,
                        val mathlib: MathLib,
                        val stdlibc: StdLibC,
                        val smp: Boolean,
                        val rndq: Boolean )
