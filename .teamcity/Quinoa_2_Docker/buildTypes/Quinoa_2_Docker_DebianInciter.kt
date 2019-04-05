package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object Quinoa_2_Docker_DebianInciter : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_ImageForSingleExecutable)
    uuid = "a4c193fd-6e45-44a5-b8a1-4567c2341d5f"
    id = "Quinoa_2_Docker_DebianInciter"
    name = "Debian-inciter"
    description = "Build inciter only on Debian"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-debian-executable-only")
        param("executable", "inciter")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "debian-inciter")
        param("workdir", "tools/docker")
    }
})
