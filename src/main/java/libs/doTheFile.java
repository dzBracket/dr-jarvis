package libs;

import DataClass.customizable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ant.compress.taskdefs.Ar;
import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class doTheFile {
    public customizable config;
    File file;
    public File tmp=new File("tempDisteny/");
    ArrayList<String> filesListInDir=new ArrayList<>();

    public <type>doTheFile(File f) throws IOException {
        Unzip unzipper = new Unzip();
        unzipper.setSrc(f);
        unzipper.setDest(tmp);
        unzipper.execute();
        //populateFilesList(tmp);



    }
    public void execute() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    config=mapper.readValue(new File("tempDisteny/nock"),customizable.class);
    file=new File(config.getURL());
int i=1;
    while (file.exists()){
        file=new File(config.getURL()+i);
        i++;
    }
    config.setURL(file.getAbsolutePath());
    Files.copy(Paths.get("tempDisteny/didi"), file.toPath());
    deleteDirectoryStream(tmp.toPath());
}


    void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }


}
