package com.dqqdo.doplugin

import org.gradle.api.Plugin
import org.gradle.api.Project


public class DoMonitorPlugin implements Plugin<Project> {

    void apply(Project target) {


//        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
//        int p = nameOfRunningVM.indexOf('@');
//        String pid = nameOfRunningVM.substring(0, p);
        println "description  --- " + target.description
        println "name  --- " + target.name
        println "displayName  --- " + target.displayName
        println "depth  --- " + target.depth
        println "path  --- " + target.path
        println "getRootDir  --- " + target.getRootDir()
//        println "nameOfRunningVM  --- " + nameOfRunningVM

    }
}