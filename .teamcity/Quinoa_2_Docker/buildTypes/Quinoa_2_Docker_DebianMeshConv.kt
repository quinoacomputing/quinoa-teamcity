package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object Quinoa_2_Docker_DebianMeshConv : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_ImageForSingleExecutable)
    uuid = "a4c4453d-6e45-44a5-b8a1-3e70c2341d5f"
    id = "Quinoa_2_Docker_DebianMeshConv"
    name = "Debian-meshconv"
    description = "Build meshconv only on Debian"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-debian-executable-only")
        param("executable", "meshconv")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "debian-meshconv")
        param("workdir", "tools/docker")
    }
})
