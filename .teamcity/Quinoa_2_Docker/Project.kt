package Quinoa_2_Docker

import Quinoa_2_Docker.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "79eec645-d3ee-40df-8bb5-85abc2b6d8f0"
    id = "Quinoa_2_Docker"
    parentId = "Quinoa_2"
    name = "Docker"
    description = "Docker images"

    buildType(Quinoa_2_Docker_Debian)
    buildType(Quinoa_2_Docker_DebianUnittest)
    buildType(Quinoa_2_Docker_DebianWalker)
    buildType(Quinoa_2_Docker_DebianInciter)
    buildType(Quinoa_2_Docker_DebianMeshConv)
    buildType(Quinoa_2_Docker_DebianFileConv)
    buildType(Quinoa_2_Docker_DebianRNGTest)
    buildType(Quinoa_2_Docker_Alpine)

    template(Quinoa_2_Docker_ImageForSingleExecutable)
    template(Quinoa_2_Docker_Image)
})
