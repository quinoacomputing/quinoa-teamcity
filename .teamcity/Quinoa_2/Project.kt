package Quinoa_2

import Quinoa_2.vcsRoots.*
import Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster
import Quinoa_2.vcsRoots.Quinoa_2_GitGithubComQuinoacomputingQuinoaTeamcityGitRefsHeadsMaster
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "e2dcb749-3b0c-44d8-ba6f-3e84b68b0fb0"
    id = "Quinoa_2"
    parentId = "_Root"
    name = "Quinoa"
    description = "Adaptive computational fluid dynamics"

    vcsRoot(Quinoa_2_GitGithubComQuinoacomputingQuinoaGitRefsHeadsMaster)
    vcsRoot(Quinoa_2_GitGithubComQuinoacomputingQuinoaTeamcityGitRefsHeadsMaster)
    vcsRoot(Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster)

    features {
        versionedSettings {
            id = "PROJECT_EXT_5"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.PREFER_SETTINGS_FROM_VCS
            rootExtId = Quinoa_2_GitGithubComQuinoacomputingQuinoaTeamcityGitRefsHeadsMaster.id
            showChanges = false
            settingsFormat = VersionedSettings.Format.KOTLIN
        }
    }
})
