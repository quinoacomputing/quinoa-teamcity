package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object Quinoa_2_Docker_DebianRNGTest : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_ImageForSingleExecutable)
    uuid = "a4c443fd-6e45-44a5-b983-3e70c2341d5f"
    id = "Quinoa_2_Docker_DebianRNGTest"
    name = "Debian-rngtest"
    description = "Build rngtest only on Debian"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-debian-executable-only")
        param("executable", "rngtest")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "debian-rngtest")
        param("workdir", "tools/docker")
    }
})
