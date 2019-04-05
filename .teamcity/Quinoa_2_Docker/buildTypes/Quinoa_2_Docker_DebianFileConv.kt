package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object Quinoa_2_Docker_DebianFileConv : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_ImageForSingleExecutable)
    uuid = "a4c193fd-7542-44a5-b8a1-3e70c2341d5f"
    id = "Quinoa_2_Docker_DebianFileConv"
    name = "Debian-fileconv"
    description = "Build fileconv only on Debian"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-debian-executable-only")
        param("executable", "fileconv")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "debian-fileconv")
        param("workdir", "tools/docker")
    }
})
