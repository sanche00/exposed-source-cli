package com.ssg.ic.sp.exposed.cli

import kotlinx.cli.*
fun main(args: Array<String>) {

    val parser = ArgParser("${App.appName}:: ${App.version}")
    val version by parser.option(ArgType.Boolean, shortName = "V", description = "Version").default(false)

    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()

    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")

    val dao by parser.option(ArgType.Boolean, shortName = "d", description = "entity 추가").default(false)

    // Add all input to parser
    parser.parse(args)

    if(version) println(App.version)
}
object App {
    const val appName = "Exposed Code Generate CLI App"
    const val version = "0.0.1"
}