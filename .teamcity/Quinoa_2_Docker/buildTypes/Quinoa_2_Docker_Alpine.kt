package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object Quinoa_2_Docker_Alpine : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_Image)
    uuid = "da2c6f8c-ebc4-4a09-aa57-8a004eca318f"
    id = "Quinoa_2_Docker_Alpine"
    name = "Alpine"
    description = "Build on Alpine and push to quinoacomputing/quinoa:alpine@hub.docker.com"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-alpine")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "alpine")
        param("workdir", "tools/docker")
    }
})
