package Quinoa_2.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster : GitVcsRoot({
    uuid = "791b352b-6878-4566-9401-eb9b1a2d2349"
    id = "Quinoa_2_GitGithubComQuinoacomputingQuinoacomputingGithubIoGitRefsHeadsMaster"
    name = "git@github.com:quinoacomputing/quinoacomputing.github.io.git#refs/heads/master"
    url = "git@github.com:quinoacomputing/quinoacomputing.github.io.git"
    checkoutSubmodules = GitVcsRoot.CheckoutSubmodules.IGNORE
    authMethod = defaultPrivateKey {
    }
})
