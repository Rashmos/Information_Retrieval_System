/*      */ package org.tartarus.snowball.ext;
/*      */ 
/*      */ import org.tartarus.snowball.Among;
/*      */ import org.tartarus.snowball.SnowballProgram;
/*      */ 
/*      */ public class CatalanStemmer extends SnowballProgram
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   17 */   private static final CatalanStemmer methodObject = new CatalanStemmer();
/*      */ 
/*   19 */   private static final Among[] a_0 = { new Among("", -1, 13, "", methodObject), new Among("·", 0, 12, "", methodObject), new Among("à", 0, 2, "", methodObject), new Among("á", 0, 1, "", methodObject), new Among("è", 0, 4, "", methodObject), new Among("é", 0, 3, "", methodObject), new Among("ì", 0, 6, "", methodObject), new Among("í", 0, 5, "", methodObject), new Among("ï", 0, 11, "", methodObject), new Among("ò", 0, 8, "", methodObject), new Among("ó", 0, 7, "", methodObject), new Among("ú", 0, 9, "", methodObject), new Among("ü", 0, 10, "", methodObject) };
/*      */ 
/*   35 */   private static final Among[] a_1 = { new Among("la", -1, 1, "", methodObject), new Among("-la", 0, 1, "", methodObject), new Among("sela", 0, 1, "", methodObject), new Among("le", -1, 1, "", methodObject), new Among("me", -1, 1, "", methodObject), new Among("-me", 4, 1, "", methodObject), new Among("se", -1, 1, "", methodObject), new Among("-te", -1, 1, "", methodObject), new Among("hi", -1, 1, "", methodObject), new Among("'hi", 8, 1, "", methodObject), new Among("li", -1, 1, "", methodObject), new Among("-li", 10, 1, "", methodObject), new Among("'l", -1, 1, "", methodObject), new Among("'m", -1, 1, "", methodObject), new Among("-m", -1, 1, "", methodObject), new Among("'n", -1, 1, "", methodObject), new Among("-n", -1, 1, "", methodObject), new Among("ho", -1, 1, "", methodObject), new Among("'ho", 17, 1, "", methodObject), new Among("lo", -1, 1, "", methodObject), new Among("selo", 19, 1, "", methodObject), new Among("'s", -1, 1, "", methodObject), new Among("las", -1, 1, "", methodObject), new Among("selas", 22, 1, "", methodObject), new Among("les", -1, 1, "", methodObject), new Among("-les", 24, 1, "", methodObject), new Among("'ls", -1, 1, "", methodObject), new Among("-ls", -1, 1, "", methodObject), new Among("'ns", -1, 1, "", methodObject), new Among("-ns", -1, 1, "", methodObject), new Among("ens", -1, 1, "", methodObject), new Among("los", -1, 1, "", methodObject), new Among("selos", 31, 1, "", methodObject), new Among("nos", -1, 1, "", methodObject), new Among("-nos", 33, 1, "", methodObject), new Among("vos", -1, 1, "", methodObject), new Among("us", -1, 1, "", methodObject), new Among("-us", 36, 1, "", methodObject), new Among("'t", -1, 1, "", methodObject) };
/*      */ 
/*   77 */   private static final Among[] a_2 = { new Among("ica", -1, 4, "", methodObject), new Among("lógica", 0, 3, "", methodObject), new Among("enca", -1, 1, "", methodObject), new Among("ada", -1, 2, "", methodObject), new Among("ancia", -1, 1, "", methodObject), new Among("encia", -1, 1, "", methodObject), new Among("ència", -1, 1, "", methodObject), new Among("ícia", -1, 1, "", methodObject), new Among("logia", -1, 3, "", methodObject), new Among("inia", -1, 1, "", methodObject), new Among("íinia", 9, 1, "", methodObject), new Among("eria", -1, 1, "", methodObject), new Among("ària", -1, 1, "", methodObject), new Among("atòria", -1, 1, "", methodObject), new Among("alla", -1, 1, "", methodObject), new Among("ella", -1, 1, "", methodObject), new Among("ívola", -1, 1, "", methodObject), new Among("ima", -1, 1, "", methodObject), new Among("íssima", 17, 1, "", methodObject), new Among("quíssima", 18, 5, "", methodObject), new Among("ana", -1, 1, "", methodObject), new Among("ina", -1, 1, "", methodObject), new Among("era", -1, 1, "", methodObject), new Among("sfera", 22, 1, "", methodObject), new Among("ora", -1, 1, "", methodObject), new Among("dora", 24, 1, "", methodObject), new Among("adora", 25, 1, "", methodObject), new Among("adura", -1, 1, "", methodObject), new Among("esa", -1, 1, "", methodObject), new Among("osa", -1, 1, "", methodObject), new Among("assa", -1, 1, "", methodObject), new Among("essa", -1, 1, "", methodObject), new Among("issa", -1, 1, "", methodObject), new Among("eta", -1, 1, "", methodObject), new Among("ita", -1, 1, "", methodObject), new Among("ota", -1, 1, "", methodObject), new Among("ista", -1, 1, "", methodObject), new Among("ialista", 36, 1, "", methodObject), new Among("ionista", 36, 1, "", methodObject), new Among("iva", -1, 1, "", methodObject), new Among("ativa", 39, 1, "", methodObject), new Among("nça", -1, 1, "", methodObject), new Among("logía", -1, 3, "", methodObject), new Among("ic", -1, 4, "", methodObject), new Among("ístic", 43, 1, "", methodObject), new Among("enc", -1, 1, "", methodObject), new Among("esc", -1, 1, "", methodObject), new Among("ud", -1, 1, "", methodObject), new Among("atge", -1, 1, "", methodObject), new Among("ble", -1, 1, "", methodObject), new Among("able", 49, 1, "", methodObject), new Among("ible", 49, 1, "", methodObject), new Among("isme", -1, 1, "", methodObject), new Among("ialisme", 52, 1, "", methodObject), new Among("ionisme", 52, 1, "", methodObject), new Among("ivisme", 52, 1, "", methodObject), new Among("aire", -1, 1, "", methodObject), new Among("icte", -1, 1, "", methodObject), new Among("iste", -1, 1, "", methodObject), new Among("ici", -1, 1, "", methodObject), new Among("íci", -1, 1, "", methodObject), new Among("logi", -1, 3, "", methodObject), new Among("ari", -1, 1, "", methodObject), new Among("tori", -1, 1, "", methodObject), new Among("al", -1, 1, "", methodObject), new Among("il", -1, 1, "", methodObject), new Among("all", -1, 1, "", methodObject), new Among("ell", -1, 1, "", methodObject), new Among("ívol", -1, 1, "", methodObject), new Among("isam", -1, 1, "", methodObject), new Among("issem", -1, 1, "", methodObject), new Among("ìssem", -1, 1, "", methodObject), new Among("íssem", -1, 1, "", methodObject), new Among("íssim", -1, 1, "", methodObject), new Among("quíssim", 73, 5, "", methodObject), new Among("amen", -1, 1, "", methodObject), new Among("ìssin", -1, 1, "", methodObject), new Among("ar", -1, 1, "", methodObject), new Among("ificar", 77, 1, "", methodObject), new Among("egar", 77, 1, "", methodObject), new Among("ejar", 77, 1, "", methodObject), new Among("itar", 77, 1, "", methodObject), new Among("itzar", 77, 1, "", methodObject), new Among("fer", -1, 1, "", methodObject), new Among("or", -1, 1, "", methodObject), new Among("dor", 84, 1, "", methodObject), new Among("dur", -1, 1, "", methodObject), new Among("doras", -1, 1, "", methodObject), new Among("ics", -1, 4, "", methodObject), new Among("lógics", 88, 3, "", methodObject), new Among("uds", -1, 1, "", methodObject), new Among("nces", -1, 1, "", methodObject), new Among("ades", -1, 2, "", methodObject), new Among("ancies", -1, 1, "", methodObject), new Among("encies", -1, 1, "", methodObject), new Among("ències", -1, 1, "", methodObject), new Among("ícies", -1, 1, "", methodObject), new Among("logies", -1, 3, "", methodObject), new Among("inies", -1, 1, "", methodObject), new Among("ínies", -1, 1, "", methodObject), new Among("eries", -1, 1, "", methodObject), new Among("àries", -1, 1, "", methodObject), new Among("atòries", -1, 1, "", methodObject), new Among("bles", -1, 1, "", methodObject), new Among("ables", 103, 1, "", methodObject), new Among("ibles", 103, 1, "", methodObject), new Among("imes", -1, 1, "", methodObject), new Among("íssimes", 106, 1, "", methodObject), new Among("quíssimes", 107, 5, "", methodObject), new Among("formes", -1, 1, "", methodObject), new Among("ismes", -1, 1, "", methodObject), new Among("ialismes", 110, 1, "", methodObject), new Among("ines", -1, 1, "", methodObject), new Among("eres", -1, 1, "", methodObject), new Among("ores", -1, 1, "", methodObject), new Among("dores", 114, 1, "", methodObject), new Among("idores", 115, 1, "", methodObject), new Among("dures", -1, 1, "", methodObject), new Among("eses", -1, 1, "", methodObject), new Among("oses", -1, 1, "", methodObject), new Among("asses", -1, 1, "", methodObject), new Among("ictes", -1, 1, "", methodObject), new Among("ites", -1, 1, "", methodObject), new Among("otes", -1, 1, "", methodObject), new Among("istes", -1, 1, "", methodObject), new Among("ialistes", 124, 1, "", methodObject), new Among("ionistes", 124, 1, "", methodObject), new Among("iques", -1, 4, "", methodObject), new Among("lógiques", 127, 3, "", methodObject), new Among("ives", -1, 1, "", methodObject), new Among("atives", 129, 1, "", methodObject), new Among("logíes", -1, 3, "", methodObject), new Among("allengües", -1, 1, "", methodObject), new Among("icis", -1, 1, "", methodObject), new Among("ícis", -1, 1, "", methodObject), new Among("logis", -1, 3, "", methodObject), new Among("aris", -1, 1, "", methodObject), new Among("toris", -1, 1, "", methodObject), new Among("ls", -1, 1, "", methodObject), new Among("als", 138, 1, "", methodObject), new Among("ells", 138, 1, "", methodObject), new Among("ims", -1, 1, "", methodObject), new Among("íssims", 141, 1, "", methodObject), new Among("quíssims", 142, 5, "", methodObject), new Among("ions", -1, 1, "", methodObject), new Among("cions", 144, 1, "", methodObject), new Among("acions", 145, 2, "", methodObject), new Among("esos", -1, 1, "", methodObject), new Among("osos", -1, 1, "", methodObject), new Among("assos", -1, 1, "", methodObject), new Among("issos", -1, 1, "", methodObject), new Among("ers", -1, 1, "", methodObject), new Among("ors", -1, 1, "", methodObject), new Among("dors", 152, 1, "", methodObject), new Among("adors", 153, 1, "", methodObject), new Among("idors", 153, 1, "", methodObject), new Among("ats", -1, 1, "", methodObject), new Among("itats", 156, 1, "", methodObject), new Among("bilitats", 157, 1, "", methodObject), new Among("ivitats", 157, 1, "", methodObject), new Among("ativitats", 159, 1, "", methodObject), new Among("ïtats", 156, 1, "", methodObject), new Among("ets", -1, 1, "", methodObject), new Among("ants", -1, 1, "", methodObject), new Among("ents", -1, 1, "", methodObject), new Among("ments", 164, 1, "", methodObject), new Among("aments", 165, 1, "", methodObject), new Among("ots", -1, 1, "", methodObject), new Among("uts", -1, 1, "", methodObject), new Among("ius", -1, 1, "", methodObject), new Among("trius", 169, 1, "", methodObject), new Among("atius", 169, 1, "", methodObject), new Among("ès", -1, 1, "", methodObject), new Among("és", -1, 1, "", methodObject), new Among("ís", -1, 1, "", methodObject), new Among("dís", 174, 1, "", methodObject), new Among("ós", -1, 1, "", methodObject), new Among("itat", -1, 1, "", methodObject), new Among("bilitat", 177, 1, "", methodObject), new Among("ivitat", 177, 1, "", methodObject), new Among("ativitat", 179, 1, "", methodObject), new Among("ïtat", -1, 1, "", methodObject), new Among("et", -1, 1, "", methodObject), new Among("ant", -1, 1, "", methodObject), new Among("ent", -1, 1, "", methodObject), new Among("ient", 184, 1, "", methodObject), new Among("ment", 184, 1, "", methodObject), new Among("ament", 186, 1, "", methodObject), new Among("isament", 187, 1, "", methodObject), new Among("ot", -1, 1, "", methodObject), new Among("isseu", -1, 1, "", methodObject), new Among("ìsseu", -1, 1, "", methodObject), new Among("ísseu", -1, 1, "", methodObject), new Among("triu", -1, 1, "", methodObject), new Among("íssiu", -1, 1, "", methodObject), new Among("atiu", -1, 1, "", methodObject), new Among("ó", -1, 1, "", methodObject), new Among("ió", 196, 1, "", methodObject), new Among("ció", 197, 1, "", methodObject), new Among("ació", 198, 1, "", methodObject) };
/*      */ 
/*  280 */   private static final Among[] a_3 = { new Among("aba", -1, 1, "", methodObject), new Among("esca", -1, 1, "", methodObject), new Among("isca", -1, 1, "", methodObject), new Among("ïsca", -1, 1, "", methodObject), new Among("ada", -1, 1, "", methodObject), new Among("ida", -1, 1, "", methodObject), new Among("uda", -1, 1, "", methodObject), new Among("ïda", -1, 1, "", methodObject), new Among("ia", -1, 1, "", methodObject), new Among("aria", 8, 1, "", methodObject), new Among("iria", 8, 1, "", methodObject), new Among("ara", -1, 1, "", methodObject), new Among("iera", -1, 1, "", methodObject), new Among("ira", -1, 1, "", methodObject), new Among("adora", -1, 1, "", methodObject), new Among("ïra", -1, 1, "", methodObject), new Among("ava", -1, 1, "", methodObject), new Among("ixa", -1, 1, "", methodObject), new Among("itza", -1, 1, "", methodObject), new Among("ía", -1, 1, "", methodObject), new Among("aría", 19, 1, "", methodObject), new Among("ería", 19, 1, "", methodObject), new Among("iría", 19, 1, "", methodObject), new Among("ïa", -1, 1, "", methodObject), new Among("isc", -1, 1, "", methodObject), new Among("ïsc", -1, 1, "", methodObject), new Among("ad", -1, 1, "", methodObject), new Among("ed", -1, 1, "", methodObject), new Among("id", -1, 1, "", methodObject), new Among("ie", -1, 1, "", methodObject), new Among("re", -1, 1, "", methodObject), new Among("dre", 30, 1, "", methodObject), new Among("ase", -1, 1, "", methodObject), new Among("iese", -1, 1, "", methodObject), new Among("aste", -1, 1, "", methodObject), new Among("iste", -1, 1, "", methodObject), new Among("ii", -1, 1, "", methodObject), new Among("ini", -1, 1, "", methodObject), new Among("esqui", -1, 1, "", methodObject), new Among("eixi", -1, 1, "", methodObject), new Among("itzi", -1, 1, "", methodObject), new Among("am", -1, 1, "", methodObject), new Among("em", -1, 1, "", methodObject), new Among("arem", 42, 1, "", methodObject), new Among("irem", 42, 1, "", methodObject), new Among("àrem", 42, 1, "", methodObject), new Among("írem", 42, 1, "", methodObject), new Among("àssem", 42, 1, "", methodObject), new Among("éssem", 42, 1, "", methodObject), new Among("iguem", 42, 1, "", methodObject), new Among("ïguem", 42, 1, "", methodObject), new Among("avem", 42, 1, "", methodObject), new Among("àvem", 42, 1, "", methodObject), new Among("ávem", 42, 1, "", methodObject), new Among("irìem", 42, 1, "", methodObject), new Among("íem", 42, 1, "", methodObject), new Among("aríem", 55, 1, "", methodObject), new Among("iríem", 55, 1, "", methodObject), new Among("assim", -1, 1, "", methodObject), new Among("essim", -1, 1, "", methodObject), new Among("issim", -1, 1, "", methodObject), new Among("àssim", -1, 1, "", methodObject), new Among("èssim", -1, 1, "", methodObject), new Among("éssim", -1, 1, "", methodObject), new Among("íssim", -1, 1, "", methodObject), new Among("ïm", -1, 1, "", methodObject), new Among("an", -1, 1, "", methodObject), new Among("aban", 66, 1, "", methodObject), new Among("arian", 66, 1, "", methodObject), new Among("aran", 66, 1, "", methodObject), new Among("ieran", 66, 1, "", methodObject), new Among("iran", 66, 1, "", methodObject), new Among("ían", 66, 1, "", methodObject), new Among("arían", 72, 1, "", methodObject), new Among("erían", 72, 1, "", methodObject), new Among("irían", 72, 1, "", methodObject), new Among("en", -1, 1, "", methodObject), new Among("ien", 76, 1, "", methodObject), new Among("arien", 77, 1, "", methodObject), new Among("irien", 77, 1, "", methodObject), new Among("aren", 76, 1, "", methodObject), new Among("eren", 76, 1, "", methodObject), new Among("iren", 76, 1, "", methodObject), new Among("àren", 76, 1, "", methodObject), new Among("ïren", 76, 1, "", methodObject), new Among("asen", 76, 1, "", methodObject), new Among("iesen", 76, 1, "", methodObject), new Among("assen", 76, 1, "", methodObject), new Among("essen", 76, 1, "", methodObject), new Among("issen", 76, 1, "", methodObject), new Among("éssen", 76, 1, "", methodObject), new Among("ïssen", 76, 1, "", methodObject), new Among("esquen", 76, 1, "", methodObject), new Among("isquen", 76, 1, "", methodObject), new Among("ïsquen", 76, 1, "", methodObject), new Among("aven", 76, 1, "", methodObject), new Among("ixen", 76, 1, "", methodObject), new Among("eixen", 96, 1, "", methodObject), new Among("ïxen", 76, 1, "", methodObject), new Among("ïen", 76, 1, "", methodObject), new Among("in", -1, 1, "", methodObject), new Among("inin", 100, 1, "", methodObject), new Among("sin", 100, 1, "", methodObject), new Among("isin", 102, 1, "", methodObject), new Among("assin", 102, 1, "", methodObject), new Among("essin", 102, 1, "", methodObject), new Among("issin", 102, 1, "", methodObject), new Among("ïssin", 102, 1, "", methodObject), new Among("esquin", 100, 1, "", methodObject), new Among("eixin", 100, 1, "", methodObject), new Among("aron", -1, 1, "", methodObject), new Among("ieron", -1, 1, "", methodObject), new Among("arán", -1, 1, "", methodObject), new Among("erán", -1, 1, "", methodObject), new Among("irán", -1, 1, "", methodObject), new Among("iïn", -1, 1, "", methodObject), new Among("ado", -1, 1, "", methodObject), new Among("ido", -1, 1, "", methodObject), new Among("ando", -1, 2, "", methodObject), new Among("iendo", -1, 1, "", methodObject), new Among("io", -1, 1, "", methodObject), new Among("ixo", -1, 1, "", methodObject), new Among("eixo", 121, 1, "", methodObject), new Among("ïxo", -1, 1, "", methodObject), new Among("itzo", -1, 1, "", methodObject), new Among("ar", -1, 1, "", methodObject), new Among("tzar", 125, 1, "", methodObject), new Among("er", -1, 1, "", methodObject), new Among("eixer", 127, 1, "", methodObject), new Among("ir", -1, 1, "", methodObject), new Among("ador", -1, 1, "", methodObject), new Among("as", -1, 1, "", methodObject), new Among("abas", 131, 1, "", methodObject), new Among("adas", 131, 1, "", methodObject), new Among("idas", 131, 1, "", methodObject), new Among("aras", 131, 1, "", methodObject), new Among("ieras", 131, 1, "", methodObject), new Among("ías", 131, 1, "", methodObject), new Among("arías", 137, 1, "", methodObject), new Among("erías", 137, 1, "", methodObject), new Among("irías", 137, 1, "", methodObject), new Among("ids", -1, 1, "", methodObject), new Among("es", -1, 1, "", methodObject), new Among("ades", 142, 1, "", methodObject), new Among("ides", 142, 1, "", methodObject), new Among("udes", 142, 1, "", methodObject), new Among("ïdes", 142, 1, "", methodObject), new Among("atges", 142, 1, "", methodObject), new Among("ies", 142, 1, "", methodObject), new Among("aries", 148, 1, "", methodObject), new Among("iries", 148, 1, "", methodObject), new Among("ares", 142, 1, "", methodObject), new Among("ires", 142, 1, "", methodObject), new Among("adores", 142, 1, "", methodObject), new Among("ïres", 142, 1, "", methodObject), new Among("ases", 142, 1, "", methodObject), new Among("ieses", 142, 1, "", methodObject), new Among("asses", 142, 1, "", methodObject), new Among("esses", 142, 1, "", methodObject), new Among("isses", 142, 1, "", methodObject), new Among("ïsses", 142, 1, "", methodObject), new Among("ques", 142, 1, "", methodObject), new Among("esques", 161, 1, "", methodObject), new Among("ïsques", 161, 1, "", methodObject), new Among("aves", 142, 1, "", methodObject), new Among("ixes", 142, 1, "", methodObject), new Among("eixes", 165, 1, "", methodObject), new Among("ïxes", 142, 1, "", methodObject), new Among("ïes", 142, 1, "", methodObject), new Among("abais", -1, 1, "", methodObject), new Among("arais", -1, 1, "", methodObject), new Among("ierais", -1, 1, "", methodObject), new Among("íais", -1, 1, "", methodObject), new Among("aríais", 172, 1, "", methodObject), new Among("eríais", 172, 1, "", methodObject), new Among("iríais", 172, 1, "", methodObject), new Among("aseis", -1, 1, "", methodObject), new Among("ieseis", -1, 1, "", methodObject), new Among("asteis", -1, 1, "", methodObject), new Among("isteis", -1, 1, "", methodObject), new Among("inis", -1, 1, "", methodObject), new Among("sis", -1, 1, "", methodObject), new Among("isis", 181, 1, "", methodObject), new Among("assis", 181, 1, "", methodObject), new Among("essis", 181, 1, "", methodObject), new Among("issis", 181, 1, "", methodObject), new Among("ïssis", 181, 1, "", methodObject), new Among("esquis", -1, 1, "", methodObject), new Among("eixis", -1, 1, "", methodObject), new Among("itzis", -1, 1, "", methodObject), new Among("áis", -1, 1, "", methodObject), new Among("aréis", -1, 1, "", methodObject), new Among("eréis", -1, 1, "", methodObject), new Among("iréis", -1, 1, "", methodObject), new Among("ams", -1, 1, "", methodObject), new Among("ados", -1, 1, "", methodObject), new Among("idos", -1, 1, "", methodObject), new Among("amos", -1, 1, "", methodObject), new Among("ábamos", 197, 1, "", methodObject), new Among("áramos", 197, 1, "", methodObject), new Among("iéramos", 197, 1, "", methodObject), new Among("íamos", 197, 1, "", methodObject), new Among("aríamos", 201, 1, "", methodObject), new Among("eríamos", 201, 1, "", methodObject), new Among("iríamos", 201, 1, "", methodObject), new Among("aremos", -1, 1, "", methodObject), new Among("eremos", -1, 1, "", methodObject), new Among("iremos", -1, 1, "", methodObject), new Among("ásemos", -1, 1, "", methodObject), new Among("iésemos", -1, 1, "", methodObject), new Among("imos", -1, 1, "", methodObject), new Among("adors", -1, 1, "", methodObject), new Among("ass", -1, 1, "", methodObject), new Among("erass", 212, 1, "", methodObject), new Among("ess", -1, 1, "", methodObject), new Among("ats", -1, 1, "", methodObject), new Among("its", -1, 1, "", methodObject), new Among("ents", -1, 1, "", methodObject), new Among("às", -1, 1, "", methodObject), new Among("aràs", 218, 1, "", methodObject), new Among("iràs", 218, 1, "", methodObject), new Among("arás", -1, 1, "", methodObject), new Among("erás", -1, 1, "", methodObject), new Among("irás", -1, 1, "", methodObject), new Among("és", -1, 1, "", methodObject), new Among("arés", 224, 1, "", methodObject), new Among("ís", -1, 1, "", methodObject), new Among("iïs", -1, 1, "", methodObject), new Among("at", -1, 1, "", methodObject), new Among("it", -1, 1, "", methodObject), new Among("ant", -1, 1, "", methodObject), new Among("ent", -1, 1, "", methodObject), new Among("int", -1, 1, "", methodObject), new Among("ut", -1, 1, "", methodObject), new Among("ït", -1, 1, "", methodObject), new Among("au", -1, 1, "", methodObject), new Among("erau", 235, 1, "", methodObject), new Among("ieu", -1, 1, "", methodObject), new Among("ineu", -1, 1, "", methodObject), new Among("areu", -1, 1, "", methodObject), new Among("ireu", -1, 1, "", methodObject), new Among("àreu", -1, 1, "", methodObject), new Among("íreu", -1, 1, "", methodObject), new Among("asseu", -1, 1, "", methodObject), new Among("esseu", -1, 1, "", methodObject), new Among("eresseu", 244, 1, "", methodObject), new Among("àsseu", -1, 1, "", methodObject), new Among("ésseu", -1, 1, "", methodObject), new Among("igueu", -1, 1, "", methodObject), new Among("ïgueu", -1, 1, "", methodObject), new Among("àveu", -1, 1, "", methodObject), new Among("áveu", -1, 1, "", methodObject), new Among("itzeu", -1, 1, "", methodObject), new Among("ìeu", -1, 1, "", methodObject), new Among("irìeu", 253, 1, "", methodObject), new Among("íeu", -1, 1, "", methodObject), new Among("aríeu", 255, 1, "", methodObject), new Among("iríeu", 255, 1, "", methodObject), new Among("assiu", -1, 1, "", methodObject), new Among("issiu", -1, 1, "", methodObject), new Among("àssiu", -1, 1, "", methodObject), new Among("èssiu", -1, 1, "", methodObject), new Among("éssiu", -1, 1, "", methodObject), new Among("íssiu", -1, 1, "", methodObject), new Among("ïu", -1, 1, "", methodObject), new Among("ix", -1, 1, "", methodObject), new Among("eix", 265, 1, "", methodObject), new Among("ïx", -1, 1, "", methodObject), new Among("itz", -1, 1, "", methodObject), new Among("ià", -1, 1, "", methodObject), new Among("arà", -1, 1, "", methodObject), new Among("irà", -1, 1, "", methodObject), new Among("itzà", -1, 1, "", methodObject), new Among("ará", -1, 1, "", methodObject), new Among("erá", -1, 1, "", methodObject), new Among("irá", -1, 1, "", methodObject), new Among("irè", -1, 1, "", methodObject), new Among("aré", -1, 1, "", methodObject), new Among("eré", -1, 1, "", methodObject), new Among("iré", -1, 1, "", methodObject), new Among("í", -1, 1, "", methodObject), new Among("iï", -1, 1, "", methodObject), new Among("ió", -1, 1, "", methodObject) };
/*      */ 
/*  566 */   private static final Among[] a_4 = { new Among("a", -1, 1, "", methodObject), new Among("e", -1, 1, "", methodObject), new Among("i", -1, 1, "", methodObject), new Among("ïn", -1, 1, "", methodObject), new Among("o", -1, 1, "", methodObject), new Among("ir", -1, 1, "", methodObject), new Among("s", -1, 1, "", methodObject), new Among("is", 6, 1, "", methodObject), new Among("os", 6, 1, "", methodObject), new Among("ïs", 6, 1, "", methodObject), new Among("it", -1, 1, "", methodObject), new Among("eu", -1, 1, "", methodObject), new Among("iu", -1, 1, "", methodObject), new Among("iqu", -1, 2, "", methodObject), new Among("itz", -1, 1, "", methodObject), new Among("à", -1, 1, "", methodObject), new Among("á", -1, 1, "", methodObject), new Among("é", -1, 1, "", methodObject), new Among("ì", -1, 1, "", methodObject), new Among("í", -1, 1, "", methodObject), new Among("ï", -1, 1, "", methodObject), new Among("ó", -1, 1, "", methodObject) };
/*      */ 
/*  591 */   private static final char[] g_v = { '\021', 'A', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '', '', 'Q', '\006', '\n' };
/*      */   private int I_p2;
/*      */   private int I_p1;
/*      */ 
/*      */   private void copy_from(CatalanStemmer other)
/*      */   {
/*  597 */     this.I_p2 = other.I_p2;
/*  598 */     this.I_p1 = other.I_p1;
/*  599 */     super.copy_from(other);
/*      */   }
/*      */ 
/*      */   private boolean r_mark_regions()
/*      */   {
/*  605 */     this.I_p1 = this.limit;
/*  606 */     this.I_p2 = this.limit;
/*      */ 
/*  608 */     int v_1 = this.cursor;
/*      */ 
/*  615 */     while (!in_grouping(g_v, 97, 252))
/*      */     {
/*  621 */       if (this.cursor >= this.limit)
/*      */       {
/*      */         break label205;
/*      */       }
/*  625 */       this.cursor += 1;
/*      */     }
/*      */ 
/*  631 */     while (!out_grouping(g_v, 97, 252))
/*      */     {
/*  637 */       if (this.cursor >= this.limit)
/*      */       {
/*      */         break label205;
/*      */       }
/*  641 */       this.cursor += 1;
/*      */     }
/*      */ 
/*  644 */     this.I_p1 = this.cursor;
/*      */ 
/*  649 */     while (!in_grouping(g_v, 97, 252))
/*      */     {
/*  655 */       if (this.cursor >= this.limit)
/*      */       {
/*      */         break label205;
/*      */       }
/*  659 */       this.cursor += 1;
/*      */     }
/*      */ 
/*  665 */     while (!out_grouping(g_v, 97, 252))
/*      */     {
/*  671 */       if (this.cursor >= this.limit)
/*      */       {
/*      */         break label205;
/*      */       }
/*  675 */       this.cursor += 1;
/*      */     }
/*      */ 
/*  678 */     this.I_p2 = this.cursor;
/*      */ 
/*  680 */     label205: this.cursor = v_1;
/*  681 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_cleaning()
/*      */   {
/*      */     int v_1;
/*      */     while (true)
/*      */     {
/*  690 */       v_1 = this.cursor;
/*      */ 
/*  694 */       this.bra = this.cursor;
/*      */ 
/*  696 */       int among_var = find_among(a_0, 13);
/*  697 */       if (among_var == 0)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  702 */       this.ket = this.cursor;
/*  703 */       switch (among_var) {
/*      */       case 0:
/*  705 */         break;
/*      */       case 1:
/*  709 */         slice_from("a");
/*  710 */         break;
/*      */       case 2:
/*  714 */         slice_from("a");
/*  715 */         break;
/*      */       case 3:
/*  719 */         slice_from("e");
/*  720 */         break;
/*      */       case 4:
/*  724 */         slice_from("e");
/*  725 */         break;
/*      */       case 5:
/*  729 */         slice_from("i");
/*  730 */         break;
/*      */       case 6:
/*  734 */         slice_from("i");
/*  735 */         break;
/*      */       case 7:
/*  739 */         slice_from("o");
/*  740 */         break;
/*      */       case 8:
/*  744 */         slice_from("o");
/*  745 */         break;
/*      */       case 9:
/*  749 */         slice_from("u");
/*  750 */         break;
/*      */       case 10:
/*  754 */         slice_from("u");
/*  755 */         break;
/*      */       case 11:
/*  759 */         slice_from("i");
/*  760 */         break;
/*      */       case 12:
/*  764 */         slice_from(".");
/*  765 */         break;
/*      */       case 13:
/*  769 */         if (this.cursor >= this.limit)
/*      */         {
/*      */           break label246;
/*      */         }
/*  773 */         this.cursor += 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  778 */     label246: this.cursor = v_1;
/*      */ 
/*  781 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_R1() {
/*  785 */     if (this.I_p1 > this.cursor)
/*      */     {
/*  787 */       return false;
/*      */     }
/*  789 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_R2() {
/*  793 */     if (this.I_p2 > this.cursor)
/*      */     {
/*  795 */       return false;
/*      */     }
/*  797 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_attached_pronoun()
/*      */   {
/*  804 */     this.ket = this.cursor;
/*      */ 
/*  806 */     int among_var = find_among_b(a_1, 39);
/*  807 */     if (among_var == 0)
/*      */     {
/*  809 */       return false;
/*      */     }
/*      */ 
/*  812 */     this.bra = this.cursor;
/*  813 */     switch (among_var) {
/*      */     case 0:
/*  815 */       return false;
/*      */     case 1:
/*  819 */       if (!r_R1())
/*      */       {
/*  821 */         return false;
/*      */       }
/*      */ 
/*  824 */       slice_del();
/*      */     }
/*      */ 
/*  827 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_standard_suffix()
/*      */   {
/*  834 */     this.ket = this.cursor;
/*      */ 
/*  836 */     int among_var = find_among_b(a_2, 200);
/*  837 */     if (among_var == 0)
/*      */     {
/*  839 */       return false;
/*      */     }
/*      */ 
/*  842 */     this.bra = this.cursor;
/*  843 */     switch (among_var) {
/*      */     case 0:
/*  845 */       return false;
/*      */     case 1:
/*  849 */       if (!r_R1())
/*      */       {
/*  851 */         return false;
/*      */       }
/*      */ 
/*  854 */       slice_del();
/*  855 */       break;
/*      */     case 2:
/*  859 */       if (!r_R2())
/*      */       {
/*  861 */         return false;
/*      */       }
/*      */ 
/*  864 */       slice_del();
/*  865 */       break;
/*      */     case 3:
/*  869 */       if (!r_R2())
/*      */       {
/*  871 */         return false;
/*      */       }
/*      */ 
/*  874 */       slice_from("log");
/*  875 */       break;
/*      */     case 4:
/*  879 */       if (!r_R2())
/*      */       {
/*  881 */         return false;
/*      */       }
/*      */ 
/*  884 */       slice_from("ic");
/*  885 */       break;
/*      */     case 5:
/*  889 */       if (!r_R1())
/*      */       {
/*  891 */         return false;
/*      */       }
/*      */ 
/*  894 */       slice_from("c");
/*      */     }
/*      */ 
/*  897 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_verb_suffix()
/*      */   {
/*  904 */     this.ket = this.cursor;
/*      */ 
/*  906 */     int among_var = find_among_b(a_3, 283);
/*  907 */     if (among_var == 0)
/*      */     {
/*  909 */       return false;
/*      */     }
/*      */ 
/*  912 */     this.bra = this.cursor;
/*  913 */     switch (among_var) {
/*      */     case 0:
/*  915 */       return false;
/*      */     case 1:
/*  919 */       if (!r_R1())
/*      */       {
/*  921 */         return false;
/*      */       }
/*      */ 
/*  924 */       slice_del();
/*  925 */       break;
/*      */     case 2:
/*  929 */       if (!r_R2())
/*      */       {
/*  931 */         return false;
/*      */       }
/*      */ 
/*  934 */       slice_del();
/*      */     }
/*      */ 
/*  937 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean r_residual_suffix()
/*      */   {
/*  944 */     this.ket = this.cursor;
/*      */ 
/*  946 */     int among_var = find_among_b(a_4, 22);
/*  947 */     if (among_var == 0)
/*      */     {
/*  949 */       return false;
/*      */     }
/*      */ 
/*  952 */     this.bra = this.cursor;
/*  953 */     switch (among_var) {
/*      */     case 0:
/*  955 */       return false;
/*      */     case 1:
/*  959 */       if (!r_R1())
/*      */       {
/*  961 */         return false;
/*      */       }
/*      */ 
/*  964 */       slice_del();
/*  965 */       break;
/*      */     case 2:
/*  969 */       if (!r_R1())
/*      */       {
/*  971 */         return false;
/*      */       }
/*      */ 
/*  974 */       slice_from("ic");
/*      */     }
/*      */ 
/*  977 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean stem()
/*      */   {
/*  989 */     int v_1 = this.cursor;
/*      */ 
/*  992 */     if (!r_mark_regions());
/*  997 */     this.cursor = v_1;
/*      */ 
/*  999 */     this.limit_backward = this.cursor; this.cursor = this.limit;
/*      */ 
/* 1002 */     int v_2 = this.limit - this.cursor;
/*      */ 
/* 1005 */     if (!r_attached_pronoun());
/* 1010 */     this.cursor = (this.limit - v_2);
/*      */ 
/* 1012 */     int v_3 = this.limit - this.cursor;
/*      */ 
/* 1017 */     int v_4 = this.limit - this.cursor;
/*      */ 
/* 1020 */     if (!r_standard_suffix())
/*      */     {
/* 1026 */       this.cursor = (this.limit - v_4);
/*      */ 
/* 1028 */       if (r_verb_suffix());
/*      */     }
/*      */ 
/* 1034 */     this.cursor = (this.limit - v_3);
/*      */ 
/* 1036 */     int v_5 = this.limit - this.cursor;
/*      */ 
/* 1039 */     if (!r_residual_suffix());
/* 1044 */     this.cursor = (this.limit - v_5);
/* 1045 */     this.cursor = this.limit_backward;
/* 1046 */     int v_6 = this.cursor;
/*      */ 
/* 1049 */     if (!r_cleaning());
/* 1054 */     this.cursor = v_6;
/* 1055 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object o) {
/* 1059 */     return o instanceof CatalanStemmer;
/*      */   }
/*      */ 
/*      */   public int hashCode() {
/* 1063 */     return CatalanStemmer.class.getName().hashCode();
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.tartarus.snowball.ext.CatalanStemmer
 * JD-Core Version:    0.6.2
 */