  
public class Definition implements Comparable<Definition>{
  private String KeyWord;
  private String info;

  public Definition(String KeyWord, String info) {
       this.KeyWord = KeyWord;
       this.info = info;
  }

  public String getKeyWord() {
    return KeyWord;
  }

  public String getinfo() {
    return info;
  }

  @Override
  public int compareTo(Definition o) {
    // TODO Auto-generated method stub
    return this.KeyWord.compareTo(o.getKeyWord());
  }

}