import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataLoader {
    
    private WordRBT<Definition> map;
    private File file;
    
    public DataLoader(File file, WordRBT<Definition> map)  {
        this.map = map;
        this.file = file;
        try {
            setData();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void setData() throws FileNotFoundException {
        String KeyWord;
        String info;
        Definition addMe;

        if (file == null) {
          throw new FileNotFoundException("Error. No file found. Please use "
                + "valid file ending in .txt");
        }

        Scanner scan = new Scanner(file);

        while(scan.hasNextLine()) {
           KeyWord = scan.next();
           info = scan.nextLine();
           addMe = new Definition(KeyWord, info);

           map.insert(addMe);
        }

        if (scan != null) {
            scan.close();
        }
      }


    public WordRBT<Definition> getMap() {
        return this.map;
    }


    public void setFile(File file) {
        this.file = file;
    }
    
    
}