package Quinoa_2_Docker.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '95961fba-6827-45d0-af17-46b9368f76d3' (id = 'Quinoa_2_Docker_Debian')
accordingly and delete the patch script.
*/
changeBuildType("95961fba-6827-45d0-af17-46b9368f76d3") {
    expectSteps {
    }
    steps {
        insert(0) {
            script {
                name = "Clean docker database"
                id = "RUNNER_33"
                scriptContent = """
                    docker rmi -f ${'$'}(docker images -q --filter "dangling=true")
                    docker rmi -f ${'$'}(docker images | grep "<none>" | awk "{print \${'$'}3}")
                    docker rm `docker ps -a | grep Exited | awk '{print ${'$'}1 }'`
                """.trimIndent()
            }
        }
        check(stepsOrder == arrayListOf<String>()) {
            "Unexpected build steps order: $stepsOrder"
        }
        stepsOrder = arrayListOf("RUNNER_33", "RUNNER_23", "RUNNER_24", "RUNNER_30", "RUNNER_32")
    }

    triggers {
        add {
            finishBuildTrigger {
                id = "TRIGGER_3"
                buildTypeExtId = "Quinoa_2_Docker_Alpine"
                branchFilter = """
                    +:<default>
                    +:develop
                """.trimIndent()
            }
        }
    }

    dependencies {
        add("Quinoa_2_Docker_Alpine") {
            snapshot {
                onDependencyFailure = FailureAction.IGNORE
                onDependencyCancel = FailureAction.IGNORE
            }
        }

    }

    expectDisabledSettings()
    updateDisabledSettings("vcsTrigger")
}
