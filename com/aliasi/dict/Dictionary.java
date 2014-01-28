package com.aliasi.dict;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract interface Dictionary<C> extends Set<DictionaryEntry<C>>
{
  public abstract Iterator<DictionaryEntry<C>> phraseEntryIt(String paramString);

  public abstract List<DictionaryEntry<C>> phraseEntryList(String paramString);

  public abstract Iterator<DictionaryEntry<C>> categoryEntryIt(C paramC);

  public abstract List<DictionaryEntry<C>> categoryEntryList(C paramC);

  public abstract int size();

  public abstract List<DictionaryEntry<C>> entryList();

  public abstract void addEntry(DictionaryEntry<C> paramDictionaryEntry);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.Dictionary
 * JD-Core Version:    0.6.2
 */