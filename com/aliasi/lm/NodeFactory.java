/*      */ package com.aliasi.lm;
/*      */ 
/*      */ class NodeFactory
/*      */ {
/* 1296 */   static Node[] TERMINAL_NODES = new Node[1024];
/*      */ 
/* 1520 */   static Node[] EMPTY_NODES = new Node[0];
/*      */ 
/*      */   static char[] sliceToArray(char[] cs, int start, int end)
/*      */   {
/* 1290 */     if ((start == 0) && (end == cs.length)) return cs;
/* 1291 */     char[] result = new char[end - start];
/* 1292 */     for (int i = 0; i < result.length; i++)
/* 1293 */       result[i] = cs[(start + i)];
/* 1294 */     return result;
/*      */   }
/*      */ 
/*      */   static Node createNode(long count)
/*      */   {
/* 1303 */     if (count < TERMINAL_NODES.length)
/* 1304 */       return TERMINAL_NODES[((int)count)];
/* 1305 */     return createTerminalNode(count);
/*      */   }
/*      */   static Node createNode(char[] cs, int start, int end, long count) {
/* 1308 */     switch (end - start) { case 0:
/* 1309 */       return createNode(count);
/*      */     case 1:
/* 1310 */       return createNode(cs[start], count);
/*      */     case 2:
/* 1311 */       return createNode(cs[start], cs[(start + 1)], count);
/*      */     case 3:
/* 1312 */       return createNode(cs[start], cs[(start + 1)], cs[(start + 2)], count);
/*      */     case 4:
/* 1313 */       return createNode(cs[start], cs[(start + 1)], cs[(start + 2)], cs[(start + 3)], count);
/*      */     }
/* 1315 */     return createPATArrayNode(sliceToArray(cs, start, end), count);
/*      */   }
/*      */ 
/*      */   static Node createNode(char[] cs, Node[] dtrs, long count) {
/* 1319 */     switch (dtrs.length) { case 0:
/* 1320 */       return createNode(count);
/*      */     case 1:
/* 1321 */       return createNode(cs[0], dtrs[0], count);
/*      */     case 2:
/* 1322 */       return createNode(cs[0], dtrs[0], cs[1], dtrs[1], count);
/*      */     case 3:
/* 1323 */       return createNode(cs[0], dtrs[0], cs[1], dtrs[1], cs[2], dtrs[2], count);
/*      */     }
/* 1325 */     return createArrayDtrNode(cs, dtrs, count);
/*      */   }
/*      */ 
/*      */   static Node createNodePrune(char[] cs, Node[] dtrs, long count) {
/* 1329 */     int numOutcomes = 0;
/* 1330 */     for (int i = 0; i < dtrs.length; i++)
/* 1331 */       if (dtrs[i] != null) numOutcomes++;
/* 1332 */     if (numOutcomes == dtrs.length) return createNode(cs, dtrs, count);
/* 1333 */     char[] csOut = new char[numOutcomes];
/* 1334 */     Node[] dtrsOut = new Node[numOutcomes];
/* 1335 */     int indexOut = 0;
/* 1336 */     for (int i = 0; i < dtrs.length; i++) {
/* 1337 */       if (dtrs[i] != null) {
/* 1338 */         csOut[indexOut] = cs[i];
/* 1339 */         dtrsOut[indexOut] = dtrs[i];
/* 1340 */         indexOut++;
/*      */       }
/*      */     }
/* 1343 */     return createNode(csOut, dtrsOut, count);
/*      */   }
/*      */ 
/*      */   static Node createNode(char[] cs, int start, int end, long headCount, long tailCount) {
/* 1347 */     if (end == start)
/* 1348 */       return createNode(headCount);
/* 1349 */     if (headCount == tailCount)
/* 1350 */       return createNode(cs, start, end, headCount);
/* 1351 */     return createNode(cs[start], createNode(cs, start + 1, end, tailCount), headCount);
/*      */   }
/*      */ 
/*      */   static Node createTerminalNode(long count)
/*      */   {
/* 1356 */     if (count <= 127L)
/* 1357 */       return new TerminalNodeByte(count);
/* 1358 */     if (count <= 32767L)
/* 1359 */       return new TerminalNodeShort(count);
/* 1360 */     if (count <= 2147483647L) {
/* 1361 */       return new TerminalNodeInt(count);
/*      */     }
/* 1363 */     return new TerminalNodeLong(count);
/*      */   }
/*      */   static Node createNode(char c, long count) {
/* 1366 */     if (count == 1L)
/* 1367 */       return new PAT1NodeOne(c);
/* 1368 */     if (count == 2L)
/* 1369 */       return new PAT1NodeTwo(c);
/* 1370 */     if (count == 3L)
/* 1371 */       return new PAT1NodeThree(c);
/* 1372 */     if (count <= 127L)
/* 1373 */       return new PAT1NodeByte(c, count);
/* 1374 */     if (count <= 32767L)
/* 1375 */       return new PAT1NodeShort(c, count);
/* 1376 */     if (count <= 2147483647L) {
/* 1377 */       return new PAT1NodeInt(c, count);
/*      */     }
/* 1379 */     return new PAT1NodeLong(c, count);
/*      */   }
/*      */   static Node createNode(char c1, char c2, long count) {
/* 1382 */     if (count == 1L)
/* 1383 */       return new PAT2NodeOne(c1, c2);
/* 1384 */     if (count == 2L)
/* 1385 */       return new PAT2NodeTwo(c1, c2);
/* 1386 */     if (count == 3L)
/* 1387 */       return new PAT2NodeThree(c1, c2);
/* 1388 */     if (count <= 127L)
/* 1389 */       return new PAT2NodeByte(c1, c2, count);
/* 1390 */     if (count <= 32767L)
/* 1391 */       return new PAT2NodeShort(c1, c2, count);
/* 1392 */     if (count <= 2147483647L) {
/* 1393 */       return new PAT2NodeInt(c1, c2, count);
/*      */     }
/* 1395 */     return new PAT2NodeLong(c1, c2, count);
/*      */   }
/*      */   static Node createNode(char c1, char c2, char c3, long count) {
/* 1398 */     if (count == 1L)
/* 1399 */       return new PAT3NodeOne(c1, c2, c3);
/* 1400 */     if (count == 2L)
/* 1401 */       return new PAT3NodeTwo(c1, c2, c3);
/* 1402 */     if (count == 3L)
/* 1403 */       return new PAT3NodeThree(c1, c2, c3);
/* 1404 */     if (count <= 127L)
/* 1405 */       return new PAT3NodeByte(c1, c2, c3, count);
/* 1406 */     if (count <= 32767L)
/* 1407 */       return new PAT3NodeShort(c1, c2, c3, count);
/* 1408 */     if (count <= 2147483647L) {
/* 1409 */       return new PAT3NodeInt(c1, c2, c3, count);
/*      */     }
/* 1411 */     return new PAT3NodeLong(c1, c2, c3, count);
/*      */   }
/*      */   static Node createNode(char c1, char c2, char c3, char c4, long count) {
/* 1414 */     if (count == 1L)
/* 1415 */       return new PAT4NodeOne(c1, c2, c3, c4);
/* 1416 */     if (count == 2L)
/* 1417 */       return new PAT4NodeTwo(c1, c2, c3, c4);
/* 1418 */     if (count == 3L)
/* 1419 */       return new PAT4NodeThree(c1, c2, c3, c4);
/* 1420 */     if (count <= 127L)
/* 1421 */       return new PAT4NodeByte(c1, c2, c3, c4, count);
/* 1422 */     if (count <= 32767L)
/* 1423 */       return new PAT4NodeShort(c1, c2, c3, c4, count);
/* 1424 */     if (count <= 2147483647L) {
/* 1425 */       return new PAT4NodeInt(c1, c2, c3, c4, count);
/*      */     }
/* 1427 */     return new PAT4NodeLong(c1, c2, c3, c4, count);
/*      */   }
/*      */   static Node createPATArrayNode(char[] cs, long count) {
/* 1430 */     if (count == 1L)
/* 1431 */       return new PATArrayNodeOne(cs);
/* 1432 */     if (count == 2L)
/* 1433 */       return new PATArrayNodeTwo(cs);
/* 1434 */     if (count == 3L)
/* 1435 */       return new PATArrayNodeThree(cs);
/* 1436 */     if (count <= 127L)
/* 1437 */       return new PATArrayNodeByte(cs, count);
/* 1438 */     if (count <= 32767L)
/* 1439 */       return new PATArrayNodeShort(cs, count);
/* 1440 */     if (count <= 2147483647L) {
/* 1441 */       return new PATArrayNodeInt(cs, count);
/*      */     }
/* 1443 */     return new PATArrayNodeLong(cs, count);
/*      */   }
/*      */   static Node createPATNode(char firstC, char[] restCs, long count) {
/* 1446 */     switch (restCs.length) {
/*      */     case 0:
/* 1448 */       return createNode(firstC, count);
/*      */     case 1:
/* 1450 */       return createNode(firstC, restCs[0], count);
/*      */     case 2:
/* 1452 */       return createNode(firstC, restCs[0], restCs[1], count);
/*      */     case 3:
/* 1454 */       return createNode(firstC, restCs[0], restCs[1], restCs[2], count);
/*      */     }
/* 1456 */     char[] cs = new char[restCs.length + 1];
/* 1457 */     cs[0] = firstC;
/* 1458 */     System.arraycopy(restCs, 0, cs, 1, restCs.length);
/* 1459 */     return createPATArrayNode(cs, count);
/*      */   }
/*      */ 
/*      */   static Node createNodeFold(char c, Node dtr, long count) {
/* 1463 */     if (dtr.count() == count) {
/* 1464 */       if ((dtr instanceof AbstractPATNode)) {
/* 1465 */         AbstractPATNode patDtr = (AbstractPATNode)dtr;
/* 1466 */         return createPATNode(c, patDtr.chars(), count);
/*      */       }
/* 1468 */       if ((dtr instanceof TerminalNode)) {
/* 1469 */         return createNode(c, count);
/*      */       }
/*      */     }
/* 1472 */     return createNode(c, dtr, count);
/*      */   }
/*      */   static Node createNode(char c, Node dtr, long count) {
/* 1475 */     if (count <= 127L)
/* 1476 */       return new OneDtrNodeByte(c, dtr, count);
/* 1477 */     if (count <= 32767L)
/* 1478 */       return new OneDtrNodeShort(c, dtr, count);
/* 1479 */     if (count <= 2147483647L) {
/* 1480 */       return new OneDtrNodeInt(c, dtr, count);
/*      */     }
/* 1482 */     return new OneDtrNodeLong(c, dtr, count);
/*      */   }
/*      */ 
/*      */   static Node createNode(char c1, Node dtr1, char c2, Node dtr2, long count)
/*      */   {
/* 1487 */     if (count <= 127L)
/* 1488 */       return new TwoDtrNodeByte(c1, dtr1, c2, dtr2, count);
/* 1489 */     if (count <= 32767L)
/* 1490 */       return new TwoDtrNodeShort(c1, dtr1, c2, dtr2, count);
/* 1491 */     if (count <= 2147483647L) {
/* 1492 */       return new TwoDtrNodeInt(c1, dtr1, c2, dtr2, count);
/*      */     }
/* 1494 */     return new TwoDtrNodeLong(c1, dtr1, c2, dtr2, count);
/*      */   }
/*      */ 
/*      */   static Node createNode(char c1, Node dtr1, char c2, Node dtr2, char c3, Node dtr3, long count)
/*      */   {
/* 1500 */     if (count <= 127L)
/* 1501 */       return new ThreeDtrNodeByte(c1, dtr1, c2, dtr2, c3, dtr3, count);
/* 1502 */     if (count <= 32767L)
/* 1503 */       return new ThreeDtrNodeShort(c1, dtr1, c2, dtr2, c3, dtr3, count);
/* 1504 */     if (count <= 2147483647L) {
/* 1505 */       return new ThreeDtrNodeInt(c1, dtr1, c2, dtr2, c3, dtr3, count);
/*      */     }
/* 1507 */     return new ThreeDtrNodeLong(c1, dtr1, c2, dtr2, c3, dtr3, count);
/*      */   }
/*      */   static Node createArrayDtrNode(char[] cs, Node[] dtrs, long count) {
/* 1510 */     if (count <= 127L)
/* 1511 */       return new ArrayDtrNodeByte(cs, dtrs, count);
/* 1512 */     if (count <= 32767L)
/* 1513 */       return new ArrayDtrNodeShort(cs, dtrs, count);
/* 1514 */     if (count <= 2147483647L) {
/* 1515 */       return new ArrayDtrNodeInt(cs, dtrs, count);
/*      */     }
/* 1517 */     return new ArrayDtrNodeLong(cs, dtrs, count);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1299 */     for (int i = 0; i < TERMINAL_NODES.length; i++)
/* 1300 */       TERMINAL_NODES[i] = createTerminalNode(i);
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.NodeFactory
 * JD-Core Version:    0.6.2
 */