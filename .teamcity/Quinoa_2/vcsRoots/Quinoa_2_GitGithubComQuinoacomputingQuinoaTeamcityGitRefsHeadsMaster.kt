package Quinoa_2.vcsRoots

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.vcs.GitVcsRoot

object Quinoa_2_GitGithubComQuinoacomputingQuinoaTeamcityGitRefsHeadsMaster : GitVcsRoot({
    uuid = "39847b99-5323-4453-aa17-eaed879142a6"
    extId = "Quinoa_2_GitGithubComQuinoacomputingQuinoaTeamcityGitRefsHeadsMaster"
    name = "git@github.com:quinoacomputing/quinoa-teamcity.git#refs/heads/master"
    url = "git@github.com:quinoacomputing/quinoa-teamcity.git"
    authMethod = defaultPrivateKey {
        userName = "git"
    }
})
