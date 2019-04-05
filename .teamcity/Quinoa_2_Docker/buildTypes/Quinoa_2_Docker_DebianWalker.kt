package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object Quinoa_2_Docker_DebianWalker : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_ImageForSingleExecutable)
    uuid = "a4c193fd-6e45-44a5-b8a1-3e70c2341d5f"
    id = "Quinoa_2_Docker_DebianWalker"
    name = "Debian-walker"
    description = "Build walker only on Debian"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-debian-executable-only")
        param("executable", "walker")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "debian-walker")
        param("workdir", "tools/docker")
    }
})
