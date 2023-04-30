package test
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.create.table.CreateTable
import org.junit.jupiter.api.Test

class SQLParsingTest {

    @Test
    fun createTest() {
        val createSql = """
            CREATE TABLE public.tsh_sal_m (
            	cmny_cd varchar(16) NOT NULL,
            	sal_no varchar(14) NOT NULL,
            	sal_dt varchar(8) NOT NULL,
            	shp_cd varchar(8) NOT NULL,
            	wh_cd varchar(8) NULL,
            	sal_cls_cd varchar(8) NOT NULL,
            	sal_tp_cd varchar(8) NOT NULL,
            	cust_no varchar(20) NULL,
            	tt_csm_amt numeric(14) NOT NULL,
            	tt_sal_qty numeric(8) NOT NULL,
            	tt_sal_amt numeric(14) NOT NULL,
            	tt_edc_amt numeric(14) NOT NULL,
            	tt_rsal_amt numeric(14) NOT NULL,
            	tt_fwd_amt numeric(14) NOT NULL,
            	tt_sett_amt numeric(14) NOT NULL,
            	sale_tm varchar(2) NULL,
            	sale_crusr_id varchar(20) NULL,
            	memo varchar(1000) NULL,
            	or_sal_no varchar(14) NULL,
            	del_yn varchar(1) NOT NULL,
            	fr_reg_id varchar(20) NOT NULL,
            	fr_reg_dtm timestamp NOT NULL,
            	fr_reg_ip varchar(20) NOT NULL,
            	fnl_upd_id varchar(20) NOT NULL,
            	fnl_upd_dtm timestamp NOT NULL,
            	fnl_upd_ip varchar(20) NOT NULL,
            	CONSTRAINT tsh_sal_m_pk PRIMARY KEY (cmny_cd, sal_no),
            	CONSTRAINT r_991 FOREIGN KEY (cmny_cd, wh_cd) REFERENCES tba_wh_m(cmny_cd, wh_cd) ON DELETE SET NULL,
            	CONSTRAINT r_993 FOREIGN KEY (cmny_cd, or_sal_no) REFERENCES tsh_sal_m(cmny_cd, sal_no) ON DELETE SET NULL,
            	CONSTRAINT tsh_sal_m_fk01 FOREIGN KEY (cmny_cd, shp_cd) REFERENCES tba_shp_m(cmny_cd, shp_cd)
            )
        """.trimIndent()
        val statement = CCJSqlParserUtil.parse(createSql)
//        Assertions.assertTrue(statement is CreateTable)
//        if(statement is CreateTable) {
//            println(statement.table)
//            statement.columnDefinitions.forEach{
//                println(it.columnName)
//                println(it.colDataType.dataType)
//                println(it.colDataType.arrayData.joinToString())
//                println(it.columnSpecs.joinToString())
//            }
//            statement.indexes.forEach{
//                if(it is NamedConstraint) {
//                    println(it.type)
//                    println(it.columns.map { it.columnName }.joinToString())
//                }
//
//                if(it is ForeignKeyIndex) {
//                    println(it.type)
//                    println(it.table)
//                    println(it.columns.map { it.columnName }.joinToString())
//                    println(it.referencedColumnNames.joinToString())
//                }
//            }
//        }

        if (statement is CreateTable) {
            var modelCode = ""
            modelCode += """object ${statement.table}  : Table("${statement.table}") {"""
            statement.columnDefinitions.forEach {
                modelCode += "\n"
                modelCode += """    val ${it.columnName} =  ${it.colDataType.dataType}("${it.columnName}", ${it.colDataType.argumentsStringList?.joinToString()})"""
            }
            modelCode += "\n"
            modelCode += """}"""
            println(modelCode)
        }

    }
}