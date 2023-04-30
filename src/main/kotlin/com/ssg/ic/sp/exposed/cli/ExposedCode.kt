package com.ssg.ic.sp.exposed.cli

import com.ssg.ic.sp.toCamel
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.create.table.ColDataType
import net.sf.jsqlparser.statement.create.table.ColumnDefinition
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.create.table.Index
import org.jetbrains.exposed.sql.Table
import java.io.Reader
import java.io.Writer
import kotlin.math.log
import kotlin.reflect.full.declaredFunctions

const val DSL_CREATE_START = """object %s : Table("%s") {"""
const val DSL_COLUMN_VAL = """    val %s = %s("%s""""
const val END = """}"""

fun Writer.writeAndNewLine(str: String): Unit {
    write(str + "\n")
}

fun List<String>.isNull(): Boolean {
    return !contains("NOT") && contains("NULL")
}

class ExposedCode {

    fun createDSLCode(reader: Reader, writer: Writer, isDao: Boolean) {
        val statement = CCJSqlParserUtil.parse(reader)
        if (statement !is CreateTable) {
            throw Exception("Create 문이 아닙니다.")
        }
        createDSLCode(statement, writer, isDao)
    }

    fun createDSLCode(statement: CreateTable, writer: Writer, isDao: Boolean) {
        writer.writeAndNewLine(DSL_CREATE_START.format(statement.table.name.toCamel(), statement.table.name))
        statement.columnDefinitions.forEach { column ->
            writer.writeAndNewLine(createDSLColumn(column))
        }
        statement.indexes.forEach { index ->
            writer.writeAndNewLine(createDSLIndex(index))
        }
        writer.write(END)
        writer.flush()
    }

    private fun createDSLIndex(index: Index): String {
        when (index.type) {
            "PRIMARY KEY" -> return """    override val primaryKey = PrimaryKey(${
                index.columnsNames.map { it.toCamel() }.joinToString()
            })"""

            else -> return ""
        }
    }

    private fun createDSLColumn(column: ColumnDefinition): String {
        var str =
            DSL_COLUMN_VAL.format(column.columnName.toCamel(), getDSLDataType(column.colDataType), column.columnName)
        str += if (column.colDataType.argumentsStringList == null) {
            ")"
        } else {
            ", ${column.colDataType.argumentsStringList.joinToString()})"
        }
//        str += column.columnSpecs
        if (column.columnSpecs.isNull()) {
            str += ".nullable()"
        }
        return str;
    }

    private fun getDSLDataType(colDataType: ColDataType): String {
        if (findType(colDataType.dataType)) {
            return colDataType.dataType
        } else {
            println("not find dataType : ${colDataType.dataType}")
        }
        return colDataType.dataType
    }

    fun findType(dataType: String): Boolean {
        if (Table::class.declaredFunctions.stream().filter {
                it.name == dataType
            }.findAny().isPresent) {
            return true;
        }else if(dataType == "timestamp"){
            return true;
        }else {
            return false;
        }
    }

}