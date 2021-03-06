package Quinoa_2_Docker.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger

object Quinoa_2_Docker_Debian : BuildType({
    template(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_Image)
    uuid = "95961fba-6827-45d0-af17-46b9368f76d3"
    id = "Quinoa_2_Docker_Debian"
    name = "Debian"
    description = "Build on Debian and push to quinoacomputing/quinoa:debian@hub.docker.com"

    params {
        param("dockerfile", "Dockerfile.quinoa-build-debian")
        param("organization", "quinoacomputing")
        param("repository", "quinoa")
        param("tag", "debian")
        param("workdir", "tools/docker")
    }

    // triggers {
    //     finishBuildTrigger {
    //         id = "TRIGGER_3"
    //         buildTypeExtId = "Quinoa_2_Docker_Alpine"
    //         branchFilter = """
    //             +:<default>
    //             +:develop
    //         """.trimIndent()
    //     }
    // }

    // dependencies {
    //     dependency(Quinoa_2_Docker.buildTypes.Quinoa_2_Docker_Alpine) {
    //             snapshot {
    //                     onDependencyFailure = FailureAction.IGNORE
    //                     onDependencyCancel = FailureAction.IGNORE
    //             }
    //     }
    // }
})
