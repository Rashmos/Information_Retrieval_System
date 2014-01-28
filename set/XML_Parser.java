/*     */ package set;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class XML_Parser
/*     */ {
/*     */   public ArrayList<String> Title;
/*     */   public ArrayList<Integer> DocNo;
/*     */   public ArrayList<String> Text;
/*     */ 
/*     */   public XML_Parser()
/*     */   {
/*  23 */     this.Title = new ArrayList();
/*  24 */     this.DocNo = new ArrayList();
/*  25 */     this.Text = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void ReadXml()
/*     */     throws IOException
/*     */   {
/*  31 */     System.out.println("Enter path of folder from where you want to access data");
/*  32 */     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/*  33 */     String location = br.readLine();
/*  34 */     File folder = new File(location);
/*  35 */     File[] listOfFiles = folder.listFiles();
/*     */ 
/*  39 */     PrintStream out = null;
/*  40 */     PrintStream out1 = null;
/*  41 */     for (int i = 0; i < listOfFiles.length; i++)
/*     */     {
/*     */       try
/*     */       {
/*  46 */         File fXmlFile = new File(listOfFiles[i]);
/*  47 */         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*  48 */         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*  49 */         Document doc = dBuilder.parse(fXmlFile);
/*  50 */         doc.getDocumentElement().normalize();
/*     */ 
/*  52 */         NodeList nList = doc.getElementsByTagName("DOC");
/*     */ 
/*  54 */         for (int temp = 0; temp < nList.getLength(); temp++)
/*     */         {
/*  56 */           Node nNode = nList.item(temp);
/*  57 */           if (nNode.getNodeType() == 1)
/*     */           {
/*  60 */             Element eElement = (Element)nNode;
/*     */ 
/*  62 */             String doc_no = getTagValue("DOCNO", eElement);
/*  63 */             doc_no = doc_no.replace("\n", "");
/*  64 */             this.DocNo.add(Integer.valueOf(Integer.parseInt(doc_no)));
/*     */ 
/*  66 */             String text = getTagValue("TEXT", eElement).toString();
/*  67 */             text = text.replace("\n", " ");
/*  68 */             this.Text.add(text);
/*     */ 
/*  70 */             String title = getTagValue("TITLE", eElement);
/*  71 */             title = title.replace("\n", "");
/*  72 */             this.Title.add(title);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*  79 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ArrayList<String> getTitle()
/*     */   {
/*  87 */     return this.Title;
/*     */   }
/*     */   public ArrayList<Integer> getDocNo() {
/*  90 */     return this.DocNo;
/*     */   }
/*     */   public ArrayList<String> getText() {
/*  93 */     return this.Text;
/*     */   }
/*     */ 
/*     */   private static String getTagValue(String sTag, Element eElement) {
/*  97 */     NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
/*  98 */     Node nValue = nlList.item(0);
/*     */ 
/* 100 */     return nValue.getNodeValue();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     set.XML_Parser
 * JD-Core Version:    0.6.2
 */