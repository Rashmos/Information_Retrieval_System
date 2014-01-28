/*      */ package com.aliasi.xml;
/*      */ 
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public class XHtmlWriter extends SAXWriter
/*      */ {
/*      */   public static final String A = "a";
/*      */   public static final String ABBR = "abbr";
/*      */   public static final String ACRONYM = "acronym";
/*      */   public static final String ADDRESS = "address";
/*      */   public static final String APPLET = "applet";
/*      */   public static final String AREA = "area";
/*      */   public static final String B = "b";
/*      */   public static final String BASE = "base";
/*      */   public static final String BASEFONT = "basefont";
/*      */   public static final String BDO = "bdo";
/*      */   public static final String BIG = "big";
/*      */   public static final String BLOCKQUOTE = "blockquote";
/*      */   public static final String BODY = "body";
/*      */   public static final String BR = "br";
/*      */   public static final String BUTTON = "button";
/*      */   public static final String CAPTION = "caption";
/*      */   public static final String CENTER = "center";
/*      */   public static final String CITE = "cite";
/*      */   public static final String CODE = "code";
/*      */   public static final String COL = "col";
/*      */   public static final String COLGROUP = "colgroup";
/*      */   public static final String DD = "dd";
/*      */   public static final String DEL = "del";
/*      */   public static final String DFN = "dfn";
/*      */   public static final String DIR = "dir";
/*      */   public static final String DIV = "div";
/*      */   public static final String DL = "dl";
/*      */   public static final String DT = "dt";
/*      */   public static final String EM = "em";
/*      */   public static final String FIELDSET = "fieldset";
/*      */   public static final String FONT = "font";
/*      */   public static final String FORM = "form";
/*      */   public static final String FRAME = "frame";
/*      */   public static final String FRAMESET = "frameset";
/*      */   public static final String HEAD = "head";
/*      */   public static final String H1 = "h1";
/*      */   public static final String H2 = "h2";
/*      */   public static final String H3 = "h3";
/*      */   public static final String H4 = "h4";
/*      */   public static final String H5 = "h5";
/*      */   public static final String H6 = "h6";
/*      */   public static final String HR = "hr";
/*      */   public static final String HTML = "html";
/*      */   public static final String I = "i";
/*      */   public static final String IFRAME = "iframe";
/*      */   public static final String IMG = "img";
/*      */   public static final String INPUT = "input";
/*      */   public static final String INS = "ins";
/*      */   public static final String KBD = "kbd";
/*      */   public static final String LABEL = "label";
/*      */   public static final String LEGEND = "legend";
/*      */   public static final String LI = "li";
/*      */   public static final String LINK = "link";
/*      */   public static final String MAP = "map";
/*      */   public static final String MENU = "menu";
/*      */   public static final String META = "meta";
/*      */   public static final String NOFRAMES = "noframes";
/*      */   public static final String NOSCRIPT = "noscript";
/*      */   public static final String OBJECT = "object";
/*      */   public static final String OL = "ol";
/*      */   public static final String OPTGROUP = "optgroup";
/*      */   public static final String OPTION = "option";
/*      */   public static final String P = "p";
/*      */   public static final String PARAM = "param";
/*      */   public static final String PRE = "pre";
/*      */   public static final String Q = "q";
/*      */   public static final String S = "s";
/*      */   public static final String SAMP = "samp";
/*      */   public static final String SCRIPT = "script";
/*      */   public static final String SELECT = "select";
/*      */   public static final String SMALL = "small";
/*      */   public static final String SPAN = "span";
/*      */   public static final String STRIKE = "strike";
/*      */   public static final String STRONG = "strong";
/*      */   public static final String STYLE = "style";
/*      */   public static final String SUB = "sub";
/*      */   public static final String SUP = "sup";
/*      */   public static final String TABLE = "table";
/*      */   public static final String TBODY = "tbody";
/*      */   public static final String TD = "td";
/*      */   public static final String TEXTAREA = "textarea";
/*      */   public static final String TFOOT = "tfoot";
/*      */   public static final String TH = "th";
/*      */   public static final String THEAD = "thead";
/*      */   public static final String TITLE = "title";
/*      */   public static final String TR = "tr";
/*      */   public static final String TT = "tt";
/*      */   public static final String U = "u";
/*      */   public static final String UL = "ul";
/*      */   public static final String VAR = "var";
/*      */   public static final String ABBR_ATT = "abbr";
/*      */   public static final String ACCEPT = "accept";
/*      */   public static final String ACCEPT_CHARSET = "accept-charset";
/*      */   public static final String ACCESSKEY = "accesskey";
/*      */   public static final String ACTION = "action";
/*      */   public static final String ALIGN = "align";
/*      */   public static final String ALT = "alt";
/*      */   public static final String ARCHIVE = "archive";
/*      */   public static final String AXIS = "axis";
/*      */   public static final String BORDER = "border";
/*      */   public static final String CELLSPACING = "cellspacing";
/*      */   public static final String CELLPADDING = "cellpadding";
/*      */   public static final String CHAR = "char";
/*      */   public static final String CHAROFF = "charoff";
/*      */   public static final String CHARSET = "charset";
/*      */   public static final String CHECKED = "checked";
/*      */   public static final String CITE_ATT = "cite";
/*      */   public static final String CLASS = "class";
/*      */   public static final String CLASSID = "classid";
/*      */   public static final String CODEBASE = "codebase";
/*      */   public static final String CODETYPE = "codetype";
/*      */   public static final String COLS = "cols";
/*      */   public static final String COLSPAN = "colspan";
/*      */   public static final String CONTENT = "content";
/*      */   public static final String COORDS = "coords";
/*      */   public static final String DATA = "data";
/*      */   public static final String DATETIME = "datetime";
/*      */   public static final String DECLARE = "declare";
/*      */   public static final String DEFER = "defer";
/*      */   public static final String DIR_ATT = "dir";
/*      */   public static final String DISABLED = "disabled";
/*      */   public static final String ENCTYPE = "enctype";
/*      */   public static final String FOR = "for";
/*      */   public static final String FRAME_ATT = "frame";
/*      */   public static final String HEADERS = "headers";
/*      */   public static final String HEIGHT = "height";
/*      */   public static final String HREF = "href";
/*      */   public static final String HREFLANG = "hreflang";
/*      */   public static final String HTTP_EQUIV = "http-equiv";
/*      */   public static final String ID = "id";
/*      */   public static final String ISMAP = "ismap";
/*      */   public static final String LABEL_ATT = "label";
/*      */   public static final String LANG = "lang";
/*      */   public static final String LONGDESC = "longdesc";
/*      */   public static final String MAXLENGTH = "maxlength";
/*      */   public static final String MEDIA = "media";
/*      */   public static final String METHOD = "method";
/*      */   public static final String MULTIPLE = "multiple";
/*      */   public static final String NAME = "name";
/*      */   public static final String NOHREF = "nohref";
/*      */   public static final String ONBLUR = "onblur";
/*      */   public static final String ONCHANGE = "onchange";
/*      */   public static final String ONCLICK = "onclick";
/*      */   public static final String ONDBLCLICK = "ondblclick";
/*      */   public static final String ONFOCUS = "onfocus";
/*      */   public static final String ONKEYDOWN = "onkeydown";
/*      */   public static final String ONKEYPRESS = "onkeypress";
/*      */   public static final String ONKEYUP = "onkeyup";
/*      */   public static final String ONLOAD = "onload";
/*      */   public static final String ONMOUSEDOWN = "onmousedown";
/*      */   public static final String ONMOUSEMOVE = "onmousemove";
/*      */   public static final String ONMOUSEOUT = "onmouseout";
/*      */   public static final String ONMOUSEOVER = "onmouseover";
/*      */   public static final String ONMOUSEUP = "onmouseup";
/*      */   public static final String ONRESET = "onreset";
/*      */   public static final String ONSELECT = "onselect";
/*      */   public static final String ONSUBMIT = "onsubmit";
/*      */   public static final String ONUNLOAD = "onunload";
/*      */   public static final String PROFILE = "profile";
/*      */   public static final String READONLY = "readonly";
/*      */   public static final String REL = "rel";
/*      */   public static final String REV = "rev";
/*      */   public static final String ROWS = "rows";
/*      */   public static final String ROWSPAN = "rowspan";
/*      */   public static final String RULES = "rules";
/*      */   public static final String SCHEME = "scheme";
/*      */   public static final String SCOPE = "scope";
/*      */   public static final String SELECTED = "selected";
/*      */   public static final String SHAPE = "shape";
/*      */   public static final String SIZE = "size";
/*      */   public static final String SPAN_ATT = "span";
/*      */   public static final String SRC = "src";
/*      */   public static final String STANDBY = "standby";
/*      */   public static final String STYLE_ATT = "style";
/*      */   public static final String SUMMARY = "summary";
/*      */   public static final String TABINDEX = "tabindex";
/*      */   public static final String TITLE_ATT = "title";
/*      */   public static final String TYPE = "type";
/*      */   public static final String USEMAP = "usemap";
/*      */   public static final String VALIGN = "valign";
/*      */   public static final String VALUE = "value";
/*      */   public static final String VALUETYPE = "valuetype";
/*      */   public static final String WIDTH = "width";
/*      */   public static final String XML_LANG = "xml:lang";
/*      */   public static final String XML_SPACE = "xml:space";
/*      */   public static final String XMLNS = "xmlns";
/*      */   private static final boolean XHTML_MODE = true;
/*      */   private static final String XHTML_1_0_STRICT_DTD = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
/*      */ 
/*      */   public XHtmlWriter(OutputStream out, String charsetName)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*   98 */     super(out, charsetName, true);
/*   99 */     setDTDString("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
/*      */   }
/*      */ 
/*      */   public XHtmlWriter()
/*      */   {
/*  104 */     super(true);
/*  105 */     setDTDString("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
/*      */   }
/*      */ 
/*      */   public void a()
/*      */     throws SAXException
/*      */   {
/*  118 */     a(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void a(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  128 */     startSimpleElement("a", atts);
/*      */   }
/*      */ 
/*      */   public void a(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  139 */     startSimpleElement("a", att, val);
/*      */   }
/*      */ 
/*      */   public void a(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  153 */     startSimpleElement("a", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void a(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  170 */     startSimpleElement("a", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void a(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  190 */     startSimpleElement("a", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void abbr()
/*      */     throws SAXException
/*      */   {
/*  203 */     abbr(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void abbr(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  213 */     startSimpleElement("abbr", atts);
/*      */   }
/*      */ 
/*      */   public void abbr(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  224 */     startSimpleElement("abbr", att, val);
/*      */   }
/*      */ 
/*      */   public void abbr(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  238 */     startSimpleElement("abbr", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void abbr(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  255 */     startSimpleElement("abbr", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void abbr(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  275 */     startSimpleElement("abbr", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void acronym()
/*      */     throws SAXException
/*      */   {
/*  288 */     acronym(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void acronym(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  298 */     startSimpleElement("acronym", atts);
/*      */   }
/*      */ 
/*      */   public void acronym(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  309 */     startSimpleElement("acronym", att, val);
/*      */   }
/*      */ 
/*      */   public void acronym(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  323 */     startSimpleElement("acronym", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void acronym(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  340 */     startSimpleElement("acronym", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void acronym(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  360 */     startSimpleElement("acronym", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void address()
/*      */     throws SAXException
/*      */   {
/*  373 */     address(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void address(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  383 */     startSimpleElement("address", atts);
/*      */   }
/*      */ 
/*      */   public void address(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  394 */     startSimpleElement("address", att, val);
/*      */   }
/*      */ 
/*      */   public void address(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  408 */     startSimpleElement("address", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void address(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  425 */     startSimpleElement("address", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void address(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  445 */     startSimpleElement("address", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void applet()
/*      */     throws SAXException
/*      */   {
/*  458 */     applet(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void applet(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  468 */     startSimpleElement("applet", atts);
/*      */   }
/*      */ 
/*      */   public void applet(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  479 */     startSimpleElement("applet", att, val);
/*      */   }
/*      */ 
/*      */   public void applet(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  493 */     startSimpleElement("applet", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void applet(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  510 */     startSimpleElement("applet", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void applet(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  530 */     startSimpleElement("applet", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void area()
/*      */     throws SAXException
/*      */   {
/*  543 */     area(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void area(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  553 */     startSimpleElement("area", atts);
/*      */   }
/*      */ 
/*      */   public void area(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  564 */     startSimpleElement("area", att, val);
/*      */   }
/*      */ 
/*      */   public void area(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  578 */     startSimpleElement("area", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void area(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  595 */     startSimpleElement("area", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void area(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  615 */     startSimpleElement("area", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void b()
/*      */     throws SAXException
/*      */   {
/*  628 */     b(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void b(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  638 */     startSimpleElement("b", atts);
/*      */   }
/*      */ 
/*      */   public void b(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  649 */     startSimpleElement("b", att, val);
/*      */   }
/*      */ 
/*      */   public void b(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  663 */     startSimpleElement("b", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void b(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  680 */     startSimpleElement("b", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void b(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  700 */     startSimpleElement("b", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void base()
/*      */     throws SAXException
/*      */   {
/*  713 */     base(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void base(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  723 */     startSimpleElement("base", atts);
/*      */   }
/*      */ 
/*      */   public void base(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  734 */     startSimpleElement("base", att, val);
/*      */   }
/*      */ 
/*      */   public void base(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  748 */     startSimpleElement("base", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void base(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  765 */     startSimpleElement("base", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void base(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  785 */     startSimpleElement("base", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void basefont()
/*      */     throws SAXException
/*      */   {
/*  798 */     basefont(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void basefont(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  808 */     startSimpleElement("basefont", atts);
/*      */   }
/*      */ 
/*      */   public void basefont(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  819 */     startSimpleElement("basefont", att, val);
/*      */   }
/*      */ 
/*      */   public void basefont(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  833 */     startSimpleElement("basefont", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void basefont(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  850 */     startSimpleElement("basefont", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void basefont(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  870 */     startSimpleElement("basefont", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void bdo()
/*      */     throws SAXException
/*      */   {
/*  883 */     bdo(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void bdo(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  893 */     startSimpleElement("bdo", atts);
/*      */   }
/*      */ 
/*      */   public void bdo(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  904 */     startSimpleElement("bdo", att, val);
/*      */   }
/*      */ 
/*      */   public void bdo(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/*  918 */     startSimpleElement("bdo", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void bdo(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/*  935 */     startSimpleElement("bdo", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void bdo(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/*  955 */     startSimpleElement("bdo", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void big()
/*      */     throws SAXException
/*      */   {
/*  968 */     big(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void big(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  978 */     startSimpleElement("big", atts);
/*      */   }
/*      */ 
/*      */   public void big(String att, String val)
/*      */     throws SAXException
/*      */   {
/*  989 */     startSimpleElement("big", att, val);
/*      */   }
/*      */ 
/*      */   public void big(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1003 */     startSimpleElement("big", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void big(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1020 */     startSimpleElement("big", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void big(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1040 */     startSimpleElement("big", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void blockquote()
/*      */     throws SAXException
/*      */   {
/* 1053 */     blockquote(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void blockquote(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1063 */     startSimpleElement("blockquote", atts);
/*      */   }
/*      */ 
/*      */   public void blockquote(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1074 */     startSimpleElement("blockquote", att, val);
/*      */   }
/*      */ 
/*      */   public void blockquote(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1088 */     startSimpleElement("blockquote", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void blockquote(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1105 */     startSimpleElement("blockquote", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void blockquote(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1125 */     startSimpleElement("blockquote", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void body()
/*      */     throws SAXException
/*      */   {
/* 1138 */     body(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void body(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1148 */     startSimpleElement("body", atts);
/*      */   }
/*      */ 
/*      */   public void body(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1159 */     startSimpleElement("body", att, val);
/*      */   }
/*      */ 
/*      */   public void body(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1173 */     startSimpleElement("body", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void body(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1190 */     startSimpleElement("body", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void body(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1210 */     startSimpleElement("body", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void br()
/*      */     throws SAXException
/*      */   {
/* 1223 */     br(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void br(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1233 */     startSimpleElement("br", atts);
/*      */   }
/*      */ 
/*      */   public void br(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1244 */     startSimpleElement("br", att, val);
/*      */   }
/*      */ 
/*      */   public void br(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1258 */     startSimpleElement("br", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void br(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1275 */     startSimpleElement("br", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void br(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1295 */     startSimpleElement("br", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void button()
/*      */     throws SAXException
/*      */   {
/* 1308 */     button(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void button(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1318 */     startSimpleElement("button", atts);
/*      */   }
/*      */ 
/*      */   public void button(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1329 */     startSimpleElement("button", att, val);
/*      */   }
/*      */ 
/*      */   public void button(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1343 */     startSimpleElement("button", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void button(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1360 */     startSimpleElement("button", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void button(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1380 */     startSimpleElement("button", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void caption()
/*      */     throws SAXException
/*      */   {
/* 1393 */     caption(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void caption(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1403 */     startSimpleElement("caption", atts);
/*      */   }
/*      */ 
/*      */   public void caption(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1414 */     startSimpleElement("caption", att, val);
/*      */   }
/*      */ 
/*      */   public void caption(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1428 */     startSimpleElement("caption", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void caption(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1445 */     startSimpleElement("caption", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void caption(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1465 */     startSimpleElement("caption", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void center()
/*      */     throws SAXException
/*      */   {
/* 1478 */     center(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void center(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1488 */     startSimpleElement("center", atts);
/*      */   }
/*      */ 
/*      */   public void center(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1499 */     startSimpleElement("center", att, val);
/*      */   }
/*      */ 
/*      */   public void center(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1513 */     startSimpleElement("center", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void center(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1530 */     startSimpleElement("center", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void center(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1550 */     startSimpleElement("center", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void cite()
/*      */     throws SAXException
/*      */   {
/* 1563 */     cite(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void cite(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1573 */     startSimpleElement("cite", atts);
/*      */   }
/*      */ 
/*      */   public void cite(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1584 */     startSimpleElement("cite", att, val);
/*      */   }
/*      */ 
/*      */   public void cite(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1598 */     startSimpleElement("cite", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void cite(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1615 */     startSimpleElement("cite", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void cite(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1635 */     startSimpleElement("cite", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void code()
/*      */     throws SAXException
/*      */   {
/* 1648 */     code(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void code(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1658 */     startSimpleElement("code", atts);
/*      */   }
/*      */ 
/*      */   public void code(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1669 */     startSimpleElement("code", att, val);
/*      */   }
/*      */ 
/*      */   public void code(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1683 */     startSimpleElement("code", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void code(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1700 */     startSimpleElement("code", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void code(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1720 */     startSimpleElement("code", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void col()
/*      */     throws SAXException
/*      */   {
/* 1733 */     col(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void col(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1743 */     startSimpleElement("col", atts);
/*      */   }
/*      */ 
/*      */   public void col(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1754 */     startSimpleElement("col", att, val);
/*      */   }
/*      */ 
/*      */   public void col(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1768 */     startSimpleElement("col", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void col(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1785 */     startSimpleElement("col", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void col(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1805 */     startSimpleElement("col", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void colgroup()
/*      */     throws SAXException
/*      */   {
/* 1818 */     colgroup(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void colgroup(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1828 */     startSimpleElement("colgroup", atts);
/*      */   }
/*      */ 
/*      */   public void colgroup(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1839 */     startSimpleElement("colgroup", att, val);
/*      */   }
/*      */ 
/*      */   public void colgroup(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1853 */     startSimpleElement("colgroup", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void colgroup(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1870 */     startSimpleElement("colgroup", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void colgroup(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1890 */     startSimpleElement("colgroup", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void dd()
/*      */     throws SAXException
/*      */   {
/* 1903 */     dd(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void dd(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1913 */     startSimpleElement("dd", atts);
/*      */   }
/*      */ 
/*      */   public void dd(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 1924 */     startSimpleElement("dd", att, val);
/*      */   }
/*      */ 
/*      */   public void dd(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 1938 */     startSimpleElement("dd", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void dd(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 1955 */     startSimpleElement("dd", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void dd(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 1975 */     startSimpleElement("dd", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void del()
/*      */     throws SAXException
/*      */   {
/* 1988 */     del(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void del(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1998 */     startSimpleElement("del", atts);
/*      */   }
/*      */ 
/*      */   public void del(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2009 */     startSimpleElement("del", att, val);
/*      */   }
/*      */ 
/*      */   public void del(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2023 */     startSimpleElement("del", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void del(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2040 */     startSimpleElement("del", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void del(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2060 */     startSimpleElement("del", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void dfn()
/*      */     throws SAXException
/*      */   {
/* 2073 */     dfn(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void dfn(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2083 */     startSimpleElement("dfn", atts);
/*      */   }
/*      */ 
/*      */   public void dfn(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2094 */     startSimpleElement("dfn", att, val);
/*      */   }
/*      */ 
/*      */   public void dfn(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2108 */     startSimpleElement("dfn", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void dfn(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2125 */     startSimpleElement("dfn", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void dfn(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2145 */     startSimpleElement("dfn", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void dir()
/*      */     throws SAXException
/*      */   {
/* 2158 */     dir(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void dir(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2168 */     startSimpleElement("dir", atts);
/*      */   }
/*      */ 
/*      */   public void dir(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2179 */     startSimpleElement("dir", att, val);
/*      */   }
/*      */ 
/*      */   public void dir(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2193 */     startSimpleElement("dir", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void dir(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2210 */     startSimpleElement("dir", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void dir(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2230 */     startSimpleElement("dir", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void div()
/*      */     throws SAXException
/*      */   {
/* 2243 */     div(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void div(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2253 */     startSimpleElement("div", atts);
/*      */   }
/*      */ 
/*      */   public void div(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2264 */     startSimpleElement("div", att, val);
/*      */   }
/*      */ 
/*      */   public void div(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2278 */     startSimpleElement("div", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void div(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2295 */     startSimpleElement("div", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void div(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2315 */     startSimpleElement("div", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void dl()
/*      */     throws SAXException
/*      */   {
/* 2328 */     dl(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void dl(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2338 */     startSimpleElement("dl", atts);
/*      */   }
/*      */ 
/*      */   public void dl(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2349 */     startSimpleElement("dl", att, val);
/*      */   }
/*      */ 
/*      */   public void dl(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2363 */     startSimpleElement("dl", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void dl(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2380 */     startSimpleElement("dl", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void dl(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2400 */     startSimpleElement("dl", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void dt()
/*      */     throws SAXException
/*      */   {
/* 2413 */     dt(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void dt(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2423 */     startSimpleElement("dt", atts);
/*      */   }
/*      */ 
/*      */   public void dt(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2434 */     startSimpleElement("dt", att, val);
/*      */   }
/*      */ 
/*      */   public void dt(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2448 */     startSimpleElement("dt", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void dt(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2465 */     startSimpleElement("dt", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void dt(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2485 */     startSimpleElement("dt", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void em()
/*      */     throws SAXException
/*      */   {
/* 2498 */     em(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void em(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2508 */     startSimpleElement("em", atts);
/*      */   }
/*      */ 
/*      */   public void em(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2519 */     startSimpleElement("em", att, val);
/*      */   }
/*      */ 
/*      */   public void em(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2533 */     startSimpleElement("em", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void em(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2550 */     startSimpleElement("em", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void em(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2570 */     startSimpleElement("em", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void fieldset()
/*      */     throws SAXException
/*      */   {
/* 2583 */     fieldset(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void fieldset(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2593 */     startSimpleElement("fieldset", atts);
/*      */   }
/*      */ 
/*      */   public void fieldset(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2604 */     startSimpleElement("fieldset", att, val);
/*      */   }
/*      */ 
/*      */   public void fieldset(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2618 */     startSimpleElement("fieldset", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void fieldset(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2635 */     startSimpleElement("fieldset", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void fieldset(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2655 */     startSimpleElement("fieldset", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void font()
/*      */     throws SAXException
/*      */   {
/* 2668 */     font(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void font(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2678 */     startSimpleElement("font", atts);
/*      */   }
/*      */ 
/*      */   public void font(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2689 */     startSimpleElement("font", att, val);
/*      */   }
/*      */ 
/*      */   public void font(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2703 */     startSimpleElement("font", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void font(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2720 */     startSimpleElement("font", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void font(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2740 */     startSimpleElement("font", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void form()
/*      */     throws SAXException
/*      */   {
/* 2753 */     form(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void form(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2763 */     startSimpleElement("form", atts);
/*      */   }
/*      */ 
/*      */   public void form(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2774 */     startSimpleElement("form", att, val);
/*      */   }
/*      */ 
/*      */   public void form(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2788 */     startSimpleElement("form", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void form(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2805 */     startSimpleElement("form", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void form(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2825 */     startSimpleElement("form", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void frame()
/*      */     throws SAXException
/*      */   {
/* 2838 */     frame(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void frame(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2848 */     startSimpleElement("frame", atts);
/*      */   }
/*      */ 
/*      */   public void frame(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2859 */     startSimpleElement("frame", att, val);
/*      */   }
/*      */ 
/*      */   public void frame(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2873 */     startSimpleElement("frame", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void frame(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2890 */     startSimpleElement("frame", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void frame(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2910 */     startSimpleElement("frame", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void frameset()
/*      */     throws SAXException
/*      */   {
/* 2923 */     frameset(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void frameset(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 2933 */     startSimpleElement("frameset", atts);
/*      */   }
/*      */ 
/*      */   public void frameset(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 2944 */     startSimpleElement("frameset", att, val);
/*      */   }
/*      */ 
/*      */   public void frameset(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 2958 */     startSimpleElement("frameset", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void frameset(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 2975 */     startSimpleElement("frameset", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void frameset(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 2995 */     startSimpleElement("frameset", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void head()
/*      */     throws SAXException
/*      */   {
/* 3008 */     head(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void head(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3018 */     startSimpleElement("head", atts);
/*      */   }
/*      */ 
/*      */   public void head(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3029 */     startSimpleElement("head", att, val);
/*      */   }
/*      */ 
/*      */   public void head(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3043 */     startSimpleElement("head", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void head(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3060 */     startSimpleElement("head", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void head(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3080 */     startSimpleElement("head", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void h1()
/*      */     throws SAXException
/*      */   {
/* 3093 */     h1(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void h1(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3103 */     startSimpleElement("h1", atts);
/*      */   }
/*      */ 
/*      */   public void h1(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3114 */     startSimpleElement("h1", att, val);
/*      */   }
/*      */ 
/*      */   public void h1(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3128 */     startSimpleElement("h1", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void h1(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3145 */     startSimpleElement("h1", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void h1(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3165 */     startSimpleElement("h1", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void h2()
/*      */     throws SAXException
/*      */   {
/* 3178 */     h2(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void h2(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3188 */     startSimpleElement("h2", atts);
/*      */   }
/*      */ 
/*      */   public void h2(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3199 */     startSimpleElement("h2", att, val);
/*      */   }
/*      */ 
/*      */   public void h2(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3213 */     startSimpleElement("h2", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void h2(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3230 */     startSimpleElement("h2", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void h2(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3250 */     startSimpleElement("h2", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void h3()
/*      */     throws SAXException
/*      */   {
/* 3263 */     h3(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void h3(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3273 */     startSimpleElement("h3", atts);
/*      */   }
/*      */ 
/*      */   public void h3(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3284 */     startSimpleElement("h3", att, val);
/*      */   }
/*      */ 
/*      */   public void h3(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3298 */     startSimpleElement("h3", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void h3(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3315 */     startSimpleElement("h3", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void h3(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3335 */     startSimpleElement("h3", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void h4()
/*      */     throws SAXException
/*      */   {
/* 3348 */     h4(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void h4(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3358 */     startSimpleElement("h4", atts);
/*      */   }
/*      */ 
/*      */   public void h4(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3369 */     startSimpleElement("h4", att, val);
/*      */   }
/*      */ 
/*      */   public void h4(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3383 */     startSimpleElement("h4", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void h4(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3400 */     startSimpleElement("h4", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void h4(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3420 */     startSimpleElement("h4", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void h5()
/*      */     throws SAXException
/*      */   {
/* 3433 */     h5(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void h5(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3443 */     startSimpleElement("h5", atts);
/*      */   }
/*      */ 
/*      */   public void h5(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3454 */     startSimpleElement("h5", att, val);
/*      */   }
/*      */ 
/*      */   public void h5(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3468 */     startSimpleElement("h5", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void h5(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3485 */     startSimpleElement("h5", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void h5(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3505 */     startSimpleElement("h5", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void h6()
/*      */     throws SAXException
/*      */   {
/* 3518 */     h6(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void h6(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3528 */     startSimpleElement("h6", atts);
/*      */   }
/*      */ 
/*      */   public void h6(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3539 */     startSimpleElement("h6", att, val);
/*      */   }
/*      */ 
/*      */   public void h6(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3553 */     startSimpleElement("h6", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void h6(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3570 */     startSimpleElement("h6", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void h6(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3590 */     startSimpleElement("h6", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void hr()
/*      */     throws SAXException
/*      */   {
/* 3603 */     hr(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void hr(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3613 */     startSimpleElement("hr", atts);
/*      */   }
/*      */ 
/*      */   public void hr(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3624 */     startSimpleElement("hr", att, val);
/*      */   }
/*      */ 
/*      */   public void hr(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3638 */     startSimpleElement("hr", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void hr(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3655 */     startSimpleElement("hr", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void hr(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3675 */     startSimpleElement("hr", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void html()
/*      */     throws SAXException
/*      */   {
/* 3688 */     html(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void html(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3698 */     startSimpleElement("html", atts);
/*      */   }
/*      */ 
/*      */   public void html(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3709 */     startSimpleElement("html", att, val);
/*      */   }
/*      */ 
/*      */   public void html(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3723 */     startSimpleElement("html", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void html(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3740 */     startSimpleElement("html", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void html(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3760 */     startSimpleElement("html", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void i()
/*      */     throws SAXException
/*      */   {
/* 3773 */     i(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void i(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3783 */     startSimpleElement("i", atts);
/*      */   }
/*      */ 
/*      */   public void i(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3794 */     startSimpleElement("i", att, val);
/*      */   }
/*      */ 
/*      */   public void i(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3808 */     startSimpleElement("i", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void i(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3825 */     startSimpleElement("i", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void i(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3845 */     startSimpleElement("i", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void iframe()
/*      */     throws SAXException
/*      */   {
/* 3858 */     iframe(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void iframe(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3868 */     startSimpleElement("iframe", atts);
/*      */   }
/*      */ 
/*      */   public void iframe(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3879 */     startSimpleElement("iframe", att, val);
/*      */   }
/*      */ 
/*      */   public void iframe(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3893 */     startSimpleElement("iframe", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void iframe(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3910 */     startSimpleElement("iframe", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void iframe(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 3930 */     startSimpleElement("iframe", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void img()
/*      */     throws SAXException
/*      */   {
/* 3943 */     img(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void img(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 3953 */     startSimpleElement("img", atts);
/*      */   }
/*      */ 
/*      */   public void img(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 3964 */     startSimpleElement("img", att, val);
/*      */   }
/*      */ 
/*      */   public void img(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 3978 */     startSimpleElement("img", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void img(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 3995 */     startSimpleElement("img", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void img(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4015 */     startSimpleElement("img", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void input()
/*      */     throws SAXException
/*      */   {
/* 4028 */     input(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void input(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4038 */     startSimpleElement("input", atts);
/*      */   }
/*      */ 
/*      */   public void input(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4049 */     startSimpleElement("input", att, val);
/*      */   }
/*      */ 
/*      */   public void input(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4063 */     startSimpleElement("input", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void input(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4080 */     startSimpleElement("input", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void input(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4100 */     startSimpleElement("input", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void ins()
/*      */     throws SAXException
/*      */   {
/* 4113 */     ins(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void ins(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4123 */     startSimpleElement("ins", atts);
/*      */   }
/*      */ 
/*      */   public void ins(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4134 */     startSimpleElement("ins", att, val);
/*      */   }
/*      */ 
/*      */   public void ins(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4148 */     startSimpleElement("ins", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void ins(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4165 */     startSimpleElement("ins", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void ins(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4185 */     startSimpleElement("ins", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void kbd()
/*      */     throws SAXException
/*      */   {
/* 4198 */     kbd(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void kbd(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4208 */     startSimpleElement("kbd", atts);
/*      */   }
/*      */ 
/*      */   public void kbd(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4219 */     startSimpleElement("kbd", att, val);
/*      */   }
/*      */ 
/*      */   public void kbd(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4233 */     startSimpleElement("kbd", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void kbd(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4250 */     startSimpleElement("kbd", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void kbd(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4270 */     startSimpleElement("kbd", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void label()
/*      */     throws SAXException
/*      */   {
/* 4283 */     label(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void label(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4293 */     startSimpleElement("label", atts);
/*      */   }
/*      */ 
/*      */   public void label(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4304 */     startSimpleElement("label", att, val);
/*      */   }
/*      */ 
/*      */   public void label(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4318 */     startSimpleElement("label", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void label(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4335 */     startSimpleElement("label", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void label(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4355 */     startSimpleElement("label", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void legend()
/*      */     throws SAXException
/*      */   {
/* 4368 */     legend(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void legend(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4378 */     startSimpleElement("legend", atts);
/*      */   }
/*      */ 
/*      */   public void legend(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4389 */     startSimpleElement("legend", att, val);
/*      */   }
/*      */ 
/*      */   public void legend(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4403 */     startSimpleElement("legend", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void legend(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4420 */     startSimpleElement("legend", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void legend(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4440 */     startSimpleElement("legend", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void li()
/*      */     throws SAXException
/*      */   {
/* 4453 */     li(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void li(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4463 */     startSimpleElement("li", atts);
/*      */   }
/*      */ 
/*      */   public void li(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4474 */     startSimpleElement("li", att, val);
/*      */   }
/*      */ 
/*      */   public void li(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4488 */     startSimpleElement("li", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void li(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4505 */     startSimpleElement("li", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void li(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4525 */     startSimpleElement("li", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void link()
/*      */     throws SAXException
/*      */   {
/* 4538 */     link(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void link(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4548 */     startSimpleElement("link", atts);
/*      */   }
/*      */ 
/*      */   public void link(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4559 */     startSimpleElement("link", att, val);
/*      */   }
/*      */ 
/*      */   public void link(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4573 */     startSimpleElement("link", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void link(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4590 */     startSimpleElement("link", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void link(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4610 */     startSimpleElement("link", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void map()
/*      */     throws SAXException
/*      */   {
/* 4623 */     map(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void map(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4633 */     startSimpleElement("map", atts);
/*      */   }
/*      */ 
/*      */   public void map(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4644 */     startSimpleElement("map", att, val);
/*      */   }
/*      */ 
/*      */   public void map(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4658 */     startSimpleElement("map", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void map(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4675 */     startSimpleElement("map", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void map(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4695 */     startSimpleElement("map", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void menu()
/*      */     throws SAXException
/*      */   {
/* 4708 */     menu(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void menu(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4718 */     startSimpleElement("menu", atts);
/*      */   }
/*      */ 
/*      */   public void menu(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4729 */     startSimpleElement("menu", att, val);
/*      */   }
/*      */ 
/*      */   public void menu(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4743 */     startSimpleElement("menu", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void menu(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4760 */     startSimpleElement("menu", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void menu(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4780 */     startSimpleElement("menu", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void meta()
/*      */     throws SAXException
/*      */   {
/* 4793 */     meta(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void meta(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4803 */     startSimpleElement("meta", atts);
/*      */   }
/*      */ 
/*      */   public void meta(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4814 */     startSimpleElement("meta", att, val);
/*      */   }
/*      */ 
/*      */   public void meta(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4828 */     startSimpleElement("meta", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void meta(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4845 */     startSimpleElement("meta", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void meta(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4865 */     startSimpleElement("meta", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void noframes()
/*      */     throws SAXException
/*      */   {
/* 4878 */     noframes(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void noframes(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4888 */     startSimpleElement("noframes", atts);
/*      */   }
/*      */ 
/*      */   public void noframes(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4899 */     startSimpleElement("noframes", att, val);
/*      */   }
/*      */ 
/*      */   public void noframes(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4913 */     startSimpleElement("noframes", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void noframes(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 4930 */     startSimpleElement("noframes", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void noframes(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 4950 */     startSimpleElement("noframes", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void noscript()
/*      */     throws SAXException
/*      */   {
/* 4963 */     noscript(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void noscript(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 4973 */     startSimpleElement("noscript", atts);
/*      */   }
/*      */ 
/*      */   public void noscript(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 4984 */     startSimpleElement("noscript", att, val);
/*      */   }
/*      */ 
/*      */   public void noscript(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 4998 */     startSimpleElement("noscript", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void noscript(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5015 */     startSimpleElement("noscript", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void noscript(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5035 */     startSimpleElement("noscript", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void object()
/*      */     throws SAXException
/*      */   {
/* 5048 */     object(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void object(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5058 */     startSimpleElement("object", atts);
/*      */   }
/*      */ 
/*      */   public void object(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5069 */     startSimpleElement("object", att, val);
/*      */   }
/*      */ 
/*      */   public void object(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5083 */     startSimpleElement("object", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void object(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5100 */     startSimpleElement("object", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void object(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5120 */     startSimpleElement("object", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void ol()
/*      */     throws SAXException
/*      */   {
/* 5133 */     ol(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void ol(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5143 */     startSimpleElement("ol", atts);
/*      */   }
/*      */ 
/*      */   public void ol(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5154 */     startSimpleElement("ol", att, val);
/*      */   }
/*      */ 
/*      */   public void ol(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5168 */     startSimpleElement("ol", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void ol(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5185 */     startSimpleElement("ol", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void ol(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5205 */     startSimpleElement("ol", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void optgroup()
/*      */     throws SAXException
/*      */   {
/* 5218 */     optgroup(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void optgroup(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5228 */     startSimpleElement("optgroup", atts);
/*      */   }
/*      */ 
/*      */   public void optgroup(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5239 */     startSimpleElement("optgroup", att, val);
/*      */   }
/*      */ 
/*      */   public void optgroup(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5253 */     startSimpleElement("optgroup", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void optgroup(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5270 */     startSimpleElement("optgroup", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void optgroup(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5290 */     startSimpleElement("optgroup", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void option()
/*      */     throws SAXException
/*      */   {
/* 5303 */     option(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void option(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5313 */     startSimpleElement("option", atts);
/*      */   }
/*      */ 
/*      */   public void option(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5324 */     startSimpleElement("option", att, val);
/*      */   }
/*      */ 
/*      */   public void option(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5338 */     startSimpleElement("option", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void option(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5355 */     startSimpleElement("option", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void option(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5375 */     startSimpleElement("option", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void p()
/*      */     throws SAXException
/*      */   {
/* 5388 */     p(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void p(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5398 */     startSimpleElement("p", atts);
/*      */   }
/*      */ 
/*      */   public void p(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5409 */     startSimpleElement("p", att, val);
/*      */   }
/*      */ 
/*      */   public void p(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5423 */     startSimpleElement("p", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void p(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5440 */     startSimpleElement("p", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void p(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5460 */     startSimpleElement("p", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void param()
/*      */     throws SAXException
/*      */   {
/* 5473 */     param(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void param(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5483 */     startSimpleElement("param", atts);
/*      */   }
/*      */ 
/*      */   public void param(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5494 */     startSimpleElement("param", att, val);
/*      */   }
/*      */ 
/*      */   public void param(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5508 */     startSimpleElement("param", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void param(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5525 */     startSimpleElement("param", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void param(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5545 */     startSimpleElement("param", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void pre()
/*      */     throws SAXException
/*      */   {
/* 5558 */     pre(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void pre(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5568 */     startSimpleElement("pre", atts);
/*      */   }
/*      */ 
/*      */   public void pre(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5579 */     startSimpleElement("pre", att, val);
/*      */   }
/*      */ 
/*      */   public void pre(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5593 */     startSimpleElement("pre", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void pre(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5610 */     startSimpleElement("pre", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void pre(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5630 */     startSimpleElement("pre", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void q()
/*      */     throws SAXException
/*      */   {
/* 5643 */     q(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void q(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5653 */     startSimpleElement("q", atts);
/*      */   }
/*      */ 
/*      */   public void q(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5664 */     startSimpleElement("q", att, val);
/*      */   }
/*      */ 
/*      */   public void q(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5678 */     startSimpleElement("q", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void q(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5695 */     startSimpleElement("q", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void q(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5715 */     startSimpleElement("q", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void s()
/*      */     throws SAXException
/*      */   {
/* 5728 */     s(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void s(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5738 */     startSimpleElement("s", atts);
/*      */   }
/*      */ 
/*      */   public void s(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5749 */     startSimpleElement("s", att, val);
/*      */   }
/*      */ 
/*      */   public void s(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5763 */     startSimpleElement("s", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void s(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5780 */     startSimpleElement("s", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void s(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5800 */     startSimpleElement("s", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void samp()
/*      */     throws SAXException
/*      */   {
/* 5813 */     samp(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void samp(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5823 */     startSimpleElement("samp", atts);
/*      */   }
/*      */ 
/*      */   public void samp(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5834 */     startSimpleElement("samp", att, val);
/*      */   }
/*      */ 
/*      */   public void samp(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5848 */     startSimpleElement("samp", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void samp(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5865 */     startSimpleElement("samp", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void samp(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5885 */     startSimpleElement("samp", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void script()
/*      */     throws SAXException
/*      */   {
/* 5898 */     script(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void script(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5908 */     startSimpleElement("script", atts);
/*      */   }
/*      */ 
/*      */   public void script(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 5919 */     startSimpleElement("script", att, val);
/*      */   }
/*      */ 
/*      */   public void script(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 5933 */     startSimpleElement("script", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void script(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 5950 */     startSimpleElement("script", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void script(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 5970 */     startSimpleElement("script", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void select()
/*      */     throws SAXException
/*      */   {
/* 5983 */     select(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void select(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 5993 */     startSimpleElement("select", atts);
/*      */   }
/*      */ 
/*      */   public void select(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6004 */     startSimpleElement("select", att, val);
/*      */   }
/*      */ 
/*      */   public void select(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6018 */     startSimpleElement("select", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void select(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6035 */     startSimpleElement("select", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void select(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6055 */     startSimpleElement("select", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void small()
/*      */     throws SAXException
/*      */   {
/* 6068 */     small(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void small(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6078 */     startSimpleElement("small", atts);
/*      */   }
/*      */ 
/*      */   public void small(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6089 */     startSimpleElement("small", att, val);
/*      */   }
/*      */ 
/*      */   public void small(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6103 */     startSimpleElement("small", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void small(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6120 */     startSimpleElement("small", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void small(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6140 */     startSimpleElement("small", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void span()
/*      */     throws SAXException
/*      */   {
/* 6153 */     span(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void span(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6163 */     startSimpleElement("span", atts);
/*      */   }
/*      */ 
/*      */   public void span(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6174 */     startSimpleElement("span", att, val);
/*      */   }
/*      */ 
/*      */   public void span(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6188 */     startSimpleElement("span", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void span(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6205 */     startSimpleElement("span", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void span(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6225 */     startSimpleElement("span", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void strike()
/*      */     throws SAXException
/*      */   {
/* 6238 */     strike(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void strike(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6248 */     startSimpleElement("strike", atts);
/*      */   }
/*      */ 
/*      */   public void strike(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6259 */     startSimpleElement("strike", att, val);
/*      */   }
/*      */ 
/*      */   public void strike(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6273 */     startSimpleElement("strike", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void strike(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6290 */     startSimpleElement("strike", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void strike(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6310 */     startSimpleElement("strike", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void strong()
/*      */     throws SAXException
/*      */   {
/* 6323 */     strong(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void strong(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6333 */     startSimpleElement("strong", atts);
/*      */   }
/*      */ 
/*      */   public void strong(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6344 */     startSimpleElement("strong", att, val);
/*      */   }
/*      */ 
/*      */   public void strong(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6358 */     startSimpleElement("strong", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void strong(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6375 */     startSimpleElement("strong", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void strong(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6395 */     startSimpleElement("strong", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void style()
/*      */     throws SAXException
/*      */   {
/* 6408 */     style(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void style(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6418 */     startSimpleElement("style", atts);
/*      */   }
/*      */ 
/*      */   public void style(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6429 */     startSimpleElement("style", att, val);
/*      */   }
/*      */ 
/*      */   public void style(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6443 */     startSimpleElement("style", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void style(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6460 */     startSimpleElement("style", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void style(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6480 */     startSimpleElement("style", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void sub()
/*      */     throws SAXException
/*      */   {
/* 6493 */     sub(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void sub(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6503 */     startSimpleElement("sub", atts);
/*      */   }
/*      */ 
/*      */   public void sub(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6514 */     startSimpleElement("sub", att, val);
/*      */   }
/*      */ 
/*      */   public void sub(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6528 */     startSimpleElement("sub", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void sub(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6545 */     startSimpleElement("sub", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void sub(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6565 */     startSimpleElement("sub", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void sup()
/*      */     throws SAXException
/*      */   {
/* 6578 */     sup(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void sup(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6588 */     startSimpleElement("sup", atts);
/*      */   }
/*      */ 
/*      */   public void sup(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6599 */     startSimpleElement("sup", att, val);
/*      */   }
/*      */ 
/*      */   public void sup(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6613 */     startSimpleElement("sup", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void sup(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6630 */     startSimpleElement("sup", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void sup(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6650 */     startSimpleElement("sup", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void table()
/*      */     throws SAXException
/*      */   {
/* 6663 */     table(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void table(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6673 */     startSimpleElement("table", atts);
/*      */   }
/*      */ 
/*      */   public void table(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6684 */     startSimpleElement("table", att, val);
/*      */   }
/*      */ 
/*      */   public void table(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6698 */     startSimpleElement("table", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void table(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6715 */     startSimpleElement("table", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void table(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6735 */     startSimpleElement("table", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void tbody()
/*      */     throws SAXException
/*      */   {
/* 6748 */     tbody(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void tbody(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6758 */     startSimpleElement("tbody", atts);
/*      */   }
/*      */ 
/*      */   public void tbody(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6769 */     startSimpleElement("tbody", att, val);
/*      */   }
/*      */ 
/*      */   public void tbody(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6783 */     startSimpleElement("tbody", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void tbody(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6800 */     startSimpleElement("tbody", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void tbody(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6820 */     startSimpleElement("tbody", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void td()
/*      */     throws SAXException
/*      */   {
/* 6833 */     td(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void td(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6843 */     startSimpleElement("td", atts);
/*      */   }
/*      */ 
/*      */   public void td(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6854 */     startSimpleElement("td", att, val);
/*      */   }
/*      */ 
/*      */   public void td(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6868 */     startSimpleElement("td", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void td(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6885 */     startSimpleElement("td", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void td(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6905 */     startSimpleElement("td", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void textarea()
/*      */     throws SAXException
/*      */   {
/* 6918 */     textarea(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void textarea(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 6928 */     startSimpleElement("textarea", atts);
/*      */   }
/*      */ 
/*      */   public void textarea(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 6939 */     startSimpleElement("textarea", att, val);
/*      */   }
/*      */ 
/*      */   public void textarea(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 6953 */     startSimpleElement("textarea", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void textarea(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 6970 */     startSimpleElement("textarea", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void textarea(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 6990 */     startSimpleElement("textarea", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void tfoot()
/*      */     throws SAXException
/*      */   {
/* 7003 */     tfoot(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void tfoot(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7013 */     startSimpleElement("tfoot", atts);
/*      */   }
/*      */ 
/*      */   public void tfoot(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7024 */     startSimpleElement("tfoot", att, val);
/*      */   }
/*      */ 
/*      */   public void tfoot(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7038 */     startSimpleElement("tfoot", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void tfoot(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7055 */     startSimpleElement("tfoot", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void tfoot(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7075 */     startSimpleElement("tfoot", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void th()
/*      */     throws SAXException
/*      */   {
/* 7088 */     th(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void th(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7098 */     startSimpleElement("th", atts);
/*      */   }
/*      */ 
/*      */   public void th(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7109 */     startSimpleElement("th", att, val);
/*      */   }
/*      */ 
/*      */   public void th(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7123 */     startSimpleElement("th", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void th(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7140 */     startSimpleElement("th", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void th(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7160 */     startSimpleElement("th", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void thead()
/*      */     throws SAXException
/*      */   {
/* 7173 */     thead(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void thead(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7183 */     startSimpleElement("thead", atts);
/*      */   }
/*      */ 
/*      */   public void thead(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7194 */     startSimpleElement("thead", att, val);
/*      */   }
/*      */ 
/*      */   public void thead(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7208 */     startSimpleElement("thead", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void thead(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7225 */     startSimpleElement("thead", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void thead(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7245 */     startSimpleElement("thead", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void title()
/*      */     throws SAXException
/*      */   {
/* 7258 */     title(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void title(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7268 */     startSimpleElement("title", atts);
/*      */   }
/*      */ 
/*      */   public void title(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7279 */     startSimpleElement("title", att, val);
/*      */   }
/*      */ 
/*      */   public void title(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7293 */     startSimpleElement("title", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void title(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7310 */     startSimpleElement("title", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void title(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7330 */     startSimpleElement("title", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void tr()
/*      */     throws SAXException
/*      */   {
/* 7343 */     tr(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void tr(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7353 */     startSimpleElement("tr", atts);
/*      */   }
/*      */ 
/*      */   public void tr(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7364 */     startSimpleElement("tr", att, val);
/*      */   }
/*      */ 
/*      */   public void tr(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7378 */     startSimpleElement("tr", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void tr(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7395 */     startSimpleElement("tr", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void tr(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7415 */     startSimpleElement("tr", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void tt()
/*      */     throws SAXException
/*      */   {
/* 7428 */     tt(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void tt(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7438 */     startSimpleElement("tt", atts);
/*      */   }
/*      */ 
/*      */   public void tt(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7449 */     startSimpleElement("tt", att, val);
/*      */   }
/*      */ 
/*      */   public void tt(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7463 */     startSimpleElement("tt", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void tt(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7480 */     startSimpleElement("tt", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void tt(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7500 */     startSimpleElement("tt", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void u()
/*      */     throws SAXException
/*      */   {
/* 7513 */     u(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void u(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7523 */     startSimpleElement("u", atts);
/*      */   }
/*      */ 
/*      */   public void u(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7534 */     startSimpleElement("u", att, val);
/*      */   }
/*      */ 
/*      */   public void u(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7548 */     startSimpleElement("u", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void u(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7565 */     startSimpleElement("u", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void u(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7585 */     startSimpleElement("u", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void ul()
/*      */     throws SAXException
/*      */   {
/* 7598 */     ul(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void ul(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7608 */     startSimpleElement("ul", atts);
/*      */   }
/*      */ 
/*      */   public void ul(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7619 */     startSimpleElement("ul", att, val);
/*      */   }
/*      */ 
/*      */   public void ul(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7633 */     startSimpleElement("ul", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void ul(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7650 */     startSimpleElement("ul", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void ul(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7670 */     startSimpleElement("ul", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   public void var()
/*      */     throws SAXException
/*      */   {
/* 7684 */     var(EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void var(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 7694 */     startSimpleElement("var", atts);
/*      */   }
/*      */ 
/*      */   public void var(String att, String val)
/*      */     throws SAXException
/*      */   {
/* 7705 */     startSimpleElement("var", att, val);
/*      */   }
/*      */ 
/*      */   public void var(String att1, String val1, String att2, String val2)
/*      */     throws SAXException
/*      */   {
/* 7719 */     startSimpleElement("var", att1, val1, att2, val2);
/*      */   }
/*      */ 
/*      */   public void var(String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 7736 */     startSimpleElement("var", att1, val1, att2, val2, att3, val3);
/*      */   }
/*      */ 
/*      */   public void var(String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 7756 */     startSimpleElement("var", att1, val1, att2, val2, att3, val3, att4, val4);
/*      */   }
/*      */ 
/*      */   private void startSimpleElement(String qName, String att1, String val1, String att2, String val2, String att3, String val3)
/*      */     throws SAXException
/*      */   {
/* 8254 */     Attributes atts = createAttributes(att1, val1, att2, val2, att3, val3);
/* 8255 */     startSimpleElement(qName, atts);
/*      */   }
/*      */ 
/*      */   private void startSimpleElement(String qName, String att1, String val1, String att2, String val2, String att3, String val3, String att4, String val4)
/*      */     throws SAXException
/*      */   {
/* 8264 */     Attributes atts = createAttributes(att1, val1, att2, val2, att3, val3, att4, val4);
/*      */ 
/* 8266 */     startSimpleElement(qName, atts);
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.XHtmlWriter
 * JD-Core Version:    0.6.2
 */